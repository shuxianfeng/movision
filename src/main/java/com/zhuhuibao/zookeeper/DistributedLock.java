package com.zhuhuibao.zookeeper;

import com.zhuhuibao.exception.LockException;
import com.zhuhuibao.utils.PropertiesUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * zookeeper 分布式锁
 *
 * @author jianglz
 * @since 16/6/2.
 */
@Component
public class DistributedLock implements Lock, Watcher {
    private static final Logger log = LoggerFactory.getLogger(DistributedClient.class);

    private ZooKeeper zk;
    private String root = "/locks";//根
    private String lockName;//竞争资源的标志
    private String waitNode;//等待前一个锁
    private String myZnode;//当前锁
    private CountDownLatch latch;//计数器
    private List<Exception> exception = new ArrayList<>();


    /**
     * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
     *
     * @param lockName 竞争资源标志,lockName中不能包含单词lock
     */
    public DistributedLock(String lockName) {
        this.lockName = lockName;
        // 创建一个与服务器的连接
        try {
            zk = new ZooKeeper(PropertiesUtils.getValue("zookeeper_hosts"),
                    Integer.valueOf(PropertiesUtils.getValue("zookeeper_session_timeout")), this);
            Stat stat = zk.exists(root, false);
            if (stat == null) {
                // 创建根节点
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            exception.add(e);
        }
    }


    public void lock() {

        if (exception.size() > 0) {
            throw new LockException(9999,exception.get(0).getMessage());
        }
        try {
            if (this.tryLock()) {
                log.info("Thread " + Thread.currentThread().getId() + " " + myZnode + " get lock true");
                return;
            } else {
                waitForLock(waitNode, Integer.valueOf(PropertiesUtils.getValue("zookeeper_session_timeout")));//等待锁
            }
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(9999,e.getMessage());
        }

    }

    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if (lockName.contains(splitStr))
                throw new LockException(9999,"lockName can not contains '_lock_' ");
            //创建临时子节点
            myZnode = zk.create(root + "/" + lockName + splitStr, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info(myZnode + " is created ");
            //取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);
            //取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<>();
            for (String node : subNodes) {
                String _node = node.split(splitStr)[0];
                if (_node.equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            log.info(myZnode + "==" + lockObjNodes.get(0));
            if (myZnode.equals(root + "/" + lockObjNodes.get(0))) {
                //如果是最小的节点,则表示取得锁
                return true;
            }
            //如果不是最小的节点，找到比自己小1的节点
            String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
            waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(9999,e.getMessage());
        }
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        try {
            return this.tryLock() || waitForLock(waitNode, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void unlock() {
        try {
            log.info("unlock " + myZnode);
            zk.delete(myZnode, -1);
            myZnode = null;
            zk.close();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public Condition newCondition() {
        return null;
    }


    private boolean waitForLock(String lower, long waitTime) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(root + "/" + lower, true);
        //判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
        if (stat != null) {
            log.info("Thread " + Thread.currentThread().getId() + " waiting for " + root + "/" + lower);
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    /**
     * zookeeper节点的监视器
     *
     * @param watchedEvent event
     */
    public void process(WatchedEvent watchedEvent) {
        if (this.latch != null) {
            this.latch.countDown();
        }
    }
}

package com.zhuhuibao.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 以一个DistributedClient对象模拟一个进程的形式, 演示zookeeper分布式锁的实现.
 *
 * @author jianglz
 * @since 16/6/2.
 */
public class DistributedClient {

    private static final Logger log = LoggerFactory.getLogger(DistributedClient.class);

    // 超时时间
    private static final int SESSION_TIMEOUT = 5000;
    // zookeeper server列表
    private final static String hosts = "localhost:2181,localhost:2182,localhost:2183"; //localhost:2181,localhost:2182,localhost:2183
    private final static String groupNode = "locks";
    private final static String subNode = "sub";

    private ZooKeeper zk;
    // 当前client创建的子节点
    private String thisPath;
    // 当前client等待的子节点
    private String waitPath;

    /**
     * 你可以向CountDownLatch对象设置一个初始的数字作为计数值，任何调用这个对象上的await()方法都会阻塞，直到这个计数器的计数值被其他的线程减为0为止。
     */
    private CountDownLatch latch = new CountDownLatch(1);


    /**
     * 连接zookeeper
     */
    public void connectZookeeper() throws Exception {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    // 连接建立时, 打开latch, 唤醒wait在该latch上的线程
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }

                    // 发生了waitPath的删除事件
                    /**
                     * 存在的问题：
                     * 现在有subs5 sub6  subs7  subs8几个子节点，当前subs5正获得锁，如果subs6对应的client6挂掉，
                     * 则subs6被删除--触发了client7那边的监听，导致client7也拿到了锁，导致5和7的客户端同时得到锁。
                     *
                     * 为了避免上述问题：
                     * 可以在接到waitPath的删除通知的时候, 进行一次确认, 确认当前的thisPath是否真的是列表中最小的节点.
                     */
                    if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                        // 确认thisPath是否真的是列表中的最小节点
                        List<String> childrenNodes = zk.getChildren("/" + groupNode, false);
                        String thisNode = thisPath.substring(("/" + groupNode + "/").length());
                        // 排序
                        Collections.sort(childrenNodes);
                        int index = childrenNodes.indexOf(thisNode);
                        if (index == 0) {
                            // 确实是最小节点
                            doSomething();
                        } else {
                            // 说明waitPath是由于出现异常而挂掉的
                            // 更新waitPath
                            waitPath = "/" + groupNode + "/" + childrenNodes.get(index - 1);
                            // 重新注册监听, 并判断此时waitPath是否已删除
                            if (zk.exists(waitPath, true) == null) {
                                doSomething();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 等待连接建立
        latch.await();

        // 创建子节点
        thisPath = zk.create("/" + groupNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        // wait一小会, 让结果更清晰一些
        Thread.sleep(10);

        // 注意, 没有必要监听"/locks"的子节点的变化情况
        List<String> childrenNodes = zk.getChildren("/" + groupNode, false);

        // 列表中只有一个子节点, 那肯定就是thisPath, 说明client获得锁
        if (childrenNodes.size() == 1) {
            doSomething();
        } else {
            String thisNode = thisPath.substring(("/" + groupNode + "/").length());
            // 排序
            Collections.sort(childrenNodes);
            int index = childrenNodes.indexOf(thisNode);
            switch (index) {
                case -1:
                    // never happened
                    break;
                case 0:
                    // index == 0, 说明thisNode在列表中最小, 当前client获得锁
                    doSomething();
                    break;
                default:
                    // 获得排名比thisPath前1位的节点
                    this.waitPath = "/" + groupNode + "/" + childrenNodes.get(index - 1);
                    // 在waitPath上注册监听器, 当waitPath被删除时, zookeeper会回调监听器的process方法
                    zk.getData(waitPath, true, new Stat());
                    break;
            }
        }
    }


    /**
     * 共享资源的访问逻辑写在这个方法中
     */
    private void doSomething() throws Exception {
        try {
            System.out.println("gain lock: " + thisPath);
            Thread.sleep(2000);
            // do something
            System.out.println("do something....");
        } finally {
            System.out.println("finished: " + thisPath);
            // 将thisPath删除, 监听thisPath的client将获得通知
            // 相当于释放锁
            zk.delete(this.thisPath, -1);
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    try {
                        DistributedClient dl = new DistributedClient();
                        dl.connectZookeeper();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        Thread.sleep(Long.MAX_VALUE);
    }
}

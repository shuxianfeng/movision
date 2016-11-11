package com.zhuhuibao.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 使用并发锁 控制并发数据的实例
 * @author zhuangyuhao
 * @time   2016年10月19日 上午9:07:52
 *
 */
public class BusinessLockUtil {
	
	private static final Logger log = LoggerFactory.getLogger(BusinessLockUtil.class);
	
	@Autowired
	RedisUtil client;
    /** 
     * 锁定某个业务对象 
     * timeout = 0 时，非阻塞调用，如果对象已锁定立刻返回失败 
     * timeout > 0 时，阻塞调用，
     * 如果对象已锁定，会等待直到	1）获取锁并返回成功 2）超时并返回失败 
     *  
     * @param mutex 
     *            互斥对象 
     * @param timeout 
     *            超时时间，单位：秒 
     * @return true：锁定成功，false：锁定失败 
     *      long) 
     */  
    public static boolean lock(MutexElement mutex, int timeout) {  
  
        // 输入参数校验  
        if (mutex == null || mutex.getType() == null  
                || StringUtils.isEmpty(mutex.getBusinessNo())) {  
            throw new RuntimeException("互斥参数为空");  
        }  
  
        Jedis jedis = null;  
        String key = mutex.getType() + mutex.getBusinessNo();  
        String value = mutex.getBusinessDesc();  
  
        try {  
            //获取jedis实例 
            jedis = RedisUtil.getJedis();  
            long nano = System.nanoTime();  
            do {  
                log.debug("try lock key: " + key);  
                  
                // 使用setnx模拟锁  
                Long i = jedis.setnx(key, value);  
                  
                if (i == 1) {   // setnx成功，获得锁  
                    jedis.expire(key, mutex.getTime());  
                    log.debug("get lock, key: " + key + " , expire in " + mutex.getTime() + " seconds.");  
                    return true;  
                } else {    // 存在锁  
                    if (log.isDebugEnabled()) {  
                        String desc = jedis.get(key);  
                        log.debug("key: " + key + " locked by another business：" + desc);  
                    }  
                }  
                  
                if (timeout == 0) { // 非阻塞调用，则退出  
                    break;  
                }  
                  
                Thread.sleep(1000); // 每秒访问一次  
                  
            } while ((System.nanoTime() - nano) < timeout * 1000l * 1000l * 1000l);  
  
            // 得不到锁，返回失败  
            return false;  
  
        } catch (JedisConnectionException je) {  
            log.error(je.getMessage(), je);  
            RedisUtil.returnBrokenResource(jedis);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
        } finally {  
            if (jedis != null) {  
                RedisUtil.returnResource(jedis);  
            }  
        }  
        // 锁不再作为业务的的强制必要条件  
        // 发生REDIS异常，则不再处理锁  
        return true;  
    }  
      
    /** 
     * 解除某个业务对象锁定 
     *  
     * @author leo 
     * @param mutex 
     *            互斥对象 
     */  
    public static void unlock(MutexElement mutex) {  
  
        // 输入参数校验  
        if (mutex == null || mutex.getType() == null  
                || StringUtils.isEmpty(mutex.getBusinessNo())) {  
            throw new RuntimeException("互斥参数为空");  
        }  
  
        Jedis jedis = null;  
          
        String key = mutex.getType() + mutex.getBusinessNo();  
          
        try {  
            jedis = RedisUtil.getJedis();  
              
            jedis.del(key);  
            log.debug("release lock, key :" + key);  
        } catch (JedisConnectionException je) {  
            log.error(je.getMessage(), je);  
            RedisUtil.returnBrokenResource(jedis);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);  
        } finally {  
            if (jedis != null) {  
                RedisUtil.returnResource(jedis);  
            }  
        }  
  
    }  
    
    public static void main(String[] args) {
    	
    	BusinessLockUtil lock = new BusinessLockUtil();
    	int time = 10;
    	MutexElement mutex = new MutexElement("wxpay_notify",  
                "1", "微信支付通知",time);  
    	
        boolean result = lock.lock(mutex,  0);  
        //加锁成功  
        if (result) {  
        	//TODO 业务处理
        }  
        //解锁  
        lock.unlock(mutex);  
	}
    
}

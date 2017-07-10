package com.movision.utils;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/10 13:33
 */
@Service
public class ShairUtil {
    private static final Logger log = LoggerFactory.getLogger(ShairUtil.class);
    @Autowired
    private PostService postService;

    /**
     * 向缓存中设置字符串内容
     *
     * @param
     * @param value value
     * @return
     * @throws Exception
     */
    public static JedisPool jedisPool;

    public boolean set(String value) {
        Jedis jedis = null;
        String key = "postid" + 1;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 根据key 获取内容
     *
     * @param id
     * @return
     */
    public Object get(int id) {
        Jedis jedis = null;
        Map map = new HashMap();
        String key = "postid" + 1;
        try {
            jedis = jedisPool.getResource();
            Object value = jedis.get(key);
            if (value == null) {
                List<Post> list = postService.queryPostDetailById(id);
                map.put("list", list);
                map.put("type", 0);
                return map;
            } else {
                map.put("type", 1);
                map.put("value", value);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}

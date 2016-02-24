package com.zhuhuibao.mybatis.service;

import com.zhuhuibao.utils.pagination.model.Paging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zhuhuibao.mybatis.entity.User;
import com.zhuhuibao.mybatis.entity.member.Member;
import com.zhuhuibao.mybatis.mapper.UserMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jianglz
 * @since 15/12/10.
 */
@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 手机号码查询用户 {缓存}
     *
     * @param mobile 手机号码
     * @return User
     */
    @Cacheable(value = "userCache", key = "#mobile")
    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }


    /**
     * 通过ID查询   {缓存}
     *
     * @param id 用户ID
     * @return User
     */
    @Cacheable(value = "userCache", key = "#id")
    public User findById(Integer id) {
        log.debug("数据库中查到此用户ID[" + id + "]");
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有用户  {缓存}
     *
     * @return List<User> users
     */
    // 查询所有，不要key,默认以方法名+参数值+内容 作为key
    @Cacheable(value = "userCache")
    public List<User> findAll() {
        log.debug("查询所有用户");
        return userMapper.findAll();
    }

    /**
     * 分页查询所有用户
     * @param pager    分页属性
     * @return   user
     */
    public List<User> findAllByPager(Paging<User> pager) {
        log.debug("查询所有用户");
        return userMapper.findAllByPager(pager.getRowBounds());
    }

    /**
     * 添加用户
     *
     * @param user 用户实例
     *             自动添加新增数据到缓存中
     */
    @CachePut(value = "userCache", key = "#user.id")
    public void addUser(User user) {
        log.debug("添加用户");
        if (user != null && user.getId() != null) {
            userMapper.insert(user);
        }
    }
    
    /**
     * 通过ID删除用户  {清除缓存}
     *
     * @param id 用户ID
     */
    @CacheEvict(value = "userCache", key = "#id")
    public void deleteUser(Integer id) {
        log.debug("通过ID删除用户");
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新用户数据 {自动更新缓存}
     *
     * @param user user
     */
    @CachePut(value = "userCache", key = "#user.id")
    public void updateUser(User user) {
        log.debug("移除缓存中此用户ID[" + user.getId() + "]对应的用户名[" + user.getName() + "]的缓存");
//        userMapper.updateByPrimaryKey(user);
        userMapper.updateByPrimaryKeySelective(user);  //部分字段更新
    }

    /**
     * 清除缓存中所有数据
     * allEntries为true表示清除value中的全部缓存,默认为false
     */
    @CacheEvict(value = "userCache", allEntries = true)
    public void removeAll() {
        log.debug("清除缓存中所有数据");
    }

}

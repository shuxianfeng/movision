package com.movision.mybatis.user.service;

import com.movision.mybatis.user.entity.LoginUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/17 19:43
 */
@Service
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    public LoginUser queryLoginUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selectLoginUserByPhone(user);
    }

    public boolean insertUser(User user) {
        try {
            log.info("插入用户表，user=" + user.toString());
            int n = userMapper.insert(user);
            return n == 1;
        } catch (Exception e) {
            log.error("插入用户表异常，user=" + user.toString(), e);
            throw e;
        }
    }

    public boolean updateUserPointsAdd(Map mapadd) {
        try {
            log.info("根据手机号给贴主增加积分");
            int n = userMapper.updateUserPointsAdd(mapadd);
            return n == 1;
        } catch (NumberFormatException e) {
            log.error("给贴主增加积分失败");
            throw e;
        }
    }

    public boolean updateUserPointsMinus(Map map) {
        try {
            log.info("根据用户id操作用户积分减操作");
            int n = userMapper.updateUserPointsMinus(map);
            return n == 1;
        } catch (NumberFormatException e) {
            log.error("减少用户积分操作失败");
            throw e;
        }
    }

}

package com.movision.mybatis.userPhoto.service;

import com.movision.mybatis.userPhoto.entity.UserPhoto;
import com.movision.mybatis.userPhoto.mapper.UserPhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/21 17:15
 */
@Service
public class UserPhotoService {

    private static Logger log = LoggerFactory.getLogger(UserPhotoService.class);

    @Autowired
    private UserPhotoMapper userPhotoMapper;

    public Integer add(UserPhoto userPhoto) {
        try {
            log.debug("新增字典表：用户头像");
            return userPhotoMapper.insertSelective(userPhoto);
        } catch (Exception e) {
            log.error("新增字典表：用户头像失败", e);
            throw e;
        }
    }
}

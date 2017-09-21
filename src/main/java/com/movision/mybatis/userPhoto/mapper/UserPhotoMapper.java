package com.movision.mybatis.userPhoto.mapper;

import com.movision.mybatis.userPhoto.entity.UserPhoto;

public interface UserPhotoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPhoto record);

    int insertSelective(UserPhoto record);

    UserPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPhoto record);

    int updateByPrimaryKey(UserPhoto record);
}
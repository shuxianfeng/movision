package com.movision.mybatis.userPhoto.mapper;

import com.movision.mybatis.userPhoto.entity.UserPhoto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPhoto record);

    int insertSelective(UserPhoto record);

    UserPhoto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPhoto record);

    int updateByPrimaryKey(UserPhoto record);

    List<UserPhoto> queryUserPhonts(Integer number);
}
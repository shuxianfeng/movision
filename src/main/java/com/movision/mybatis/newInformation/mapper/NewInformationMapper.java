package com.movision.mybatis.newInformation.mapper;

import com.movision.mybatis.newInformation.entity.NewInformation;

import java.util.Map;

public interface NewInformationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewInformation record);

    int insertSelective(NewInformation record);

    NewInformation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewInformation record);

    int updateByPrimaryKey(NewInformation record);

    Integer queryUserByNewInformation(Integer postid);

    void updateUserByNewInformation(NewInformation news);

    void insertUserByNewInformation(NewInformation news);

    Integer queryUserByNewInformationByCommentid(Integer commentid);

    Integer querySystemByNewInformation(String accid);

    Integer queryCollByNewInformation(String toaccid);

    void updateNewInformtions(Map userid);

    NewInformation queryNewInformationByUserid(Integer userid);
}
package com.movision.mybatis.weixinguangzhu.mapper;

import com.movision.mybatis.weixinguangzhu.entity.WeixinGuangzhu;

public interface WeixinGuangzhuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WeixinGuangzhu record);

    int insertSelective(WeixinGuangzhu record);

    WeixinGuangzhu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeixinGuangzhu record);

    int updateByPrimaryKey(WeixinGuangzhu record);

    int updateCount(int id);

    int selectCount(String openids);

    int manyC(String openid);

    int overplusMany(String openid);

    int lessCount(String openid);

    String selectOpenid(int userid);

    int updateC(String openid);
}
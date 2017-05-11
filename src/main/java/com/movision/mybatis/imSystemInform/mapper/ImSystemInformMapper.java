package com.movision.mybatis.imSystemInform.mapper;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ImSystemInformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImSystemInform record);

    int insertSelective(ImSystemInform record);

    ImSystemInform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImSystemInform record);

    int updateByPrimaryKey(ImSystemInform record);

    List<ImSystemInformVo> findAll(RowBounds rowBounds);

    List<ImSystemInform> findAllSystemInform(Map map, RowBounds rowBounds);//条件搜索

    ImSystemInform queryBodyAll(Integer id);//查询全部内容

    ImSystemInformVo queryByUserid();//查询最新一条记录

    Integer querySystemPushByUserid(Integer userid);//查询是否有未读系统通知

    Integer queryInform(String indity);

    List<String> queryUnreadSystemMessage(Integer userid);

    List<ImSystemInformVo> findAllIm(Map map, RowBounds rowBounds);
}
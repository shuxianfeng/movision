package com.movision.mybatis.systemPush.mapper;

import com.movision.mybatis.systemPush.entity.SystemPush;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SystemPushMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemPush record);

    int insertSelective(SystemPush record);

    SystemPush selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemPush record);

    int updateByPrimaryKey(SystemPush record);

    List<SystemPush> findAllSystemPush(RowBounds rowBounds);

    List<SystemPush> findAllPushCondition(Map map, RowBounds rowBounds);//品牌搜索

    SystemPush queryPushBody(Integer id);//查看消息内容

    Integer deleteSystemPush(Integer id);//删除消息

    int addPush(SystemPush systemPush);//增加消息推送

}
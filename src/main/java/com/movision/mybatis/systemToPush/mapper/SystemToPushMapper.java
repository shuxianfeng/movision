package com.movision.mybatis.systemToPush.mapper;

import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.systemToPush.entity.SystemToPush;
import com.movision.mybatis.user.entity.UserVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SystemToPushMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemToPush record);

    int insertSelective(SystemToPush record);

    SystemToPush selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemToPush record);

    int updateByPrimaryKey(SystemToPush record);

    List<SystemToPush> findAllSystemToPush(RowBounds rowBounds);

    List<SystemToPush> findAllSystemCondition(Map map, RowBounds rowBounds);

    SystemToPush querySystemToPushBody(Integer id);

    Integer deleteSystemToPush(Integer id);

    Integer addSystemToPush(SystemToPush systemToPush);//增加

    List<ImUser> queryUser(int postid);


}
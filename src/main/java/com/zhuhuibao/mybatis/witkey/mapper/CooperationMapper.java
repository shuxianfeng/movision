package com.zhuhuibao.mybatis.witkey.mapper;

import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CooperationMapper {

    //发布任务
    int publishCooperation(Cooperation record);

    //编辑任务
    int updateCooperation(Cooperation record);

    int updateCooperationViews(Cooperation record);

    //删除任务
    int deleteCooperation(String id);

    //查询一条任务的信息
    Map<String,Object> queryCooperationInfoById(String id);

    Map<String,Object> queryUnloginCooperationInfo(String id);

    //根据条件查询任务信息列表（分页）
    List<Map<String,String>> findAllCooperationByPager(RowBounds rowBounds, Cooperation cooperation);

    //根据条件查询任务信息列表（分页）
    List<Map<String,String>> findAllCooperationByPager4Mobile(RowBounds rowBounds, Map cooperation);

    //手机端威客首页查询威客数据
    List<Map<String,String>> findAllCooperation4Mobile(Cooperation cooperation);

    //最热合作信息
    List<Map<String,String>> queryHotCooperation(Map<String, Object> map);

    int queryMyWitkeyListSize(Map<String,Object> map);

    List<Map<String,String>> findAllWitkeyTaskList(RowBounds rowBounds, Map<String, Object> map);

    int deleteWitkeyTask(String id);

    Map<String,Object> queryUnloginCooperationInfoById(String id);

    List<Map<String,String>> findAllWitkeyByCompanyId(RowBounds rowBounds, Map<String, Object> map);
}
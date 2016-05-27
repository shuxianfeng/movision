package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CooperationMapper {

    //发布任务
    int publishCooperation(Cooperation record);

    //编辑任务
    int updateCooperation(Cooperation record);

    //删除任务
    int deleteCooperation(String id);

    //查询一条任务的信息
    Cooperation queryCooperationInfoById(String id);

    //根据条件查询任务信息列表（分页）
    List<Map<String,String>> findAllCooperationByPager(RowBounds rowBounds,Cooperation cooperation);

    //最热合作信息
    List<Cooperation> queryHotCooperation(Map<String,Object> map);
}
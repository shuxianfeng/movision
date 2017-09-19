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

    List<ImSystemInformVo> findAllIm(Date informTime, RowBounds rowBounds);

    ImSystemInformVo queryMyMsgInforDetails(ImSystemInform imSystemInform);

    List<ImSystemInform> findAllOperationInformList(ImSystemInform inform, RowBounds rowBounds);

    ImSystemInform queryOperationInformById(ImSystemInform imSystemInform);

    List<ImSystemInform> findAllActiveMessage(Map map, RowBounds rowBounds);

    int updateActiveMessage(ImSystemInform imSystemInform);

    ImSystemInform queryActiveById(int id);

    String queryActiveBody(int id);

    Date queryDate(int userid);
}
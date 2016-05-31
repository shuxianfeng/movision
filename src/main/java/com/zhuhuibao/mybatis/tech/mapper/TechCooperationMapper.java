package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 技术合作：技术成果，技术需求 DAO层
 * @author  penglong
 * @create 2016-05-27
 */
public interface TechCooperationMapper {

    int insertSelective(TechCooperation record);

    TechCooperation selectByPrimaryKey(Long id);

    TechCooperation previewTechCooperationDetail(Long id);

    int updateByPrimaryKeySelective(TechCooperation record);

    List<Map<String,String>> findAllTechCooperationPager(RowBounds rowBounds,Map<String, Object> condition);

    List<Map<String,String>> findAllOMSTechCooperationPager(RowBounds rowBounds,Map<String,Object> condition);

    int deleteTechCooperation(Map<String, Object> condition);

    int updateTechCooperationViews(Long id);
}
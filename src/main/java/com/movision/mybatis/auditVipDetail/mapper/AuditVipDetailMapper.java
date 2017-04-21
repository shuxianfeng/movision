package com.movision.mybatis.auditVipDetail.mapper;

import com.movision.mybatis.auditVipDetail.entity.AuditVipDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface AuditVipDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuditVipDetail record);

    int insertSelective(AuditVipDetail record);

    AuditVipDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuditVipDetail record);

    int updateByPrimaryKey(AuditVipDetail record);

    AuditVipDetail selectByApplyId(@Param("apply_id") Integer apply_id);

    Integer insertVIPDetail(Map map);

    Integer queryVipDetail(Map map);

    Integer updateVipDetail(Map map);
}
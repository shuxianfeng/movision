package com.movision.mybatis.applyVipDetail.mapper;

import com.movision.mybatis.applyVipDetail.entity.ApplyVipDetail;
import org.apache.ibatis.annotations.Param;

public interface ApplyVipDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyVipDetail record);

    int insertSelective(ApplyVipDetail record);

    ApplyVipDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyVipDetail record);

    int updateByPrimaryKey(ApplyVipDetail record);

    ApplyVipDetail selectLatestVipApplyRecord(@Param("userid") Integer userid);
}
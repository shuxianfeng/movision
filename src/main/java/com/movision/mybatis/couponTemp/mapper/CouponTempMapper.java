package com.movision.mybatis.couponTemp.mapper;

import com.movision.mybatis.couponTemp.entity.CouponTemp;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponTempMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponTemp record);

    int insertSelective(CouponTemp record);

    CouponTemp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponTemp record);

    int updateByPrimaryKey(CouponTemp record);
}
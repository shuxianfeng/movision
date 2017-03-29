package com.movision.mybatis.couponTemp.mapper;

import com.movision.mybatis.couponTemp.entity.CouponTemp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponTempMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponTemp record);

    int insertSelective(CouponTemp record);

    CouponTemp selectByPrimaryKey(Integer id);

    List<CouponTemp> checkIsGetCoupon(String phone);

    void delCouponTemp(String phone);

    int updateByPrimaryKeySelective(CouponTemp record);

    int updateByPrimaryKey(CouponTemp record);
}
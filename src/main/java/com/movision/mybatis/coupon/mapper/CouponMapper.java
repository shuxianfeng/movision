package com.movision.mybatis.coupon.mapper;

import com.movision.mybatis.coupon.entity.Coupon;

import java.util.List;

public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);

    List<Coupon> queryCanUseCoupon(int userid);

    List<Coupon> queryBeUsedCoupon(int userid);

    List<Coupon> queryHaveOverdueCoupon(int userid);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);
}
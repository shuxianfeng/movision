package com.movision.mybatis.coupon.mapper;

import com.movision.mybatis.coupon.entity.Coupon;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
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

    List<Coupon> findAllMyCouponList(RowBounds rowBounds, Map map);

    List<Coupon> queryCouponList(int userid);

    Coupon queryCouponById(int couponid);

    void useCoupon(int couponid);

    List<Coupon> queryDiscountCouponList(String userid, RowBounds rowBounds);
}
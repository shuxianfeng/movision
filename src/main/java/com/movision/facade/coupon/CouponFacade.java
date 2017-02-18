package com.movision.facade.coupon;

import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/18 11:13
 */
@Service
public class CouponFacade {

    @Autowired
    private CouponService couponService;

    public Map<String, Object> choiceCoupon(String userid) {

        Map<String, Object> map = new HashMap<>();

        //查询可使用未过期的优惠券
        List<Coupon> canUseCouponList = couponService.queryCanUseCoupon(Integer.parseInt(userid));
        //查询已使用的优惠券
        List<Coupon> beUsedCouponList = couponService.queryBeUsedCoupon(Integer.parseInt(userid));
        //查询已过期的优惠券
        List<Coupon> haveOverdueCouponList = couponService.queryHaveOverdueCoupon(Integer.parseInt(userid));

        map.put("canUseCouponList", canUseCouponList);
        map.put("beUsedCouponList", beUsedCouponList);
        map.put("haveOverdueCouponList", haveOverdueCouponList);
        return map;
    }
}

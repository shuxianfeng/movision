package com.movision.facade.coupon;

import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManage;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<CouponDistributeManageVo> queryCurReceiveCoupon(String isself, String shopid) {
        //查询当前可以领取的优惠券列表
        List<CouponDistributeManageVo> couponDistributeManageVoList = new ArrayList<>();
        Map<String, Object> parammap = new HashMap<>();

        if (isself.equals("1")) {//自营
            couponDistributeManageVoList = couponService.queryCurReceiveCoupon();

        }
//        else if (isself.equals("0")){//第三方（2.0预留）
//            parammap.put("shopid", shopid);
//            couponDistributeManageVoList = couponService.queryCurReceiveCouponByShop(parammap);
//        }
        return couponDistributeManageVoList;
    }

    public List<Coupon> findAllMyCouponList(Paging<Coupon> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return couponService.findAllMyCouponList(paging, map);
    }


}

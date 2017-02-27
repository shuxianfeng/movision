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

    public int getCoupon(String userid, String id) throws Exception {
        //用户领取优惠券接口
        int flag = 0;
        //首先校验领取的这个优惠券的领取权限
        int sum = couponService.checkCoupon(Integer.parseInt(id));

        if (sum == 1) {
            flag = 1;
            //向用户优惠券表中插入一条优惠券数据，扣减可领优惠券的总张数和总金额，并记录用户的领取记录
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            parammap.put("id", Integer.parseInt(id));
            couponService.getCoupon(parammap);
        } else if (sum == 0) {
            flag = 0;
        }
        return flag;
    }

    public List<Coupon> findAllMyCouponList(Paging<Coupon> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return couponService.findAllMyCouponList(paging, map);
    }


}

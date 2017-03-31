package com.movision.facade.coupon;

import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.entity.CouponVo;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;
import com.movision.mybatis.couponReceiveRecord.entity.CouponReceiveRecord;
import com.movision.mybatis.couponShareRecord.entity.CouponShareRecord;
import com.movision.mybatis.couponShareRecord.entity.CouponShareRecordVo;
import com.movision.mybatis.couponTemp.entity.CouponTemp;
import com.movision.mybatis.couponTemp.entity.CouponTempVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/2/18 11:13
 */
@Service
public class CouponFacade {

    @Autowired
    private CouponService couponService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CartService cartService;

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

    public List<CouponDistributeManageVo> queryCurReceiveCoupon(String userid, String isself, String shopid) {
        //查询当前可以领取的优惠券列表
        List<CouponDistributeManageVo> couponDistributeManageVoList = new ArrayList<>();
        Map<String, Object> parammap = new HashMap<>();

        if (isself.equals("1")) {//自营
            couponDistributeManageVoList = couponService.queryCurReceiveCoupon();
            for (int i = 0; i < couponDistributeManageVoList.size(); i++) {
                //查询用户是否领取过该优惠券
                Map<String, Object> map = new HashMap<>();
                map.put("userid", Integer.parseInt(userid));
                map.put("id", couponDistributeManageVoList.get(i).getId());
                int num = couponService.checkIsHaveGet(map);
                couponDistributeManageVoList.get(i).setIsHaveGet(num);

                //检查当前优惠券的状态
                CouponDistributeManageVo vo = couponDistributeManageVoList.get(i);
                Date startDate = vo.getStartdate();//优惠券生效时间
                Date endDate = vo.getEnddate();//优惠券失效时间
                int restnum = vo.getRestnum();//可领优惠券剩余数量
                Date now = new Date();
                if (now.after(startDate) && now.before(endDate) && restnum > 0) {
                    couponDistributeManageVoList.get(i).setStatus(0);//可领取
                } else if (now.after(startDate) && now.before(endDate) && restnum <= 0) {
                    couponDistributeManageVoList.get(i).setStatus(3);//已抢光
                } else if (now.before(startDate) && restnum > 0) {
                    couponDistributeManageVoList.get(i).setStatus(1);//未开始
                } else if (now.after(endDate)) {
                    couponDistributeManageVoList.get(i).setStatus(2);//已结束
                }
            }
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

        //检查该用户有没领取过该优惠券
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("id", Integer.parseInt(id));
        int num = couponService.checkIsHaveGet(parammap);

        if (sum == 1) {//有可领优惠券
            if (num == 0) {//未领取过
                flag = 1;
                //向用户优惠券表中插入一条优惠券数据，扣减可领优惠券的总张数和总金额，并记录用户的领取记录
                couponService.getCoupon(parammap);
            } else if (num == 1) {//已领取过
                flag = -1;
            }
        } else if (sum == 0) {//无可领优惠券
            flag = 0;
        }
        return flag;
    }

    public int checkHaveDistribute() {
        //检查当前是否存在可分享的优惠券活动
        return couponService.checkHaveDistribute();
    }

    @Transactional
    public CouponShareRecordVo getCouponDistributeInfo(String userid) {
        //封装返回的优惠券分享实体(剥离的分享实体)
        CouponShareRecordVo couponShareRecordVo = new CouponShareRecordVo();

        //先查询分享的优惠券信息
        CouponDistributeManageVo couponDistributeManageVo = couponService.getCouponDistributeInfo();
        if (null != couponDistributeManageVo) {
            //再将分享出去的部分剥离到yw_coupon_share_record 优惠券分享记录表中
            CouponShareRecord couponShareRecord = new CouponShareRecord();
            couponShareRecord.setDistributeid(couponDistributeManageVo.getId());
            couponShareRecord.setUserid(Integer.parseInt(userid));
            couponShareRecord.setSinglesharenum(couponDistributeManageVo.getSinglesharenum());
            couponShareRecord.setReceivednum(0);
            couponShareRecord.setRestnum(couponDistributeManageVo.getSinglesharenum());
            couponShareRecord.setIntime(new Date());
            couponShareRecord.setIsdel(0);
            couponService.insertCouponShareRecord(couponShareRecord);
            //更新优惠券分发管理表中的信息
            couponService.updateCouponDistributeInfo(couponDistributeManageVo.getId());

            int shareid = couponShareRecord.getId();//返回的分享id

            couponShareRecordVo.setId(shareid);
            couponShareRecordVo.setDistributeid(couponDistributeManageVo.getId());
            couponShareRecordVo.setUserid(Integer.parseInt(userid));
            couponShareRecordVo.setSinglesharenum(couponDistributeManageVo.getSinglesharenum());
            couponShareRecordVo.setReceivednum(0);
            couponShareRecordVo.setRestnum(couponDistributeManageVo.getSinglesharenum());
            couponShareRecordVo.setIntime(new Date());
            couponShareRecordVo.setIsdel(0);
            couponShareRecordVo.setTitle(couponDistributeManageVo.getTitle());
            couponShareRecordVo.setContent(couponDistributeManageVo.getContent());
            couponShareRecordVo.setStartdate(couponDistributeManageVo.getStartdate());
            couponShareRecordVo.setEnddate(couponDistributeManageVo.getEnddate());
            couponShareRecordVo.setAmount(couponDistributeManageVo.getAmount());
            couponShareRecordVo.setFullamount(couponDistributeManageVo.getFullamount());
            couponShareRecordVo.setScope(couponDistributeManageVo.getScope());
            couponShareRecordVo.setShopid(couponDistributeManageVo.getShopid());
            couponShareRecordVo.setChannel(couponDistributeManageVo.getChannel());
            couponShareRecordVo.setTrasurl(couponDistributeManageVo.getTrasurl());
            couponShareRecordVo.setBannerurl(couponDistributeManageVo.getBannerurl());
            couponShareRecordVo.setCouponrule(couponDistributeManageVo.getCouponrule());
            couponShareRecordVo.setType(couponDistributeManageVo.getType());
            couponShareRecordVo.setStatus(0);
            if (couponDistributeManageVo.getScope() == 0) {
                couponShareRecordVo.setShopname("全场通用");
            } else if (couponDistributeManageVo.getScope() == 1) {
                couponShareRecordVo.setShopname("美番券");
            } else if (couponDistributeManageVo.getScope() == 2) {
                couponShareRecordVo.setShopname(goodsService.queryShopnameById(couponDistributeManageVo.getShopid()) + "券");
            }
            couponShareRecordVo.setIsHaveGet(0);

        }
        //支付成功后分享优惠券到第三方平台前获取分享的优惠券信息
        return couponShareRecordVo;
    }

    @Transactional
    public Map<String, Object> getCouponDistributeInfo(String id, String phone) {
        int flag = 0;//定义标志位

        //封装领取的优惠券返回体
        CouponTempVo couponTempVo = new CouponTempVo();

        //根据分享id查询当前分享的优惠券详情
        CouponDistributeManageVo couponDistributeManageVo = couponService.getCouponDistributeInfoById(Integer.parseInt(id));

        //首先检查该用户是否领取过该优惠券
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("id", couponDistributeManageVo.getId());
        parammap.put("phone", phone);
        int sum = couponService.checkIsHave(parammap);

        if (sum == 0) {
            String title = couponDistributeManageVo.getTitle();
            String content = couponDistributeManageVo.getContent();
            int type = couponDistributeManageVo.getScope();
            int shopid = 0;
            if (type == 2) {
                shopid = couponDistributeManageVo.getShopid();
            }
            int statue = '0';
            Date begintime = couponDistributeManageVo.getStartdate();
            Date endtime = couponDistributeManageVo.getEnddate();
            Date intime = new Date();
            double tmoney = couponDistributeManageVo.getAmount();
            double usemoney = couponDistributeManageVo.getFullamount();
            int isdel = 0;

            //封装优惠券临时表实体,写入表中
            CouponTemp couponTemp = new CouponTemp();
            couponTemp.setPhone(phone);
            couponTempVo.setPhone(phone);
            couponTemp.setTitle(title);
            couponTempVo.setTitle(title);
            couponTemp.setContent(content);
            couponTempVo.setContent(content);
            couponTemp.setType(type);
            couponTempVo.setType(type);
            if (type == 2) {
                couponTemp.setShopid(shopid);
                couponTempVo.setShopid(shopid);
                //根据shopid查询店铺名称
                String shopname = cartService.queryShopName(shopid);
                couponTempVo.setShopname(shopname);
            } else if (type == 1) {
                couponTemp.setShopid(-1);
                couponTempVo.setShopid(-1);
                couponTempVo.setShopname("美番");
            }
            couponTemp.setStatue(statue);
            couponTempVo.setStatue(statue);
            couponTemp.setBegintime(begintime);
            couponTempVo.setBegintime(begintime);
            couponTemp.setEndtime(endtime);
            couponTempVo.setEndtime(endtime);
            couponTemp.setIntime(intime);
            couponTempVo.setIntime(intime);
            couponTemp.setTmoney(tmoney);
            couponTempVo.setTmoney(tmoney);
            couponTemp.setUsemoney(usemoney);
            couponTempVo.setUsemoney(usemoney);
            couponTemp.setIsdel(isdel);
            couponTempVo.setIsdel(isdel);
            couponService.insertCouponTemp(couponTemp);

            //根据分享id更新分享记录表
            couponService.updateCouponShareRecord(Integer.parseInt(id));

            //插入优惠券领取记录表
            CouponReceiveRecord couponReceiveRecord = new CouponReceiveRecord();
            couponReceiveRecord.setPhone(phone);
            couponReceiveRecord.setDistributeid(couponDistributeManageVo.getId());
            couponReceiveRecord.setIntime(new Date());
            couponService.insertCouponReceiveRecord(couponReceiveRecord);

            flag = 1;
        } else if (sum > 0) {
            flag = -1;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("couponTempVo", couponTempVo);
        map.put("flag", flag);
        return map;
    }

    public List<Coupon> findAllMyCouponList(Paging<Coupon> paging, int userid, int status) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("status", status);
        return couponService.findAllMyCouponList(paging, map);
    }

    /**
     * 后台管理--查询用户优惠券列表
     *
     * @param pager
     * @param userid
     * @return
     */
    public List<CouponVo> queryDiscountCouponList(String userid, Paging<CouponVo> pager) {
        return couponService.queryDiscountCouponList(userid, pager);
    }

}

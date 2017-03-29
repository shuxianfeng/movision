package com.movision.mybatis.coupon.service;

import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.mapper.CouponMapper;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManage;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;
import com.movision.mybatis.couponDistributeManage.mapper.CouponDistributeManageMapper;
import com.movision.mybatis.couponReceiveRecord.entity.CouponReceiveRecord;
import com.movision.mybatis.couponReceiveRecord.mapper.CouponReceiveRecordMapper;
import com.movision.mybatis.couponShareRecord.entity.CouponShareRecord;
import com.movision.mybatis.couponShareRecord.mapper.CouponShareRecordMapper;
import com.movision.mybatis.couponTemp.entity.CouponTemp;
import com.movision.mybatis.couponTemp.mapper.CouponTempMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/18 11:11
 */
@Service
@Transactional
public class CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponDistributeManageMapper couponDistributeManageMapper;

    @Autowired
    private CouponShareRecordMapper couponShareRecordMapper;

    @Autowired
    private CouponTempMapper couponTempMapper;

    @Autowired
    private CouponReceiveRecordMapper couponReceiveRecordMapper;

    private Logger log = LoggerFactory.getLogger(CouponService.class);

    public List<Coupon> queryCanUseCoupon(int userid) {
        try {
            log.info("根据用户id查询用户名下所有可用优惠券列表");
            return couponMapper.queryCanUseCoupon(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户名下所有可用优惠券列表失败");
            throw e;
        }
    }

    public List<Coupon> queryBeUsedCoupon(int userid) {
        try {
            log.info("根据用户id查询用户名下所有已用优惠券列表");
            return couponMapper.queryBeUsedCoupon(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户名下所有已用优惠券列表失败");
            throw e;
        }
    }

    public List<Coupon> queryHaveOverdueCoupon(int userid) {
        try {
            log.info("根据用户id查询用户名下所有过期失效的优惠券列表");
            return couponMapper.queryHaveOverdueCoupon(userid);
        } catch (Exception e) {
            log.error("根据用户id查询用户名下所有过期失效的优惠券列表失败");
            throw e;
        }
    }

    public List<CouponDistributeManageVo> queryCurReceiveCoupon() {
        try {
            log.info("查询当前可领取的优惠券列表");
            return couponDistributeManageMapper.queryCurReceiveCoupon();
        } catch (Exception e) {
            log.error("查询当前可领取的优惠券列表失败");
            throw e;
        }
    }

    public int checkCoupon(int id) {
        try {
            log.info("检查是否有合法的可领取优惠券");
            return couponDistributeManageMapper.checkCoupon(id);
        } catch (Exception e) {
            log.error("检查是否有合法的可领取优惠券失败");
            throw e;
        }
    }

    public int checkIsHaveGet(Map<String, Object> parammap) {
        try {
            log.info("检查该用户是不是已经领取过该优惠券");
            return couponDistributeManageMapper.checkIsHaveGet(parammap);
        } catch (Exception e) {
            log.info("检查该用户是不是已经领取过该优惠券失败");
            throw e;
        }
    }

    @Transactional
    public void getCoupon(Map<String, Object> parammap) throws Exception {
        try {
            log.info("用户领取优惠券");
            //首先扣减系统优惠券的剩余张数和已领张数
            couponDistributeManageMapper.deductCoupon(parammap);

            //给用户表里增加一条优惠券
            CouponDistributeManage couponDistributeManage = couponDistributeManageMapper.selectByPrimaryKey((int) parammap.get("id"));
            Coupon coupon = new Coupon();
            coupon.setUserid((int) parammap.get("userid"));
            coupon.setTitle(couponDistributeManage.getTitle());
            coupon.setContent(couponDistributeManage.getContent());
            coupon.setType(couponDistributeManage.getScope());
            coupon.setShopid(couponDistributeManage.getShopid());
            coupon.setCategory(-1);
            coupon.setStatue(0);
            coupon.setBegintime(couponDistributeManage.getStartdate());
            coupon.setEndtime(couponDistributeManage.getEnddate());
            coupon.setIntime(new Date());
            coupon.setTmoney(couponDistributeManage.getAmount());
            coupon.setUsemoney(couponDistributeManage.getFullamount());
            coupon.setIsdel(0);
            couponMapper.insertSelective(coupon);

            //记录一条用户的领取记录
            parammap.put("intime", new Date());
            couponDistributeManageMapper.insertGetRecord(parammap);

//            throw new RuntimeException("抛出个运行时异常！");//经测试抛运行时异常，事务能够正常全部回滚
        } catch (Exception e) {
            log.error("用户领取优惠券失败");
            throw e;
        }
    }

    public int checkHaveDistribute() {
        try {
            log.info("检查当前是否存在可分享的优惠券活动");
            return couponDistributeManageMapper.checkHaveDistribute();
        } catch (Exception e) {
            log.error("检查当前是否存在可分享的优惠券活动失败");
            throw e;
        }
    }

    public CouponDistributeManageVo getCouponDistributeInfo() {
        try {
            log.info("分享优惠券到第三方平台前先获取分享的优惠券信息");
            return couponDistributeManageMapper.getCouponDistributeInfo();
        } catch (Exception e) {
            log.error("分享优惠券到第三方平台前先获取分享的优惠券信息失败");
            throw e;
        }
    }

    public int checkIsHave(Map<String, Object> parammap) {
        try {
            log.info("检查该用户有没有领取过该优惠券");
            return couponReceiveRecordMapper.checkIsHave(parammap);
        } catch (Exception e) {
            log.error("检查该用户有没有领取过该优惠券失败");
            throw e;
        }
    }

    public CouponDistributeManageVo getCouponDistributeInfoById(int id) {
        try {
            log.info("通过分享记录id查询分享的优惠券信息");
            return couponDistributeManageMapper.getCouponDistributeInfoById(id);
        } catch (Exception e) {
            log.error("通过分享记录id查询分享的优惠券信息失败");
            throw e;
        }
    }

    public void insertCouponTemp(CouponTemp couponTemp) {
        try {
            log.info("插入一条优惠券临时记录");
            couponTempMapper.insertSelective(couponTemp);
        } catch (Exception e) {
            log.error("插入一条优惠券临时记录表失败");
            throw e;
        }
    }

    public void updateCouponShareRecord(int id) {
        try {
            log.info("更新分享记录");
            couponShareRecordMapper.updateCouponShareRecord(id);
        } catch (Exception e) {
            log.error("更新分享记录失败");
            throw e;
        }
    }

    public void insertCouponReceiveRecord(CouponReceiveRecord couponReceiveRecord) {
        try {
            log.info("插入优惠券领取记录表");
            couponReceiveRecordMapper.insertSelective(couponReceiveRecord);
        } catch (Exception e) {
            log.error("插入优惠券领取记录表失败");
            throw e;
        }
    }

    public void insertCouponShareRecord(CouponShareRecord couponShareRecord) {
        try {
            log.info("优惠券分享记录插入到表中");
            couponShareRecordMapper.insertSelective(couponShareRecord);
        } catch (Exception e) {
            log.error("优惠券分享记录插入到表中失败");
            throw e;
        }
    }

    public void updateCouponDistributeInfo(int id) {
        try {
            log.info("更新优惠券分发管理表中的信息");
            couponDistributeManageMapper.updateCouponDistributeInfo(id);
        } catch (Exception e) {
            log.error("更新优惠券分发管理表中的信息失败");
            throw e;
        }
    }

    public List<Coupon> findAllMyCouponList(Paging<Coupon> paging, Map map) {
        try {
            log.info("查询我的优惠券列表");
            return couponMapper.findAllMyCouponList(paging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我的优惠券列表失败", e);
            throw e;
        }
    }

    /**
     * 查询用户优惠券列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<Coupon> queryDiscountCouponList(String userid, Paging<Coupon> pager) {
        try {
            log.info("查询用户优惠券列表");
            return couponMapper.queryDiscountCouponList(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询用户优惠券列表异常");
            throw e;
        }
    }

    public List<Coupon> queryCouponList(int userid) {
        try {
            log.info("根据用户id查询所有可用优惠券");
            return couponMapper.queryCouponList(userid);
        } catch (Exception e) {
            log.error("根据用户id查询所有可用优惠券失败");
            throw e;
        }
    }

    public Coupon queryCouponById(int couponid) {
        try {
            log.info("根据优惠券id查询优惠券信息");
            return couponMapper.queryCouponById(couponid);
        } catch (Exception e) {
            log.error("根据优惠券id查询优惠券信息失败");
            throw e;
        }
    }

    public void useCoupon(int couponid) {
        try {
            log.info("使用后扣除优惠券");
            couponMapper.useCoupon(couponid);
        } catch (Exception e) {
            log.error("使用后扣除优惠券失败");
            throw e;
        }
    }

    public List<CouponTemp> checkIsGetCoupon(String phone) {
        try {
            log.info("检查当前手机号是否领取过该优惠券");
            return couponTempMapper.checkIsGetCoupon(phone);
        } catch (Exception e) {
            log.error("检查当前手机号是否领取过该优惠券失败");
            throw e;
        }
    }

    public void insertCouponList(List<Coupon> couponList) {
        try {
            log.info("插入正式的优惠券列表");
            couponMapper.insertCouponList(couponList);
        } catch (Exception e) {
            log.error("插入正式的优惠券列表失败");
            throw e;
        }
    }

    public void delCouponTemp(String phone) {
        try {
            log.info("删除优惠券领取临时表");
            couponTempMapper.delCouponTemp(phone);
        } catch (Exception e) {
            log.error("删除优惠券领取临时表失败");
            throw e;
        }
    }
}

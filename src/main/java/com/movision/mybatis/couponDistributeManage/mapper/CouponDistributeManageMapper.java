package com.movision.mybatis.couponDistributeManage.mapper;

import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManage;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;

import java.util.List;
import java.util.Map;

public interface CouponDistributeManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponDistributeManage record);

    int insertSelective(CouponDistributeManage record);

    CouponDistributeManage selectByPrimaryKey(Integer id);

    int checkIsHaveGet(Map<String, Object> parammap);

    void insertGetRecord(Map<String, Object> parammap);

    List<CouponDistributeManageVo> queryCurReceiveCoupon();

    int checkCoupon(int id);

    void deductCoupon(Map<String, Object> parammap);

    int updateByPrimaryKeySelective(CouponDistributeManage record);

    int updateByPrimaryKey(CouponDistributeManage record);
}
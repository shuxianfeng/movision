package com.movision.mybatis.couponDistributeManage.mapper;

import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManage;
import com.movision.mybatis.couponDistributeManage.entity.CouponDistributeManageVo;

import java.util.List;

public interface CouponDistributeManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponDistributeManage record);

    int insertSelective(CouponDistributeManage record);

    CouponDistributeManage selectByPrimaryKey(Integer id);

    List<CouponDistributeManageVo> queryCurReceiveCoupon();

    int updateByPrimaryKeySelective(CouponDistributeManage record);

    int updateByPrimaryKey(CouponDistributeManage record);
}
package com.movision.mybatis.couponShareRecord.mapper;

import com.movision.mybatis.couponShareRecord.entity.CouponShareRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponShareRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponShareRecord record);

    void updateCouponShareRecord(int id);

    int insertSelective(CouponShareRecord record);

    CouponShareRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponShareRecord record);

    int updateByPrimaryKey(CouponShareRecord record);
}
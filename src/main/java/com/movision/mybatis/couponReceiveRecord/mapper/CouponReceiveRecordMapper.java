package com.movision.mybatis.couponReceiveRecord.mapper;

import com.movision.mybatis.couponReceiveRecord.entity.CouponReceiveRecord;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CouponReceiveRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponReceiveRecord record);

    int checkIsHave(Map<String, Object> parammap);

    int insertSelective(CouponReceiveRecord record);

    CouponReceiveRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponReceiveRecord record);

    int updateByPrimaryKey(CouponReceiveRecord record);
}
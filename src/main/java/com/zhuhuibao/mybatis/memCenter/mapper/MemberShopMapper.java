package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MemberShopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShop record);

    int insertSelective(MemberShop record);

    MemberShop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShop record);

    int updateByPrimaryKey(MemberShop record);

    MemberShop findByCompanyID(@Param("companyId") Long companyId);

    List<MemberShop> findAllByCondition(RowBounds rowBounds, Map<String, String> paramMap);

    Map<String,String> queryShopBanner(String id);
}
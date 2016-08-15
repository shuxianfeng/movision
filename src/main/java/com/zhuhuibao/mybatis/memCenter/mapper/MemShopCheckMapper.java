package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.MemShopCheck;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MemShopCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemShopCheck record);

    int insertSelective(MemShopCheck record);

    MemShopCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemShopCheck record);

    int updateByPrimaryKey(MemShopCheck record);

    MemShopCheck findByCompanyID(@Param("companyId") Long companyId);

    List<MemShopCheck> findAllByCondition(RowBounds rowBounds, Map<String, String> paramMap);
}
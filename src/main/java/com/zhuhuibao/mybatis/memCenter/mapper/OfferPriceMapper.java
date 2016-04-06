package com.zhuhuibao.mybatis.memCenter.mapper;

import java.util.*;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;

public interface OfferPriceMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OfferPrice record);

    OfferPrice selectByPrimaryKey(Long id);
    
    List<AskPriceSimpleBean> findAllAskingPriceInfo(RowBounds rowBounds,AskPrice price);
    
    List<AskPriceSimpleBean> findAllOfferedPriceInfo(RowBounds rowBounds,Map<String,String> priceMap);

    int updateByPrimaryKeySelective(OfferPrice record);

}
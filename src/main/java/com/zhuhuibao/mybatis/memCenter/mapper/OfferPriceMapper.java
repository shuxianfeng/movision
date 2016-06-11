package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferAskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface OfferPriceMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OfferPrice record);

    OfferPrice selectByPrimaryKey(Long id);
    
    List<AskPriceSimpleBean> findAllAskingPriceInfo(RowBounds rowBounds,AskPrice price);
    
    List<AskPriceSimpleBean> findAllOfferedPriceInfo(RowBounds rowBounds,Map<String,String> priceMap);

    int updateByPrimaryKeySelective(OfferPrice record);
    
    List<AskPriceSimpleBean> queryAllOfferPriceByAskID(Long id);
    
    OfferAskPrice queryOfferPriceInfoByID(Long id);
}
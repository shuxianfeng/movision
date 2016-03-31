package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.AskPriceBean;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;

public interface AskPriceMapper {

    int saveAskPrice(AskPrice record);

    AskPriceBean queryAskPriceByID(String id);

}
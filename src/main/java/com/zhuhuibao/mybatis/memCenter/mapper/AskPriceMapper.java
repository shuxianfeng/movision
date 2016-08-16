package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AskPriceMapper {

    int saveAskPrice(AskPrice record);

    AskPriceBean queryAskPriceByID(String id);

    List<AskPriceResultBean> findAll(AskPriceSearchBean askPriceSearch);

    List<AskPriceResultBean> findAllByPager1(RowBounds rowBounds, AskPriceSearchBean askPriceSearch);

    List<AskPrice> find();

    List<AskPrice> queryNewPriceInfo(@Param("count") int count,@Param("createid")String createid);

    List<AskPrice> findAllNewPriceInfoList(RowBounds rowBounds,AskPriceSearchBean askPriceSearch);

    AskPriceBean queryAskPrice(Map<String,Object> map);

}
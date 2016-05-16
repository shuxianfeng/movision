package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

/**
 * 招中标公告DAO层
 * @author  penglong
 * @create 2016-05-13
 */
public interface TenderTonedMapper {
    int insertSelective(TenderToned record);

    TenderToned selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TenderToned record);

    List<TenderToned> findAllTenderTonedPager(Map<String,Object> map,RowBounds rowBounds);

    List<TenderToned> queryLatestTenderToned(Map<String,Object> map);
}
package com.movision.mybatis.weixinlist.mapper;

import com.movision.mybatis.weixinlist.entity.WeixinList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Random;

public interface WeixinListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WeixinList record);

    int insertSelective(WeixinList record);

    WeixinList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WeixinList record);

    int updateByPrimaryKey(WeixinList record);

    List<WeixinList> findAllList(RowBounds rowBounds);
}
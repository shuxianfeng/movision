package com.movision.mybatis.goods.mapper;

import com.movision.mybatis.goods.entity.Goods;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<Goods> queryActiveGoods(RowBounds rowBounds, int postid);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}
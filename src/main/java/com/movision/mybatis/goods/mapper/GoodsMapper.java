package com.movision.mybatis.goods.mapper;

import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<GoodsVo> queryActiveGoods(RowBounds rowBounds, int postid);

    List<GoodsVo> queryMonthHot();

    List<GoodsVo> queryDefaultGoods(Map<String, Object> parammap);

    List<GoodsVo> queryWeekHot();

    List<GoodsVo> queryAllMonthHot(RowBounds rowBounds);

    List<GoodsVo> queryAllWeekHot(RowBounds rowBounds);

    List<GoodsVo> queryLastDayGodList();

    List<GoodsVo> queryAllGodRecommend(RowBounds rowBounds);

    List<GoodsVo> queryEssenceGoods();

    List<GoodsVo> queryHotGoods();

    GoodsDetail queryGoodDetail(int goodsid);

    List<GoodsImg> queryGoodsImgList(int goodsid);

    int queryStore(int goodsid);

    List<GoodsVo> queryComboGoodsList(int comboid);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}
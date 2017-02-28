package com.movision.mybatis.goods.mapper;

import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import org.apache.ibatis.session.RowBounds;
import org.apache.taglibs.standard.lang.jstl.Literal;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<GoodsVo> queryActiveGoods(int postid);

    List<GoodsVo> queryMonthHot();

    List<GoodsVo> queryDefaultGoods(Map<String, Object> parammap);

    List<GoodsVo> queryWeekHot();

    List<GoodsVo> findAllMonthHot(RowBounds rowBounds);

    List<GoodsVo> findAllWeekHot(RowBounds rowBounds);

    List<GoodsVo> queryLastDayGodList();

    List<GoodsVo> findAllGodRecommend(RowBounds rowBounds);

    List<GoodsVo> queryEssenceGoods();

    List<GoodsVo> queryHotGoods();

    GoodsDetail queryGoodDetail(int goodsid);

    List<GoodsImg> queryGoodsImgList(int goodsid);

    int queryStore(int goodsid);

    List<GoodsVo> queryComboGoodsList(int comboid);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<GoodsVo> findAllGoodsList(RowBounds rowBounds);//查询商品列表

    int deleteGoods(Integer id);//删除商品

    int AddToGoods(Integer id);//上架

    int queryisdel(Integer id);
    int DownToGoods(Integer id);//下架
    List<GoodsVo> findAllGoodsCondition(Map map, RowBounds rowBounds);//条件查询

    int recommendHot(Integer id);//推荐到热门

    int recommendisessence(Integer id);//推荐到精选

    int updateDate(GoodsVo goodsVo);//修改推荐日期

    List<GoodsVo> findAllQueryPostByGoodsList(RowBounds rowBounds);//查询商品列表（帖子使用）

    List<Goods> findAllMyCollectGoodsList(RowBounds rowBounds, Map<String, Object> map);//查询我收藏的商品列表

    List<GoodsVo> findAllQueryLikeGoods(Map map, RowBounds rowBounds);//查询商品列表，联合搜索（帖子使用）

    List<GoodsVo> findAllCombo(RowBounds rowBounds);//查询套餐列表

    List<GoodsVo> findAllType();//查询商品分类

    List<GoodsVo> findAllBrand();//查看品牌

    GoodsVo findGoodDetail(Integer id);//商品详情

    int updateGoods(GoodsVo goodsVo);//修改商品

    int updateImage(GoodsImg goodsImg);//修改图片

    int updateCom(Integer id);//取消推荐

    int todayCommend(Integer id);//今日推荐

    List<GoodsAssessmentVo> findAllAssessment(RowBounds rowBounds);//查询评价列表

    List<GoodsAssessmentVo> findAllAssessmentCondition(Map map, RowBounds rowBounds);//条件查询商品

    GoodsAssessmentVo queryAssessmentRemark(Integer id);//评论详情

    List<GoodsImg> queryImgGoods(Integer id);//商品参数图

    List<GoodsImg> queryCommodityDescription(Integer id);//商品描述图

    List<GoodsImg> queryblueprint(Integer id);//晒图

}
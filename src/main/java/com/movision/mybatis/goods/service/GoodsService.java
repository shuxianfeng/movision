package com.movision.mybatis.goods.service;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/3 19:38
 */
@Service
@Transactional
public class GoodsService {

    private static Logger log = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public List<GoodsVo> queryActiveGoods(Paging<Goods> pager, String postid) {
        try {
            log.info("查询商城促销类商品列表");
            return goodsMapper.queryActiveGoods(pager.getRowBounds(), Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询商城促销类商品列表失败");
            throw e;
        }

    }

    public List<GoodsVo> queryMonthHot() {
        try {
            log.info("查询月度销量前十商品列表");
            return goodsMapper.queryMonthHot();
        } catch (Exception e) {
            log.error("查询月度销量前十商品列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryDefaultGoods(Map<String, Object> parammap) {
        try {
            log.info("查询月度热销商品的缺省商品列表");
            return goodsMapper.queryDefaultGoods(parammap);
        } catch (Exception e) {
            log.error("查询月度热销商品的缺省商品列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryWeekHot() {
        try {
            log.info("查询一周销量前十商品列表");
            return goodsMapper.queryWeekHot();
        } catch (Exception e) {
            log.error("查询一周销量前十商品列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryAllMonthHot(Paging<GoodsVo> pager) {
        try {
            log.info("月度热销商品--点击查看全部接口返回列表");
            return goodsMapper.queryAllMonthHot(pager.getRowBounds());
        } catch (Exception e) {
            log.error("月度热销商品--点击查看全部接口返回列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryAllWeekHot(Paging<GoodsVo> pager) {
        try {
            log.info("一周热销商品--点击查看全部接口返回列表");
            return goodsMapper.queryAllWeekHot(pager.getRowBounds());
        } catch (Exception e) {
            log.error("一周热销商品--点击查看全部接口返回列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryLastDayGodList() {
        try {
            log.info("查询最近一期的推荐神器列表");
            return goodsMapper.queryLastDayGodList();
        } catch (Exception e) {
            log.error("查询最近一期的推荐神器列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryAllGodRecommend(Paging<GoodsVo> pager) {
        try {
            log.info("查询往期每日神器推荐列表");
            return goodsMapper.queryAllGodRecommend(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询往期每日神器推荐列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryEssenceGoods() {
        try {
            log.info("查询商城首页精华商品列表");
            return goodsMapper.queryEssenceGoods();
        } catch (Exception e) {
            log.error("查询商城首页精华商品列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryHotGoods() {
        try {
            log.info("查询商城首页热门商品列表");
            return goodsMapper.queryHotGoods();
        } catch (Exception e) {
            log.error("查询商城首页热门商品列表失败");
            throw e;
        }
    }

    public List<Category> queryGoodsCategory() {
        try {
            log.info("查询商城首页商品分类列表");
            return categoryMapper.queryGoodsCategory();
        } catch (Exception e) {
            log.error("查询商城首页商品分类列表失败");
            throw e;
        }
    }

    public GoodsDetail queryGoodDetail(int goodsid) {
        try {
            log.info("根据商品id查询商品详情");
            return goodsMapper.queryGoodDetail(goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询商品详情失败");
            throw e;
        }
    }

    public List<GoodsImg> queryGoodsImgList(int goodsid) {
        try {
            log.info("根据商品id查询商品实物图列表");
            return goodsMapper.queryGoodsImgList(goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询商品实物图列表失败");
            throw e;
        }
    }
}

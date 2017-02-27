package com.movision.mybatis.goods.service;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.mapper.AddressMapper;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import com.movision.mybatis.combo.entity.Combo;
import com.movision.mybatis.combo.entity.ComboVo;
import com.movision.mybatis.combo.mapper.ComboMapper;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentCategery;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessment.mapper.GoodsAssessmentMapper;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import com.movision.mybatis.role.entity.Role;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;
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

    @Autowired
    private GoodsAssessmentMapper goodsAssessmentMapper;

    @Autowired
    private ComboMapper comboMapper;

    @Autowired
    private AddressMapper addressMapper;

    public List<GoodsVo> queryActiveGoods(String postid) {
        try {
            log.info("查询商城促销类商品列表");
            return goodsMapper.queryActiveGoods(Integer.parseInt(postid));
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
            return goodsMapper.findAllMonthHot(pager.getRowBounds());
        } catch (Exception e) {
            log.error("月度热销商品--点击查看全部接口返回列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryAllWeekHot(Paging<GoodsVo> pager) {
        try {
            log.info("一周热销商品--点击查看全部接口返回列表");
            return goodsMapper.findAllWeekHot(pager.getRowBounds());
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
            return goodsMapper.findAllGodRecommend(pager.getRowBounds());
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

    public List<GoodsAssessmentVo> queryGoodsAssessment(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询所有商品评论");
            return goodsAssessmentMapper.findAllGoodsAssessment(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询所有商品评论失败");
            throw e;
        }
    }

    public List<GoodsAssessmentVo> queryImgGoodsAssessment(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询所有有图评论");
            return goodsAssessmentMapper.findAllImgGoodsAssessment(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询所有有图评论失败");
            throw e;
        }
    }

    public List<GoodsAssessmentVo> queryQualityGoodsAssessment(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询所有质量好的评论");
            return goodsAssessmentMapper.findAllQualityGoodsAssessment(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询所有质量好的评论失败");
            throw e;
        }
    }

    public List<GoodsAssessmentVo> queryFastGoodsAssessment(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询送货快的评论");
            return goodsAssessmentMapper.findAllFastGoodsAssessment(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询送货快的评论失败");
            throw e;
        }
    }

    public List<GoodsAssessmentVo> queryAttitudeGoodsAssessment(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询态度好的评论");
            return goodsAssessmentMapper.findAllAttitudeGoodsAssessment(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询态度好的评论失败");
            throw e;
        }
    }

    public List<GoodsAssessmentVo> queryQualityGeneral(Paging<GoodsAssessmentVo> pager, int goodsid) {
        try {
            log.info("根据商品id查询质量一般的评论");
            return goodsAssessmentMapper.findAllQualityGeneral(pager.getRowBounds(), goodsid);
        } catch (Exception e) {
            log.error("根据商品id查询质量一般的评论失败");
            throw e;
        }
    }

    public GoodsAssessmentVo queryPassessment(int pid) {
        try {
            log.info("根据父评论id查询父评论对象");
            return goodsAssessmentMapper.queryPassessment(pid);
        } catch (Exception e) {
            log.error("根据父评论id查询父评论对象失败");
            throw e;
        }
    }

    public List<GoodsAssessmentImg> queryGoodsAssessmentImg(int assessmentid) {
        try {
            log.info("根据评论id查询评论中的晒图列表");
            return goodsAssessmentMapper.queryGoodsAssessmentImg(assessmentid);
        } catch (Exception e) {
            log.error("根据评论id查询评论中的晒图列表失败");
            throw e;
        }
    }

    public GoodsAssessmentCategery queryAssessmentCategorySum(int goodsid) {
        try {
            log.info("查询各类商品评论的数量");
            return goodsAssessmentMapper.queryAssessmentCategorySum(goodsid);
        } catch (Exception e) {
            log.error("查询各类商品评论的数量失败");
            throw e;
        }
    }

    public int queryStore(int goodsid) {
        try {
            log.info("查询商品库存");
            return goodsMapper.queryStore(goodsid);
        } catch (Exception e) {
            log.error("查询商品库存失败");
            throw e;
        }
    }

    public List<ComboVo> queryCombo(int goodsid) {
        try {
            log.info("查询套餐类别");
            return comboMapper.queryCombo(goodsid);
        } catch (Exception e) {
            log.error("查询套餐类别失败");
            throw e;
        }
    }

    public int queryComboStork(int comboid) {
        try {
            log.info("查询套餐库存");
            return comboMapper.queryComboStork(comboid);
        } catch (Exception e) {
            log.error("查询套餐库存失败");
            throw e;
        }
    }

    public List<Address> queryAddressList(int userid) {
        try {
            log.info("查询该用户的所有收货地址列表");
            return addressMapper.queryAddressList(userid);
        } catch (Exception e) {
            log.error("查询该用户的所有收货地址列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryComboGoodsList(int comboid) {
        try {
            log.info("查询该商品的该套餐中包含的所有商品列表");
            return goodsMapper.queryComboGoodsList(comboid);
        } catch (Exception e) {
            log.error("查询该商品的该套餐中包含的所有商品列表失败");
            throw e;
        }
    }

    /**
     * 商品管理--查询商品列表
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryGoodsList(Paging<GoodsVo> pager) {
        try {
            log.info("查询商品列表");
            return goodsMapper.findAllGoodsList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询商品列表失败");
            throw e;
        }
    }

    /**
     * 商品管理--删除商品
     *
     * @param id
     * @return
     */
    public int deleteGoods(Integer id) {
        try {
            log.info("删除商品");
            return goodsMapper.deleteGoods(id);
        } catch (Exception e) {
            log.error("删除商品失败");
            throw e;
        }
    }

    /**
     * 商品管理--条件查询
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryGoodsCondition(Map map, Paging<GoodsVo> pager) {
        try {
            log.info("条件查询");
            return goodsMapper.findAllGoodsCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询失败");
            throw e;
        }
    }

    /**
     * 商品管理*--上架
     *
     * @param id
     * @return
     */
    public int queryByGoods(Integer id) {
        try {
            log.info("上架");
            return goodsMapper.AddToGoods(id);
        } catch (Exception e) {
            log.error("上架失败");
            throw e;
        }
    }

    /**
     * 商品管理-推荐到热门
     *
     * @param id
     * @return
     */
    public int queryHot(Integer id) {
        try {
            log.info("推荐到热门");
            return goodsMapper.recommendHot(id);
        } catch (Exception e) {
            log.error("推荐到热门失败");
            throw e;
        }
    }

    /**
     * 商品管理-推荐到精选
     *
     * @param id
     * @return
     */
    public int queryisessence(Integer id) {
        try {
            log.info("推荐到精选");
            return goodsMapper.recommendisessence(id);
        } catch (Exception e) {
            log.error("推荐到精选失败");
            throw e;
        }
    }

    /**
     * 商品管理--修改推荐日期
     *
     * @param goodsVo
     * @return
     */
    public int updateDate(GoodsVo goodsVo) {
        try {
            log.info("修改推荐日期");
            return goodsMapper.updateDate(goodsVo);
        } catch (Exception e) {
            log.error("修改推荐日期失败");
            throw e;
        }
    }

    /**
     * 用于查询商品列表（帖子使用）
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryPostByGoodsList(Paging<GoodsVo> pager) {
        try {
            log.info("查询商品列表");
            return goodsMapper.findAllQueryPostByGoodsList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询商品列表异常");
            throw e;
        }
    }

    /**
     * 查询我收藏的商品列表
     *
     * @param pager
     * @return
     */
    public List<Goods> findAllMyCollectGoodsList(Paging<Goods> pager, Map map) {
        try {
            log.info("查询我收藏的商品列表");
            return goodsMapper.findAllMyCollectGoodsList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我收藏的商品列表失败", e);
            throw e;
        }
    }

    /**
     * 用于条件查询商品列表（帖子使用）
     *
     * @param map
     * @param pager
     * @return
     */
    public List<GoodsVo> findAllQueryLikeGoods(Map map, Paging<GoodsVo> pager) {
        try {
            log.info("根据条件查询商品列表");
            return goodsMapper.findAllQueryLikeGoods(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据条件查询商品列表异常");
            throw e;
        }
    }

    /**
     * 查询套餐列表
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> findAllCombo(Paging<GoodsVo> pager) {
        try {
            log.info("查询套餐列表");
            return goodsMapper.findAllCombo(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询套餐列表失败", e);
            throw e;
        }
    }

}

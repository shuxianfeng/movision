package com.movision.mybatis.goods.service;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.mapper.AddressMapper;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.mapper.CategoryMapper;
import com.movision.mybatis.combo.entity.Combo;
import com.movision.mybatis.combo.entity.ComboVo;
import com.movision.mybatis.combo.mapper.ComboMapper;
import com.movision.mybatis.goods.entity.*;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentCategery;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessment.mapper.GoodsAssessmentMapper;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import com.movision.mybatis.goodscombo.entity.GoodsCombo;
import com.movision.mybatis.goodscombo.entity.GoodsComboDetail;
import com.movision.mybatis.goodscombo.entity.GoodsComboVo;
import com.movision.mybatis.role.entity.Role;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.executor.ExecutorException;
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

    public List<GoodsAssessmentVo> queryAllOfficialReply(int goodsid) {
        try {
            log.info("查询所有官方回复的信息");
            return goodsAssessmentMapper.queryAllOfficialReply(goodsid);
        } catch (Exception e) {
            log.error("查询所有官方回复的信息失败");
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
     * 商品管理--删除评价
     *
     * @param id
     * @return
     */
    public int deleteAssessment(Integer id) {
        try {
            log.info("删除商品");
            return goodsMapper.deleteAssessment(id);
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
     * 商品管理*--查询id
     *
     * @param id
     * @return
     */
    public int queryisdel(Integer id) {
        try {
            log.info("上架");
            return goodsMapper.queryisdel(id);
        } catch (Exception e) {
            log.error("上架失败");
            throw e;
        }
    }
    /**
     * 商品管理*--下架
     *
     * @param id
     * @return
     */
    public int queryByGoodsDown(Integer id) {
        try {
            log.info("下架");
            return goodsMapper.DownToGoods(id);
        } catch (Exception e) {
            log.error("下架失败");
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
    public List<Map> findAllMyCollectGoodsList(Paging<Map> pager, Map map) {
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
    public List<GoodsComboVo> findAllCombo(Paging<GoodsComboVo> pager) {
        try {
            log.info("查询套餐列表");
            return goodsMapper.findAllComboT(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询套餐列表失败", e);
            throw e;
        }
    }

    /**
     * 查询商品分类
     *
     * @return
     */
    public List<GoodsVo> findAllType() {
        try {
            log.info("查询商品分类");
            return goodsMapper.findAllType();
        } catch (Exception e) {
            log.info("查询商品分类失败", e);
            throw e;
        }
    }

    /**
     * 查询品牌
     *
     * @return
     */
    public List<GoodsVo> findAllBrand() {
        try {
            log.info("查询商品品牌");
            return goodsMapper.findAllBrand();
        } catch (Exception e) {
            log.info("查询商品品牌失败", e);
            throw e;
        }
    }

    /**
     * 商品详情
     *
     * @param id
     * @return
     */
    public GoodsVo findGoodDetail(Integer id) {
        try {
            log.info("商品详情");
            return goodsMapper.findGoodDetail(id);
        } catch (Exception e) {
            log.error("商品详情失败", e);
            throw e;
        }
    }

    /**
     * 修改商品
     *
     * @param goodsVo
     * @return
     */
    public int updateGoods(GoodsVo goodsVo) {
        try {
            log.info("修改商品");
            return goodsMapper.updateGoods(goodsVo);
        } catch (Exception e) {
            log.error("修改商品失败", e);
            throw e;
        }
    }

    /**
     * 修改图片
     *
     * @param map
     * @return
     */
    public int updateImage(Map map) {
        try {
            log.info("修改图片");
            return goodsMapper.updateImage(map);
        } catch (Exception e) {
            log.error("修改图片失败", e);
            throw e;
        }
    }

    /**
     * 取消今日推荐
     *
     * @param
     * @return
     */
    public int updateCom(Integer id) {
        try {
            log.info("取消今日推荐");
            return goodsMapper.updateCom(id);
        } catch (Exception e) {
            log.error("取消今日推荐", e);
            throw e;
        }
    }

    /**
     * 今日推荐
     *
     * @param id
     * @return
     */
    public Goods todayCommend(Integer id) {
        try {
            log.info("今日推荐");
            return goodsMapper.todayCommend(id);
        } catch (Exception e) {
            log.error("今日推荐失败", e);
            throw e;
        }
    }

    /**
     * 查询评价列表
     *
     * @param pager
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessment(Paging<GoodsAssessmentVo> pager) {
        try {

            log.info("查询评价列表");
            return goodsMapper.findAllAssessment(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询评价列表失败", e);
            throw e;
        }
    }

    /**
     * 条件查询评价列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessmentCodition(Map map, Paging<GoodsAssessmentVo> pager) {
        try {

            log.info("条件查询评价列表");
            return goodsMapper.findAllAssessmentCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询评价列表失败", e);
            throw e;
        }
    }

    /**
     * 评价详情
     *
     * @param id
     * @return
     */
    public GoodsAssessmentVo queryAssessmentRemark(Integer id) {
        try {
            log.info("评价详情");
            return goodsMapper.queryAssessmentRemark(id);
        } catch (Exception e) {
            log.error("评价详情失败", e);
            throw e;
        }
    }

    /**
     * 商品参数图
     *
     * @param goodsid
     * @return
     */
    public GoodsImg queryImgGoods(Integer goodsid) {
        try {
            log.info("商品参数图");
            return goodsMapper.queryImgGoods(goodsid);
        } catch (Exception e) {
            log.error("商品参数图失败", e);
            throw e;
        }
    }

    /**
     * 商品描述
     *
     * @param goodsid
     * @return
     */
    public GoodsImg queryCommodityDescription(Integer goodsid) {
        try {
            log.info("商品描述");
            return goodsMapper.queryCommodityDescription(goodsid);
        } catch (Exception e) {
            log.error("商品描述图失败", e);
            throw e;
        }
    }

    /**
     * 商品图片
     *
     * @param goodsid
     * @return
     */
    public List<GoodsImg> queryAllGoodsPicture(Integer goodsid) {
        try {
            log.info("商品描述");
            return goodsMapper.queryAllGoodsPicture(goodsid);
        } catch (Exception e) {
            log.error("商品描述图失败", e);
            throw e;
        }
    }
    /**
     * 晒图
     *
     * @param id
     * @return
     */
    public List<GoodsImg> queryblueprint(Integer id) {
        try {
            log.info("晒图");
            return goodsMapper.queryblueprint(id);
        } catch (Exception e) {
            log.error("晒图失败", e);
            throw e;
        }
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    public int deleteGoodsPicture(Integer id) {
        try {
            log.info("删除图片");
            return goodsMapper.deleteGoodsPicture(id);
        } catch (Exception e) {
            log.error("删除图片失败", e);
            throw e;
        }
    }

    /**
     * 回复评论
     *
     * @param goodsAssessment
     * @return
     */
    public int addAssessment(GoodsAssessment goodsAssessment) {
        try {
            log.info("回复评论");
            return goodsMapper.addAssessment(goodsAssessment);
        } catch (Exception e) {
            log.error("回复评论失败", e);
            throw e;
        }
    }

    /**
     * 添加参数图
     *
     * @param goodsImg
     * @return
     */
    public int updateImgGoods(GoodsImg goodsImg) {
        try {
            log.info("添加参数图");
            return goodsMapper.updateImgGoods(goodsImg);
        } catch (Exception e) {
            log.error("添加参数图失败", e);
            throw e;
        }
    }

    /**
     * 添加描述图
     *
     * @param img
     * @return
     */
    public int updateCommodityDescription(GoodsImg img) {
        try {
            log.info("添加描述图");
            return goodsMapper.updateCommodityDescription(img);
        } catch (Exception e) {
            log.error("添加描述图失败", e);
            throw e;
        }
    }

    /**
     * 增加商品图片
     *
     * @param goodsImg
     * @return
     */
    public int addPicture(GoodsImg goodsImg) {
        try {
            log.info("增加商品图片");
            return goodsMapper.addPicture(goodsImg);
        } catch (Exception e) {
            log.error("增加商品图片失败", e);
            throw e;
        }

    }

    /**
     * 增加商品
     *
     * @param goodsVo
     * @return
     */
    public int addGoods(GoodsVo goodsVo) {
        try {
            log.info("增加商品");
            return goodsMapper.addGoods(goodsVo);
        } catch (Exception e) {
            log.error("增加商品失败");
            throw e;
        }
    }

    /**
     * 根据id查询
     *
     * @param comboid
     * @return
     */
    public List<GoodsComboVo> findAllC(Integer comboid) {
        try {
            log.info("根据id查询");
            return goodsMapper.findAllC(comboid);
        } catch (Exception e) {
            log.error("根据id查询失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询商品
     *
     * @param comboid
     * @return
     */
    public GoodsComboVo queryName(Integer comboid) {
        try {
            log.info("根据id查询商品");
            return goodsMapper.queryName(comboid);
        } catch (Exception e) {
            log.error("根据id查询商品失败", e);
            throw e;
        }
    }

    /**
     * 删除套餐
     *
     * @param comboid
     * @return
     */
    public int deleteComGoods(Integer comboid) {
        try {
            log.info("删除套餐");
            return goodsMapper.deleteComGoods(comboid);
        } catch (Exception e) {
            log.error("删除套餐失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询套餐是否有商品
     *
     * @param comboid
     * @return
     */
    public int queryByCom(Integer comboid) {
        try {
            log.info("根据id查询套餐是否有商品");
            return goodsMapper.queryByCom(comboid);
        } catch (Exception e) {
            log.error("根据id查询套餐是否有商品失败");
            throw e;
        }
    }

    /**
     * 条件套餐搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<GoodsComboVo> findAllComCondition(Map map, Paging<GoodsComboVo> pager) {
        try {
            log.info("条件套餐搜索");
            return goodsMapper.findAllComCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件套餐搜索失败", e);
            throw e;

        }
    }

    /**
     * 根据id查询详情
     *
     * @param comboid
     * @return
     */
    public List<GoodsComboVo> findByIdCom(Integer comboid) {
        try {
            log.info("根据id查询详情");
            return goodsMapper.findByIdCom(comboid);
        } catch (Exception e) {
            log.error("根据id查询详情失败", e);
            throw e;
        }
    }


    /**
     * 根据id查询商品信息
     *
     * @param comboid
     * @param pager
     * @return
     */
    public List<GoodsCom> findAllGoods(Integer comboid, Paging<GoodsCom> pager) {

        try {
            log.info("根据id查询商品信息");
            return goodsMapper.findAllGoods(comboid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据id查询商品信息失败", e);
            throw e;
        }
    }

    /**
     * 根据套餐id查询库存
     *
     * @param comboid
     * @return
     */
    public Integer queryAllStock(Integer comboid) {
        try {
            log.info("根据套餐id查询库存");
            return goodsMapper.queryAllStock(comboid);
        } catch (Exception e) {
            log.error("根据套餐id查询库存失败", e);
            throw e;
        }
    }

    /**
     * 修改套餐
     *
     * @param goodsCom
     * @return
     */
    public Integer updateComDetail(Combo goodsCom) {
        try {
            log.info("修改套餐");
            return goodsMapper.updateComDetail(goodsCom);
        } catch (Exception e) {
            log.error("修改套餐失败", e);
            throw e;
        }
    }

    /**
     * 增加套餐
     *
     * @param combo
     * @return
     */
    public Integer addCom(Combo combo) {
        try {
            log.info("增加套餐");
            return goodsMapper.addCom(combo);
        } catch (Exception e) {
            log.error("增加套餐失败", e);
            throw e;
        }
    }

    /**
     * 根据套餐id插入商品
     *
     * @param goodsCom
     * @return
     */
    public Integer addGoods(GoodsCombo goodsCom) {
        try {
            log.info("根据套餐id插入商品");
            return goodsMapper.addComGoods(goodsCom);
        } catch (Exception e) {
            log.error("根据套餐id插入商品失败", e);
            throw e;
        }
    }

    /**
     * 增加参数图
     *
     * @param goodsImg
     * @return
     */
    public Integer addImgGoods(GoodsImg goodsImg) {
        try {
            log.info("增加参数图");
            return goodsMapper.addImgGoods(goodsImg);
        } catch (Exception e) {
            log.error("增加参数图失败", e);
            throw e;
        }
    }

    /**
     * 增加描述图
     *
     * @param goodsImg
     * @return
     */
    public Integer addCommodityDescription(GoodsImg goodsImg) {
        try {
            log.info("增加描述图");
            return goodsMapper.addCommodityDescription(goodsImg);
        } catch (Exception e) {
            log.error("增加描述图失败", e);
            throw e;
        }
    }


}

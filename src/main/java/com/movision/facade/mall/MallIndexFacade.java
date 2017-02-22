package com.movision.facade.mall;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/13 15:05
 */
@Service
public class MallIndexFacade {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private HomepageManageService homepageManageService;

    /**
     * 查询月度销量前十的商品列表
     * @return
     */
    public Map<String, Object> queryMonthWeekDayHot() {

        Map<String, Object> map = new HashMap<>();

        //月度
        List<GoodsVo> monthHotList;
        //先查询商城首页月度热销banner
        HomepageManage monthHomepage = homepageManageService.queryBanner(3);//商城--月度热销banner类型topictype为3
        map.put("monthHomepage", monthHomepage);
        //查询月热销榜前十商品
        monthHotList = goodsService.queryMonthHot();
        //如果热销榜商品不足10件（用其他商品随机填充）
        map.put("monthHotList", addDefaultGoods(monthHotList));//调用补充缺省商品方法addDefaultGoods

        //一周
        List<GoodsVo> weekHotList;
        //先查询商城首页一周热销banner
        HomepageManage weekHomepage = homepageManageService.queryBanner(4);//商城--一周热销banner类型topictype为4
        map.put("weekHomepage", weekHomepage);
        //查询一周热销榜前十商品
        weekHotList = goodsService.queryWeekHot();
        //如果热销榜商品不足10件（用其他商品随机填充）
        map.put("weekHotList", addDefaultGoods(weekHotList));//调用补充缺省商品方法addDefaultGoods

        //每日
        List<GoodsVo> dayGodRecommendList;
        //先查询商城首页一周热销banner
        HomepageManage dayGodRecommendHomepage = homepageManageService.queryBanner(5);//商城--每日神器推荐banner的topictype为5
        map.put("dayGodRecommendHomepage", dayGodRecommendHomepage);
        //查询最近的一天的推荐神器列表(目前最多显示10个商品)
        dayGodRecommendList = goodsService.queryLastDayGodList();
        map.put("dayGodRecommendList", dayGodRecommendList);

        return map;
    }

    public List<GoodsVo> addDefaultGoods(List<GoodsVo> list) {
        if (list.size() < 10) {
            List<GoodsVo> defaultList;//热销缺省商品列表
            int[] ids = new int[list.size()];//已经在月度/一周热销榜的商品id的数组
            for (int i = 0; i < list.size(); i++) {
                ids[i] = list.get(i).getId();
            }
            int defaultcount = 10 - list.size();//前十热销榜缺省数
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("ids", ids);
            parammap.put("defaultcount", defaultcount);
            defaultList = goodsService.queryDefaultGoods(parammap);
            for (int i = 0; i < defaultList.size(); i++) {
                list.add(defaultList.get(i));
            }
        }
        return list;
    }

    /**
     * 查询所有月度热销商品列表
     *
     * @return
     */
    public List<GoodsVo> queryAllMonthHot(Paging<GoodsVo> pager) {

        List<GoodsVo> allMonthHotList = goodsService.queryAllMonthHot(pager);

        return allMonthHotList;
    }

    /**
     * 查询所有一周热销商品列表
     *
     * @return
     */
    public List<GoodsVo> queryAllWeekHot(Paging<GoodsVo> pager) {

        List<GoodsVo> allWeekHotList = goodsService.queryAllWeekHot(pager);

        return allWeekHotList;
    }

    /**
     * 查询往期所有每日神器推荐列表
     */
    public List<GoodsVo> queryAllGodRecommend(Paging<GoodsVo> pager) {

        List<GoodsVo> dayGodRecommendList = goodsService.queryAllGodRecommend(pager);

        return dayGodRecommendList;
    }

    /**
     * 查询商城首页--精华商品列表
     */
    public List<GoodsVo> queryEssenceGoods() {
        return goodsService.queryEssenceGoods();
    }

    /**
     * 查询商城首页--热门商品列表
     */
    public List<GoodsVo> queryHotGoods() {
        return goodsService.queryHotGoods();
    }

    /**
     * 查询商城首页--商品分类列表
     */
    public List<Category> queryGoodsCategory() {
        return goodsService.queryGoodsCategory();
    }
}
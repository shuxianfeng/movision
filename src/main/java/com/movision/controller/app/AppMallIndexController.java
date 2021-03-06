package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.mall.MallIndexFacade;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/13 14:43
 * 用于提供APP商城首页所有接口
 */
@RestController
@RequestMapping("/app/mall/")
public class AppMallIndexController {
    @Autowired
    private MallIndexFacade mallIndexFacade;

    /**
     * 查询商城首页--月度一周每日（三块合并）热销榜单
     *
     * @return
     */
    @ApiOperation(value = "商城首页月度一周每日（三块合并）热销榜单返回接口", notes = "用户返回首页月度一周每日（三块合并）热销榜单数据", response = Response.class)
    @RequestMapping(value = "monthWeekDayHot", method = RequestMethod.POST)
    public Response queryMonthHot() {
        Response response = new Response();

        Map<String, Object> map = mallIndexFacade.queryMonthWeekDayHot();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

//    /**
//     * 查询商城首页--一周热销榜单
//     *
//     * @Return
//     */
//    @ApiOperation(value = "商城首页一周热销榜单返回接口", notes = "用户返回首页一周热销榜单数据", response = Response.class)
//    @RequestMapping(value = "weekHot", method = RequestMethod.POST)
//    public Response queryWeekHot() {
//        Response response = new Response();
//
//        Map<String, Object> map = mallIndexFacade.queryWeekHot();
//
//        if (response.getCode() == 200) {
//            response.setMessage("查询成功");
//        }
//        response.setData(map);
//        return response;
//    }

//    /**
//     * 查询商城首页--每日神器推荐
//     *
//     * @return
//     */
//    @ApiOperation(value = "商城首页--每日神器推荐", notes = "用户返回首页每日神器推荐板块数据", response = Response.class)
//    @RequestMapping(value = "dayGodRecommend", method = RequestMethod.POST)
//    public Response queryDayGodRecommend() {
//        Response response = new Response();
//
//        Map<String, Object> map = mallIndexFacade.queryDayGodRecommend();
//
//        if (response.getCode() == 200) {
//            response.setMessage("查询成功");
//        }
//        response.setData(map);
//        return response;
//    }

    /**
     * 查询商城首页--月度热销榜单“查看全部”
     *
     * @return
     */
    @ApiOperation(value = "商城首页月度热销榜单--点击“查看全部”接口", notes = "点击查看全部跳转到全部月度热销榜单分页列表", response = Response.class)
    @RequestMapping(value = "allMonthHot", method = RequestMethod.POST)
    public Response queryAllMonthHot(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<GoodsVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<GoodsVo> allMonthHotList = mallIndexFacade.queryAllMonthHot(pager);
        pager.result(allMonthHotList);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    /**
     * 查询商城首页--一周热销榜单“查看全部”
     *
     * @return
     */
    @ApiOperation(value = "商城首页一周热销榜单--点击“查看全部”接口", notes = "点击查看全部跳转到全部一周热销榜单分页列表", response = Response.class)
    @RequestMapping(value = "allWeekHot", method = RequestMethod.POST)
    public Response queryAllWeekHot(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<GoodsVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<GoodsVo> allWeekHotList = mallIndexFacade.queryAllWeekHot(pager);
        pager.result(allWeekHotList);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    /**
     * 查询商城首页--往期所有推荐的神器列表
     *
     * @return
     */
    @ApiOperation(value = "商城首页--往期推荐的所有神器列表", notes = "用户点击商城首页中每日神器推荐中的“查看全部”返回的数据接口")
    @RequestMapping(value = "allGodRecommend", method = RequestMethod.POST)
    public Response queryAllGodRecommend(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<GoodsVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<GoodsVo> allGodRecommendList = mallIndexFacade.queryAllGodRecommend(pager);
        pager.result(allGodRecommendList);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    /**
     * 查询商城首页--特惠模块接口
     *
     * @return
     */
    @ApiOperation(value = "查询商城首页--特惠模块列表接口", notes = "用户用于加载商城首页特惠板块数据列表")
    @RequestMapping(value = "essenceGoods", method = RequestMethod.POST)
    public Response queryEssenceGoods() {
        Response response = new Response();

        List<GoodsVo> essenceGoodsList = mallIndexFacade.queryEssenceGoods();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(essenceGoodsList);
        return response;
    }

    /**
     * 查询商城首页--热门模块接口
     */
    @ApiOperation(value = "查询商城首页--热门模块列表接口", notes = "用户用于加载商城首页热门板块数据列表")
    @RequestMapping(value = "hotGoods", method = RequestMethod.POST)
    public Response queryHotGoods() {
        Response response = new Response();

        List<GoodsVo> hotGoodsList = mallIndexFacade.queryHotGoods();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(hotGoodsList);
        return response;
    }

    /**
     * 商城首页--商品分类列表接口
     */
    @ApiOperation(value = "查询商城首页--商品分类列表接口", notes = "用户用于加载商城首页商品分类板块数据列表")
    @RequestMapping(value = "goodsCategory", method = RequestMethod.POST)
    public Response queryGoodsCategory() {
        Response response = new Response();

        List<Category> goodsCategoryList = mallIndexFacade.queryGoodsCategory();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsCategoryList);
        return response;
    }
}

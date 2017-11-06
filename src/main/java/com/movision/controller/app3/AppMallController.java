package com.movision.controller.app3;

import com.movision.common.Response;
import com.movision.facade.index.FacadePost;
import com.movision.facade.mall.MallIndexFacade;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/10/19 14:27
 * 3.0商城接口（含商城首页接口）
 */
@RestController
@RequestMapping("/app/mall3/")
public class AppMallController {

    @Autowired
    private MallIndexFacade mallIndexFacade;

    @Autowired
    private FacadePost facadePost;

    /**
     * 商城首页——限时特卖模块接口
     *
     * @return
     */
    @ApiOperation(value = "商城首页——限时特卖模块接口", notes = "用于返回3.0商城首页中的限时特卖商品列表（返回字段isend 0 未结束 1 已结束）", response = Response.class)
    @RequestMapping(value = "getFlashSale", method = RequestMethod.POST)
    public Response getFlashSale(){
        Response response = new Response();

        List<GoodsVo> FlashSaleGoodsList = mallIndexFacade.getFlashSale();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(FlashSaleGoodsList);

        return response;
    }

    /**
     * 商城首页——点击限时特卖进入限时特卖列表页接口
     *
     * @return
     */
    @ApiOperation(value= "商城首页——点击限时特卖进入限时特卖列表页接口", notes = "商城首页——点击限时特卖进入限时特卖列表页接口（返回字段isend 0 未结束 1 已结束）", response = Response.class)
    @RequestMapping(value = "findAllFlashSale", method = RequestMethod.POST)
    public Response findAllFlashSale(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<GoodsVo> flashSaleList = mallIndexFacade.findAllFlashSale(pager);

        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }
        pager.result(flashSaleList);
        response.setData(pager);

        return response;
    }

    /**
     * 商城首页——性价比推荐的两篇商品推荐贴
     *
     * @return
     */
    @ApiOperation(value = "商城首页——性价比推荐的两篇商品推荐贴查询接口", notes = "查询商城首页——性价比推荐的两篇商品推荐贴查询接口", response = Response.class)
    @RequestMapping(value = "getCostRecommendPost", method = RequestMethod.POST)
    public Response getCostRecommendPost(){
        Response response = new Response();

        List<Post> costRecommendPostList = facadePost.getCostRecommendPost();

        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }
        response.setData(costRecommendPostList);
        return response;
    }

    @ApiOperation(value="商城——性价比推荐点击后进入性价比推荐列表页", notes = "查询商城——性价比推荐点击后进入性价比推荐列表页数据", response = Response.class)
    @RequestMapping(value="findAllCostRecommendPostList", method = RequestMethod.POST)
    public Response findAllCostRecommendPostList(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<Post> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<Post> allCostRecommendPostList = facadePost.findAllCostRecommendPostList(pager);
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }
        pager.result(allCostRecommendPostList);
        response.setData(pager);
        return response;
    }

//    @ApiOperation(value="商城首页--热门商品模块展示五个目前最热门的商品（根据销量）", notes = "查询商城首页--热门商品模块展示五个目前最热门的商品（根据销量）", response = Response.class)
//    @RequestMapping(value = "getHotGoods", method = RequestMethod.POST)
//    public Response getHotGoods(){
//        Response response = new Response();
//
//        List<>
//
//        return response;
//    }
}

package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.order.AppOrderFacade;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.post.entity.Post;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/24 20:27
 */
@RestController
@RequestMapping("app/mine/")
public class MyInfoController {

    @Autowired
    private GoodsFacade goodsFacade;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private AppOrderFacade orderFacade;


    @ApiOperation(value = "查询我的达人页信息:收藏商品", notes = "查询我的达人页信息:收藏商品", response = Response.class)
    @RequestMapping(value = {"/get_my_info_goods"}, method = RequestMethod.POST)
    public Response getMyInfoGoods(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        //获取当前用户信息
        ShiroRealm.ShiroUser user = ShiroUtil.getAppUser();
        //获取当前用户id
        int userid = ShiroUtil.getAppUserID();
        //获取最喜欢的商品
        Paging<Goods> goodsPaging = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Goods> goodsList = goodsFacade.findAllMyCollectGoods(goodsPaging, userid);
        goodsPaging.result(goodsList);

        response.setData(goodsPaging);
        return response;
    }

    @ApiOperation(value = "查询我的达人页信息：收藏帖子/活动", notes = "查询我的达人页信息：收藏帖子/活动", response = Response.class)
    @RequestMapping(value = {"/get_my_info_post"}, method = RequestMethod.POST)
    public Response getMyInfoPost(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        //获取当前用户信息
        ShiroRealm.ShiroUser user = ShiroUtil.getAppUser();
        //获取当前用户id
        int userid = ShiroUtil.getAppUserID();

        // 获取收藏的精选：帖子/活动
        Paging<Post> postPaging = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Post> postList = postFacade.findAllMyCollectPostList(postPaging, userid);
        postPaging.result(postList);

        response.setData(postPaging);
        return response;
    }

    @ApiOperation(value = "我的订单列表", notes = "我的订单列表", response = Response.class)
    @RequestMapping(value = {"/get_my_order_list"}, method = RequestMethod.POST)
    public Response getMyOrderList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Paging<Orders> paging = new Paging<Orders>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Orders> ordersList = orderFacade.getMyOrderList(paging, ShiroUtil.getAppUserID());
        paging.result(ordersList);
        response.setData(paging);
        return response;
    }

    @ApiOperation(value = "我的礼券列表", notes = "我的礼券列表", response = Response.class)
    @RequestMapping(value = {"/get_my_coupon_list"}, method = RequestMethod.POST)
    public Response getMyCouponList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Paging<Orders> paging = new Paging<Orders>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        response.setData(paging);
        return response;
    }

}

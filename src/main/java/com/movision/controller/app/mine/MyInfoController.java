package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.coupon.CouponFacade;
import com.movision.facade.order.OrderAppFacade;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.post.entity.Post;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private OrderAppFacade orderFacade;

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CircleAppFacade circleAppFacade;


    @ApiOperation(value = "查询我的达人页信息:收藏商品", notes = "查询我的达人页信息:收藏商品", response = Response.class)
    @RequestMapping(value = {"/get_my_info_goods"}, method = RequestMethod.GET)
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
    @RequestMapping(value = {"/get_my_info_post"}, method = RequestMethod.GET)
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

    @ApiOperation(value = "查询我的订单列表", notes = "查询我的订单列表", response = Response.class)
    @RequestMapping(value = {"/get_my_order_list"}, method = RequestMethod.GET)
    public Response getMyOrderList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Paging<Orders> paging = new Paging<Orders>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Orders> ordersList = orderFacade.getMyOrderList(paging, ShiroUtil.getAppUserID());
        paging.result(ordersList);
        response.setData(paging);
        return response;
    }

    @ApiOperation(value = "查询我的礼券列表", notes = "查询我的礼券列表", response = Response.class)
    @RequestMapping(value = {"/get_my_coupon_list"}, method = RequestMethod.GET)
    public Response getMyCouponList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Paging<Coupon> paging = new Paging<Coupon>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Coupon> couponList = couponFacade.findAllMyCouponList(paging, ShiroUtil.getAppUserID());
        paging.result(couponList);
        response.setData(paging);
        return response;
    }

    @ApiOperation(value = "查询我关注的圈子列表", notes = "查询我关注的圈子列表", response = Response.class)
    @RequestMapping(value = {"/get_my_follow_circle_list"}, method = RequestMethod.GET)
    public Response getMyFollowCircleList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                          @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Paging<Circle> paging = new Paging<Circle>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Circle> circleList = circleAppFacade.findAllMyFollowCircleList(paging, ShiroUtil.getAppUserID());
        paging.result(circleList);
        response.setData(paging);
        return response;
    }




}

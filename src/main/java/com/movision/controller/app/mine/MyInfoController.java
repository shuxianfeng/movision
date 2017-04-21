package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.coupon.CouponFacade;
import com.movision.facade.im.ImFacade;
import com.movision.facade.index.SuggestionFacade;
import com.movision.facade.order.OrderAppFacade;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.PersonInfo;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    private OrderAppFacade orderFacade;

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CircleAppFacade circleAppFacade;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private SuggestionFacade suggestionFacade;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private ImFacade imFacade;


    @ApiOperation(value = "查询我的达人页信息:收藏商品", notes = "查询我的达人页信息:收藏商品", response = Response.class)
    @RequestMapping(value = {"/get_my_info_goods"}, method = RequestMethod.GET)
    public Response getMyInfoGoods(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        //获取当前用户id
        int userid = ShiroUtil.getAppUserID();
        //获取最喜欢的商品
        Paging<Map> goodsPaging = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map> goodsList = goodsFacade.findAllMyCollectGoods(goodsPaging, userid);
        goodsPaging.result(goodsList);

        response.setData(goodsPaging);
        return response;
    }

    @ApiOperation(value = "查询我的达人页信息：收藏帖子/活动", notes = "查询我的达人页信息：收藏帖子/活动", response = Response.class)
    @RequestMapping(value = {"/get_my_info_post"}, method = RequestMethod.GET)
    public Response getMyInfoPost(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
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
                                    @RequestParam(required = false, defaultValue = "10") String pageSize,
                                    @ApiParam(value = "券状态：0 可用 1 已用 2 失效") @RequestParam Integer status) throws Exception {
        Response response = new Response();
        Paging<Coupon> paging = new Paging<Coupon>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Coupon> couponList = couponFacade.findAllMyCouponList(paging, ShiroUtil.getAppUserID(), status);
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

    @ApiOperation(value = "查询我的积分页面:新手任务+每日任务", notes = "查询我的积分页面:新手任务+每日任务", response = Response.class)
    @RequestMapping(value = {"/get_my_point_data"}, method = RequestMethod.GET)
    public Response getMyPointData() throws Exception {
        Response response = new Response();
        Map map = pointRecordFacade.getMyAllTypePoint();
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "修改个人资料", notes = "修改个人资料", response = Response.class)
    @RequestMapping(value = "/update_my_info", method = RequestMethod.POST)
    public Response updateMyInfo(@ApiParam @ModelAttribute PersonInfo personInfo) {

        Response response = new Response();
        personInfo.setId(ShiroUtil.getAppUserID());
        userFacade.finishPersonDataProcess(personInfo);
        return response;
    }

    @ApiOperation(value = "上传个人资料头像图片", notes = "上传个人资料头像图片", response = Response.class)
    @RequestMapping(value = {"/upload_person_info_pic"}, method = RequestMethod.POST)
    public Response updateMyInfo(@RequestParam(value = "file", required = false) MultipartFile file) {

        Map m = movisionOssClient.uploadObject(file, "img", "person");
        String url = String.valueOf(m.get("url"));
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        map.put("width", m.get("width"));
        map.put("height", m.get("height"));
        return new Response(map);
    }


    @ApiOperation(value = "提交意见反馈", notes = "提交意见反馈", response = Response.class)
    @RequestMapping(value = "submit_suggestion", method = RequestMethod.POST)
    public Response insertSuggestion(@ApiParam(value = "选填手机号，qq号等") @RequestParam(required = false) String phone,
                                     @ApiParam(value = "反馈内容") @RequestParam String content) {
        Response response = new Response();
        suggestionFacade.insertSuggestion(phone, content);
        return response;
    }

    @ApiOperation(value = "签到", notes = "签到", response = Response.class)
    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public Response sign() {
        Response response = new Response();
        //签到送积分
        pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.sign.getCode());
        //给个人加积分
        userFacade.addPersonPointBySign(PointConstant.POINT.sign.getCode());

        return response;
    }

    @ApiOperation(value = "查询是否签到", notes = "查询是否签到", response = Response.class)
    @RequestMapping(value = "query_sign_or_not", method = RequestMethod.GET)
    public Response querySignOrNot() {
        Response response = new Response();
        response.setData(pointRecordFacade.signToday());
        return response;
    }

    @ApiOperation(value = "查询积分明细列表", notes = "查询积分明细列表", response = Response.class)
    @RequestMapping(value = "query_point_record_list", method = RequestMethod.GET)
    public Response queryPointRecordList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Map> paging = new Paging<Map>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map> list = pointRecordFacade.findAllMyPointList(paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }


    /*@ApiOperation(value = "模拟评论赚积分", notes = "模拟评论赚积分", response = Response.class)
    @RequestMapping(value = "test_add_point_record", method = RequestMethod.POST)
    public Response testAddPointRecord() {
        Response response = new Response();
        pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.comment.getCode(), 0);
        return response;
    }*/





}

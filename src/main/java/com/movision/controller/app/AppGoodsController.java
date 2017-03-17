package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.Goods.GoodsFacade;
import com.movision.facade.coupon.CouponFacade;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/15 18:17
 * 用于提供商品相关的所有接口
 */
@RestController
@RequestMapping("/app/goods/")
public class AppGoodsController {

    @Autowired
    private GoodsFacade goodsFacade;

    @Autowired
    private CouponFacade couponFacade;

    /**
     * 商品详情接口
     */
    @ApiOperation(value = "点击商品缩略图进入商品详情页面", notes = "用户点击商品缩略图返回商品详情数据接口", response = Response.class)
    @RequestMapping(value = "goodDetail", method = RequestMethod.POST)
    public Response queryGoodDetail(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                    @ApiParam(value = "用户id") @RequestParam String userid) {
        Response response = new Response();

        GoodsDetail goodsDetail = goodsFacade.queryGoodDetail(goodsid, userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsDetail);
        return response;
    }

    /**
     * 查询商品评论列表接口
     */
    @ApiOperation(value = "查询商品评论列表接口", notes = "用户点击商品评价，返回用户查询的评论分页列表", response = Response.class)
    @RequestMapping(value = "goodAssessment", method = RequestMethod.POST)
    public Response queryGoodAssessment(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                        @ApiParam(value = "评论类型(0全部 1有图 2质量好 3送货快 4态度不错 5质量一般)") @RequestParam String type,
                                        @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Map<String, Object> map = goodsFacade.queryGoodsAssessment(pageNo, pageSize, goodsid, type);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 商品评论接口
     */
    @ApiOperation(value = "商品评论接口", notes = "用户订单付款完成后进行商品评价的接口", response = Response.class)
    @RequestMapping(value = "insertGoodAssessment", method = RequestMethod.POST)
    public Response insertGoodAssessment(@ApiParam(value = "用户id") @RequestParam String userid,
                                         @ApiParam(value = "商品id") @RequestParam String goodsid,
                                         @ApiParam(value = "子订单id") @RequestParam String suborderid,
                                         @ApiParam(value = "商品评分") @RequestParam String goodpoint,
                                         @ApiParam(value = "物流评分") @RequestParam String logisticpoint,
                                         @ApiParam(value = "服务态度评分") @RequestParam String servicepoint,
                                         @ApiParam(value = "评价内容") @RequestParam String content,
                                         @ApiParam(value = "是否匿名：0 不匿名 1 匿名") @RequestParam String isanonymity,
                                         @ApiParam(value = "晒单图片url(多张图片url用英文逗号分隔)") @RequestParam(required = false) String imgsurl) {
        Response response = new Response();

        goodsFacade.insertGoodAssessment(userid, goodsid, suborderid, goodpoint, logisticpoint, servicepoint, content, isanonymity, imgsurl);

        if (response.getCode() == 200) {
            response.setMessage("评论成功");
        } else {
            response.setMessage("评论失败");
        }
        return response;
    }

    /**
     * 商品租用配置和套餐、活动选择接口
     */
    @ApiOperation(value = "商品租赁日期和套餐、活动选择接口", notes = "用户租用商品时，点击立即租用之前选择套餐类型枚举值数据返回。（返回200查询成功、返回300表示当前商品库存为0，日期选择页面不弹出并弱提示：您选择的商品已抢空）", response = Response.class)
    @RequestMapping(value = "rentChoice", method = RequestMethod.POST)
    public Response queryRentChoice(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                    @ApiParam(value = "商品定位：0 租赁 1 出售") @RequestParam String goodsposition) {
        Response response = new Response();

        Map<String, Object> map = goodsFacade.queryCombo(goodsid, goodsposition);
        if ((int) map.get("storenum") > 0) {
            response.setData(map);
            response.setCode(200);
            response.setMessage("查询成功");
        } else {
            response.setCode(300);
            response.setMessage("库存不足");
        }
        return response;
    }

    /**
     * 租用的商品加入购物车接口(临时屏蔽，租用的商品和出售的商品加入购物车公用接口)
     */
//    @ApiOperation(value = "租用的商品加入购物车接口", notes = "用户租用商品时，点击加入购物车或立即租用之前选择租用日期和套餐类型等的选择数据返回", response = Response.class)
//    @RequestMapping(value = "rentGoodsCart", method = RequestMethod.POST)
//    public Response rentGoodsCart(@ApiParam(value = "商品id") @RequestParam String goodsid) {
//        Response response = new Response();
//
//        Map<String, Object> map = new HashMap<>();
//
//        if (response.getCode() == 200) {
//            response.setMessage("查询成功");
//        }
//        response.setData(map);
//        return response;
//    }

    /**
     * 提交订单前，点击选择优惠券，跳转页面，返回所有优惠券列表
     */
    @ApiOperation(value = "提交订单时选择优惠券，返回所有优惠券列表接口", notes = "提交订单前，点击选择优惠券，页面跳转，并在该接口中返回所有该用户名下的优惠券列表", response = Response.class)
    @RequestMapping(value = "choiceCoupon", method = RequestMethod.POST)
    public Response choiceCoupon(@ApiParam(value = "用户id") @RequestParam String userid) {
        Response response = new Response();

        Map<String, Object> map = couponFacade.choiceCoupon(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 租用的商品立即租用接口(选择好商品及套餐类型后调用)
     */
    @ApiOperation(value = "租用的商品立即租用接口(选择好商品及套餐类型后调用)", notes = "租用的商品立即租用接口，选择好商品及套餐类型后调用(返回所有地址列表、所含套餐商品列表、用户可用积分数)", response = Response.class)
    @RequestMapping(value = "immediateRent", method = RequestMethod.POST)
    public Response immediateRent(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                  @ApiParam(value = "用户id") @RequestParam String userid,
                                  @ApiParam(value = "套餐类型id（不选择套餐时传空）") @RequestParam(required = false) String combotype,
                                  @ApiParam(value = "活动id（不参加活动时传空）") @RequestParam(required = false) String discountid,
                                  @ApiParam(value = "租用日期（多天用英文逗号隔开）") @RequestParam String rentdate,
                                  @ApiParam(value = "是否需要跟机员 0不需要 1需要") @RequestParam String isdebug,
                                  @ApiParam(value = "商品数量") @RequestParam String num,
                                  @ApiParam(value = "当前选择的定位地址-省code") @RequestParam String provincecode,
                                  @ApiParam(value = "当前选择的定位地址-市code") @RequestParam String citycode) throws ParseException {
        Response response = new Response();

        Map<String, Object> map = goodsFacade.immediateRent(goodsid, userid, combotype, discountid, rentdate, isdebug, num, provincecode, citycode);

        if ((int) map.get("code") == 200) {
            response.setCode(200);
            response.setMessage("请求成功");
        } else {
            response.setCode(300);
            response.setMessage("请求失败");
        }
        response.setData(map);
        return response;
    }

    /**
     * 购买的商品立即购买接口
     */
    @ApiOperation(value = "购买的商品立即购买接口", notes = "用户租用商品时，点击加入购物车或立即租用之前选择租用日期和套餐类型等的选择数据返回", response = Response.class)
    @RequestMapping(value = "immediateBuy", method = RequestMethod.POST)
    public Response immediateBuy(@ApiParam(value = "商品id") @RequestParam String goodsid) {
        Response response = new Response();

        Map<String, Object> map = new HashMap<>();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }
}
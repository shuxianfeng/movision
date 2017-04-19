package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.collection.CollectionFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/1/22 15:08
 */
@RestController
@RequestMapping("/app/collection/")
public class AppCollectionController {

    @Autowired
    private CollectionFacade collectionFacade;

    @ApiOperation(value = "收藏帖子接口", notes = "用户在帖子详情页面点击收藏时调用此接口进行收藏操作", response = Response.class)
    @RequestMapping(value = "collectionPost", method = RequestMethod.POST)
    public Response collectionPost(@ApiParam(value = "当前浏览的帖子id") @RequestParam String postid,
                                   @ApiParam(value = "当前登录的用户id") @RequestParam String userid,
                                   @ApiParam(value = "收藏类型：0 帖子 1 商品") @RequestParam String type) {
        Response response = new Response();

        int count = collectionFacade.collectionPost(postid, userid, type);

        if (count == 1) {
            response.setCode(200);
            response.setMessage("收藏成功");
        } else if (count == -1) {
            response.setCode(300);
            response.setMessage("已收藏过该帖子");
        }
        return response;
    }

    @ApiOperation(value = "取消收藏帖子活动/商品接口", notes = "用户在帖子详情页面点击取消收藏时调用此接口进行取消收藏操作", response = Response.class)
    @RequestMapping(value = "cancelCollection", method = RequestMethod.POST)
    public Response cancelCollection(@ApiParam(value = "当前浏览的帖子id(取消收藏帖子活动时必填)") @RequestParam(required = false) String postid,
                                     @ApiParam(value = "当前的商品id(取消收藏商品时必填)") @RequestParam(required = false) String goodsid,
                                     @ApiParam(value = "当前登录的用户id") @RequestParam String userid,
                                     @ApiParam(value = "类型：0 帖子/活动 1 商品") @RequestParam String type) {
        Response response = new Response();

        collectionFacade.cancelCollection(postid, goodsid, userid, type);

        if (response.getCode() == 200) {
            response.setMessage("取消收藏成功");
        } else {
            response.setMessage("取消收藏失败");
        }
        return response;
    }

    @ApiOperation(value = "收藏商品接口", notes = "用户在商品详情页面点击收藏时调用此接口进行收藏操作", response = Response.class)
    @RequestMapping(value = "collectionGoods", method = RequestMethod.POST)
    public Response collectionGoods(@ApiParam(value = "商品id") @RequestParam String goodsid,
                                    @ApiParam(value = "当前登录的用户id") @RequestParam String userid,
                                    @ApiParam(value = "收藏类型：0 帖子 1 商品") @RequestParam String type) {
        Response response = new Response();

        int count = collectionFacade.collectionGoods(goodsid, userid, type);

        if (count == 1) {
            response.setCode(200);
            response.setMessage("收藏成功");
        } else if (count == -1) {
            response.setCode(300);
            response.setMessage("已收藏过该商品");
        }
        return response;
    }
}

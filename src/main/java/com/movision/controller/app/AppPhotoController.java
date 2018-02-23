package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadePhoto;
import com.movision.mybatis.photo.entity.PhotoVo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 9:39
 */
@RestController
@RequestMapping("/app/Photo/")
public class AppPhotoController {
    private static Logger log = LoggerFactory.getLogger(AppPhotoController.class);

    @Autowired
    private FacadePhoto facadePhoto;

    @ApiOperation(value = "发布约拍", notes = "发布约拍", response = Response.class)
    @RequestMapping(value = "releaseAnAppointmentPost", method = RequestMethod.POST)
    public Response releaseAnAppointmentPost(@ApiParam(value = "用户id") @RequestParam String userid,
                                             @ApiParam(value = "需求类目（约摄影师 OR 约模特）") @RequestParam String title,
                                             @ApiParam(value = "金额") @RequestParam String money,
                                             @ApiParam(value = "内容") @RequestParam String content,
                                             @ApiParam(value = "约拍时间") @RequestParam(required = false) String intime,
                                             @ApiParam(value = "返图是否公开0 是 1 否（需返图时为必填）") @RequestParam(required = false) String returnMap,
                                             @ApiParam(value = "是否返图0 需要返图 1 不需要',") @RequestParam(required = false) String personnal,
                                             @ApiParam(value = "城市") @RequestParam String city,
                                             @ApiParam(value = "作品配图") @RequestParam(required = false) String subjectMatter,
                                             @ApiParam(value = "返图张数") @RequestParam(required = false) String personnalnumber,
                                             @ApiParam(value = "标签") @RequestParam(required = false) String labellist
                                             ){
        Response response = new Response();
        Map map=facadePhoto.releaseAnAppointmentPost(userid,title,money,content,intime,returnMap,personnal,city,subjectMatter,personnalnumber,labellist);
        if (map.get("flag").equals(-2)) {
            response.setCode(300);
            response.setMessage("系统异常，APP发帖失败");
        }else {
            response.setCode(200);
            response.setMessage("发帖成功");
        }

        return response;
    }

    @ApiOperation(value = "查询约拍详情", notes = "查询约拍详情", response = Response.class)
    @RequestMapping(value = "queryPhotoDetails", method = RequestMethod.POST)
    public Response queryPhotoDetails(@ApiParam(value = "约拍id") @RequestParam String id){
        Response response = new Response();
        PhotoVo photoVo =facadePhoto.queryPhotoDetails(id);
        if(response.getCode()==200){
            response.setMessage("查询成功");
            response.setData(photoVo);
        }
        return response;
    }


    @ApiOperation(value = "查询约拍列表", notes = "查询约拍列表", response = Response.class)
    @RequestMapping(value = "findAllPhoto", method = RequestMethod.POST)
    public Response findAllPhoto(  @ApiParam(value = "需求类目（约摄影师 OR 约模特）") @RequestParam String title,
                                   @ApiParam(value = "金额") @RequestParam String money,
                                   @ApiParam(value = "城市") @RequestParam String city,
                                   @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "15") String pageSize){
        Response response = new Response();
        Paging<PhotoVo> pager = new Paging<PhotoVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<PhotoVo> photoVo =facadePhoto.findAllPhoto(title,money,city,pager);
        if(response.getCode()==200){
            response.setMessage("查询成功");
            response.setData(photoVo);
        }
        return response;
    }


    @ApiOperation(value = "添加竞拍人", notes = "添加竞拍人", response = Response.class)
    @RequestMapping(value = "insertPhotoOrder", method = RequestMethod.POST)
    public Response insertPhotoOrder(  @ApiParam(value = "竞拍用户id") @RequestParam String userid,
                                   @ApiParam(value = "竞拍金额") @RequestParam String money,
                                   @ApiParam(value = "约拍id") @RequestParam String photoid ){
        Response response = new Response();
        Map photoVo =facadePhoto.insertPhotoOrder(userid,money,photoid);
        if (photoVo.get("flag").equals(-2)) {
            response.setCode(300);
            response.setMessage("系统异常，添加竞拍人失败");
        }else if (photoVo.get("flag").equals(-1)) {
            response.setCode(400);
            response.setMessage("发布者不能自己竞拍自己的");
        }else if (photoVo.get("flag").equals(-3)) {
            response.setCode(600);
            response.setMessage("您已竞拍过该约拍");
        }else {
            response.setCode(200);
            response.setMessage("发帖成功");
        }
        return response;
    }



    @ApiOperation(value = "修改用户信用分", notes = "修改用户信用分", response = Response.class)
    @RequestMapping(value = "photoCreditScore", method = RequestMethod.POST)
    public Response photoCreditScore(  @ApiParam(value = "0 好评 1中评") @RequestParam int flag,
                                        @ApiParam(value = "约拍id") @RequestParam String id,
                                       @ApiParam(value = "登录id") @RequestParam String uid){
        Response response = new Response();
        int photoVo =facadePhoto.photoCreditScore(flag,id,uid);
        if(response.getCode()==200){
            response.setMessage("修改成功");
            response.setData(photoVo);
        }
        return response;
    }

    @ApiOperation(value = "修改约拍价格", notes = "修改约拍价格", response = Response.class)
    @RequestMapping(value = "updatePhotoMoney", method = RequestMethod.POST)
    public Response updatePhotoMoney(  @ApiParam(value = "发布者id") @RequestParam String userid,
                                       @ApiParam(value = "约拍id") @RequestParam String id,
                                       @ApiParam(value = "价格") @RequestParam String money ){
        Response response = new Response();
        Map photoVo =facadePhoto.updatePhotoMoney(userid,id,money);
        if (photoVo.get("flag").equals(-2)) {
            response.setCode(300);
            response.setMessage("您没有权限修改此条信息");
        }else {
            response.setCode(200);
            response.setMessage("修改约拍价格成功");
        }
        return response;
    }


    @ApiOperation(value = "选中接单人", notes = "选中接单人", response = Response.class)
    @RequestMapping(value = "selectedOrder", method = RequestMethod.POST)
    public Response selectedOrder(  @ApiParam(value = "约拍id") @RequestParam String id,
                                       @ApiParam(value = "选中人的id") @RequestParam String userid){
        Response response = new Response();
        int flag =facadePhoto.selectedOrder( id,userid);
        if(response.getCode()==200){
            response.setMessage("修改成功");
            response.setData(flag);
        }
        return response;
    }



    @ApiOperation(value = "发布人确定拍摄完成", notes = "发布人确定拍摄完成", response = Response.class)
    @RequestMapping(value = "updateOrderPhoto", method = RequestMethod.POST)
    public Response updateOrderPhoto(  @ApiParam(value = "约拍id") @RequestParam String id,
                                       @ApiParam(value = "用户id") @RequestParam String userid){
        Response response = new Response();
        Map flag =facadePhoto.updateOrderPhoto(id,userid);
        if (flag.get("flag").equals(-2)) {
            response.setCode(300);
            response.setMessage("您没有权限确定拍摄");
        }else {
            response.setCode(200);
            response.setMessage("发布人确定拍摄完成成功");
        }
        return response;
    }


    @ApiOperation(value = "是否免单", notes = "是否免单", response = Response.class)
    @RequestMapping(value = "clickSure", method = RequestMethod.POST)
    public Response clickSure(  @ApiParam(value = "免单人id") @RequestParam String userid,
                                       @ApiParam(value = "订单id") @RequestParam String id){
        Response response = new Response();
        int flag =facadePhoto.clickSure(userid,id);
        if(response.getCode()==200){
             response.setMessage("免单成功");
             response.setData(flag);
        }
        return response;
    }


}

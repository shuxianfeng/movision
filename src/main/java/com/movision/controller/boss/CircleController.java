package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.CircleFacade;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CategoryVo;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleDetails;
import com.movision.mybatis.circle.entity.CircleIndexList;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.video.entity.Video;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/8 17:55
 */
@RestController
@RequestMapping("/boss/circle")
public class CircleController {
    @Autowired
    CircleFacade circleFacade;

    /**
     * 后台管理-查询圈子列表
     *
     * @return
     */
    @ApiOperation(value = "查询圈子列表", notes = "用于查询圈子列表接口", response = Response.class)
    @RequestMapping(value = "/circle_list", method = RequestMethod.POST)
    public Response circleByList() {
        Response response = new Response();
        List<CircleIndexList> list = circleFacade.queryCircleByList();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }

    /**
     * 后台管理-查询精贴列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询精贴列表", notes = "查询精贴列表", response = Response.class)
    @RequestMapping(value="/Isessence_list",method = RequestMethod.POST)
    public  Response queryPostIsessenceByList(@RequestParam(required = false,defaultValue = "1") String pageNo,
                                              @RequestParam(required = false,defaultValue = "10") String pageSize){
        Response response= new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = circleFacade.queryPostIsessenceByList(pager);
        if(response.getCode()==200){
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-查询发现页排序
     *
     * @return
     */
    @ApiOperation(value = "查询发现页排序", notes = "用于查询圈子可推荐到发现页的排序接口", response = Response.class)
    @RequestMapping(value = "add_discover_list", method = RequestMethod.POST)
    public Response queryDiscoverList() {
        Response response = new Response();
        Map<String, List> map = circleFacade.queryDiscoverList();
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 后台管理-修改圈子发现页排序
     *
     * @return
     */
    @ApiOperation(value = "圈子推荐到发现页", notes = "用于把圈子推荐到发现页排序的接口", response = Response.class)
    @RequestMapping(value = "update_circle_orderid", method = RequestMethod.POST)
    public Response updateDiscover(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                   @ApiParam(value = "排序id") @RequestParam String orderid) {
        Response response = new Response();
        Map<String, Integer> map = circleFacade.updateDiscover(circleid, orderid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 后台管理-圈子推荐到首页
     *
     * @param circleid
     * @return
     */
    @ApiOperation(value = "圈子推荐首页", notes = "用于圈子推荐到首页的接口", response = Response.class)
    @RequestMapping(value = "update_circle_index", method = RequestMethod.POST)
    public Response updateCircleIndex(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        Map<String, Integer> map = circleFacade.updateCircleIndex(circleid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }


    /**
     * 后台管理-查看圈子详情
     *
     * @param circleid
     * @return
     */
    @ApiOperation(value = "查看圈子详情", notes = "用于查看圈子详情接口", response = Response.class)
    @RequestMapping(value = "query_circle_details", method = RequestMethod.POST)
    public Response queryCircleDetails(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        CircleDetails circle = circleFacade.quryCircleDetails(circleid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(circle);
        return response;
    }

    /**
     * 圈子类型
     *
     * @param circleid
     * @return
     */
    public Response queryCircletype(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData("");
        return response;
    }

    /**
     * 编辑圈子
     *
     * @param id
     * @param name
     * @param category
     * @param admin
     * @param createtime
     * @param photo
     * @param introduction
     * @param erweima
     * @param status
     * @param isdiscover
     * @param orderid
     * @param permission
     * @return
     */
    @ApiOperation(value = "圈子编辑", notes = "用于圈子编辑接口", response = Response.class)
    @RequestMapping(value = "update_circle", method = RequestMethod.POST)
    public Response updateCircle(HttpServletRequest request,
                                 @ApiParam(value = "圈子id") @RequestParam String id,
                                 @ApiParam(value = "圈子名称") @RequestParam String name,
                                 @ApiParam(value = "圈子类型") @RequestParam String category,
                                 @ApiParam(value = "圈主id") @RequestParam String userid,
                                 @ApiParam(value = "管理员列表") @RequestParam String admin,
                                 @ApiParam(value = "创建时间") @RequestParam String createtime,
                                 @ApiParam(value = "圈子否封面") @RequestParam(required = false) MultipartFile photo,
                                 @ApiParam(value = "圈子简介") @RequestParam String introduction,
                                 @ApiParam(value = "圈子二维码") @RequestParam(required = false) String erweima,
                                 @ApiParam(value = "审核状态") @RequestParam(required = false) String status,
                                 @ApiParam(value = "推荐到首页") @RequestParam(required = false) String isdiscover,
                                 @ApiParam(value = "推荐排序") @RequestParam(required = false) String orderid,
                                 @ApiParam(value = "发帖权限") @RequestParam(required = false) String permission) {
        Response response = new Response();
        Map<String, Integer> map = circleFacade.updateCircle(request, id, name, category, userid, admin, createtime, photo, introduction, erweima, status, isdiscover, orderid, permission);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 圈子编辑时数据回显
     *
     * @param circleid
     * @return
     */
    @ApiOperation(value = "编辑圈子数据回显", notes = "用于编辑帖子时数据回显", response = Response.class)
    @RequestMapping(value = "query_circle_show", method = RequestMethod.POST)
    public Response queryCircleByShow(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();
        Map<String, CircleDetails> map = circleFacade.queryCircleByShow(circleid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 添加圈子
     * @param request
     * @param name
     * @param category
     * @param userid
     * @param admin
     * @param criclemanid
     * @param photo
     * @param introduction
     * @param erweima
     * @param status
     * @param isdiscover
     * @param orderid
     * @return
     */
    @ApiOperation(value = "圈子添加", notes = "用于圈子添加接口", response = Response.class)
    @RequestMapping(value = "add_circle", method = RequestMethod.POST)
    public Response addCircle(HttpServletRequest request,
                              @ApiParam(value = "圈子名称") @RequestParam String name,
                              @ApiParam(value = "圈子类型0 科技 1 交友 2 摄影 3 影视 4 达人秀") @RequestParam String category,
                              @ApiParam(value = "圈主id") @RequestParam String userid,
                              @ApiParam(value = "管理员列表") @RequestParam String admin,
                              @ApiParam(value = "创建人") @RequestParam String criclemanid,
                              @ApiParam(value = "圈子否封面") @RequestParam(required = false) MultipartFile photo,
                              @ApiParam(value = "圈子简介") @RequestParam String introduction,
                              @ApiParam(value = "圈子二维码") @RequestParam(required = false) String erweima,
                              @ApiParam(value = "审核状态") @RequestParam(required = false) String status,
                              @ApiParam(value = "推荐到首页") @RequestParam(required = false) String isdiscover,
                              @ApiParam(value = "推荐排序") @RequestParam(required = false) String orderid,
                              @ApiParam(value = "发帖权限") @RequestParam(required = false) String scope) {
        Response response = new Response();
        Map<String, Integer> map = circleFacade.addCircle(request, name, category, userid, admin, criclemanid, photo, introduction, erweima, status, isdiscover, orderid, scope);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 查询圈子分类
     * @return
     */
    @ApiOperation(value = "查询圈子分类", notes = "用于查询圈子分类接口", response = Response.class)
    @RequestMapping(value = "query_circle_type_list", method = RequestMethod.POST)
    public Response queryCircleTypeList() {
        List<Category> map = circleFacade.queryCircleTypeList();
        Response response = new Response();
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 添加圈子分类
     *
     * @param typename
     * @return
     */
    @ApiOperation(value = "添加圈子分类", notes = "用于添加圈子分类接口", response = Response.class)
    @RequestMapping(value = "add_circle_type", method = RequestMethod.POST)
    public Response addCircleType(@ApiParam(value = "圈子名称") @RequestParam String typename) {
        Response response = new Response();
        Map<String, Integer> map = circleFacade.addCircleType(typename);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "查看圈子帖子列表", notes = "用于查看圈子中帖子列表接口", response = Response.class)
    @RequestMapping(value = "query_circle_post_list", method = RequestMethod.POST)
    public Response queryCircleByPostList(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                          @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                          @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = circleFacade.findAllQueryCircleByPostList(circleid, pager);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

}

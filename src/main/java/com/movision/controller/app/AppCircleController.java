package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadeCircle;
import com.movision.facade.index.FacadePost;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.post.entity.PostVo;
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
 * @Date 2017/1/20 15:26
 */
@RestController
@RequestMapping("/app/circle/")
public class AppCircleController {

    @Autowired
    private FacadeCircle facadeCircle;

    @Autowired
    private FacadePost facadePost;

    @ApiOperation(value = "圈子详情页1", notes = "用户返回圈子详情页上半版数据，圈子首页上半部分内容", response = Response.class)
    @RequestMapping(value = "index1", method = RequestMethod.POST)
    public Response queryCircleIndex1(@ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();

        CircleVo circleVo = facadeCircle.queryCircleIndex1(Integer.parseInt(circleid));

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(circleVo);
        return response;
    }

    @ApiOperation(value = "圈子详情页2", notes = "用户返回圈子详情页下半版数据，圈子首页下半部分内容的所有帖子（含分页）", response = Response.class)
    @RequestMapping(value = "index2", method = RequestMethod.POST)
    public Response queryCircleIndex2(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                      @ApiParam(value = "第几页") @RequestParam(required = false) String pageNo,
                                      @ApiParam(value = "多少条数据") @RequestParam(required = false) String pageSize) {
        Response response = new Response();

        List<PostVo> postlist = facadePost.queryCircleIndex2(pageNo, pageSize, circleid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(postlist);
        return response;
    }

    @ApiOperation(value = "圈子分类", notes = "用户返回所有圈子（按类别分类）", response = Response.class)
    @RequestMapping(value = "circlelist", method = RequestMethod.POST)
    public Response queryCircleList() {
        Response response = new Response();

        List<CircleCategoryVo> circleCategoryList = facadeCircle.queryCircleCategoryList();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(circleCategoryList);
        return response;
    }
}

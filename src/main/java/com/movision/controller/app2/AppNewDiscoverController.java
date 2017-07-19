package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.index.FacadeDiscover;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/7/17 20:54
 * 用户返回美番2.0发现页所有数据的接口控制器
 */
@RestController
@RequestMapping("/app/discover2/")
public class AppNewDiscoverController {
    @Autowired
    private FacadeDiscover facadeDiscover;

    @Autowired
    private CircleAppFacade circleAppFacade;

    @ApiOperation(value = "美番2.0发现页上半部分数据返回接口", notes = "用于返回发现页首页的全版数据", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response getDiscoverIndex(){
        Response response = new Response();

        Map<String, Object> map = facadeDiscover.queryDiscoverIndexData2Up();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "美番2.0发现页热门圈子接口", notes = "用户返回发现页热门圈子数据（一批三条）", response = Response.class)
    @RequestMapping(value = "hotcircle", method = RequestMethod.POST)
    public Response getHotCircle(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "3") String pageSize,
                                 @ApiParam(value = "用户id（登录状态下一定不能为空，非登录状态下可不传）") @RequestParam(required = false) String userid){
        Response response = new Response();

        Paging<CircleVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));//默认第一页三条
        List<CircleVo> circleList = circleAppFacade.queryHotCircle(pager, userid);
        pager.result(circleList);
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }

        response.setData(pager);
        return response;
    }
}

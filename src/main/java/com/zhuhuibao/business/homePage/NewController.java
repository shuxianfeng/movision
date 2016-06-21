package com.zhuhuibao.business.homePage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.CooperationConstants;
import com.zhuhuibao.common.constant.ProjectConstant;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页新增接口
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
public class NewController {

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "首页威客信息（三个列表集合）", notes = "首页威客信息（三个列表集合）", response = Response.class)
    @RequestMapping(value = "/rest/witkey/site/sel_three_serviceList", method = RequestMethod.GET)
    public Response sel_witkey_service(@ApiParam(value = "条数") @RequestParam int count)  {
        Response Response = new Response();
        //任务
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("type", 1);
        map.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        map.put("status", CooperationConstants.Status.AUDITED.toString());
        List<Map<String,String>> cooperation1 = cooperationService.queryHotCooperation(map);
        //服务
        Map<String, Object> map1 = new HashMap<>();
        map1.put("count", count);
        map1.put("type", 2);
        map1.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        map1.put("status", CooperationConstants.Status.AUDITED.toString());
        List<Map<String,String>> cooperation2 = cooperationService.queryHotCooperation(map1);
        //资质合作
        Map<String, Object> map2 = new HashMap<>();
        map2.put("count", count);
        map2.put("type", 3);
        map2.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        map2.put("status", CooperationConstants.Status.AUDITED.toString());
        List<Map<String,String>> cooperation3 = cooperationService.queryHotCooperation(map2);

        Map map3 = new HashMap();
        map3.put("list1",cooperation1);
        map3.put("list2",cooperation2);
        map3.put("list3",cooperation3);
        Response.setData(map3);
        return Response;
    }

    @ApiOperation(value = "首页最新项目信息(默认7个)", notes = "首页最新项目信息(默认7个)", response = Response.class)
    @RequestMapping(value = "/rest/project/site/base/sel_hpLatestProject", method = RequestMethod.GET)
    public Response selectLatestPorject()
    {
        Response response = new Response();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count", ProjectConstant.PROJECT_HOMEPAGE_COUNT_SEVEN);
        List<Map<String,String>> projectList = projectService.queryHomepageLatestProject(map);
        response.setData(projectList);
        return response;
    }
}

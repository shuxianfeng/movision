package com.zhuhuibao.business.homePage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.expo.service.ExpoService;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页新增接口
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
@RequestMapping("/rest/home/site/")
public class NewController {

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ExpertService expertService;

    @Autowired
    private ExpoService expoService;

    @Autowired
    private ChannelNewsService channelNewsService;

    @Autowired
    private JobPositionService jobService;

    @ApiOperation(value = "首页威客信息（三个列表集合）", notes = "首页威客信息（三个列表集合）", response = Response.class)
    @RequestMapping(value = "sel_three_serviceList", method = RequestMethod.GET)
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
    @RequestMapping(value = "sel_hpLatestProject", method = RequestMethod.GET)
    public Response selectLatestPorject()
    {
        Response response = new Response();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("count", ProjectConstant.PROJECT_HOMEPAGE_COUNT_SEVEN);
        List<Map<String,String>> projectList = projectService.queryHomepageLatestProject(map);
        response.setData(projectList);
        return response;
    }

    @ApiOperation(value = "首页第三屏权威专家，前沿技术，筑慧会展（个数运营控制）",
            notes = "首页第三屏权威专家，前沿技术，筑慧会展（个数运营控制）", response = Response.class)
    @RequestMapping(value = "sel_expert_tech_expo_List", method = RequestMethod.GET)
    public Response sel_expertList(@ApiParam(value="频道类型 1:平台主站")@RequestParam String chanType,
                                   @ApiParam(value="频道下子页面.index:首页;") @RequestParam String page,
                                   @ApiParam(value="广告所在区域:F3:第三屏") @RequestParam String advArea)
    {
        Response response = new Response();
        Map map = new HashMap();

        //查询权威专家
        Map<String, Object> expertMap = new HashMap<>();
        expertMap.put("chanType",chanType);
        expertMap.put("page",page);
        expertMap.put("advArea",advArea);
        expertMap.put("advType","expert");
        expertMap.put("is_deleted",ExpertConstant.EXPERT_DELETE_ZERO);
        expertMap.put("status", ExpertConstant.EXPERT_STATUS_ONE);
        List<Map<String,String>> expertList = expertService.queryHomepageExpertList(expertMap);

        //查询前沿技术
        Map<String, Object> technologyMap = new HashMap<>();
        technologyMap.put("chanType",chanType);
        technologyMap.put("page",page);
        technologyMap.put("advArea",advArea);
        technologyMap.put("advType","technology");
        technologyMap.put("status", Constants.StatusMark.YSH.toString());
        List<Map<String,String>> technologyList = channelNewsService.queryHomepageTechnologyList(technologyMap);

        //查询筑慧会展
        Map<String, Object> exhibitionMap = new HashMap<>();
        exhibitionMap.put("chanType",chanType);
        exhibitionMap.put("page",page);
        exhibitionMap.put("advArea",advArea);
        exhibitionMap.put("advType","exhibition");
        exhibitionMap.put("is_deleted",ExhibitionConstant.EXHIBITION_DELETE_ZERO);
        exhibitionMap.put("status", ExhibitionConstant.EXHIBITION_STATUS_ONE);
        List<Map<String,String>> exhibitionList = expoService.queryHomepageExhibitionList(exhibitionMap);

        map.put("expertList",expertList);
        map.put("technologyList",technologyList);
        map.put("exhibitionList",exhibitionList);

        response.setData(map);
        return response;
    }

    @ApiOperation(value = "查询最新招聘职位", notes = "查询最新招聘职位", response = Response.class)
    @RequestMapping(value = "sel_latest_position", method = RequestMethod.GET)
    public Response searchNewPosition() throws IOException {

        List<Map<String,Object>> list = jobService.findNewPositions(6);
        return new Response(list);
    }
}

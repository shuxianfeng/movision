package com.zhuhuibao.business.cooperation;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.CooperationConstants;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.service.CooperationService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 威客接口
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/cooperationSite")
public class CooperationSiteController {
    private static final Logger log = LoggerFactory.getLogger(CooperationSiteController.class);

    @Autowired
    private CooperationService cooperationService;


    /**
     * 查询任务列表（分页）
     */
    @ApiOperation(value = "查询任务列表（前台分页）", notes = "查询任务列表（前台分页）", response = Response.class)
    @RequestMapping(value = "findAllCooperationByPager", method = RequestMethod.GET)
    public Response findAllCooperationByPager
    (@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize,
     @ApiParam(value = "合作类型") @RequestParam(required = false) String type,
     @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
     @ApiParam(value = "省") @RequestParam(required = false) String province,
     @ApiParam(value = "关键字") @RequestParam(required = false) String smart,
     @ApiParam(value = "发布类型，1：接任务，2：接服务，3：资质合作") @RequestParam String parentId) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Cooperation> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Cooperation cooperation = new Cooperation();
        cooperation.setSmart(smart);
        cooperation.setType(type);
        cooperation.setType1("1");
        cooperation.setCategory(category);
        cooperation.setProvince(province);
        cooperation.setParentId(parentId);
        Response Response = new Response();
        List<Cooperation> cooperationList = cooperationService.findAllCooperationByPager(pager, cooperation);
        pager.result(cooperationList);
        Response.setData(pager);
        return Response;
    }

    /**
     * 最热合作信息
     */
    @ApiOperation(value = "最热合作信息", notes = "最热合作信息", response = Response.class)
    @RequestMapping(value = "queryHotService", method = RequestMethod.GET)
    public Response queryHotService(@ApiParam(value = "条数") @RequestParam(required = false) int count,
                                      @ApiParam(value = "合作类型：1：任务，2：服务，3：资质合作")@RequestParam String type)  {
        Response Response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("type", type);
        map.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        map.put("status", CooperationConstants.Status.AUDITED.toString());
        List<Cooperation> cooperations = cooperationService.queryHotCooperation(map);
        Response.setData(cooperations);
        return Response;
    }

    /**
     * 威客信息詳情
     */
    @ApiOperation(value="威客信息詳情",notes="威客信息詳情",response = Cooperation.class)
    @RequestMapping(value = "cooperationInfo", method = RequestMethod.GET)
    public Response cooperationInfo(@RequestParam String id)  {
        Response response = new Response();
        Cooperation cooperation = cooperationService.queryCooperationInfoById(id);
        response.setData(cooperation);
        cooperation.setViews(cooperation.getViews()+1);
        cooperationService.updateCooperation(cooperation);
        return response;
    }
}

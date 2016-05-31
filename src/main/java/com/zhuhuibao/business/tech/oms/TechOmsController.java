package com.zhuhuibao.business.tech.oms;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.techtrain.entity.TechCooperation;
import com.zhuhuibao.mybatis.techtrain.entity.TechData;
import com.zhuhuibao.mybatis.techtrain.service.TechnologyService;
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
 *  技术后台运营系统
 */
@RestController

@RequestMapping("/rest/tech/oms")
@Api(value = "techOms", description = "技术后台运营系统")
public class TechOmsController {
    private static final Logger log = LoggerFactory.getLogger(TechOmsController.class);

    @Autowired
    TechnologyService techService;

    @RequestMapping(value="data/del_tech_data", method = RequestMethod.POST)
    @ApiOperation(value="删除技术资料(行业解决方案，技术文档，培训资料)",notes = "删除技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response deleteTechData( @ApiParam(value = "技术资料ID")  @RequestParam() String techDataId)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id",techDataId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechData(condition);
        return response;
    }

    @RequestMapping(value="data/upd_tech_data", method = RequestMethod.POST)
    @ApiOperation(value="修改技术资料(行业解决方案，技术文档，培训资料)",notes = "修改技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response updateTechData( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techData") TechData techData)
    {
        Response response = new Response();
        int result = techService.updateTechData(techData);
        return response;
    }

    @RequestMapping(value="coop/sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台搜索技术合作(技术成果，技术需求)",notes = "运营管理平台技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "标题") @RequestParam(required = false) String title,
                                                @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                                @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (title != null && !"".equals(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        condition.put("type", type);
        condition.put("status", status);
        List<Map<String, String>> techList = techService.findAllOMSTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="coop/upd_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="修改技术合作(技术成果，技术需求)",notes = "修改技术合作(技术成果，技术需求)",response = Response.class)
    public Response updateTechCooperation( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        Response response = new Response();
        int result = techService.updateTechCooperation(techCoop);
        return response;
    }

    @RequestMapping(value="coop/sel_tech_cooperation_detail", method = RequestMethod.POST)
    @ApiOperation(value="查询技术合作(技术成果，技术需求)",notes = "查询技术合作(技术成果，技术需求)",response = Response.class)
    public Response selectTechCooperationById( @ApiParam(value = "技术合作成果、需求ID")  @RequestParam String techCoopId)
    {
        Response response = new Response();
        TechCooperation techCoop = techService.selectTechCooperationById(techCoopId);
        response.setData(techCoop);
        return response;
    }

    @RequestMapping(value="coop/del_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="删除技术合作(技术成果，技术需求)",notes = "删除技术合作(技术成果，技术需求)",response = Response.class)
    public Response deleteTechCooperation( @ApiParam(value = "技术合作ID")  @RequestParam() String techId)
    {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("id", techId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechCooperation(condition);
        return response;
    }


}

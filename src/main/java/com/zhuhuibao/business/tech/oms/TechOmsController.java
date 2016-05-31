package com.zhuhuibao.business.tech.oms;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.techtrain.entity.TechData;
import com.zhuhuibao.mybatis.techtrain.service.TechnologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  技术后台运营系统
 */
@RestController

@RequestMapping("/rest/tech/site/oms")
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

}

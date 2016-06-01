package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.service.TechnologyService;
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
 * 技术合作
 */
@RestController
@RequestMapping("/rest/tech/site/coop")
@Api(value = "techCoop", description = "技术合作接口")
public class TechCoopController {
    private static final Logger log = LoggerFactory.getLogger(TechCoopController.class);


    @Autowired
    OrderService orderService;

    @Autowired
    TechnologyService techService;


    @RequestMapping(value="add_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="新增技术合作(技术成果，技术需求)",notes = "新增技术合作(技术成果，技术需求)",response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop)
    {
        log.info("insert tech cooperation");
        int result = techService.insertTechCooperation(techCoop);
        Response response = new Response();
        return response;
    }


    @RequestMapping(value="sel_tech_cooperation_detail", method = RequestMethod.GET)
    @ApiOperation(value="查看技术合作详情",notes = "查看技术合作详情",response = Response.class)
    public Response previewTechCooperation(@ApiParam(value = "技术合作成果、需求ID")  @RequestParam String techCoopId)
    {

        TechCooperation techCoop = techService.previewTechCooperationDetail(techCoopId);
        techService.updateTechCooperationViews(techCoopId);
        Response response = new Response();
        response.setData(techCoop);
        return response;
    }

    @RequestMapping(value="sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="频道页搜索技术合作(技术成果，技术需求)",notes = "频道页搜索技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        condition.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }


}

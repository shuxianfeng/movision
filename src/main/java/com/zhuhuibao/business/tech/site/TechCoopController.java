package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
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
 *
 * @author pl
 * @version 2016/6/8
 */
@RestController
@RequestMapping("/rest/tech/site/coop")
@Api(value = "techCoop", description = "技术合作接口")
public class TechCoopController {
    private static final Logger log = LoggerFactory.getLogger(TechCoopController.class);


    @Autowired
    OrderService orderService;

    @Autowired
    TechCooperationService techService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping(value = "add_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value = "发布技术成果", notes = "新增技术技术成果", response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果") @ModelAttribute(value = "techCoop") TechCooperation techCoop) throws Exception {
        log.info("insert tech cooperation");
        Long createId = ShiroUtil.getCreateID();
        if (null != createId) {
            techCoop.setCreateID(createId);
            int result = techService.insertTechCooperation(techCoop);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return new Response();
    }

    @RequestMapping(value = "add_tech_requirement", method = RequestMethod.POST)
    @ApiOperation(value = "发布技术需求", notes = "新增技术需求", response = Response.class)
    public Response insertTechRequirement(@ApiParam(value = "技术合作：技术需求") @ModelAttribute(value = "techCoop") TechCooperation techCoop) throws Exception {
        log.info("insert tech requirement");
        Long createId = ShiroUtil.getCreateID();
        if (null != createId) {
            techCoop.setCreateID(createId);
            int result = techService.insertTechCooperation(techCoop);
            if (result != 0) {
                return new Response();
            } else {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发布失败");
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }


    @RequestMapping(value = "sel_tech_cooperation_detail", method = RequestMethod.GET)
    @ApiOperation(value = "查看技术需求详情", notes = "查看技术需求详情", response = Response.class)
    public Response previewTechCooperation(@ApiParam(value = "技术需求ID") @RequestParam String techCoopId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", techCoopId);
        map.put("type", 2);
        Map<String, Object> techCoop = techService.previewTechCooperationDetail(map);
        techService.updateTechCooperationViews(techCoopId);
        Response response = new Response();
        response.setData(techCoop);
        return response;
    }

    @RequestMapping(value = "sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value = "频道页搜索技术合作(技术成果，技术需求)", notes = "频道页搜索技术合作(技术成果，技术需求)", response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "系统分类") @RequestParam(required = false) String systemCategory,
                                                @ApiParam(value = "应用领域") @RequestParam(required = false) String applicationArea,
                                                @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                @ApiParam(value = "页码") @RequestParam(required = false,defaultValue = "1") String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false,defaultValue = "10") String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<>();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("type", type);
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        condition.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "sel_views_order", method = RequestMethod.GET)
    @ApiOperation(value = "技术合作的点击排行", notes = "技术合作的点击排行", response = Response.class)
    public Response findDataViewsOrder() {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        map.put("count", TechConstant.COOP_DOWNLOAD_COUNT_TEN);
        List<Map<String, String>> techDataList = techService.findCoopViewsOrder(map);
        response.setData(techDataList);
        return response;
    }
}

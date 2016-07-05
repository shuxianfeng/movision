package com.zhuhuibao.business.tech.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的技术合作：技术成果，技术需求
 * @author pl
 * @version 2016/6/8 0008
 */
@RestController
@RequestMapping(value = "/rest/tech/mc/coop")
@Api(value = "TechCoopMc",description = "我的技术合作：技术成果，技术需求")
public class TechCoopMcController {

    @Autowired
    TechCooperationService techService;

    @RequestMapping(value="sel_tech_cooperation", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台搜索技术合作(技术成果，技术需求)",notes = "运营管理平台技术合作(技术成果，技术需求)",response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                                @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                @ApiParam(value = "状态") @RequestParam(required = false) String status,
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

    @RequestMapping(value="sel_tech_cooperation_detail", method = RequestMethod.GET)
    @ApiOperation(value="查询技术合作(技术成果，技术需求)",notes = "查询技术合作(技术成果，技术需求)",response = Response.class)
    public Response selectTechCooperationById( @ApiParam(value = "技术合作成果、需求ID")  @RequestParam String techCoopId)
    {
        Response response = new Response();
        Map<String,String> techCoop = techService.selectMcCoopDetail(techCoopId);
        response.setData(techCoop);
        return response;
    }

    @RequestMapping(value="upd_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="修改技术合作(技术成果，技术需求)",notes = "修改技术合作(技术成果，技术需求)",response = Response.class)
    public Response updateTechCooperation( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop) throws Exception {
        Response response = new Response();
        int result = techService.updateTechCooperation(techCoop);
        return response;
    }

    @RequestMapping(value="add_tech_cooperation", method = RequestMethod.POST)
    @ApiOperation(value="新增技术合作(技术成果，技术需求)",notes = "新增技术合作(技术成果，技术需求)",response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techCoop")TechCooperation techCoop) throws Exception {
        Long createId = ShiroUtil.getCreateID();
        if(null != createId) {
            techCoop.setCreateID(createId);
            int result = techService.insertTechCooperation(techCoop);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        Response response = new Response();
        return response;
    }

    @RequestMapping(value="del_tech_cooperation", method = RequestMethod.GET)
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

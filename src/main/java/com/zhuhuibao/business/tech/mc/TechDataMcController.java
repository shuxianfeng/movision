package com.zhuhuibao.business.tech.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.tech.entity.TechData;
import com.zhuhuibao.mybatis.tech.service.TechDataService;
import com.zhuhuibao.mybatis.tech.service.TechDownloadDataService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心我的技术资料
 *
 * @author pl
 * @version 2016/6/8 0008
 */
@RestController
@RequestMapping(value = "/rest/tech/mc/data")
@Api(value = "TechDataMc",description = "会员中心我的技术资料")
public class TechDataMcController {
    @Autowired
    TechDataService techDataService;
    @Autowired
    TechDownloadDataService downloadService;

    @RequestMapping(value="upd_tech_data", method = RequestMethod.POST)
    @ApiOperation(value="修改技术资料(行业解决方案，技术文档，培训资料)",notes = "修改技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response updateTechData( @ApiParam(value = "技术合作：技术成果，技术需求")  @ModelAttribute(value="techData") TechData techData)
    {
        Response response = new Response();
        int result = techDataService.updateTechData(techData);
        return response;
    }

    @RequestMapping(value="sel_tech_data_detail", method = RequestMethod.GET)
    @ApiOperation(value="查询技术资料详情(行业解决方案，技术文档，培训资料)",notes = "查询技术资料详情(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response selectTechDataDetail(@ApiParam(value = "技术资料ID")  @RequestParam String techDataId)
    {
        Map<String,String> techData = techDataService.selectMCTechDataDetail(Long.parseLong(techDataId));
        Response response = new Response();
        response.setData(techData);
        return response;
    }

    @RequestMapping(value="del_tech_data", method = RequestMethod.GET)
    @ApiOperation(value="删除技术资料(行业解决方案，技术文档，培训资料)",notes = "删除技术资料(行业解决方案，技术文档，培训资料)",response = Response.class)
    public Response deleteTechData( @ApiParam(value = "技术资料ID")  @RequestParam() String techDataId)
    {
        Response response = new Response();
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id",techDataId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techDataService.deleteTechData(condition);
        return response;
    }

    @RequestMapping(value="sel_tech_data", method = RequestMethod.GET)
    @ApiOperation(value="搜索技术资料",notes = "搜索技术资料",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "一级分类") @RequestParam(required = false) String fCategory,
                                         @ApiParam(value = "标题") @RequestParam(required = false) String title,
                                         @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("fCategory", fCategory);
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
        condition.put("status", status);

        //登录用户
        Long createid = ShiroUtil.getCreateID();
        if(createid == null) {
            throw new AuthException(MsgCodeConstant.un_login,"请登录");
        }
        condition.put("createid",createid);

        List<Map<String, String>> techList = techDataService.findAllOMSTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="sel_download_data", method = RequestMethod.GET)
    @ApiOperation(value="我下载的技术资料",notes = "我下载的技术资料",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String,String> condition = new HashMap<String,String>();
        Long createId = ShiroUtil.getCreateID();
        if(createId != null) {
            condition.put("downloaderId", String.valueOf(createId));
            if (StringUtils.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtils.isEmpty(pageSize)) {
                pageSize = "10";
            }
            Paging<Map<String, Object>> pager = new Paging<Map<String, Object>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            List<Map<String, Object>> techList = downloadService.findAllDownloadData(pager, condition);
            pager.result(techList);
            response.setData(pager);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}

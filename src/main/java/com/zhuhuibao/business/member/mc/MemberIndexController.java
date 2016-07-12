package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expo.service.ExpoService;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.*;
import com.zhuhuibao.mybatis.tech.service.TechDataService;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by cxx on 2016/6/29 0029.
 */
@RestController
@RequestMapping("/rest/member/mc/index")
public class MemberIndexController {

    @Autowired
    private ExpoService exhibitionService;

    @Autowired
    SuccessCaseService successCaseService;

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    BrandService brandService;

    @Autowired
    OfferPriceService offerPriceService;

    @Autowired
    TechDataService techDataService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    IndexService indexService;

    @ApiOperation(value = "宣传推广相关信息", notes = "宣传推广相关信息", response = Response.class)
    @RequestMapping(value = "sel_campaign_info_size", method = RequestMethod.GET)
    public Response sel_campaign_info_size() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        Map map = new HashMap();
        if(memberId!=null){
            Map<String,Object> exhibitionMap = new HashMap<>();
            exhibitionMap.put("type",3);
            exhibitionMap.put("createId", String.valueOf(memberId));
            int size1 = exhibitionService.queryMyExhibitionListSize(exhibitionMap);

            Map<String,Object> successCaseMap = new HashMap<>();
            exhibitionMap.put("createId", String.valueOf(memberId));
            int size2 = successCaseService.queryMySuccessCaseListSize(successCaseMap);

            map.put("exhibitionSize",size1);
            map.put("successCaseSize",size2);

            result.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "威客信息", notes = "威客信息", response = Response.class)
    @RequestMapping(value = "sel_witkey_info_size", method = RequestMethod.GET)
    public Response sel_witkey_info_size() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        Map map = new HashMap();
        if(memberId!=null){
            Map<String,Object> taskMap = new HashMap<>();
            taskMap.put("parentId",1);
            taskMap.put("createId", String.valueOf(memberId));
            int size1 = cooperationService.queryMyWitkeyListSize(taskMap);

            Map<String,Object> serviceMap = new HashMap<>();
            serviceMap.put("parentId",2);
            serviceMap.put("createId", String.valueOf(memberId));
            int size2 = cooperationService.queryMyWitkeyListSize(serviceMap);

            Map<String,Object> cooperationMap = new HashMap<>();
            cooperationMap.put("parentId",3);
            cooperationMap.put("createId", String.valueOf(memberId));
            int size3 = cooperationService.queryMyWitkeyListSize(cooperationMap);

            map.put("taskSize",size1);
            map.put("serviceSize",size2);
            map.put("cooperationSize",size3);

            result.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "产品相关信息", notes = "产品相关信息", response = Response.class)
    @RequestMapping(value = "sel_productInfoSize", method = RequestMethod.GET)
    public Response sel_productInfoSize() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = brandService.queryBrandProductAgentCount(memberId);
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "询报价相关信息", notes = "询报价相关信息", response = Response.class)
    @RequestMapping(value = "sel_enqueryQuoteCount", method = RequestMethod.GET)
    public Response sel_enqueryQuoteCount() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = offerPriceService.queryEnqueryQuoteCount(memberId);
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "技术资料信息", notes = "技术资料信息", response = Response.class)
    @RequestMapping(value = "sel_dataUploadDLCount", method = RequestMethod.GET)
    public Response sel_dataUploadDLCount() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = techDataService.findDataUploadDownloadCount(memberId);
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "招聘信息", notes = "招聘信息", response = Response.class)
    @RequestMapping(value = "sel_jobCount", method = RequestMethod.GET)
    public Response sel_jobCount() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = resumeService.queryJobCount(memberId);
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "账号数字资产", notes = "账号数字资产", response = Response.class)
    @RequestMapping(value = "sel_zhbInfo", method = RequestMethod.GET)
    public Response sel_zhbInfo() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = indexService.getZhbInfo(memberId);
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "会员相关信息", notes = "会员相关信息", response = Response.class)
    @RequestMapping(value = "sel_mem_info", method = RequestMethod.GET)
    public Response sel_mem_info() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = indexService.getMemInfo(String.valueOf(memberId));
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "个人求职信息", notes = "个人求职信息", response = Response.class)
    @RequestMapping(value = "sel_job_info_count", method = RequestMethod.GET)
    public Response sel_job_info_count() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = indexService.getMemJobInfo(String.valueOf(memberId));
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "普通个人专家问答", notes = "普通个人专家问答", response = Response.class)
    @RequestMapping(value = "sel_individual_question_count", method = RequestMethod.GET)
    public Response sel_individual_question_count() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = indexService.getIndividualQuestionInfo(String.valueOf(memberId));
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "专家个人专家问答", notes = "专家个人专家问答", response = Response.class)
    @RequestMapping(value = "sel_expert_question_count", method = RequestMethod.GET)
    public Response sel_expert_question_count() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map<String,Object> resultMap = indexService.getExpertQuestionInfo(String.valueOf(memberId));
            result.setData(resultMap);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "消息公告", notes = "消息公告", response = Response.class)
    @RequestMapping(value = "sel_news_notice", method = RequestMethod.GET)
    public Response sel_news_notice() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        Map map = new HashMap();
        if(memberId!=null){
            List<Map<String,String>> noticeList = indexService.queryNoticeList();
            List<Map<String,String>> newsList = indexService.queryNewsList(String.valueOf(memberId));
            map.put("noticeList",noticeList);
            map.put("newsList",newsList);
            result.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }
}

package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.mobile.web.mc.MobileExpertController;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.service.MobileTalentNetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人才网
 * <p/>
 * Created by Administrator on 2016/11/24 0024.
 */

@RestController
@RequestMapping("/rest/m/talent/network/site")
@Api(value = "mobileTalentNetwork", description = "人才网")
public class MobileTalentNetworkController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileExpertController.class);


    @Autowired
    MobileTalentNetworkService mobileTalentNetworkService;


    /***
     * 简历详情页
     */
    @RequestMapping(value = "sel_resume_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-简历详情", notes = "触屏端-人才网-简历详情", response = Response.class)
    public Response selResumeDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            getPrivilegeGoodsDetails(resultMap, Long.parseLong(id), ZhbConstant.ZhbGoodsType.CXXZJL);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
        }
        return new Response(resultMap);
    }


    /**
     * 公司详情页
     */
    @RequestMapping(value = "sel_company_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-公司详情", notes = "触屏端-人才网-公司详情", response = Response.class)
    public Response selCompanyDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        MemberDetails member = new MemberDetails();
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        try {
            //公司详情
            member = mobileTalentNetworkService.queryCompanyInfo(Long.parseLong(id));
            //公司发布的其他职位
            List<Map<String, Object>> job = mobileTalentNetworkService.queryJobByCompany(id);
            map.put("member", member);
            map.put("job", job);
            response.setData(map);
        } catch (Exception e) {
            log.error("sel_company_details error! ", e);
        }
        return response;
    }


    @RequestMapping(value = "sel_position_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-职位详情", notes = "触屏端-人才网-职位详情", response = Response.class)
    public Response selPositionDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Response response = new Response();
        try {
            Job job = mobileTalentNetworkService.getPositionByPositionId(id);
            response.setData(job);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-人才网-资讯详情", notes = "触屏端-人才网-资讯详情", response = Response.class)
    @RequestMapping(value = "sel_news_detail", method = RequestMethod.GET)
    public Response selNewsDetailetail(@ApiParam("资讯ID") @RequestParam String id) {
        Response response = new Response();
        try {
            ChannelNews news = mobileTalentNetworkService.selectByID(Long.valueOf(id));
            response.setData(news);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
        }
        return response;
    }


    @RequestMapping(value = "update_collection_resume", method = RequestMethod.POST)
    @ApiOperation(value = "触屏端-人才网-收藏简历", notes = "触屏端-人才网-收藏简历", response = Response.class)
    public Response updateCollectionResume(@ApiParam(value = "简历id") @RequestParam String id) throws Exception {
        Response response = new Response();
        try {
            response = mobileTalentNetworkService.selCollectionResume(id);
        } catch (Exception e) {
            log.error("update_collection_resume error! ", e);
        }
        return response;
    }
}

package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.mobile.web.mc.MobileExpertController;
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

import java.io.IOException;
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
        Response response=new Response();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.CXXZJL);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            boolean count = mobileTalentNetworkService.collectionResume(map);
            resultMap.put("count",count);
            response.setData(resultMap);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
        }
        return response;
    }


    /**
     * 公司详情页
     */
    @RequestMapping(value = "sel_company_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-公司详情", notes = "触屏端-人才网-公司详情", response = Response.class)
    public Response selCompanyDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Map<String, Object> map = new HashMap<>();
        Response response = new Response();
        try {
            //公司详情
            MemberDetails member = mobileTalentNetworkService.queryCompanyInfo(Long.parseLong(id));
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
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map = mobileTalentNetworkService.getPositionByPositionId(map);
            response.setData(map);
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


    @RequestMapping(value = "apply_position", method = RequestMethod.POST)
    @ApiOperation(value = "触屏端-人才网-简历投递", notes = "触屏端-人才网-简历投递", response = Response.class)
    public Response applyPosition(@ApiParam(value = "职位ID") @RequestParam String jobID) throws IOException {
        Response response = new Response();
       // Integer count = mobileTalentNetworkService.isExistApplyPosition(map);
        //根据职业的ID查找发布企业ID
        Long recID =mobileTalentNetworkService.querycompanyByJobId(jobID);
        //职位标题
        String messageText =mobileTalentNetworkService.queryJobNameByJobId(jobID);

        //投递简历
        boolean b=mobileTalentNetworkService.queryResumeByCreateId(jobID,recID,messageText);
        response.setData(b);
        return response;
    }

    @RequestMapping(value = "is_exist_apply_position", method = RequestMethod.GET)
    @ApiOperation(value = "查看此职位是否已被同一个人申请，10天后可以再次申请", notes = "1:已申请，0：未申请", response = Response.class)
    public Response isExistApplyPosition(@ApiParam(value = "职位ID") @RequestParam String JobID) throws Exception {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("jobID", JobID);
        boolean b = mobileTalentNetworkService.isExistApplyPosition(map);
        response.setData(b);
        return response;
    }


}

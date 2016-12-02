package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.service.MobileJobAndResumeService;
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
public class MobileJobAndResumeController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileJobAndResumeController.class);

    @Autowired
    MobileJobAndResumeService mJobSV;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MobileTalentNetworkService mobileTalentNetworkService;


    /***
     * 简历详情页
     */
    @RequestMapping(value = "sel_resume_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-简历详情", notes = "触屏端-人才网-简历详情", response = Response.class)
    public Response selResumeDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Map<String, Object> resultMap = new HashMap<>();
        Response response = new Response();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.CXXZJL);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            boolean count = mobileTalentNetworkService.collectionResume(map);
            resultMap.put("count", count);
            response.setData(resultMap);
        } catch (Exception e) {
            log.error("sel_resume_details error! ", e);
        }
        return response;
    }


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

    @RequestMapping(value = "sel_position_detail", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-职位详情", notes = "触屏端-人才网-职位详情", response = Response.class)
    public Response selPositionDetail(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
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


    @RequestMapping(value = "sel_position_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-职位详情", notes = "触屏端-人才网-职位详情", response = Response.class)
    public Response selPositionDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Response response = new Response();
        try {
            Job job = mJobSV.getPositionByPositionId(id);
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
            ChannelNews news = mJobSV.selectByID(Long.valueOf(id));
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


    @ApiOperation(value = "企业规模", notes = "企业规模", response = Response.class)
    @RequestMapping(value = "sel_employeeSizeList", method = RequestMethod.GET)
    public Response employeeSizeList() {
        return new Response(memberService.findEmployeeSizeList());
    }

    @ApiOperation(value = "企业性质", notes = "企业性质", response = Response.class)
    @RequestMapping(value = "sel_enterpriseTypeList", method = RequestMethod.GET)
    public Response enterpriseTypeList() {
        return new Response(memberService.findEnterpriseTypeList());
    }

    @RequestMapping(value = "sel_all_position", method = RequestMethod.GET)
    @ApiOperation(value = "职位搜索", notes = "职位频道页搜索", response = Response.class)
    public Response queryAllPosition(@ApiParam(value = "公司名称或企业名称") @RequestParam(required = false) String name,
                                     @ApiParam(value = "省代码") @RequestParam(required = false) String province,
                                     @ApiParam(value = "市代码") @RequestParam(required = false) String city,
                                     @ApiParam(value = "区代码") @RequestParam(required = false) String area,
                                     @ApiParam(value = "企业规模") @RequestParam(required = false) String employeeNumber,
                                     @ApiParam(value = "企业性质") @RequestParam(required = false) String enterpriseType,
                                     @ApiParam(value = "发布时间") @RequestParam(required = false) String days,
                                     @ApiParam(value = "薪资") @RequestParam(required = false) String salary,
                                     @ApiParam(value = "职位类别") @RequestParam(required = false) String positionType,
                                     @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {

        return new Response(mJobSV.getJobSearchResultPager(name, province, city, area,
                employeeNumber, enterpriseType, days, salary, positionType, pageNo, pageSize));

    }

    @RequestMapping(value = "sel_all_resume", method = RequestMethod.GET)
    @ApiOperation(value = "人才库搜索", notes = "人才库搜索", response = Response.class)
    public Response findAllResume(@ApiParam(value = "关键字") @RequestParam(required = false) String title,
                                  @ApiParam(value = "期望工作城市") @RequestParam(required = false) String jobCity,
                                  @ApiParam(value = "工作经验") @RequestParam(required = false) String expYear,
                                  @ApiParam(value = "刷新时间") @RequestParam(required = false) String refreshType,
                                  @ApiParam(value = "学历") @RequestParam(required = false) String education,
                                  @ApiParam(value = "职位类别") @RequestParam(required = false) String positionType,
                                  @ApiParam(value = "是否公开") @RequestParam(required = false) String isPublic,
                                  @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {

        return new Response(mJobSV.getResumePager(title, jobCity, expYear, refreshType, education,
                positionType, isPublic, pageNo, pageSize));
    }

    @ApiOperation(value = "人才网资讯列表", notes = "人才网资讯列表", response = Response.class)
    @RequestMapping(value = "sel_newslist", method = RequestMethod.GET)
    public Response listNews(@ApiParam("资讯类别 14:面试技巧 15:职场动态") @RequestParam(required = false) String type,
                             @ApiParam("限制条数") @RequestParam(required = false) String count,
                             @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                             @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) {

        return new Response(mJobSV.getJobNewsList(type, count, pageNo, pageSize));
    }

    @ApiOperation(value = "人才网首页展示数据", notes = "人才网首页展示数据", response = Response.class)
    @RequestMapping(value = "sel_rencai_index_data", method = RequestMethod.GET)
    public Response listNews() throws Exception {
        return new Response(mJobSV.getIndexInfo());
    }


    @ApiOperation(value = "筑慧猎头广告位", notes = "筑慧猎头广告位", response = Response.class)
    @RequestMapping(value = "sel_hunting_company", method = RequestMethod.GET)
    public Response selHuntingCompany() throws Exception {
        return new Response(mJobSV.getHuntingCompany());
    }



}

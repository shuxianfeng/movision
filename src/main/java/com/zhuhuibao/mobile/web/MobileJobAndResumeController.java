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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

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



    /***
     * 简历详情页
     */
    @RequestMapping(value = "sel_resume_details", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-人才网-简历详情", notes = "触屏端-人才网-简历详情", response = Response.class)
    public Response selResumeDetails(@ApiParam(value = "简历Id") @RequestParam(required = true) String id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            getPrivilegeGoodsDetails(resultMap, id, ZhbConstant.ZhbGoodsType.CXXZJL);
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
            member = mJobSV.queryCompanyInfo(Long.parseLong(id));
            //公司发布的其他职位
            List<Map<String, Object>> job = mJobSV.queryJobByCompany(id);
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
            response = mJobSV.selCollectionResume(id);
        } catch (Exception e) {
            log.error("update_collection_resume error! ", e);
        }
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
                                     @ApiParam(value = "页码") @RequestParam(required = false,defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页显示的数目") @RequestParam(required = false,defaultValue = "10") String pageSize) throws IOException {

        return new Response(mJobSV.getJobSearchResultPager(name, province, city, area,
                employeeNumber, enterpriseType, days, salary, positionType, pageNo, pageSize));

    }

    @RequestMapping(value = "sel_all_resume", method = RequestMethod.GET)
    @ApiOperation(value = "人才库搜索", notes = "人才库搜索", response = Response.class)
    public Response findAllResume(@ApiParam(value = "关键字") @RequestParam(required = false) String title,
                                  @ApiParam(value = "期望工作城市") @RequestParam(required = false) String jobCity,
                                  @ApiParam(value = "工作年限前") @RequestParam(required = false) String expYearBefore,
                                  @ApiParam(value = "工作年限后") @RequestParam(required = false) String expYearBehind,
                                  @ApiParam(value = "学历") @RequestParam(required = false) String education,
                                  @ApiParam(value = "职位类别") @RequestParam(required = false) String positionType,
                                  @ApiParam(value = "是否公开") @RequestParam(required = false) String isPublic,
                                  @ApiParam(value = "页码") @RequestParam(required = false,defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false,defaultValue = "10") String pageSize) throws IOException {

        return new Response(mJobSV.getResumePager(title, jobCity, expYearBefore, expYearBehind, education,
                positionType, isPublic, pageNo, pageSize));
    }

    @ApiOperation(value = "人才网资讯列表", notes = "人才网资讯列表", response = Response.class)
    @RequestMapping(value = "sel_newslist", method = RequestMethod.GET)
    public Response listNews(@ApiParam("资讯类别 14:面试技巧 15:职场动态 16:行业资讯") @RequestParam String type,
                             @ApiParam("限制条数") @RequestParam(required = false) String count,
                             @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                             @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) {

        return new Response(mJobSV.getJobNewsList(type, count, pageNo, pageSize));
    }

    @ApiOperation(value = "人才网首页展示数据", notes = "人才网首页展示数据", response = Response.class)
    @RequestMapping(value = "sel_rencai_index_data", method = RequestMethod.GET)
    public Response listNews() throws Exception{
        return new Response(mJobSV.getIndexInfo());
    }

}

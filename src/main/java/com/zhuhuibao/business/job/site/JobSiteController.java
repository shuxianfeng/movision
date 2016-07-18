package com.zhuhuibao.business.job.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
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
 * Created by Administrator on 2016/4/21 0021.
 */
@RestController
@RequestMapping("/rest/job/site/recruit")
public class JobSiteController {
    private final static Logger log = LoggerFactory.getLogger(JobSiteController.class);

    @Autowired
    JobPositionService job;

    @Autowired
    ResumeService resume;

    @Autowired
    SiteMailService smService;

    @Autowired
    JobRelResumeService jrrService;

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    MemberService memberService;

    @Autowired
    private SysAdvertisingService advService;

    @ApiOperation(value = "获取职位类别", notes = "获取职位类别", response = Response.class)
    @RequestMapping(value = "sel_positionType", method = RequestMethod.GET)
    public Response positionType() {
        Response response = new Response();
        List list = job.positionType();
        response.setData(list);
        return response;
    }

    @RequestMapping(value = "apply_position", method = RequestMethod.POST)
    @ApiOperation(value = "应聘职位", notes = "应聘职位", response = Response.class)
    public Response applyPosition(@ApiParam(value = "职位ID") @RequestParam String jobID,
                                  @ApiParam(value = "发布职位企业ID") @RequestParam String recID,
                                  @ApiParam(value = "职位标题") @RequestParam String messageText) throws IOException {
        log.info("applay position recID = " + recID + " messageText =" + messageText);
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if (createid != null) {
            Resume rme = resume.queryResumeByCreateId(createid);
            if (rme != null && rme.getId() != null) {
                Long resumeID = Long.valueOf(rme.getId());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("jobID", jobID);
                map.put("resumeID", resumeID);
                //职位没有被申请过或者申请10天后可以再次申请
                if (jrrService.isExistApplyPosition(map) == 0) {
                    MessageText msgText = new MessageText();
                    msgText.setSendID(createid);
                    msgText.setRecID(Long.valueOf(recID));
                    msgText.setMessageText(messageText);
                    msgText.setTypeID(resumeID);
                    msgText.setType(JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
//                    response = smService.addSiteMail(msgText);
                    //删除有可能存在的简历和职位对应的关系
                    jrrService.deleteJobRelResume(map);
                    jrrService.insert(Long.valueOf(jobID), resumeID, createid);
                }
            }

        } else {
            response.setCode(400);
            response.setMessage("you are not login!!!!");
        }
        return response;
    }

    @RequestMapping(value = "sel_companyInfo", method = RequestMethod.GET)
    @ApiOperation(value = "公司详情", notes = "公司详情", response = Response.class)
    public Response queryCompanyInfo(@ApiParam(value = "创建者ID(会员ID)") @RequestParam(required = true) Long id) throws Exception {
        log.info("query company info id " + id);
        Response response = job.queryCompanyInfo(id);
        return response;
    }

/*    @RequestMapping(value="sel_adv_position", method = RequestMethod.GET)
    @ApiOperation(value = "企业信息广告位",notes = "展示最新发布职位的企业信息，默认7条可配置",response = Response.class)
    public Response queryAdvertisingPosition() throws IOException
    {
        log.info("query advertising postion");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("isDelete",0);
        map.put("count",7);
        Response response = job.queryAdvertisingPosition(map);
        return response;
    }*/

    @RequestMapping(value = "sel_position", method = RequestMethod.GET)
    @ApiOperation(value = "职位详情页面", notes = "职位搜索查看职位详情", response = Response.class)
    public Response queryPositionInfoByID(@ApiParam(value = "招聘职位ID") @RequestParam Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        map.put("createid", String.valueOf(createid));
        map.put("id", id);
        response = job.queryPositionInfoByID(map);
        job.updateViews(id);
        return response;
    }

    @RequestMapping(value = "sel_other_position", method = RequestMethod.GET)
    @ApiOperation(value = "职位详情中的其它职位", notes = "职位详情中的其它职位", response = Response.class)
    public Response queryOtherPosition(
            @ApiParam(value = "职位ID") @RequestParam(required = true) String jobID,
            @ApiParam(value = "") @RequestParam() String createID,
            @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false) String pageSize) throws IOException {
        log.info("query position info by id");
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,Object>> pager = new Paging<Map<String,Object>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("createID", createID);
        map.put("jobID", jobID);
        Response response = new Response();
        List<Map<String,Object>> jobList = job.findAllOtherPosition(pager, map);
        pager.result(jobList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "sel_pub_position", method = RequestMethod.GET)
    @ApiOperation(value = "查询某个企业发布的职位", notes = "查询某个企业发布的职位", response = Response.class)
    public Response queryPublishPositionByID(
            @ApiParam(value = "企业ID") @RequestParam String enterpriseID,
            @ApiParam(value = "城市code") @RequestParam(required = false) String city,
            @ApiParam(value = "名称") @RequestParam(required = false) String name,
            String pageNo, String pageSize) throws IOException {
        log.info("query position info by id");
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,Object>> pager = new Paging<Map<String,Object>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("createID", enterpriseID);
        map.put("city", city);
        map.put("name", name);
        Response response = new Response();
        List<Map<String,Object>> jobList = job.findAllOtherPosition(pager, map);
        pager.result(jobList);
        response.setData(pager);
        return response;
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
        Response response = new Response();
        log.info("query position info by id");

        Map<String, Object> map = new HashMap<>();
        if (name != null && !"".equals(name)) {
            map.put("name", name.replace("_", "\\_"));
        }
        map.put("positionType", positionType);
        map.put("city", city);
        map.put("employeeNumber", employeeNumber);
        map.put("enterpriseType", enterpriseType);
        //发布时间一周内，一天内
        if (days != null && !"".equals(days)) {
            Date date = DateUtils.date2Sub(new Date(), 5, -Integer.parseInt(days));
            String publishTime = DateUtils.date2Str(date, "yyyy-MM-dd");
            map.put("publishTime", publishTime);
        }
        map.put("salary", salary);
        Paging<Map<String,Object>> pager = new Paging<Map<String,Object>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String,Object>> jobList = job.findAllOtherPosition(pager, map);
        pager.result(jobList);
        response.setData(pager);
        return response;
    }


    @RequestMapping(value = "sel_recommend_position", method = RequestMethod.GET)
    @ApiOperation(value = "根据当前职位类别查询出其它公司的职位", notes = "查找按照最新时间排序", response = Response.class)
    public Response queryRecommendPosition(@ApiParam(value = "职位ID") @RequestParam String jobID,
                                           @ApiParam(value = "职位类别ID") @RequestParam String postID) throws IOException {
        //查询不同公司发布的相同职位
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("postID", postID);
        map.put("count", JobConstant.JOB_RECOMMEND_COUNT);
        map.put("delete", JobConstant.JOB_DELETE_ZERO);
        map.put("jobID", jobID);
        Response response = job.searchSamePosition(map);
        return response;
    }

    @RequestMapping(value = "sel_hot_position", method = RequestMethod.GET)
    @ApiOperation(value = "人才网首页热门招聘 默认7个", notes = "人才网首页热门招聘 默认7个", response = Response.class)
    public Response queryHotPosition(@ApiParam(value = "频道类型 6：人才") @RequestParam String chanType,
                                     @ApiParam(value = "频道下子页面.index:首页") @RequestParam String page,
                                     @ApiParam(value = "广告所在区域:F1") @RequestParam String advArea) {
        log.debug("广告区域信息展示->频道类型:{},频道子页面:{},广告所在区域:{}", new Object[]{chanType, page, advArea});

        List<SysAdvertising> advertisings = advService.findListByCondition(chanType, page, advArea);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysAdvertising item : advertisings) {
            String jobID = item.getConnectedId();
            Map<String, Object> map = job.findJobByID(jobID);
            map.put("logo",item.getImgUrl());
            list.add(map);
        }
        return new Response(list);
    }

    @RequestMapping(value = "sel_latest_position", method = RequestMethod.GET)
    @ApiOperation(value = "人才网首页最新招聘", notes = "人才网首页最新招聘", response = Response.class)
    public Response queryLatestJob() throws Exception {
        Response response = new Response();
        List list = job.queryLatestJob(5);
        response.setData(list);
        return response;

    }

    @RequestMapping(value = "sel_greatCompany_position", method = RequestMethod.GET)
    @ApiOperation(value = "广告位名企招聘", notes = "广告位名企招聘", response = Response.class)
    public Response greatCompanyPosition() throws Exception {
        Response response = new Response();
        List enterpriseList = job.greatCompanyPosition();
        response.setData(enterpriseList);
        return response;
    }

    @RequestMapping(value = "sel_similar_company", method = RequestMethod.GET)
    @ApiOperation(value = "相似企业", notes = "相似企业", response = Response.class)
    public Response querySimilarCompany(@ApiParam(value = "企业ID(创建者ID)") @RequestParam String id) throws IOException {
        Response response = job.querySimilarCompany(id, 4);
        return response;
    }

    @RequestMapping(value = "sel_com_hot_position", method = RequestMethod.GET)
    @ApiOperation(value = "热门职位", notes = "查询名企发布的热门职位", response = Response.class)
    public Response queryEnterpriseHotPosition() throws IOException {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        //“1”推荐企业.
        map.put("recommend", JobConstant.JOB_RECOMMEND_TRUE);
        map.put("count", JobConstant.JOB_HOTPOSITION_COUNT_EIGHT);
        map.put("status", JobConstant.JOB_MEMBER_STATUS_LOGOUT);
        List<Job> jobList = job.queryEnterpriseHotPosition(map);
        response.setData(jobList);
        return response;
    }

    @RequestMapping(value = "isExist_apply_position", method = RequestMethod.GET)
    @ApiOperation(value = "查看此职位是否已被同一个人申请，10天后可以再次申请", notes = "1:已申请，0：未申请", response = Response.class)
    public Response isExistApplyPosition(@ApiParam(value = "职位ID") @RequestParam String JobID,
                                         @ApiParam(value = "简历ID") @RequestParam String resumeID) throws Exception {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jobID", JobID);
        map.put("resumeID", resumeID);
        Integer count = jrrService.isExistApplyPosition(map);
        response.setData(count);
        return response;
    }

    @RequestMapping(value = "sel_pub_job_city", method = RequestMethod.GET)
    @ApiOperation(value = "查询某企业发布职位的城市", notes = "查询某企业发布职位的城市", response = Response.class)
    public Response queryPublishJobCity(@ApiParam(value = "企业ID(创建者ID)") @RequestParam String enterpriseID) throws Exception {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("enterpriseID", enterpriseID);
        List<Map<String, String>> jobList = job.queryPublishJobCity(map);
        response.setData(jobList);
        return response;
    }

    @RequestMapping(value = "sel_adv_position", method = RequestMethod.GET)
    @ApiOperation(value = "人才网企业广告", notes = "人才网企业广告", response = Response.class)
    public Response queryAdvertisingPosition(@ApiParam(value = "频道类型 6:人才") @RequestParam String chanType,
                                             @ApiParam(value = "频道下子页面.position:职位;meeting:招聘会;hunter:猎头") @RequestParam String page,
                                             @ApiParam(value = "广告所在区域:F1:职位列表页右侧企业广告;F2:职位企业介绍页右下侧广告;" +
                                                     "F3:招聘会列表页右下侧广告;F4:猎头页底部合作企业") @RequestParam String advArea) {
        Response response = new Response();

        Map<String, Object> map = new HashMap<>();
        map.put("chanType", chanType);
        map.put("page", page);
        map.put("advArea", advArea);
        List<Map<String, String>> companyList = memberService.queryCompanyList(map);

        response.setData(companyList);
        return response;
    }
}

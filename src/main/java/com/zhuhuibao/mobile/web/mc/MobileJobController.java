package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.service.*;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 求职招聘controller
 *
 * @author liyang
 * @date 2016年11月16日
 */
@RestController
@RequestMapping("/rest/m/job/mc/")
@Api(value = "MobileJobController", description = "触屏端-盟友中心-求职招聘")
public class MobileJobController {

    private static final Logger log = LoggerFactory.getLogger(MobileJobController.class);

    @Autowired
    private MobileJobService mobileJobService;

    @Autowired
    private MobileResumeService mobileResumeService;

    @Autowired
    private MobilePaymentGoodsService mobilePaymentGoodsService;

    @Autowired
    private MobileForbidKeyWordsService mobileForbidKeyWordsService;

    @Autowired
    private MobileMemberService mobileMemberService;

    @ApiOperation(value = "触屏端-盟友中心-我发布的招聘列表页", notes = "触屏端-盟友中心-我发布的招聘列表页")
    @RequestMapping(value = "sel_position_list", method = RequestMethod.GET)
    public Response selPositionList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        try {
            if (createId != null) {
                Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
                pager.result(mobileJobService.findAllPositionByMemId(pager, String.valueOf(createId)));
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } catch (Exception e) {
            log.error("sel_position_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-刷新已发布的职位", notes = "触屏端-盟友中心-刷新已发布的职位", response = Response.class)
    @RequestMapping(value = "refresh_position", method = RequestMethod.POST)
    public Response refreshPosition(@ApiParam(value = "要刷新职位的id,两个id之间用逗号隔开") @RequestParam String ids) throws IOException {
        try {
            mobileJobService.refreshPosition(ids);
        } catch (Exception e) {
            log.error("refresh_position error! ", e);
            e.printStackTrace();
        }
        return new Response();
    }

    @ApiOperation(value = "触屏端-盟友中心-职位详情页", notes = "触屏端-盟友中心-职位详情页", response = Response.class)
    @RequestMapping(value = "sel_position", method = RequestMethod.GET)
    public Response selPosition(@ApiParam(value = "职位id") @RequestParam String id) {
        Response response = new Response();
        response.setData(mobileJobService.getPositionByPositionId(id));
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-我收到的简历列表", notes = "触屏端-盟友中心-我收到的简历", response = Response.class)
    @RequestMapping(value = "sel_receive_resume_list", method = RequestMethod.GET)
    public Response selReceiveResumeList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            pager.result(mobileResumeService.receiveResume(pager, createId.toString()));
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-我收到的简历详情", notes = "触屏端-盟友中心-我收到的简历详情", response = Response.class)
    @RequestMapping(value = "sel_resume", method = RequestMethod.GET)
    public Response selResume(@ApiParam(value = "简历id") @RequestParam String id, @ApiParam(value = "该投递简历记录的id,频道页不传，会员中心查看简历记录时候传") @RequestParam(required = false) String recordId) throws Exception {
        Long memberId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        Response response = new Response();
        try {
            if (memberId != null) {
                Map<String, String> result = mobileResumeService.queryMyReceiveResume(id, recordId);
                if (result != null) {
                    mobileResumeService.updateJobRelResume(recordId);
                    mobileResumeService.updateResume(id);
                    // 发布招聘职位已付过钱，查看应聘的简历，也属于付费查看过的简历,需要增加到已付费信息表。这部分信息不需要付费
                    mobilePaymentGoodsService.insertViewGoods(Long.parseLong(id), memberId, companyId, ZhbPaymentConstant.goodsType.CXXZJL.toString());
                    Resume resume = mobileResumeService.previewResume(id);
                    mobileResumeService.addLookRecord(id, companyId, resume.getCreateid());
                    response.setData(resume);
                } else {
                    throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
                }
            } else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } catch (Exception e) {
            log.error("sel_resume error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-我收藏的简历", notes = "触屏端-盟友中心-我收藏的简历", response = Response.class)
    @RequestMapping(value = "sel_collect_resume_list", method = RequestMethod.GET)
    public Response selCollectResumeList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            pager.result(mobileResumeService.findAllCollectResume(pager, createId.toString()));
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-批量删除收藏的简历", notes = "触屏端-盟友中心-批量删除收藏的简历", response = Response.class)
    @RequestMapping(value = "batch_del_collect_resume", method = RequestMethod.POST)
    public Response batchDelCollectResume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids) {
        Response response = new Response();
        mobileResumeService.batchDelCollectResume(ids);
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-我下载的简历", notes = "触屏端-盟友中心-我下载的简历", response = Response.class)
    @RequestMapping(value = "sel_download_resume", method = RequestMethod.GET)
    public Response sel_download_resume(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            pager.result(mobileResumeService.findAllDownloadResume(pager, createId.toString()));
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-批量删除下载的简历", notes = "触屏端-盟友中心-批量删除下载的简历", response = Response.class)
    @RequestMapping(value = "batch_del_download_resume", method = RequestMethod.POST)
    public Response batchDelDownloadResume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids) {
        mobileResumeService.batchDelDownloadResume(ids);
        return new Response();
    }

    @ApiOperation(value = "触屏端-盟友中心-查询我的简历", notes = "触屏端-盟友中心-查询我的简历", response = Response.class)
    @RequestMapping(value = "sel_my_resume", method = RequestMethod.POST)
    public Response selMyResume() throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        Map result = new HashMap();
        if (createId != null) {
            // 判断用户是否存在简历
            List<String> ids = mobileResumeService.selectIdsByCreateId(createId);
            if (ids != null && ids.size() >= 1) {
                result.put("hasResume", true);
                List<Resume> resumeList = new ArrayList<>();
                for (String id : ids) {
                    resumeList.add(mobileResumeService.previewResume(id));
                }
            } else {
                result.put("hasResume", false);
            }
            response.setData(result);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-创建简历", notes = "触屏端-盟友中心-创建简历", response = Response.class)
    @RequestMapping(value = "add_resume", method = RequestMethod.POST)
    public Response setUpResume(@ModelAttribute Resume resume) throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            // 判断用户是否存在简历
            List<String> ids = mobileResumeService.selectIdsByCreateId(createId);
            if (ids != null && ids.size() >= 1) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "该用户已存在简历");
            }
            // 期望工作地点不得超过5个
            int proCount = 0;
            int cityCount = 0;
            String jobProvinces = resume.getJobProvince();
            if (!StringUtils.isEmpty(jobProvinces)) {
                String[] provinces = resume.getJobProvince().split(",");
                proCount = provinces.length;
            }
            String jobCities = resume.getJobCity();
            if (!StringUtils.isEmpty(jobCities)) {
                String[] citys = resume.getJobCity().split(",");
                cityCount = citys.length;
            }
            if ((proCount + cityCount) > 5) {
                throw new BusinessException(MsgCodeConstant.RESUME_JOB_COUNT_LIMIT, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.RESUME_JOB_COUNT_LIMIT)));
            }
            resume.setCreateid(createId.toString());
            response.setData(mobileResumeService.setUpResume(resume));
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-更新简历", notes = "触屏端-盟友中心-更新简历", response = Response.class)
    @RequestMapping(value = "upd_resume", method = RequestMethod.POST)
    public Response updateResume(@ModelAttribute Resume resume) throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            mobileResumeService.updateResume(resume);
            response.setData(resume.getId());
            return response;
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    @ApiOperation(value = "触屏端-盟友中心-查看免费简历", notes = "触屏端-盟友中心-查看免费简历", response = Response.class)
    @RequestMapping(value = "sel_free_resume", method = RequestMethod.GET)
    public Response selFreeResume(@ApiParam(value = "简历id") @RequestParam String id) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Resume resume = mobileResumeService.previewResume(id);
            response.setData(resume);
            return response;
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    @ApiOperation(value = "触屏端-盟友中心-我申请的职位", notes = "触屏端-盟友中心-我申请的职位", response = Response.class)
    @RequestMapping(value = "sel_my_position_list", method = RequestMethod.GET)
    public Response myApplyPositionList(@RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Paging<Job> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            pager.result(mobileJobService.myApplyPosition(pager, String.valueOf(createId)));
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-查询屏蔽企业列表", notes = "触屏端-盟友中心-查询屏蔽企业列表")
    @RequestMapping(value = "sel_forbid_keywords_list", method = RequestMethod.GET)
    public Response selForbidKeyWordsList() {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            List<Map<String, String>> list = mobileForbidKeyWordsService.queryKeyWordsList(createId);
            response.setData(list);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-根据关键字获取企业", notes = "触屏端-盟友中心-根据关键字获取企业")
    @RequestMapping(value = "sel_company_by_keywords_list", method = RequestMethod.GET)
    public Response selCompanyByKeywordsList(@RequestParam(required = false) String keywords) {
        Response response = new Response();
        List<Map<String, String>> list = mobileMemberService.queryCompanyByKeywords(keywords);
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-删除屏蔽企业", notes = "触屏端-盟友中心-删除屏蔽企业")
    @RequestMapping(value = "del_forbid_keywords", method = RequestMethod.POST)
    public Response delForbidKeyWords(@ApiParam(value = "公司id") @RequestParam String id) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            mobileForbidKeyWordsService.deletleForbidKeyWords(id);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-盟友中心-增加屏蔽企业", notes = "触屏端-盟友中心-增加屏蔽企业")
    @RequestMapping(value = "add_forbid_keywords", method = RequestMethod.POST)
    public Response addForbidKeyWords(@ApiParam(value = "公司id") @RequestParam(required = false) String companyId) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            List<Map<String, String>> list = mobileForbidKeyWordsService.queryKeyWordsList(createId);
            if (list.size() == 10) {
                throw new BusinessException(MsgCodeConstant.FORBID_KEYWORDS_LIMIT, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.FORBID_KEYWORDS_LIMIT)));
            } else {
                for (Map<String, String> aList : list) {
                    Map map1 = (Map) aList;
                    String cId = (String) map1.get("company_id");
                    if (companyId.equals(cId)) {
                        throw new BusinessException(MsgCodeConstant.FORBID_KEYWORDS_REPEAT, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.FORBID_KEYWORDS_REPEAT)));
                    }
                }
                mobileForbidKeyWordsService.addForbidKeyWords(String.valueOf(createId), companyId);
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}

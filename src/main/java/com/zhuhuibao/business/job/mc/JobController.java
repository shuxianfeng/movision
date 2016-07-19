package com.zhuhuibao.business.job.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.CollectRecord;
import com.zhuhuibao.mybatis.memCenter.entity.DownloadRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/18 0018.
 */
@RestController
@RequestMapping("/rest/job/mc/recruit")
@Api(value = "Jobs", description = "会员中心-招聘管理")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobPositionService jobService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    JobRelResumeService jrrService;

    @ApiOperation(value = "获取职位类别", notes = "获取职位类别", response = Response.class)
    @RequestMapping(value = "sel_positionType", method = RequestMethod.GET)
    public Response positionType() {
        Response response = new Response();
        List list = jobService.positionType();
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "发布职位", notes = "发布职位", response = Response.class)
    @RequestMapping(value = "add_position", method = RequestMethod.POST)
    public Response publishPosition(@ApiParam(value = "职位属性")@ModelAttribute() Job job) throws Exception {
        Response response = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            job.setCreateid(String.valueOf(createid));
            jobService.publishPosition(job);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询公司已发布的职位", notes = "查询公司已发布的职位", response = Response.class)
    @RequestMapping(value = "sel_positionList", method = RequestMethod.GET)
    public Response searchPositionByMemId(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            Paging<Job> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            List list = jobService.findAllPositionByMemId(pager, String.valueOf(createid));
            pager.result(list);
            response.setData(pager);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询公司发布的某条职位的信息", notes = "查询公司发布的某条职位的信息", response = Response.class)
    @RequestMapping(value = "sel_position", method = RequestMethod.GET)
    public Response getPositionByPositionId(String id) {
        Response response = new Response();
        Job job = jobService.getPositionByPositionId(id);
        response.setData(job);
        return response;
    }

    @ApiOperation(value = "删除已发布的职位", notes = "删除已发布的职位", response = Response.class)
    @RequestMapping(value = "del_position", method = RequestMethod.POST)
    public Response deletePosition(@RequestParam String ids) throws IOException {
        return jobService.deletePosition(ids);
    }

    @ApiOperation(value = "刷新已发布的职位", notes = "刷新已发布的职位", response = Response.class)
    @RequestMapping(value = "refresh_position", method = RequestMethod.POST)
    public Response refresh_position(@RequestParam String ids) throws IOException {
        return jobService.refreshPosition(ids);
    }

    @ApiOperation(value = "更新编辑已发布的职位", notes = "更新编辑已发布的职位", response = Response.class)
    @RequestMapping(value = "upd_position", method = RequestMethod.POST)
    public Response updatePosition(@ModelAttribute() Job job) throws IOException {
        return jobService.updatePosition(job);
    }

    @ApiOperation(value = "查询最新招聘职位", notes = "查询最新招聘职位", response = Response.class)
    @RequestMapping(value = "sel_latest_position", method = RequestMethod.GET)
    public Response searchNewPosition() throws IOException {
        return jobService.searchNewPosition(6);
    }

    @ApiOperation(value = "查询推荐职位", notes = "查询推荐职位", response = Response.class)
    @RequestMapping(value = "sel_recommend_position", method = RequestMethod.GET)
    public Response searchRecommendPosition() throws IOException {
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid!=null){
            response = jobService.searchRecommendPosition(String.valueOf(createid), 6);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "我收到的简历", notes = "我收到的简历", response = Response.class)
    @RequestMapping(value = "sel_receive_resume", method = RequestMethod.GET)
    public Response receiveResume(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid!=null){
            Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            response = resumeService.receiveResume(pager,createid.toString());
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }


    @ApiOperation(value = "我申请的职位", notes = "我申请的职位", response = Response.class)
    @RequestMapping(value = "sel_my_position", method = RequestMethod.GET)
    public Response myApplyPosition(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid!=null){
            Paging<Job> pager = new Paging<Job>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            response = jobService.myApplyPosition(pager, String.valueOf(createid));
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "简历批量设为已查看", notes = "简历批量设为已查看", response = Response.class)
    @RequestMapping(value = "upd_jobRelresume", method = RequestMethod.POST)
    public Response upd_jobRelresume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids){
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",id);
            map.put("status", JobConstant.RESUME_STATUS_TWO);
            jrrService.updateJobRelResume(map);
        }
        return response;
    }

    @ApiOperation(value = "收到的简历批量删除", notes = "收到的简历批量删除", response = Response.class)
    @RequestMapping(value = "del_jobRelresume", method = RequestMethod.POST)
    public Response del_jobRelresume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids){
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",id);
            map.put("status", JobConstant.RESUME_STATUS_THREE);
            jrrService.updateJobRelResume(map);
        }
        return response;
    }

    @ApiOperation(value = "我下载的简历", notes = "我下载的简历", response = Response.class)
    @RequestMapping(value = "sel_download_resume", method = RequestMethod.GET)
    public Response sel_download_resume(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid!=null){
            Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            response = resumeService.findAllDownloadResume(pager,createid.toString());
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "下载的简历批量删除", notes = "下载的简历批量删除", response = Response.class)
    @RequestMapping(value = "del_downloadResume", method = RequestMethod.POST)
    public Response del_downloadResume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids){
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            DownloadRecord record = new DownloadRecord();
            record.setId(Long.parseLong(id));
            record.setIs_deleted(1);
            resumeService.del_downloadResume(record);
        }
        return response;
    }

    @ApiOperation(value = "我收藏的简历", notes = "我收藏的简历", response = Response.class)
    @RequestMapping(value = "sel_collect_resume", method = RequestMethod.GET)
    public Response sel_collect_resume(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid!=null){
            Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            response = resumeService.findAllCollectResume(pager,createid.toString());
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "收藏的简历批量删除", notes = "收藏的简历批量删除", response = Response.class)
    @RequestMapping(value = "del_collectResume", method = RequestMethod.POST)
    public Response del_collectResume(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids){
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            CollectRecord record = new CollectRecord();
            record.setId(Long.parseLong(id));
            record.setIs_deleted(1);
            resumeService.del_collectResume(record);
        }
        return response;
    }
}

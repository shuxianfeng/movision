package com.zhuhuibao.business.memCenter.JobManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.pojo.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by cxx on 2016/4/18 0018.
 */
@RestController
@RequestMapping("/rest/job")
@Api(value = "Jobs", description = "会员中心-招聘管理")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobPositionService jobService;

    /**
     * 职位类别
     */
    @ApiOperation(value = "获取职位类别", notes = "获取职位类别", response = JsonResult.class)
    @RequestMapping(value = "positionType", method = RequestMethod.GET)
    public JsonResult positionType() throws IOException {
        return jobService.positionType();
    }

    /**
     * 发布职位
     */
    @ApiOperation(value = "发布职位", notes = "发布职位", response = JsonResult.class)
    @RequestMapping(value = "publishPosition", method = RequestMethod.POST)
    public JsonResult publishPosition(@ApiParam(value = "职位属性") Job job) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                job.setCreateid(principal.getId().toString());
                jsonResult = jobService.publishPosition(job);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    /**
     * 查询公司已发布的职位
     */
    @ApiOperation(value = "查询公司已发布的职位", notes = "查询公司已发布的职位", response = JsonResult.class)
    @RequestMapping(value = "searchPositionByMemId", method = RequestMethod.GET)
    public JsonResult searchPositionByMemId(String pageNo, String pageSize) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                Paging<Job> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
                jsonResult = jobService.findAllPositionByMemId(pager, principal.getId().toString());
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    /**
     * 查询公司发布的某条职位的信息
     */
    //@RequiresRoles("admin")
    @ApiOperation(value = "查询公司发布的某条职位的信息", notes = "查询公司发布的某条职位的信息", response = JsonResult.class)
    @RequestMapping(value = "getPositionByPositionId", method = RequestMethod.GET)
    public JsonResult getPositionByPositionId(String id) throws IOException {
        return jobService.getPositionByPositionId(id);
    }

    /**
     * 删除已发布的职位
     */
    @ApiOperation(value = "删除已发布的职位", notes = "删除已发布的职位", response = JsonResult.class)
    @RequestMapping(value = "deletePosition", method = RequestMethod.POST)
    public JsonResult deletePosition(HttpServletRequest req) throws IOException {
        String ids[] = req.getParameterValues("ids");
        return jobService.deletePosition(ids);
    }

    /**
     * 更新编辑已发布的职位
     */
    @ApiOperation(value = "删除已发布的职位", notes = "删除已发布的职位", response = JsonResult.class)
    @RequestMapping(value = "updatePosition", method = RequestMethod.POST)
    public JsonResult updatePosition(Job job) throws IOException {
        return jobService.updatePosition(job);
    }

    /**
     * 查询最新招聘职位
     */
    @ApiOperation(value = "查询最新招聘职位", notes = "查询最新招聘职位", response = JsonResult.class)
    @RequestMapping(value = "searchNewPosition", method = RequestMethod.GET)
    public JsonResult searchNewPosition() throws IOException {
        return jobService.searchNewPosition(6);
    }

    /**
     * 查询推荐职位
     */
    @ApiOperation(value = "查询推荐职位", notes = "查询推荐职位", response = JsonResult.class)
    @RequestMapping(value = "searchRecommendPosition", method = RequestMethod.GET)
    public JsonResult searchRecommendPosition() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                jsonResult = jobService.searchRecommendPosition(principal.getId().toString(), 6);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    /**
     * 我申请的职位
     */
    @ApiOperation(value = "我申请的职位", notes = "我申请的职位", response = JsonResult.class)
    @RequestMapping(value = "myApplyPosition", method = RequestMethod.GET)
    public JsonResult myApplyPosition(String pageNo, String pageSize) throws IOException {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                Paging<Job> pager = new Paging<Job>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
                jsonResult = jobService.myApplyPosition(pager, principal.getId().toString());
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }
}

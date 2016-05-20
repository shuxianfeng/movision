package com.zhuhuibao.business.memCenter.CooperationManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.service.CooperationService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 威客接口
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/cooperation")
@Api(value="Cooperation", description="会员中心-威客管理")
public class CooperationController {
    private static final Logger log = LoggerFactory
            .getLogger(CooperationController.class);

    @Autowired
    private CooperationService cooperationService;
    /**
     * 发布任务
     */
    @ApiOperation(value="发布任务",notes="发布任务",response = JsonResult.class)
    @RequestMapping(value = "publishCooperation", method = RequestMethod.POST)
    public JsonResult publishCooperation(Cooperation cooperation) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if(null != principal){
                cooperation.setCreateId(principal.getId().toString());
                cooperationService.publishCooperation(cooperation);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    /**
     * 合作类型(大类，子类)
     */
    @ApiOperation(value="合作类型(大类，子类)",notes="合作类型(大类，子类)",response = JsonResult.class)
    @RequestMapping(value = "cooperationType", method = RequestMethod.GET)
    public JsonResult cooperationType() throws Exception {
        JsonResult jsonResult = new JsonResult();
        List list = cooperationService.cooperationType();
        jsonResult.setData(list);
        return jsonResult;
    }

    /**
     * 合作类型(子类)
     */
    @ApiOperation(value="合作类型(子类)",notes="合作类型(子类)",response = JsonResult.class)
    @RequestMapping(value = "subCooperationType", method = RequestMethod.GET)
    public JsonResult subCooperationType() throws Exception {
        JsonResult jsonResult = new JsonResult();
        List list = cooperationService.subCooperationType();
        jsonResult.setData(list);
        return jsonResult;
    }

    /**
     * 项目类别
     */
    @ApiOperation(value="项目类别",notes="项目类别",response = JsonResult.class)
    @RequestMapping(value = "cooperationCategory", method = RequestMethod.GET)
    public JsonResult cooperationCategory() throws Exception {
        JsonResult jsonResult = new JsonResult();
        List list = cooperationService.cooperationCategory();
        jsonResult.setData(list);
        return jsonResult;
    }

    /**
     * 编辑任务
     */
    @ApiOperation(value="编辑任务",notes="编辑任务",response = JsonResult.class)
    @RequestMapping(value = "updateCooperation", method = RequestMethod.POST)
    public JsonResult updateCooperation(@RequestBody Cooperation cooperation) throws Exception {
        JsonResult jsonResult = new JsonResult();
        cooperationService.updateCooperation(cooperation);
        return jsonResult;
    }

    /**
     * 批量删除任务
     */
    @ApiOperation(value="批量删除任务",notes="批量删除任务",response = JsonResult.class)
    @RequestMapping(value = "deleteCooperation", method = RequestMethod.POST)
    public JsonResult deleteCooperation(@RequestParam String ids[]) throws Exception {
        JsonResult jsonResult = new JsonResult();
        cooperationService.deleteCooperation(ids);
        return jsonResult;
    }

    /**
     * 查询一条任务的信息
     */
    @ApiOperation(value="查询一条任务的信息",notes="查询一条任务的信息",response = Cooperation.class)
    @RequestMapping(value = "queryCooperationInfoById", method = RequestMethod.GET)
    public JsonResult queryCooperationInfo(@RequestParam String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Cooperation cooperation = cooperationService.queryCooperationInfoById(id);
        jsonResult.setData(cooperation);
        return jsonResult;
    }

    /**
     * 查询我发布的任务（分页）
     */
    @ApiOperation(value="查询我发布的任务（分页）",notes="查询我发布的任务（分页）",response = JsonResult.class)
    @RequestMapping(value = "findAllMyCooperationByPager", method = RequestMethod.GET)
    public JsonResult findAllMyCooperationByPager(
            @RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize,
            @ApiParam(value = "合作标题")@RequestParam(required = false) String title,
            @ApiParam(value = "合作类型")@RequestParam(required = false) String type,
            @ApiParam(value = "审核状态")@RequestParam(required = false) String status
    ) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        JsonResult jsonResult = new JsonResult();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                Paging<Cooperation> pager = new Paging<Cooperation>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
                Cooperation cooperation = new Cooperation();
                cooperation.setCreateId(principal.getId().toString());
                cooperation.setType(type);
                cooperation.setTitle(title);
                cooperation.setStatus(status);
                List<Cooperation> cooperationList = cooperationService.findAllCooperationByPager(pager, cooperation);
                pager.result(cooperationList);
                jsonResult.setData(pager);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }
}

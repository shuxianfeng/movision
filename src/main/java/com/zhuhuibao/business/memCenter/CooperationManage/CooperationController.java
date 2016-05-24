package com.zhuhuibao.business.memCenter.CooperationManage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
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

import java.util.List;

/**
 * 威客接口
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/cooperation")
public class CooperationController {
    private static final Logger log = LoggerFactory.getLogger(CooperationController.class);

    @Autowired
    private CooperationService cooperationService;
    /**
     * 发布任务
     */
    @ApiOperation(value="发布任务",notes="发布任务",response = Response.class)
    @RequestMapping(value = "publishCooperation", method = RequestMethod.POST)
    public Response publishCooperation(Cooperation cooperation)  {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        Response Response = new Response();
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if(null != principal){
                cooperation.setCreateId(principal.getId().toString());
                cooperationService.publishCooperation(cooperation);
            }else{
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return Response;
    }

    /**
     * 合作类型(大类，子类)
     */
    @ApiOperation(value="合作类型(大类，子类)",notes="合作类型(大类，子类)",response = Response.class)
    @RequestMapping(value = "cooperationType", method = RequestMethod.GET)
    public Response cooperationType()  {
        Response Response = new Response();
        List list = cooperationService.cooperationType();
        Response.setData(list);
        return Response;
    }

    /**
     * 合作类型(子类)
     */
    @ApiOperation(value="合作类型(子类)",notes="合作类型(子类)",response = Response.class)
    @RequestMapping(value = "subCooperationType", method = RequestMethod.GET)
    public Response subCooperationType()  {
        Response Response = new Response();
        List list = cooperationService.subCooperationType();
        Response.setData(list);
        return Response;
    }

    /**
     * 项目类别
     */
    @ApiOperation(value="项目类别",notes="项目类别",response = Response.class)
    @RequestMapping(value = "cooperationCategory", method = RequestMethod.GET)
    public Response cooperationCategory()  {
        Response Response = new Response();
        List list = cooperationService.cooperationCategory();
        Response.setData(list);
        return Response;
    }

    /**
     * 编辑任务
     */
    @ApiOperation(value="编辑任务",notes="编辑任务",response = Response.class)
    @RequestMapping(value = "updateCooperation", method = RequestMethod.POST)
    public Response updateCooperation(Cooperation cooperation)  {
        Response Response = new Response();
        cooperationService.updateCooperation(cooperation);
        return Response;
    }

    /**
     * 批量删除任务
     */
    @ApiOperation(value="批量删除任务",notes="批量删除任务",response = Response.class)
    @RequestMapping(value = "deleteCooperation", method = RequestMethod.POST)
    public Response deleteCooperation(@RequestParam String ids[])  {
        Response Response = new Response();
        cooperationService.deleteCooperation(ids);
        return Response;
    }

    /**
     * 查询一条任务的信息
     */
    @ApiOperation(value="查询一条任务的信息",notes="查询一条任务的信息",response = Cooperation.class)
    @RequestMapping(value = "queryCooperationInfoById", method = RequestMethod.GET)
    public Response queryCooperationInfo(@RequestParam String id)  {
        Response Response = new Response();
        Cooperation cooperation = cooperationService.queryCooperationInfoById(id);
        Response.setData(cooperation);
        return Response;
    }

    /**
     * 查询我发布的任务（分页）
     */
    @ApiOperation(value="查询我发布的任务（后台分页）",notes="查询我发布的任务（后台分页）",response = Response.class)
    @RequestMapping(value = "findAllMyCooperationByPager", method = RequestMethod.GET)
    public Response findAllMyCooperationByPager(
            @RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize,
            @ApiParam(value = "合作标题")@RequestParam(required = false) String title,
            @ApiParam(value = "合作类型")@RequestParam(required = false) String type,
            @ApiParam(value = "审核状态")@RequestParam(required = false) String status
    )  {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Response Response = new Response();
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
                Response.setData(pager);
            }else{
                Response.setCode(401);
                Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                Response.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            Response.setCode(401);
            Response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            Response.setMsgCode(MsgCodeConstant.un_login);
        }
        return Response;
    }
}

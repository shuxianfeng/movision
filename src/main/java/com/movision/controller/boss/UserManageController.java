package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.UserManageFacade;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/24 11:09
 */
@RestController
@RequestMapping(value = "boss/user/manage")
public class UserManageController {

    @Autowired
    UserManageFacade userManageFacade;

    /**
     * 查看vip申请列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看vip申请列表", notes = "用于查看VIP申请列表", response = Response.class)
    @RequestMapping(value = "query_apply_vip_list", method = RequestMethod.POST)
    public Response queryApplyVipList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<UserVo> pager = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<UserVo> list = userManageFacade.queryApplyVipList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;

    }

    /**
     * 查看vip列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看VIP列表", notes = "用于查看VIP用户列表", response = Response.class)
    @RequestMapping(value = "query_vip_list", method = RequestMethod.POST)
    public Response queryVipList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<UserVo> pager = new Paging<UserVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<UserVo> list = userManageFacade.queryVipList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 查询投稿列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询投稿列表", notes = "用于查询投稿列表接口", response = Response.class)
    @RequestMapping(value = "query_contribute_list", method = RequestMethod.POST)
    public Response queryContributeList(@RequestParam String pageNo, @RequestParam String pageSize) {
        Response response = new Response();
        Paging<SubmissionVo> pager = new Paging<SubmissionVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SubmissionVo> list = userManageFacade.queryContributeList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 对VIP列表排序
     *
     * @param time
     * @param grade
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "VIP用户列表排序", notes = "用于VIP用户列表按加V时间和会员等级排序", response = Response.class)
    @RequestMapping(value = "query_add_v_sort", method = RequestMethod.POST)
    public Response queryAddVSortUser(@ApiParam(value = "按加V时间(传数值1)") @RequestParam(required = false) String time,
                                      @ApiParam(value = "按会员等级（传数值1）") @RequestParam(required = false) String grade,
                                      @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<UserVo> pager = new Paging<UserVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<UserVo> list = userManageFacade.queryAddVSortUser(time, grade, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 条件查询VIP申请用户列表
     *
     * @param username
     * @param phone
     * @param begintime
     * @param endtime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询VIP申请用户列表", notes = "用于根据条件查询VIP申请用户列表接口", response = Response.class)
    @RequestMapping(value = "query_unite_condition_apply", method = RequestMethod.POST)
    public Response queryUniteConditionByApply(@ApiParam(value = "用户名") @RequestParam(required = false) String username,
                                               @ApiParam(value = "手机号") @RequestParam(required = false) String phone,
                                               @ApiParam(value = "开始时间") @RequestParam(required = false) String begintime,
                                               @ApiParam(value = "结束时间") @RequestParam(required = false) String endtime,
                                               @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                               @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<UserVo> pager = new Paging<UserVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<UserVo> list = userManageFacade.queryUniteConditionByApply(username, phone, begintime, endtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 条件查询投稿列表
     *
     * @param nickname
     * @param email
     * @param type
     * @param vip
     * @param begintime
     * @param endtime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询投稿列表", notes = "用于根据条件查询投稿列表接口", response = Response.class)
    @RequestMapping(value = "query_unite_condition_contribute", method = RequestMethod.POST)
    public Response queryUniteConditionByContribute(@ApiParam(value = "用户名") @RequestParam(required = false) String nickname,
                                                    @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
                                                    @ApiParam(value = "审核状态") @RequestParam(required = false) String type,
                                                    @ApiParam(value = "vip") @RequestParam(required = false) String vip,
                                                    @ApiParam(value = "开始时间") @RequestParam(required = false) String begintime,
                                                    @ApiParam(value = "结束时间") @RequestParam(required = false) String endtime,
                                                    @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                    @ApiParam(value = "每页几条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SubmissionVo> pager = new Paging<SubmissionVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SubmissionVo> list = userManageFacade.queryUniteConditionByContribute(nickname, email, type, vip, begintime, endtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

}

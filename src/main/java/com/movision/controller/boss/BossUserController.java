package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.common.constant.ImConstant;
import com.movision.common.constant.UserConstants;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.im.ImFacade;
import com.movision.facade.user.BossUserFacade;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.bossUser.entity.BossUserVo;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.movision.utils.CommonUtils;
import com.movision.utils.im.CheckSumBuilder;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 18:06
 */
@RestController
@RequestMapping("boss/user")
public class BossUserController {

    @Autowired
    private BossUserFacade bossUserFacade;

    @Autowired
    private UserRoleRelationFacade userRoleRelationFacade;

    @Autowired
    private ImFacade imFacade;

    @RequestMapping(value = "add_boss_user", method = RequestMethod.POST)
    @ApiOperation(value = "新增boss用户", notes = "新增boss用户", response = Response.class)
    public Response addBossUser(@ApiParam @ModelAttribute BossUserVo bossUserVo) throws IOException {
        Response response = new Response();

        //1 新增用户信息
        int userid = bossUserFacade.addBySelectiveInfo(bossUserVo);
        //2 新增用户角色
        String roleid = bossUserVo.getRoleid();

        if (StringUtils.isNotEmpty(roleid)) {

            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserid(userid);
            userRoleRelation.setRoleid(Integer.valueOf(roleid));
            userRoleRelationFacade.addRelation(userRoleRelation);

            //如果是系统管理员角色， 则创建accid
            if (StringUtils.equals(roleid, UserConstants.SYSTEM_ADMIN)) {
                ImUser imUser = new ImUser();
                imUser.setAccid(CheckSumBuilder.getAccid(bossUserVo.getUsername()));
                imFacade.registerImUserAndSave(imUser, ShiroUtil.getBossUserID(), ImConstant.TYPE_BOSS);
            }

        }
        return response;
    }

    /**
     * 修改boss用户
     * PS:不能修改手机号
     *
     * @param bossUserVo
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "update_boss_user", method = RequestMethod.POST)
    @ApiOperation(value = "修改boss用户", notes = "修改boss用户", response = Response.class)
    public Response updateBossUser(@ApiParam @ModelAttribute BossUserVo bossUserVo) throws IOException {
        Response response = new Response();

        //1 更新用户信息
        bossUserFacade.updateBySelectiveInfo(bossUserVo);

        String roleid = bossUserVo.getRoleid();
        if (StringUtils.isNotEmpty(roleid)) {
            //2 更新用户的角色
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserid(Integer.valueOf(bossUserVo.getId()));
            userRoleRelation.setRoleid(Integer.valueOf(roleid));
            userRoleRelationFacade.updateByUserid(userRoleRelation);

            //如果是系统管理员角色， 则创建accid
            if (StringUtils.equals(roleid, UserConstants.SYSTEM_ADMIN)) {
                ImUser imUser = new ImUser();
                imUser.setAccid(CheckSumBuilder.getAccid(bossUserVo.getUsername()));
                imFacade.registerImUserAndSave(imUser, ShiroUtil.getBossUserID(), ImConstant.TYPE_BOSS);
            }
        }

        return response;
    }

    @RequestMapping(value = "del_boss_user", method = RequestMethod.POST)
    @ApiOperation(value = "删除boss用户", notes = "删除boss用户", response = Response.class)
    public Response delBossUser(@ApiParam(value = "用户的id，以逗号分隔") @RequestParam String userids) {
        Response response = new Response();

        int[] useridArray = CommonUtils.idsStringToIntArray(userids);
        //1 删除用户角色关系
        userRoleRelationFacade.deleteRelationsByUserid(useridArray);

        //2 删除用户
        bossUserFacade.delUser(useridArray);
        response.setCode(200);
        return response;

    }

    @RequestMapping(value = "get_boss_user_list", method = RequestMethod.GET)
    @ApiOperation(value = "用户列表（分页）", notes = "用户列表（分页）", response = Response.class)
    public Response getBossUserList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @RequestParam(required = false, defaultValue = "10") String pageSize,
                                    @ApiParam(value = "用户名") @RequestParam(required = false) String username,
                                    @ApiParam(value = "手机号") @RequestParam(required = false) String phone) {
        Response response = new Response();
        Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        List<Map<String, Object>> list = bossUserFacade.queryBossUserList(pager, username, phone);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "get_boss_user_detail", method = RequestMethod.GET)
    @ApiOperation(value = "用户详情", notes = "用户详情", response = Response.class)
    public Response getBossUserDetail(@ApiParam(value = "用户id") @RequestParam(required = true) Integer id) {
        Response response = new Response();
        Map<String, Object> result = bossUserFacade.getBossUserDetail(id);
        response.setData(result);
        return response;
    }





}

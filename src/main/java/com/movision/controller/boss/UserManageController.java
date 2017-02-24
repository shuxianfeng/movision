package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.UserManageFacade;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
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
}

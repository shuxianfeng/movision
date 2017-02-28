package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.facade.boss.PostFacade;
import com.movision.facade.user.UserFacade;
import com.movision.facade.vip.VipFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/28 15:33
 */
@RestController
@RequestMapping("app/mine/vip")
public class MyVipController {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private VipFacade vipFacade;

    @ApiOperation(value = "查询我的达人认证资格", notes = "查询我的达人认证资格", response = Response.class)
    @RequestMapping(value = "get_my_vip_qualification", method = RequestMethod.GET)
    public Response getMyVipQualification() {
        Response response = new Response();
        response.setData(postFacade.getMyVipApplyStatistics());
        return response;
    }

    @ApiOperation(value = "申请达人", notes = "申请达人", response = Response.class)
    @RequestMapping(value = "apply_vip", method = RequestMethod.POST)
    public Response applyVip() {
        Response response = new Response();
        //1 记录第一次申请时间
        userFacade.applyVip();
        //2 记录此次申请记录
        vipFacade.addVipApplyRecord();

        return response;
    }
}

package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.common.pojo.InstantInfo;
import com.movision.facade.msgCenter.MsgCenterFacade;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.model.ServicePaging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/2 11:22
 */
@RestController
@RequestMapping("app/msg_center")
public class AppMsgCenterController {

    @Autowired
    private MsgCenterFacade msgCenterFacade;


    @ApiOperation(value = "获取我的消息中心的系统通知列表（同时会把未读的系统通知置为已读）", notes = "获取我的消息中心的系统通知列表（同时会把未读的系统通知置为已读）", response = Response.class)
    @RequestMapping(value = {"/get_my_msg_center_system_list"}, method = RequestMethod.GET)
    public Response getMyMsgCenterInformationList(@ApiParam(value = "用户id") @RequestParam(required = false) String userid,
                                                  @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<ImSystemInformVo> paging = new Paging<ImSystemInformVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImSystemInformVo> list = msgCenterFacade.getMsgInformationListNew(userid, paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 查询通知详情接口
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询通知详情", notes = "用于查询通知详情接口", response = Response.class)
    @RequestMapping(value = "queryMyMsgInforDetails", method = RequestMethod.POST)
    public Response queryMyMsgInforDetails(@ApiParam(value = "通知id") @RequestParam String id) {
        Response response = new Response();
        ImSystemInformVo im = msgCenterFacade.queryMyMsgInforDetails(id);
        response.setMessage("查询成功");
        response.setData(im);
        return response;
    }

    @ApiOperation(value = "获取消息中心-动态列表（同时会把未读的动态消息置为已读）", notes = "获取消息中心-动态列表（同时会把未读的动态消息置为已读）", response = Response.class)
    @RequestMapping(value = "get_msg_center_instant_info_list", method = RequestMethod.POST)
    public Response getMsgCenterInstantInfoList(@ApiParam(value = "用户id") @RequestParam(required = false) String userid,
                                                @ApiParam @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                @ApiParam @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        ServicePaging<InstantInfo> pager = new ServicePaging<InstantInfo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List list = msgCenterFacade.getInstantInfo(userid, pager);
        pager.setRows(list);

        response.setMessage("查询成功");
        response.setData(pager);
        return response;
    }
}

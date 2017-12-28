package com.movision.controller.boss.im;

import com.movision.common.Response;
import com.movision.common.constant.ImConstant;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.mybatis.systemLayout.entity.SystemLayout;
import com.movision.mybatis.systemPush.entity.SystemPush;
import com.movision.mybatis.systemToPush.entity.SystemToPush;
import com.movision.utils.SysNoticeUtil;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 10:46
 */
@RestController
@RequestMapping("boss/notice")
public class BossImNoticeController {

    @Autowired
    private ImFacade imFacade;

    @Autowired
    private SysNoticeUtil sysNoticeUtil;
    /**
     * 该接口给系统管理员调用
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "给APP用户发送系统通知", notes = "给APP用户发送系统通知", response = Response.class)
    @RequestMapping(value = {"/send_system_inform"}, method = RequestMethod.POST)
    public Response sendSystemInform(@ApiParam(value = "系统通知内容") @RequestParam String body,
                                     @ApiParam(value = "系统通知标题") @RequestParam String title,
                                     @ApiParam(value = "系统推送内容(填就是推送，不填就是通知)") @RequestParam(required = false) String pushcontent) throws IOException {
        Response response = new Response();
        //调用云信推送
        imFacade.sendSystemInform(body, title, pushcontent, ImConstant.PUSH_MESSAGE.system_msg.getCode(), null);
        return response;
    }


    /**
     * 运营发送通知/推送
     *
     * @param
     * @param title
     * @param body
     * @param coverimg
     * @return
     */
    @ApiOperation(value = "运营发送通知", notes = "运营发送通知", response = Response.class)
    @RequestMapping(value = {"/addOperationInform"}, method = RequestMethod.POST)
    public Response addOperationInform(@ApiParam(value = "标题") @RequestParam String title,
                                       @ApiParam(value = "内容") @RequestParam String body,
                                       @ApiParam(value = "封面图") @RequestParam String coverimg) {
        Response response = new Response();
        imFacade.addOperationInform(body, title, coverimg);
        response.setMessage("推送成功");
        return response;
    }

    /**
     * 查询运营通知列表
     *
     * @param title
     * @param body
     * @param pageNo
     * @param pageSiz
     * @return
     */
    @ApiOperation(value = "查询运营通知列表", notes = "用于条件查询运营通知列表接口", response = Response.class)
    @RequestMapping(value = "queryOperationInformList", method = RequestMethod.POST)
    public Response queryOperationInformList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                             @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                             @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                             @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSiz) {
        Response response = new Response();
        Paging<ImSystemInform> pag = new Paging<ImSystemInform>(Integer.valueOf(pageNo), Integer.valueOf(pageSiz));
        List<ImSystemInform> list = imFacade.queryOperationInformList(title, body, pag);
        pag.result(list);
        response.setMessage("查询成功");
        response.setData(pag);
        return response;
    }

    /**
     * 查询运营通知详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询运营通知详情", notes = "用于查询运营通知详情", response = Response.class)
    @RequestMapping(value = "queryOperationInformById", method = RequestMethod.POST)
    public Response queryOperationInformById(@ApiParam(value = "id") @RequestParam String id) {
        Response response = new Response();
        ImSystemInform inform = imFacade.queryOperationInformById(id);
        response.setMessage("查询成功");
        response.setData(inform);
        return response;
    }

    /**
     * 更新运营通知
     *
     * @param id
     * @param title
     * @param body
     * @param coverimg
     * @return
     */
    @ApiOperation(value = "更新运营通知", response = Response.class)
    @RequestMapping(value = "updateOperationInformById", method = RequestMethod.POST)
    public Response updateOperationInformById(@ApiParam(value = "id") @RequestParam String id,
                                              @ApiParam(value = "标题") @RequestParam(required = false) String title,
                                              @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                              @ApiParam(value = "封面图") @RequestParam(required = false) String coverimg) {
        Response response = new Response();
        imFacade.updateOperationInformById(id, title, body, coverimg);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }


    /**
     * 分页
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询系统通知列表", notes = "查询系统通知列表", response = Response.class)
    @RequestMapping(value = "query_system_inform_list", method = RequestMethod.GET)
    public Response querySystemInformList(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                          @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<ImSystemInformVo> paging = new Paging<ImSystemInformVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImSystemInformVo> list = imFacade.queryAllSystemInform(paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 分页
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询消息推送列表", notes = "查询消息推送列表", response = Response.class)
    @RequestMapping(value = "query_system_push_list", method = RequestMethod.GET)
    public Response findAllSyetemPush(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SystemPush> paging = new Paging<SystemPush>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SystemPush> list = imFacade.findAllSyetemPush(paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }

    @ApiOperation(value = "查询系统通知详情", notes = "查询系统通知详情", response = Response.class)
    @RequestMapping(value = "query_system_inform_detail", method = RequestMethod.GET)
    public Response querySystemInformDetail(@ApiParam(value = "系统通知id") @RequestParam Integer id) {
        Response response = new Response();
        ImSystemInform imSystemInform = imFacade.querySystemInformDetail(id);
        response.setData(imSystemInform);
        return response;
    }

    @ApiOperation(value = "删除系统通知", notes = "删除系统通知", response = Response.class)
    @RequestMapping(value = "delete_system_inform_detail", method = RequestMethod.POST)
    public Response deleteSystemInformDetail(@ApiParam(value = "系统通知id") @RequestParam Integer id) {
        Response response = new Response();
        int imSystemInform = imFacade.deleteImSystem(id);
        response.setData(imSystemInform);
        return response;
    }

    /**
     * 条件搜索分页
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询系统通知条件搜索分页", notes = "查询系统通知条件搜索分页", response = Response.class)
    @RequestMapping(value = "query_system_informcondition_list", method = RequestMethod.POST)
    public Response findAllSystemCondition(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                           @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                           @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                           @ApiParam(value = "排序") @RequestParam(required = false) String pai) {
        Response response = new Response();
        Paging<ImSystemInform> paging = new Paging<ImSystemInform>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImSystemInform> list = imFacade.findAllSystemCondition(body, pai, paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 条件搜索分页
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询消息推送条件搜索分页", notes = "查询消息推送条件搜索分页", response = Response.class)
    @RequestMapping(value = "query_system_pushcondition_list", method = RequestMethod.POST)
    public Response findAllPushCondition(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                         @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                         @ApiParam(value = "排序") @RequestParam(required = false) String pai) {
        Response response = new Response();
        Paging<SystemPush> paging = new Paging<SystemPush>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SystemPush> list = imFacade.findAllPushCondition(body, pai, paging);
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 查询内容全部
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询内容全部", notes = "查询内容全部", response = Response.class)
    @RequestMapping(value = "query_body_all", method = RequestMethod.POST)
    public Response queryBodyAll(@ApiParam(value = "系统通知id") @RequestParam Integer id) {
        Response response = new Response();
        ImSystemInform imSystemInform = imFacade.queryBodyAll(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(imSystemInform);
        return response;
    }

    @ApiOperation(value = "查询消息内容全部", notes = "查询消息内容全部", response = Response.class)
    @RequestMapping(value = "query_pushbody_all", method = RequestMethod.POST)
    public Response queryPushBody(@ApiParam(value = "消息推送id") @RequestParam Integer id) {
        Response response = new Response();
        SystemPush imSystemInform = imFacade.queryPushBody(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(imSystemInform);
        return response;
    }

    @ApiOperation(value = "删除消息", notes = "删除消息", response = Response.class)
    @RequestMapping(value = "delete_push", method = RequestMethod.POST)
    public Response deleteSystemPush(@ApiParam(value = "消息推送id") @RequestParam Integer id) {
        Response response = new Response();
        Integer imSystemInform = imFacade.deleteSystemPush(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(imSystemInform);
        return response;
    }

    @ApiOperation(value = "发送消息没有链接的", notes = "发送消息", response = Response.class)
    @RequestMapping(value = "find_allpush", method = RequestMethod.POST)
    public Response AddPushMovement(@ApiParam(value = "短信内容") @RequestParam String body
    ) {
        Response response = new Response();
        imFacade.AddPushMovement(body);
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        return response;
    }


    @ApiOperation(value = "发送消息有链接的", notes = "发送消息", response = Response.class)
    @RequestMapping(value = "find_allpush_link", method = RequestMethod.POST)
    public Response AddPushMovementAndLink(@ApiParam(value = "短信内容") @RequestParam String body) {
        Response response = new Response();
        imFacade.AddPushMovementAndLink(body);
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        return response;
    }

    @ApiOperation(value = "推广消息", notes = "推广消息", response = Response.class)
    @RequestMapping(value = "TGSend", method = RequestMethod.POST)
    public Response TGSend() {
        Response response = new Response();
        imFacade.TGSend();
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        return response;
    }

    @ApiOperation(value = "推广消息_20171228", notes = "推广消息_20171228", response = Response.class)
    @RequestMapping(value = "TGSend_20171228", method = RequestMethod.POST)
    public Response TGSend_20171228(@ApiParam(value = "id") @RequestParam int id) {
        Response response = new Response();
        imFacade.TGSend_20171228(id);
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        return response;
    }



    @ApiOperation(value = "带不带链接发送消息", notes = "带不带链接发送消息", response = Response.class)
    @RequestMapping(value = "HavePushLink", method = RequestMethod.POST)
    public Response HavePushLink(@ApiParam(value = "短信内容") @RequestParam String body,
                                 @ApiParam(value = "1：不带链接 2 带连接") @RequestParam int flag) {
        Response response = new Response();
        imFacade.HavePushLink(body,flag);
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        return response;
    }


    /**
     * 查询系统推送
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询系统推送", notes = "查询系统推送", response = Response.class)
    @RequestMapping(value = "find_systemto_push", method = RequestMethod.POST)
    public Response findAllSystemToPush(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<SystemToPush> paging = new Paging<SystemToPush>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SystemToPush> list = imFacade.findAllSystemToPush(paging);
        if (response.getCode() == 200) {
            response.setMessage("查询系统推送成功");
        }
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 查询所有消息模板
     *
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "查询所有消息模板", notes = "查询所有消息模板", response = Response.class)
    @RequestMapping(value = "querySmsList", method = RequestMethod.POST)
    public Response querySmsList( ) {
        Response response = new Response();
       List<SystemLayout> list = imFacade.querySmsList();
        if (response.getCode() == 200) {
            response.setMessage("查询所有消息模板");
            response.setData(list);
        }
         return response;
    }


    /**
     * 查询系统推送搜索
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询系统推送搜索", notes = "查询系统推送搜索", response = Response.class)
    @RequestMapping(value = "find_systemto_push_condition", method = RequestMethod.POST)
    public Response findAllSystenToPushCondition(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                                 @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                                 @ApiParam(value = "排序") @RequestParam(required = false) String pai) {
        Response response = new Response();
        Paging<SystemToPush> paging = new Paging<SystemToPush>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<SystemToPush> list = imFacade.findAllSystenToPushCondition(body, pai, paging);
        if (response.getCode() == 200) {
            response.setMessage("查询系统推送搜索成功");
        }
        paging.result(list);
        response.setData(paging);
        return response;
    }

    /**
     * 根据id查询系统推送内容
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询系统推送内容", notes = "根据id查询系统推送内容", response = Response.class)
    @RequestMapping(value = "query_body_bytopush", method = RequestMethod.POST)
    public Response querySystemToPushBody(@ApiParam(value = "系统推送id") @RequestParam Integer id) {
        Response response = new Response();
        SystemToPush systemToPush = imFacade.querySystemToPushBody(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(systemToPush);
        return response;
    }

    /**
     * 删除系统推送
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除系统推送 ", notes = "删除系统推送", response = Response.class)
    @RequestMapping(value = "delete_systemtopush", method = RequestMethod.POST)
    public Response deleteSystemToPush(@ApiParam(value = "系统推送id") @RequestParam Integer id) {
        Response response = new Response();
        Integer systemToPush = imFacade.deleteSystemToPush(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(systemToPush);
        return response;
    }

    /**
     * 系统推送
     * 主要是推送安卓端 --废弃
     *
     * @param body
     * @param title
     * @return
     */
    /*@ApiOperation(value = "系统推送 ", notes = "系统推送", response = Response.class)
    @RequestMapping(value = "add_systemtopush", method = RequestMethod.POST)
    public Response addSystemToPush(JSONObject jsonObjectPayload,
                                    @ApiParam(value = "系统推送内容") @RequestParam String body,
                                    @ApiParam(value = "系统推送标题") @RequestParam String title) throws Exception {

        Response response = new Response();
        imFacade.systemPushMessage(body, title, jsonObjectPayload, 0);
        if (response.getCode() == 200) {
            response.setMessage("系统推送成功");
        }
        return response;
    }*/


    @ApiOperation(value = "活动通知 ", notes = "活动通知", response = Response.class)
    @RequestMapping(value = "activeMessage", method = RequestMethod.POST)
    public Response activeMessage(
            @ApiParam(value = "系统推送标题") @RequestParam String title,
            @ApiParam(value = "系统推送内容") @RequestParam String body,
            @ApiParam(value = "活动id") @RequestParam int postid) throws Exception {

        Response response = new Response();
        imFacade.activeMessage(title, body, postid);
        if (response.getCode() == 200) {
            response.setMessage("活动通知成功");
        }
        return response;

    }


    @ApiOperation(value = "查询活动通知", notes = "查询活动通知", response = Response.class)
    @RequestMapping(value = "findAllActiveMessage", method = RequestMethod.POST)
    public Response findAllActiveMessage(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                         @ApiParam(value = "内容") @RequestParam(required = false) String body,
                                         @ApiParam(value = "排序") @RequestParam(required = false) String pai) {
        Response response = new Response();
        Paging<ImSystemInform> paging = new Paging<ImSystemInform>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImSystemInform> list = imFacade.findAllActiveMessage(body, pai, paging);
        if (response.getCode() == 200) {
            response.setMessage("查询活动通知成功");
        }
        paging.result(list);
        response.setData(paging);
        return response;
    }


    @ApiOperation(value = "修改活动通知 ", notes = "修改活动通知", response = Response.class)
    @RequestMapping(value = "updateActiveMessage", method = RequestMethod.POST)
    public Response updateActiveMessage(
            @ApiParam(value = "自增长id") @RequestParam int id,
            @ApiParam(value = "活动标题") @RequestParam(required = false) String title,
            @ApiParam(value = "活动内容") @RequestParam(required = false) String body) throws Exception {

        Response response = new Response();
        int re = imFacade.updateActiveMessage(id, title, body);
        if (response.getCode() == 200) {
            response.setMessage("修改活动通知成功");
        }
        response.setData(re);
        return response;

    }


    @ApiOperation(value = "活动通知回显 ", notes = "活动通知回显", response = Response.class)
    @RequestMapping(value = "queryActiveMessageById", method = RequestMethod.POST)
    public Response queryActiveMessageById(
            @ApiParam(value = "自增长id") @RequestParam int id) throws Exception {

        Response response = new Response();
        ImSystemInform re = imFacade.queryActiveMessageById(id);
        if (response.getCode() == 200) {
            response.setMessage("活动通知回显成功");
        }
        response.setData(re);
        return response;

    }


    @ApiOperation(value = "查询活动内容 ", notes = "查询活动内容", response = Response.class)
    @RequestMapping(value = "queryActiveBody", method = RequestMethod.POST)
    public Response queryActiveBody(
            @ApiParam(value = "自增长id") @RequestParam int id) throws Exception {

        Response response = new Response();
        String re = imFacade.queryActiveBody(id);
        if (response.getCode() == 200) {
            response.setMessage("活动通知回显成功");
        }
        response.setData(re);
        return response;

    }

    /**
     * 投稿类中奖通知
     *
     * @param userid
     * @param template
     * @return
     */
    @ApiOperation(value = "投稿类中奖通知", notes = "投稿类中奖通知", response = Response.class)
    @RequestMapping(value = "submitTheWinningInform", method = RequestMethod.POST)
    public Response submitTheWinningInform(@ApiParam(value = "用户id") @RequestParam String userid,
                                           @ApiParam(value = "模板") @RequestParam String template) {
        Response response = new Response();
        String[] users = userid.split(",");
        for (int i = 0; i < users.length; i++) {
            if (users[i] != "-1") {
                sysNoticeUtil.sendSysNotice(6, null, Integer.parseInt(users[i]), template);
            }
        }
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }


}
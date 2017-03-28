package com.movision.controller.boss.im;

import com.movision.common.Response;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.systemPush.entity.SystemPush;
import com.movision.mybatis.systemToPush.entity.SystemToPush;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 10:46
 */
@RestController
@RequestMapping("boss/notice")
public class BossImController {

    @Autowired
    private ImFacade imFacade;

    /**
     * 该接口给系统管理员调用
     *
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "给APP用户发送系统通知", notes = "给APP用户发送系统通知", response = Response.class)
    @RequestMapping(value = {"/send_system_inform"}, method = RequestMethod.POST)
    public Response sendSystemInform(@ApiParam(value = "系统通知内容") @RequestParam String body,
                                     @ApiParam(value = "系统通知标题") @RequestParam String title) throws IOException {
        Response response = new Response();

        imFacade.sendSystemInform(body, title);
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
        Paging<ImSystemInform> paging = new Paging<ImSystemInform>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<ImSystemInform> list = imFacade.queryAllSystemInform(paging);
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

    @ApiOperation(value = "发送消息", notes = "发送消息", response = Response.class)
    @RequestMapping(value = "find_allpush", method = RequestMethod.POST)
    public Response AddPushMovement(@ApiParam(value = "短信内容") @RequestParam String body
    ) {
        Response response = new Response();
        boolean result = imFacade.AddPushMovement(body);
        if (response.getCode() == 200) {
            response.setMessage("发送成功");
        }
        response.setData(result);
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
     *
     * @param body
     * @param title
     * @return
     */
    @ApiOperation(value = "系统推送 ", notes = "系统推送", response = Response.class)
    @RequestMapping(value = "add_systemtopush", method = RequestMethod.POST)
    public Response addSystemToPush(@ApiParam(value = "系统推送内容") @RequestParam String body,
                                    @ApiParam(value = "系统推送标题") @RequestParam String title) {

        Response response = new Response();
        imFacade.addSystemToPush(body, title);
        if (response.getCode() == 200) {
            response.setMessage("系统推送成功");
        }
        return response;

    }
}
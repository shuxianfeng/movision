package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.upload.UploadFacade;
import com.movision.utils.SysNoticeUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是一个用于测试的控制器
 *
 *
 * @Author zhurui
 * @Date 2017/6/8 11:34
 */
@RestController
public class TestController {

    @Autowired
    private UploadFacade uploadFacade;

    @Autowired
    private SysNoticeUtil sysNoticeUtil;

    @ApiOperation(value = "测试配置文件加载", notes = "测试配置文件加载", response = Response.class)
    @RequestMapping(value = "app/test_PropertiesLoader", method = RequestMethod.GET)
    public Response testPropertiesLoader() {
        Response response = new Response();
        response.setData(uploadFacade.getConfigVar("post.incise.domain"));
        return response;
    }

    @ApiOperation(value = "测试BOSS管理员这对单个用户发送系统通知", notes = "测试BOSS管理员这对单个用户发送系统通知", response = Response.class)
    @RequestMapping(value = "app/test_sendSysInformForUser", method = RequestMethod.POST)
    public Response testSendSysInformForUser(@ApiParam(value = "发送类型：0 更改帖子的所属圈子 1 添加首页精选 2 添加圈子精选 3 删帖 4 加V 5 发现页推荐作者 6 投稿类中奖通知") @RequestParam String type,
                                             @ApiParam(value = "圈子名称（type为0时不为空，帖子被移动至的圈子名称）") @RequestParam(required = false) String circlename,
                                             @ApiParam(value = "通知的用户id") @RequestParam String userid,
                                             @ApiParam(value = "定向给用户发送任意内容：如中奖通知等（type为6是不为空）") @RequestParam(required = false) String template){
        Response response  = sysNoticeUtil.sendSysNotice(Integer.parseInt(type), circlename, Integer.parseInt(userid), template);
        return response;
    }
}

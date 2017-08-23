package com.movision.controller.h5active;

import com.movision.common.Response;
import com.movision.facade.h5wechat.WechatH5Facade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/8/18 15:44
 * 微信公众号H5活动接口控制器
 */
@RestController
@RequestMapping("/h5/wechat/")
public class WechatH5Controller {

    @Autowired
    private WechatH5Facade wechatH5Facade;

    @ApiOperation(value = "图片合成模板H5接口(结婚证模板)", notes = "图片合成模板H5接口", response = Response.class)
    @RequestMapping(value = "imgCompose", method = RequestMethod.POST)
    public Response imgCompose(@ApiParam(value = "男的名字") @RequestParam String manname,
                               @ApiParam(value = "女的名字") @RequestParam String womanname,
                               @ApiParam(value = "1:结婚证 2 离婚证") @RequestParam int type) {
        Response response = new Response();

        Map<String, Object> map = wechatH5Facade.imgCompose(manname, womanname, type);
        if ((Integer)map.get("status")==200){
            response.setMessage("合成成功");
            response.setCode((Integer)map.get("status"));
            response.setData(map);
        }else{
            response.setMessage("合成失败");
            response.setCode((Integer)map.get("status"));
        }

        return response;
    }


    @ApiOperation(value = "修改访问数量", notes = "修改访问数量", response = Response.class)
    @RequestMapping(value = "updateAccessCount", method = RequestMethod.POST)
    public Response updateAccessCount() {
        Response response = new Response();
        int access = wechatH5Facade.updateAccessCount(1077);
        if (response.getCode() == 200) {
            response.setMessage("成功");
            response.setData(access);
        }
        return response;
    }
}

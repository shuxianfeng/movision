package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.utils.ShairUtil;
import com.movision.utils.VideoUploadUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhanglei
 * @Date 2017/7/10 14:19
 */
@RestController
@RequestMapping("/app/share/")
public class AppShareController {

    @Autowired
    private ShairUtil shairUtil;

    /**
     * 保存值
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 保存值", notes = " 保存值", response = Response.class)
    @RequestMapping(value = "set_sharevalue", method = RequestMethod.POST)
    public Response set(@ApiParam("值") @RequestParam String value) {
        Response response = new Response();
        boolean result = shairUtil.set(value);
        if (response.getCode() == 200) {
            response.setMessage("保存成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 获取值
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取值", notes = " 获取值", response = Response.class)
    @RequestMapping(value = "get_sharevalue", method = RequestMethod.POST)
    public Response get(@ApiParam("id type=0 是从数据库里面拿的数据 type=1是缓存里面拿的数据") @RequestParam int id) {
        Response response = new Response();
        Object result = shairUtil.get(id);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
        }
        response.setData(result);
        return response;
    }
}

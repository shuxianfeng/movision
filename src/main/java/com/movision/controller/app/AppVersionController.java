package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.version.VersionFacade;
import com.movision.mybatis.post.entity.PostVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/7/12 17:01
 */
@RestController
@RequestMapping("/app/version/")
public class AppVersionController {

    @Autowired
    private VersionFacade versionFacade;

    @ApiOperation(value = "app判断当前版本号", notes = "用于app判断当前版本号", response = Response.class)
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public Response check(@ApiParam(value = "检查类型：0 IOS 1 安卓") @RequestParam String type,
                          @ApiParam(value = "APP版本号") @RequestParam String version) {
        Response response = new Response();

        int flag = versionFacade.queryVersion(type, version);

        if (flag == 0) {
            response.setCode(0);
            response.setMessage("app版本过旧");
        }else if (flag == 1){
            response.setCode(1);
            response.setMessage("app是最新版本");
        }
        return response;
    }
}

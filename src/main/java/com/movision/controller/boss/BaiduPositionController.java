package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.address.AddressFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author shuxf
 * @Date 2017/3/4 16:27
 * 针对后台设置当前店铺地址的百度服务接口
 * 入参 address
 */
@RestController
@RequestMapping("/boss/system")
public class BaiduPositionController {

    @Autowired
    private AddressFacade addressFacade;

    @ApiOperation(value = "后台设置店铺地址接口", notes = "针对后台设置当前店铺地址的百度服务接口", response = Response.class)
    @RequestMapping(value = "/setPosition", method = RequestMethod.POST)
    public Response setPosition(@ApiParam(value = "店铺id(-1时为自营)") @RequestParam String shopid,
                                @ApiParam(value = "省code") @RequestParam String provincecode,
                                @ApiParam(value = "市code") @RequestParam String citycode,
                                @ApiParam(value = "区县code") @RequestParam String areacode,
                                @ApiParam(value = "街道") @RequestParam String street) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Response response = new Response();

        int flag = addressFacade.setPosition(shopid, provincecode, citycode, areacode, street);

        if (flag == 1) {
            response.setCode(200);
            response.setMessage("设置成功");
        } else if (flag == -1) {
            response.setCode(300);
            response.setMessage("系统异常，设置失败");
        } else if (flag == -2) {
            response.setCode(400);
            response.setMessage("提供的地址无法解析，设置失败");
        }
        return response;
    }
}

package com.movision.controller.app.photo;

import com.movision.common.Response;
import com.movision.facade.pay.PWepayFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 9:49
 * 约拍微信支付
 */
@RestController
@RequestMapping("/app/photo/weipay/")
public class PWepayController {

    @Autowired
   private PWepayFacade pWepayFacade;

    @ApiOperation(value = "" ,notes="",response = Response.class)
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Response pwepayPhoto(@ApiParam(value = "订单编号") String orderid,HttpServletRequest request) throws  Exception{
        Response response = new Response();
        Map map=pWepayFacade.getPWepay(orderid,request);
        if(map.get("code")==200){
            response.setMessage("查询订单成功");
            response.setData(map);
        }else if(map.get("code")==300){
            response.setMessage("查询的订单不存在");
            response.setData(map);
        }

        return response;
    }




}

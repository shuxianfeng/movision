package com.movision.controller.boss;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhanglei
 * @Date 2017/3/17 15:40
 */
@RestController
@RequestMapping("/boss/logistics")
public class LogisticsInquiryController {

    public Response queryLogistics(@ApiParam(value = "订单id") @RequestParam String orderid) {
        Response response = new Response();
        return response;
    }

}

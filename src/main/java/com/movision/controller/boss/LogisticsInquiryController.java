package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.LogisticsInquiryFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/3/17 15:40
 */
@RestController
@RequestMapping("/boss/logistics")
public class LogisticsInquiryController {

    @Autowired
    private LogisticsInquiryFacade logisticsInquiryFacade;

    /**
     * 查询物流接口
     *
     * @param
     * @return
     */
    @ApiOperation(value = "查询物流接口", notes = "查询物流接口", response = Response.class)
    @RequestMapping(value = "queryLogistics", method = RequestMethod.POST)
    public Response queryLogistics(@ApiParam(value = "订单号") @RequestParam String ordernumber,
                                   @ApiParam(value = "type(0:用户退回,1：换货,2：订单)") @RequestParam int type) {
        Response response = new Response();
        Map<String, Object> parammap = logisticsInquiryFacade.LogisticInquiry(ordernumber, type);
        if (response.getCode() == 200 && parammap.get("message").equals("ok")) {
            response.setMessage("物流信息返回成功");
        } else if (parammap.get("returnCode").equals("500")) {
            response.setMessage("查询无结果，请隔段时间再查");
        } else if (parammap.get("returnCode").equals("400")) {
            response.setMessage("提交的数据不完整，或者贵公司没授权");
        } else if (parammap.get("returnCode").equals("501")) {
            response.setMessage("服务器错误，快递100服务器压力过大或需要升级，暂停服务");
        } else if (parammap.get("returnCode").equals("502")) {
            response.setMessage("服务器繁忙");
        } else if (parammap.get("returnCode").equals("503")) {
            response.setMessage("验证签名失败。");
        }
        response.setData(parammap);
        return response;
    }

}

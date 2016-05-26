package com.zhuhuibao.business.techtrain;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.mybatis.order.service.TechService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 技术培训
 */
@RestController
@RequestMapping("/rest/tech")
@Api(value="techApi",description = "技术培训接口")
public class TechController {

    private static final Logger log = LoggerFactory.getLogger(TechController.class);

    @Autowired
    TechService techService;

    @ApiOperation(value="立即支付",notes="立即支付")
    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public void doPay(HttpServletRequest request,HttpServletResponse response,
                      @ApiParam(value="json") @RequestParam(required = false) String json) throws IOException {

        Map<String,String> paramMap = JsonUtils.getMapFromJsonString(json);
        paramMap.put("exterInvokeIp",ValidateUtils.getIpAddr(request));//客户端IP地址


//        map.put("out_trade_no", out_trade_no);
//        map.put("subject", subject);
//        map.put("total_fee", total_fee);
//        map.put("body", body);

        techService.doPay(response,paramMap);
    }

}

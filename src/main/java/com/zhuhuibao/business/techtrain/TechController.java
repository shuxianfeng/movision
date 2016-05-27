package com.zhuhuibao.business.techtrain;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.DirectService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 技术培训
 */
@RestController
@RequestMapping("/rest/tech")
@Api(value = "techApi", description = "技术培训接口")
public class TechController {

    private static final Logger log = LoggerFactory.getLogger(TechController.class);

    private static final String ALIPAY_GOODS_TYPE = "0";//虚拟类商品

    private static final String SELLER_ID = AlipayPropertiesLoader.getPropertyValue("seller_id");

    @Autowired
    DirectService directService;

    @ApiOperation(value = "立即支付", notes = "立即支付")
    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam(value = "json") @RequestParam(required = false) String json) throws IOException {

        log.info("技术培训下单页面,请求参数:{}", json);

        Map<String, String> paramMap = JsonUtils.getMapFromJsonString(json);

        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("goods_type", ALIPAY_GOODS_TYPE);//商品类型
        paramMap.put("seller_id",SELLER_ID);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        directService.doPay(response, paramMap);
    }

}

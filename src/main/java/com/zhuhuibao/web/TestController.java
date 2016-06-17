package com.zhuhuibao.web;

import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.utils.JsonUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author jianglz
 * @since 16/6/17.
 */
@RestController
public class TestController {

    @RequestMapping(value="rest/zhbreturn",method = RequestMethod.POST)
    public String zhbreturn(HttpServletRequest request){
        String requestParams = request.getParameter("result");
        System.out.println("筑慧币购买回调:结果:[" + requestParams + "]");

        return "success";

    }

    @RequestMapping(value = "rest/zhbpay",method = RequestMethod.POST)
    public void zhbpay() throws IOException {

        System.out.println("筑慧币购买");

        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> map = new HashMap<>();
        map.put("orderNo","sn1234567890");
        map.put("status", "success");
        String result = JsonUtils.getJsonStringFromMap(map);
        String responseCode = restTemplate.postForObject(
                AlipayPropertiesLoader.getPropertyValue("zhb_return_url") + "?result={result}",
                null,String.class,result);
        System.out.println("回调状态:"+ responseCode);
    }
}

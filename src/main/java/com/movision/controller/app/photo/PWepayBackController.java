package com.movision.controller.app.photo;

import com.movision.common.Response;
import com.movision.facade.index.FacadePhoto;
import com.movision.facade.pay.PWepayFacade;
import com.movision.mybatis.photoOrder.service.PhotoOrderService;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2018/1/31 9:50
 * 约拍微信支付回调
 */
@RestController
@RequestMapping("/app/photo/weipayback/")
public class PWepayBackController {


    @Autowired
   private PWepayFacade pWepayFacade;

    @Autowired
    private FacadePhoto facadePhoto;

    @Autowired
    private PropertiesDBLoader propertiesDBLoader;

    /**
     * 微信异步回调接口
     *
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信支付回调接口", notes = "微信支付回调接口", response = Response.class)
    @RequestMapping(value = "getWePayBack", method = RequestMethod.POST)
    public String payNotifyUrl(HttpServletRequest request) throws Exception {
        BufferedReader reader = null;

        reader = request.getReader();
        String line = "";
        String xmlString = null;
        String total_fee = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        System.out.println("----接收到的数据如下：---" + xmlString);
        Map<String, String> map = new HashMap<String, String>();
        String result_code = "";
        String out_trade_no = "";
        map = pWepayFacade.doXMLParse(xmlString);
        result_code = map.get("result_code");
        out_trade_no = map.get("out_trade_no");
        total_fee = map.get("total_fee");
        String nonce_str = map.get("nonce_str");
        if (checkSign(xmlString)) {
            //修改数据库
            facadePhoto.updateOrderType(Integer.parseInt(out_trade_no), nonce_str, new Date(), 2, total_fee);
            return result_code;
        }else {
            return "false";
        }

    }

    private boolean checkSign(String xmlString) {
        Map<String, String> map = null;
        try {
            map = pWepayFacade.doXMLParse(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = getSign(map);
        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);
            return false;
        }
        System.out.println("恭喜，API返回的数据签名验证通过!!!");

        return true;
    }
    public String getSign(Map<String, String> map) {
        SortedMap<String ,Object> map1 = new TreeMap<>();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            map1.put(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        map1.remove("sign");
        String sign = pWepayFacade.createSign("UTF-8", map1, propertiesDBLoader.getValue("Pkey"));
        return sign;
    }




}

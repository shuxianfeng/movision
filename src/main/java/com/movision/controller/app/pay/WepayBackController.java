package com.movision.controller.app.pay;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiOperation;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 9:35
 * 微信支付回调
 */
@RestController
@RequestMapping("/app/wepayback/")
public class WepayBackController {
    /**
     * 小程序支付回调接口
     * 解析微信的回调请求（XML）
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "微信支付回调接口", notes = "微信支付回调接口", response = Response.class)
    @RequestMapping(value = "getWePayBack", method = RequestMethod.POST)
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        System.out.println("微信调起微信支付统一下单回调接口");
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
            System.out.println("属性名：" + e.getName() + "-----" + "属性值：" + e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }
}

package com.movision.facade.pay;

import com.movision.common.util.MD5Utils;
import com.movision.mybatis.photoOrder.entity.PhotoOrder;
import com.movision.mybatis.photoOrder.service.PhotoOrderService;
import com.movision.utils.HttpClientUtils;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import com.movision.utils.wechat.WechatUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author zhanglei
 * @Date 2018/3/20 9:39
 */
@Service
public class PWepayFacade {

    private static Logger log = LoggerFactory.getLogger(PWepayFacade.class);
    @Autowired
    private PropertiesDBLoader propertiesDBLoader;

    @Autowired
    private PhotoOrderService photoOrderService;

    /**
     * 约拍微信统一下单接口
     * @param orderid
     * @return
     */
    public Map<String,Object> getPWepay(String orderid,HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        //根据订单id查询订单
        PhotoOrder photoOrder=photoOrderService.selectOrder(Integer.parseInt(orderid));
        if(photoOrder!=null){
            String url=propertiesDBLoader.getValue("Punifiedorder");//微信统一下单接口
            String appid=propertiesDBLoader.getValue("Pappid");//appid
            String mchid=propertiesDBLoader.getValue("Pmchid");//商户号
            String nonce_str=  UUIDGenerator.genUUIDRemoveSep(1)[0];//随机字符串
            String notify_url=propertiesDBLoader.getValue("P_notify_url");//微信需要的异步回调地址
            String key=propertiesDBLoader.getValue("Pkey");//商户平台的秘钥
            Double money=photoOrder.getMoney();//订单金额
            String actualPay = (Math.round(money * 100) + "");
            Map<String, Object> signParams = new HashMap<>();
            putRequiredParameter(request, appid, mchid, notify_url, nonce_str, orderid, actualPay, signParams);

            String sign =createSign("UTF-8", signParams, key);//生成签名
            signParams.put("sign", sign);
            signParams.remove("key");//调用统一下单无需key（商户应用密钥）

            String requestXml = WechatUtils.map2XmlString(signParams);//生成Xml格式的字符串
            Map<String, String> resmap = HttpClientUtils.doPostByXML(url, requestXml, "UTF-8");//请求支付
            if (resmap.get("status").equals("200")) {
                map.put("code", 200);//请求成功
                map.put("data", resmap.get("result"));//返回客户端需要的数据

                //------------------------------------------------------------------------------------------------------ 转换xml为map获取微信返回的prepay_id
                Map maps =doXMLParse(resmap.get("result"));
                Long time = new Date().getTime()/1000;
                System.out.println("返回的签名"+maps.get("sign"));
                map.put("prepay_id", maps.get("prepay_id"));
                //
                map.put("nonce_str", maps.get("nonce_str"));
                map.put("mch_id", maps.get("mch_id"));
                map.put("orderid", orderid);
                map.put("timestamp",time);
                //--------------------------------------------------------------------------------------------------二次签名
                Map<String,Object> map1= new HashedMap();
                map1.put("appid",appid);
                map1.put("partnerid",mchid);
                map1.put("prepayid",String.valueOf(maps.get("prepay_id")));
                map1.put("noncestr",String.valueOf(maps.get("nonce_str")));
                map1.put("timestamp", String.valueOf(time));
                map1.put("package","Sign=WXPay");
                String twosign =createSign("UTF-8", map1, key);//生成签名
                map.put("sign", twosign);

            }

        }else {
            map.put("code",300);

        }
        return map;

    }

    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        System.out.println("替换encoding=\".*\"前");
        if(strxml.contains("encoding=\".*\"")){
            System.out.println("含有encoding");
            strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        }
        System.out.println("替换encoding=\".*\"后");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);		//出错
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }
    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    //定义签名，微信根据参数字段的ASCII码值进行排序 加密签名,故使用SortMap进行参数排序
    public static String createSign(String characterEncoding,Map<String,Object> parameters,String key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if (StringUtils.isNotEmpty(key)) {
            sb.append("key=" + key);//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        }
         System.out.println("签名封装---"+sb.toString());
        String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }


    /**
     * 统一下单封装
     * @param request
     * @param appid
     * @param mchid
     * @param notify_url
     * @param nonce_str
     * @param orderid
     * @param actualPay
     * @param signParams
     */
    public void putRequiredParameter(HttpServletRequest request, String appid, String mchid, String notify_url, String nonce_str, String orderid, String actualPay, Map<String, Object> signParams) {
        signParams.put("appid", appid);//app_id
        signParams.put("body", "测试");//商品参数信息
        signParams.put("mch_id", mchid);//微信商户账号
        signParams.put("nonce_str", nonce_str);//32位不重复的编号
        signParams.put("notify_url", notify_url);//回调页面
        signParams.put("out_trade_no", orderid);//订单编号
        signParams.put("spbill_create_ip", request.getRemoteAddr());//请求的实际ip地址
        signParams.put("total_fee", actualPay);//支付金额 单位为分
        signParams.put("trade_type", "APP");
    }

}

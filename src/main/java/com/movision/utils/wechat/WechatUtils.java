package com.movision.utils.wechat;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movision.fsearch.utils.MyUtils;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author shuxf
 * @Date 2018/2/2 14:37
 * 微信小程序后台开发过程中的所有静态工具类
 */
public class WechatUtils {

    /**
     * 用于生成微信小程序需要的3rd_session的标准字符串(真随机数)
     * @return
     */
    public static String get3rdstr(){

        //SecureRandom是java中生成真随机数的类，此处不推荐用Random
        SecureRandom  ran = new SecureRandom();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < 128; i++) {
            int r = ran.nextInt(2);//随机整数的范围0~1，必须<2
            hexString.append(r);
        }
        System.out.println(hexString.toString());
        return hexString.toString();
    }

    public static String map2XmlString(Map<String, Object> map) throws UnsupportedEncodingException {
        String xmlResult;

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            System.out.println(key + "========" + map.get(key));

            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
            System.out.println();
        }
        sb.append("</xml>");
        xmlResult = sb.toString();
        return new String(xmlResult.getBytes(), "ISO8859-1");
//        return xmlResult;
    }

    /**
     * @description 将xml字符串转换成map
     * @param xml
     * @return Map
     */
    public static Map<String, String> readStringXmlOut(String xml) {
        Map<String, String> map = new HashMap<>();
        Document doc;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            @SuppressWarnings("unchecked")
            List<Element> list = rootElt.elements();// 获取根节点下所有节点
            for (Element element : list) { // 遍历节点
                map.put(element.getName(), element.getText()); // 节点的name为map的key，text为map的value
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将入参数据封装到map对象
     * @param parammap
     * @param sign
     * @param ordersid
     * @param totalamount
     * @param openid
     * @param appid
     * @param mchid
     * @return
     */
    public static Map<String, Object> getMap(Map<String, Object> parammap, String nonce_str, String sign, String ordersid, double totalamount, String notify_url, String openid, String appid, String mchid){
        parammap.put("appid", appid);//appid
        parammap.put("mch_id", mchid);//商户号
//        parammap.put("device_info ", "WEB");//设备号
        parammap.put("nonce_str", nonce_str);//随机字符串
        parammap.put("sign", sign);//支付签名
        parammap.put("sign_type", "MD5");//目前只支持MD5
        parammap.put("body", "xiaochengxu");//美番小程序-摄影设备租赁
        parammap.put("attach", "zidingyi");//自定义数据区，可原样返回
        parammap.put("out_trade_no", ordersid);//商户订单号
        parammap.put("fee_type", "CNY");//标价币种
        parammap.put("total_fee", (int)(totalamount*100));//订单总金额，单位为分
        parammap.put("spbill_create_ip", "192.168.0.3");//APP和网页支付提交用户端ip（具体看客户端能否获取）
//        parammap.put("time_start", "20180210095030");//交易起始时间
//        parammap.put("time_expire", "20180210181930");//交易结束时间
        parammap.put("notify_url", notify_url);//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        parammap.put("trade_type", "JSAPI");
        parammap.put("openid", openid);//用户标识
        return parammap;
    }

    public static String getSign(String nonce_str, String ordersid, double totalamount, String notify_url, String openid, String appid, String mchid, String key){
        StringBuffer strb = new StringBuffer();//拼装参数字符串（sign除外）

        strb.append("appid=" + appid);
        strb.append("&attach=zidingyi");//自定义数据区，可原样返回
        strb.append("&body=xiaochengxu");//美番小程序-摄影设备租赁
//        strb.append("&device_info=WEB");
        strb.append("&fee_type=CNY");
        strb.append("&mch_id=" + mchid);
        strb.append("&nonce_str=" + nonce_str);
        strb.append("&notify_url=" + notify_url);
        strb.append("&openid=" + openid);
        strb.append("&out_trade_no=" + ordersid);
        strb.append("&sign_type=MD5");
        strb.append("&spbill_create_ip=192.168.0.3");//---------------------------------------待完善
//        strb.append("&time_expire=20180210181930");
//        strb.append("&time_start=20180210095030");
        strb.append("&total_fee=" + (int)(totalamount*100));
        strb.append("&trade_type=JSAPI");
        strb.append("&key=" + key);//商户平台设置的密钥secret

        String sign = MyUtils.getMD5(strb.toString()).toUpperCase();

        return sign;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //测试生成3rd_session---------------test1
        //WechatUtils.get3rdstr();

        //测试map和xml互转------------------test2
        Map<String, Object> map = new HashMap<>();
        String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串
        map.put("appid", "wx73d911acb6511d5a");//appid
        map.put("mch_id", "1460356102");//商户号
        map.put("device_info ", "WEB");//设备号
        map.put("nonce_str", nonce_str);//随机字符串
//        map.put("sign","sign");//支付签名
//        map.put("sign_type","sign_type");//目前只支持MD5
        map.put("body", "美番小程序-摄影设备租赁付费");
        map.put("attach", "自定义数据区，可原样返回");
        map.put("out_trade_no", "001t" + System.currentTimeMillis());//商户订单号
        map.put("fee_type", "CNY");//标价币种
        map.put("total_fee", "88");//订单总金额，单位为分
        map.put("spbill_create_ip", "192.168.0.3");//APP和网页支付提交用户端ip
        map.put("time_start", "20180209161930");//交易起始时间
        map.put("time_expire", "20180209181930");//交易结束时间
        map.put("notify_url", "https://www.mofo.shop");//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        map.put("trade_type", "JSAPI");
        map.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");//用户标识

        String xmlResult;

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            System.out.println(key + "========" + map.get(key));
            sb.append("<" + key + ">" + map.get(key) + "</" + key + ">");
            System.out.println();
        }
        sb.append("</xml>");
        xmlResult = sb.toString();
//        <xml>
//        <nonce_str>[Ljava.lang.String;@45283ce2</nonce_str>
//        <time_expire>20180209181930</time_expire>
//        <device_info>WEB</device_info>
//        <time_start>20180209161930</time_start>
//        <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
//        <fee_type>CNY</fee_type>
//        <mch_id>1460356102</mch_id>
//        <body>美番小程序-摄影设备租赁付费</body>
//        <notify_url>https://www.mofo.shop</notify_url>
//        <spbill_create_ip>192.168.0.3</spbill_create_ip>
//        <out_trade_no>001t1518164982123</out_trade_no>
//        <appid>wx73d911acb6511d5a</appid>
//        <total_fee>88</total_fee>
//        <trade_type>JSAPI</trade_type>
//        <attach>自定义数据区，可原样返回</attach>
//        </xml>
        System.out.println(xmlResult);
    }
}

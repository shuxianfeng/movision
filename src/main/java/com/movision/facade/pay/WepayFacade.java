package com.movision.facade.pay;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.utils.HttpClientUtils;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import com.movision.utils.wechat.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 11:41
 */
@Service
public class WepayFacade {

    private static Logger log = LoggerFactory.getLogger(WepayFacade.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    PropertiesDBLoader propertiesDBLoader;

    /**
     * 用户拼装微信支付的请求入参（仅适用于小程序的租赁业务）
     *
     * @param openid
     * @param ordersid
     * @return
     */
    public Map<String, Object> getWePay(String openid, String ordersid) throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        int[] ids = {Integer.parseInt(ordersid)};
        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        if (ordersList.size() > 0) {//传入的订单存在

            String url = propertiesDBLoader.getValue("unifiedorder");//---------------1.微信统一下单接口

            //------------------------------------------------------------------------2.计算订单实际支付总金额
            double totalamount = 0;
            if (ordersList.get(0).getId() == ids[0]) {
                //计算实际支付总金额
                totalamount = ordersList.get(0).getMoney();
                if (ordersList.get(0).getIsdiscount() == 1) {//如果有使用优惠券，扣减优惠券金额
                    totalamount = totalamount - ordersList.get(0).getDiscouponmoney();
                }
                if (null != ordersList.get(0).getDispointmoney()) {//如果有使用积分抵扣，扣减积分抵扣金额
                    totalamount = totalamount - ordersList.get(0).getDispointmoney();
                }
            }
            log.info("打印订单号为：" + ids[0] + "的实际需要支付的总金额=================================>" + totalamount);

            //------------------------------------------------------------------------3.获取微信签名
            String appid = propertiesDBLoader.getValue("appid");
            String mchid = propertiesDBLoader.getValue("mchid");
            String notify_url = propertiesDBLoader.getValue("wepay_notify_url");
            String key = propertiesDBLoader.getValue("secret");//一定要注意这里，这个不是小程序的secret，而是商户号平台中自己手动设置的秘钥
            String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串

            //拼接签名前字符串
            StringBuffer strb = new StringBuffer();
            strb.append("appid=" + appid);
            strb.append("&attach=自定义数据区，可原样返回");//自定义数据区，可原样返回
            strb.append("&body=美番小程序-摄影设备租赁");//美番小程序-摄影设备租赁
            strb.append("&fee_type=CNY");
            strb.append("&mch_id=" + mchid);
            strb.append("&nonce_str=" + nonce_str);
            strb.append("&notify_url=" + notify_url);
            strb.append("&openid=" + openid);
            strb.append("&out_trade_no=" + ordersid);
            strb.append("&sign_type=MD5");
            strb.append("&spbill_create_ip=192.168.0.3");//---------------------------------------待完善
            strb.append("&total_fee=" + (int) (totalamount * 100));
            strb.append("&trade_type=JSAPI");
            strb.append("&key=" + key);//商户平台设置的密钥secret

            String sign = WechatUtils.getSign(strb.toString());

            //-------------------------------------------------------------------------4.封装入参xml
            Map<String, Object> parammap = new HashMap<>();
            parammap = WechatUtils.getMap(parammap, nonce_str, sign, ordersid, totalamount, notify_url, openid, appid, mchid);//封装入参map对象
            String xml = WechatUtils.map2XmlString(parammap);//转为微信服务器需要的xml格式

            log.info("xml>>>>>>>" + xml);

            //-------------------------------------------------------------------------5.请求支付
            Map<String, String> resmap = HttpClientUtils.doPostByXML(url, xml, "utf-8");
            if (resmap.get("status").equals("200")) {
                map.put("code", 200);//请求成功
                map.put("data", resmap.get("result"));//返回客户端需要的数据
            }

        } else {
            map.put("code", 300);//订单号不存在或已取消
        }
        return map;
    }

    /**
     * 订单查询接口（微信支付）
     *
     * @param ordersid
     * @return
     */
    public Map<String, Object> queryOrderInfo(String transactionid, String ordersid) throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        String url = propertiesDBLoader.getValue("orderquery");//---------------1.微信查询订单接口

        String appid = propertiesDBLoader.getValue("appid");//小程序ID
        String mchid = propertiesDBLoader.getValue("mchid");//商户号
        String transaction_id = transactionid;//微信订单号
        String out_trade_no = ordersid;//商户订单号，即美番平台中的订单号主键
        String key = propertiesDBLoader.getValue("secret");//一定要注意这里，这个不是小程序的secret，而是商户号平台中自己手动设置的秘钥
        String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串

        //拼接签名前字符串
        StringBuffer strb = new StringBuffer();
        strb.append("appid=" + appid);
        strb.append("&mch_id=" + mchid);
        strb.append("&nonce_str=" + nonce_str);
        if (StringUtil.isNotEmpty(ordersid))
            strb.append("&out_trade_no=" + out_trade_no);
        if (StringUtil.isNotEmpty(transactionid))
            strb.append("&transaction_id=" + transaction_id);
        strb.append("&key=" + key);


        String sign = WechatUtils.getSign(strb.toString());

        //-------------------------------------------------------------------------4.封装入参xml
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("appid", appid);
        parammap.put("mch_id", mchid);
        parammap.put("nonce_str", nonce_str);
        if (StringUtil.isNotEmpty(ordersid))
            parammap.put("out_trade_no", out_trade_no);
        parammap.put("sign", sign);//支付签名
        if (StringUtil.isNotEmpty(transactionid))
            parammap.put("transaction_id", transaction_id);

        String xml = WechatUtils.map2XmlString(parammap);//转为微信服务器需要的xml格式

        log.info("xml>>>>>>>" + xml);

        //-------------------------------------------------------------------------5.请求支付
        Map<String, String> resmap = HttpClientUtils.doPostByXML(url, xml, "utf-8");
        if (resmap.get("status").equals("200")) {
            map.put("code", 200);//请求成功
            map.put("data", resmap.get("result"));//返回客户端需要的数据

            //---------------------------------------------------------------------6.后续补充查询之后的，信息刷新和业务接口
        }

        return map;
    }

    /**
     * 申请退款接口
     *
     * @param ordersid
     * @param amount
     * @return
     */
    public Map<String, Object> getRefund(String ordersid, String transactionid, String amount) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        Orders orders = orderService.getOrderById(Integer.parseInt(ordersid));//根据订单id查询订单

        int status = orders.getStatus();//获取订单状态

        if (status == 1 || status == 2 || status == 4 || status == 5) {//订单状态：已经支付状态

            String url = propertiesDBLoader.getValue("refund");//---------------1.微信申请退款接口

            String appid = propertiesDBLoader.getValue("appid");//小程序ID
            String mchid = propertiesDBLoader.getValue("mchid");//商户号
            String key = propertiesDBLoader.getValue("secret");//一定要注意这里，这个不是小程序的secret，而是商户号平台中自己手动设置的秘钥
            String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串
            String transaction_id = transactionid;//微信订单号
            String out_trade_no = ordersid;//商户订单号，即美番平台中的订单号主键
            String out_refund_no = orders.getOrdernumber();//商户退款单号：此处暂时用主订单编号代替
            int total_fee = (int) (double) orders.getRealmoney() * 100;//订单总实付金额
            int refund_fee;
            //不为空时是实际退款金额，为空时默认退订单全额----------------------------2.获取订单退款金额
            if (StringUtil.isNotEmpty(amount)) {
                refund_fee = (int) (double) Double.parseDouble(amount) * 100;//实际退款金额
                //如果输入的退款金额超过订单实付总额，不允许退款
                log.info("订单总实付金额" + total_fee);
                log.info("实际退款金额" + refund_fee);
                if (refund_fee > total_fee) {
                    map.put("code", 400);
                    return map;
                }
            } else {
                refund_fee = total_fee;
            }

            //-------------------------------------------------------------------------3.拼接签名前字符串
            StringBuffer strb = new StringBuffer();
            strb.append("appid=" + appid);
            strb.append("&mch_id=" + mchid);
            strb.append("&nonce_str=" + nonce_str);
            strb.append("&out_refund_no=" + out_refund_no);
            if (StringUtil.isNotEmpty(ordersid))
                strb.append("&out_trade_no=" + out_trade_no);
            strb.append("&refund_fee=" + refund_fee);
            strb.append("&total_fee=" + total_fee);
            if (StringUtil.isNotEmpty(transactionid))
                strb.append("&transaction_id=" + transaction_id);
            strb.append("&key=" + key);

            String sign = WechatUtils.getSign(strb.toString());

            //-------------------------------------------------------------------------4.封装入参xml
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("appid", appid);
            parammap.put("mch_id", mchid);
            parammap.put("nonce_str", nonce_str);
            parammap.put("out_refund_no", out_refund_no);
            if (StringUtil.isNotEmpty(ordersid))
                parammap.put("out_trade_no", out_trade_no);
            parammap.put("refund_fee", refund_fee);
            parammap.put("sign", sign);//支付签名
            parammap.put("total_fee", total_fee);
            if (StringUtil.isNotEmpty(transactionid))
                parammap.put("transaction_id", transaction_id);

            String xml = WechatUtils.map2XmlString(parammap);//转为微信服务器需要的xml格式

            log.info("xml>>>>>>>" + xml);

            //-------------------------------------------------------------------------5.请求退款
            String sslpath = propertiesDBLoader.getValue("sslpath");//获取微信支付证书的存储路径
            Map<String, String> resmap = HttpClientUtils.doPostByXMLVeryCert(url, xml, "utf-8", sslpath, mchid);
            if (resmap.get("status").equals("200")) {
                map.put("code", 200);//请求成功
                map.put("data", resmap.get("result"));//返回客户端需要的数据

                //---------------------------------------------------------------------6.后续补充退款之后的，信息刷新和业务接口
            } else if (resmap.get("status").equals("500")) {
                map.put("code", 500);//请求成功
                map.put("data", resmap.get("result"));//返回客户端需要的数据
            }
        } else {
            map.put("code", 300);//请求成功
        }

        return map;
    }

    /**
     * 退款查询接口
     * @param transaction_id
     * @param out_trade_no
     * @param out_refund_no
     * @param refund_id
     * @return
     */
    public Map<String, Object> refundQuery(String transaction_id, String out_trade_no, String out_refund_no, String refund_id) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        String url = propertiesDBLoader.getValue("refund");//---------------1.微信退款查询接口

        String appid = propertiesDBLoader.getValue("appid");//小程序ID
        String mchid = propertiesDBLoader.getValue("mchid");//商户号
        String key = propertiesDBLoader.getValue("secret");//一定要注意这里，这个不是小程序的secret，而是商户号平台中自己手动设置的秘钥
        String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串

        //-------------------------------------------------------------------------3.拼接签名前字符串
        StringBuffer strb = new StringBuffer();
        strb.append("appid=" + appid);
        strb.append("&mch_id=" + mchid);
        strb.append("&nonce_str=" + nonce_str);
        if (StringUtil.isNotEmpty(out_refund_no))
            strb.append("&out_refund_no=" + out_refund_no);
        if (StringUtil.isNotEmpty(out_trade_no))
            strb.append("&out_trade_no=" + out_trade_no);
        if (StringUtil.isNotEmpty(refund_id))
            strb.append("&refund_id=" + refund_id);
        if (StringUtil.isNotEmpty(transaction_id))
            strb.append("&transaction_id=" + transaction_id);
        strb.append("&key=" + key);

        String sign = WechatUtils.getSign(strb.toString());

        //-------------------------------------------------------------------------4.封装入参xml
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("appid", appid);
        parammap.put("mch_id", mchid);
        parammap.put("nonce_str", nonce_str);
        if (StringUtil.isNotEmpty(out_refund_no))
            parammap.put("out_refund_no", out_refund_no);
        if (StringUtil.isNotEmpty(out_trade_no))
            parammap.put("out_trade_no", out_trade_no);
        if (StringUtil.isNotEmpty(refund_id))
            parammap.put("refund_id", refund_id);
        parammap.put("sign", sign);//支付签名
        if (StringUtil.isNotEmpty(transaction_id))
            parammap.put("transaction_id", transaction_id);

        String xml = WechatUtils.map2XmlString(parammap);//转为微信服务器需要的xml格式

        log.info("xml>>>>>>>" + xml);

        //-------------------------------------------------------------------------5.退款查询
        Map<String, String> resmap = HttpClientUtils.doPostByXML(url, xml, "utf-8");
        if (resmap.get("status").equals("200")) {
            map.put("code", 200);//请求成功
            map.put("data", resmap.get("result"));//返回客户端需要的数据

            //---------------------------------------------------------------------6.后续补充退款之后的，信息刷新和业务接口
        } else if (resmap.get("status").equals("500")) {
            map.put("code", 500);//请求成功
            map.put("data", resmap.get("result"));//返回客户端需要的数据
        }

        return map;
    }
}
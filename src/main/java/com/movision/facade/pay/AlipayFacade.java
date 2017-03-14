package com.movision.facade.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.mybatis.subOrder.entity.SubOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movision.utils.propertiesLoader.AlipayPropertiesLoader;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/14 10:25
 */
@Service
public class AlipayFacade {

    private static Logger log = LoggerFactory.getLogger(AlipayFacade.class);

    @Autowired
    private OrderService orderService;

    /**
     * 拼装支付宝支付请求入参
     *
     * @return
     */
    public Map<String, Object> packagePayParam(String ordersid) throws UnsupportedEncodingException, AlipayApiException {

        double totalamount = 0;//订单实际支付总金额

        String[] ordersidstr = ordersid.split(",");
        int[] ids = new int[ordersidstr.length];
        for (int i = 0; i < ordersidstr.length; i++) {
            ids[i] = Integer.parseInt(ordersidstr[i]);
        }

        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        StringBuffer bodystr = new StringBuffer();
        for (int i = 0; i < ordersList.size(); i++) {
            //拼接主订单id
            bodystr.append(ordersList.get(i).getId() + ",");

            //累加实际支付总金额
            totalamount = totalamount + ordersList.get(i).getMoney();
            if (ordersList.get(i).getIsdiscount() == 1) {
                totalamount = totalamount - ordersList.get(i).getDiscouponmoney();
            }
            if (null != ordersList.get(i).getDispointmoney()) {
                totalamount = totalamount - ordersList.get(i).getDispointmoney();
            }
        }
        String body = bodystr.toString().substring(0, bodystr.toString().length() - 1);//去除末尾逗号
        log.info("打印订单实际需要支付的总金额=================================>" + totalamount);

        //根据订单id查询所有订单中的所有子订单列表（商品列表）
        List<SubOrder> subOrderList = orderService.queryAllSubOrderList(ids);

        StringBuffer subjectstr = new StringBuffer();
        for (int i = 0; i < subOrderList.size(); i++) {
            subjectstr.append(subOrderList.get(i).getGoodsid() + ",");
        }
        String subject = subjectstr.toString().substring(0, subjectstr.toString().length() - 1);//去除末尾逗号

        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
        String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
        String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥
        String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关
        String seller_id = AlipayPropertiesLoader.getValue("seller_email");//收款支付宝账号

        //请求报文体
        String biz_content = "{" + "\"body\":\"" + body + "\"," + // 订单商品的整体描述（取所有主订单的id,逗号隔开）
                "\"subject\":\"" + subject + "\"," + // 商品的标题/交易标题/订单标题/订单关键字等（取所有订单中的商品的goodsid,逗号隔开）
                "\"out_trade_no\":\"" + body + "\"," + // 商户网站唯一订单号（取所有主订单的id,逗号隔开）
                "\"timeout_express\":\"30m\"," + // 设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。当用户进入支付宝收银台页面（不包括登录页面），会触发即刻创建支付宝交易，此时开始计时。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
                "\"total_amount\":\"" + String.valueOf(totalamount) + "\"," + // 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
                "\"seller_id\":\"" + seller_id + "\"," + // 收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
                "\"product_code\":\"QUICK_MSECURITY_PAY\"," + // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
                "\"goods_type\":\"1\"," + // 商品主类型：0—虚拟类商品，1—实物类商品 注：虚拟类商品不支持使用花呗渠道
                "\"passback_params\":\"" + java.net.URLEncoder.encode(body, "utf-8") + "\"," + // 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
                "\"promo_params\":\"\"," + // 优惠参数 注：仅与支付宝协商后可用  {"storeIdType":"1"}
                "\"extend_params\":\"\"," + // 业务扩展参数，详见下面的“业务扩展参数说明” {"sys_service_provider_id":"2088511833207846"}

                //业务扩展参数说明
                //参数                       类型    是否必填   最大长度  描述                                                                  示例值
                //sys_service_provider_id	String	否	      64	   系统商编号，该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID	    2088511833207846
                //needBuyerRealnamed	    String	否	      1	       是否发起实名校验 T：发起 F：不发起	                                    T
                //TRANS_MEMO	            String	否	      128	   账务备注 注：该字段显示在离线账单的账务备注中	                            促销

                "\"enable_pay_channels\":\"\"," + // 可用渠道，用户只能在指定渠道范围内支付 当有多个渠道时用“,”分隔  注：与disable_pay_channels互斥（不限用户支付渠道）
                "\"disable_pay_channels\":\"\"," + // 禁用渠道，用户不可用指定渠道支付 当有多个渠道时用“,”分隔 注：与enable_pay_channels互斥（不限用户支付渠道）
                "\"store_id\":\"NJ_001\"" + // 商户门店编号
                "}";

        String charset = "GBK";
        String format = "json";
        String method = "alipay.trade.app.pay";//App支付接口  alipay.trade.app.pay
        String notify_url = AlipayPropertiesLoader.getValue("service_notify_url");//回调接口（支付后服务端通知）
        String sign_type = "RSA2";
        String timestamp = df.format(new Date());
        String version = "1.0";

        //手动拼装待签内容
//        String content = "app_id="+app_id+"&biz_content="+biz_content+"&charset="+charset+"&method="+method+"&sign_type"+sign_type+"&timestamp="+timestamp+"&version="+version;
        StringBuffer contentstr = new StringBuffer();
        contentstr.append("app_id=");
        contentstr.append(app_id);
        contentstr.append("&biz_content=");
        contentstr.append(biz_content);
        contentstr.append("&charset=");
        contentstr.append(charset);
        contentstr.append("&method=");
        contentstr.append(method);
        contentstr.append("&sign_type");
        contentstr.append(sign_type);
        contentstr.append("&timestamp=");
        contentstr.append(timestamp);
        contentstr.append("&version=");
        contentstr.append(version);
        String content = contentstr.toString();

        //根据签名规则，请求签名（手动签名需要的参数：app_id, biz_content, charset, method, sign_type, timestamp, version, appprivatekey, charset, sign_type）
        String sign = AlipaySignature.rsaSign(content, appprivatekey, charset, sign_type);

        //拼装返回数据结构
        Map<String, Object> contentmap = new HashMap<>();
        contentmap.put("app_id", app_id);
        contentmap.put("method", method);
        contentmap.put("charset", charset);
        contentmap.put("sign_type", sign_type);
        contentmap.put("timestamp", timestamp);
        contentmap.put("biz_content", biz_content);
        contentmap.put("sign", sign);
        contentmap.put("version", version);

        map.put("URL", alipaygateway);
        map.put("METHOD", "POST");
        map.put("CONTENT", contentmap);
        return map;
    }
}

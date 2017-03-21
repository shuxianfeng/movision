package com.movision.facade.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservice.service.AfterServcieServcie;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.mybatis.record.service.RecordService;
import com.movision.mybatis.subOrder.entity.SubOrder;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.L;
import com.movision.utils.UpdateOrderPayBack;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movision.utils.propertiesLoader.AlipayPropertiesLoader;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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

    @Autowired
    private UpdateOrderPayBack updateOrderPayBack;

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private AfterServcieServcie afterServcieServcie;

    /**
     * 拼装支付宝支付请求入参
     *
     * @return
     */
    public Map<String, Object> packagePayParam(String ordersid) throws UnsupportedEncodingException, AlipayApiException {

        double totalamount = 0;//订单实际支付总金额

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        String[] ordersidstr = ordersid.split(",");
        int[] ids = new int[ordersidstr.length];
        for (int i = 0; i < ordersidstr.length; i++) {
            ids[i] = Integer.parseInt(ordersidstr[i]);
        }

        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        if (null != ordersList && ordersList.size() == ordersidstr.length) {//传入的订单均存在且均为待支付的情况下

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

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

            String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
            String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
//        String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥（请求接口入参目前未用到）
            String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关
            String seller_id = AlipayPropertiesLoader.getValue("seller_id");//收款支付宝用户号UID

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
            String sign_type = "RSA2";
            String timestamp = df.format(new Date());
            String version = "1.0";

            //手动拼装待签内容
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

            map.put("code", 200);
            map.put("URL", alipaygateway);
            map.put("METHOD", "POST");
            map.put("CONTENT", contentmap);

        } else if (null == ordersList || ordersList.size() != ordersidstr.length) {

            map.put("code", 300);

        }
        return map;
    }

    /**
     * 支付宝支付后，APP前台同步通知接口
     */
    public int alipayback(String resultStatus, String result) throws AlipayApiException, ParseException {
        int flag = 0;//设置标志位

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        if (resultStatus.equals("9000") || resultStatus.equals("8000") || resultStatus.equals("4000") || resultStatus.equals("6001") ||
                resultStatus.equals("6002") || resultStatus.equals("6004")) {
            //解析json
            JSONObject jSONObject = JSONObject.parseObject(result);
            String sign = (String) jSONObject.get("sign");//签名（用于验签）
            String sign_type = (String) jSONObject.get("sign_type");//签名类型
            String alipay_trade_app_pay_response = jSONObject.getString("alipay_trade_app_pay_response");//签名原始字符串
            String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥
            String charsets = "GBK";

            //校验签名
            boolean signVerified = AlipaySignature.rsaCheck(alipay_trade_app_pay_response, sign, alipublickey, charsets, sign_type);

            if (signVerified) {
                //验签通过
                //解析原始字符串，持久化存储处理结果
                JSONObject jObject = JSONObject.parseObject(alipay_trade_app_pay_response);
                String code = (String) jObject.get("code");//结果码
                String msg = (String) jObject.get("msg");//处理结果的描述
                String app_id = (String) jObject.get("app_id");//支付宝分配给开发者的应用Id（未使用）
                String out_trade_no = (String) jObject.get("out_trade_no");//商户网站唯一订单号
                String trade_no = (String) jObject.get("trade_no");//该交易在支付宝系统中的交易流水号,最长64位
                String total_amount = (String) jObject.get("total_amount");//该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01,100000000.00]，精确到小数点后两位。
                String seller_id = (String) jObject.get("seller_id");//收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字（未使用）
                String charset = (String) jObject.get("charset");//编码格式（未使用）
                String timestamp = (String) jObject.get("timestamp");//时间

                Date intime = df.parse(timestamp);//转化后的时间

                if (code.equals("10000")) {
                    //接口调用成功,持久化存储
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                    log.info("订单实付总金额>>>>>>>>>>>>>>>>>>>>>>>>" + total_amount);
                    String[] tradenostr = out_trade_no.split(",");
                    int[] tradenoarray = new int[tradenostr.length];//获取主订单号int数组
                    for (int i = 0; i < tradenostr.length; i++) {
                        tradenoarray[i] = Integer.parseInt(tradenostr[i]);
                    }

                    //更改订单状态，记录流水号、实际支付金额、交易时间、支付方式
                    int type = 1;//支付宝类型为1  微信为2
                    updateOrderPayBack.updateOrder(tradenoarray, trade_no, intime, type);

                } else if (code.equals("20000")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("20001")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40001")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40002")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40004")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40006")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                }
                flag = 1;
            } else {
                //验签失败
                flag = -1;
            }
        }

        return flag;
    }


    /**支付宝交易退款
     *
     * @param ordersid
     * @return
     */
    public Map<String, Object> tradingARefund(String ordersid) throws AlipayApiException {
        double totalamount = 0;//订单实际支付总金额
        String[] ordersidstr = ordersid.split(",");
        int[] ids = new int[ordersidstr.length];
        for (int i = 0; i < ordersidstr.length; i++) {
            ids[i] = Integer.parseInt(ordersidstr[i]);
        }

        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);
        Map<String, Object> contentmap = new HashedMap();
        if (null != ordersList && ordersList.size() == ordersidstr.length) {//传入的订单均存在且均为待支付的情况下

            StringBuffer bodystr = new StringBuffer();
            String transactionNumber = null;
            for (int i = 0; i < ordersList.size(); i++) {
                //拼接主订单id
                bodystr.append(ordersList.get(i).getId() + ",");

                //累加实际退款金额
                totalamount = totalamount + ordersList.get(i).getRealmoney();
                transactionNumber = ordersList.get(i).getPaycode();
            }
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String body = bodystr.toString().substring(0, bodystr.toString().length() - 1);//去除末尾逗号
            log.info("打印订单实际退款总金额=================================>" + totalamount);
            String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
            String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
            String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关
            String alipaygateway1 = "https://openapi.alipay.com/gateway.do";
            String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥（请求接口入参目前未用到）


            String charset = "GBK";
            String format = "json";
            String sign_type = "RSA2";
            String method = "alipay.trade.app.pay";//App支付接口  alipay.trade.app.pay
            String version = "1.0";
            String timestamp = sd.format(new Date());


            AlipayClient alipayClient = new DefaultAlipayClient(alipaygateway, app_id, appprivatekey, format, charset, alipublickey, sign_type);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent("{" +
                    "\"out_trade_no\":\"" + body + "\"," +  //订单支付时传入的商户订单号,不能和 trade_no同时为空。
                    "\"trade_no\":\"" + transactionNumber + "\"," +  //支付宝交易号，和商户订单号不能同时为空
                    "\"refund_amount\":" + totalamount + "," +  //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
                    "\"refund_reason\":\"\"," +  //退款的原因说明
                    "\"out_request_no\":\"\"," +  //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
                    "\"operator_id\":\"\"," +  //商户的操作员编号
                    "\"store_id\":\"\"," +  //商户的门店编号
                    "\"terminal_id\":\"\"" +   //商户的终端编号
                    "}");
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功" + response.getBody());
                //使用的优惠券和积分返还
                for (int i = 0; i < ordersList.size(); i++) {
                    if (ordersList.get(i).getIsdiscount() == 1) {//是否使用优惠券
                        orderService.updateOrderDiscount(ordersList.get(i).getCouponsid());//返还优惠券
                    }
                    if (ordersList.get(i).getDispointmoney() != null && ordersList.get(i).getDispointmoney() > 0) {//是否使用了积分
                        Map m = new HashedMap();
                        m.put("dispointmoney", ordersList.get(i).getDispointmoney());
                        m.put("userid", ordersList.get(i).getUserid());
                        m.put("orderid", ordersList.get(i).getId());
                        Integer integral = userService.queryUserUseIntegral(m);//查询订单对应用户使用的积分
                        m.put("integral", integral);
                        userService.updateUserPoints(m);//把积分返还
                        m.put("intime", new Date());//生成时间
                        recordService.addIntegralRecord(m);//增加用户积分操作记录
                        orderService.updateOrderByIntegral(ordersList.get(i).getId());//修改订单状态
                    }
                    Afterservice afterservice = new Afterservice();
                    afterservice.setOrderid(ordersList.get(i).getId());//订单id
                    afterservice.setAddressid(ordersList.get(i).getAddressid());//配送地址
                    afterservice.setAmountdue(ordersList.get(i).getRealmoney());//应退款金额
                    afterservice.setAfterstatue(2);//售后类型退款
                    afterservice.setAftersalestatus(1);//销售状态
                    afterservice.setProcessingstatus(2);//处理状态
                    afterservice.setProcessingtime(new Date());//处理时间
                    afterservice.setUserid(ordersList.get(i).getUserid());//用户id
                    afterservice.setRemark(ordersList.get(i).getRemark());//留言
                    afterServcieServcie.insertAfterInformation(afterservice);//插入售后详情
                }
                contentmap.put("code", 200);
                contentmap.put("msg", response.getMsg());
                contentmap.put("type", response);
            } else {
                String code = response.getCode();
                String msg = response.getSubMsg();
                System.out.println("调用失败" + response.getBody());
                if (code.equals("20000")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("20001")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40001")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40002")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40004")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                } else if (code.equals("40006")) {
                    log.info("返回码code>>>>>>>>>>>" + code + ",处理结果>>>>>>>>>>>>>>" + msg);
                }
                contentmap.put("code", 300);
                contentmap.put("msg", msg);
                contentmap.put("type", response);
            }
        }
        return contentmap;
    }

    /**
     * 支付宝支付订单的查询
     *
     * @param orderid
     * @return
     * @throws AlipayApiException
     */
    public Map alipayTradeQuery(String orderid) throws AlipayApiException {

        String[] ordersidstr = orderid.split(",");
        int[] ids = new int[ordersidstr.length];
        for (int i = 0; i < ordersidstr.length; i++) {
            ids[i] = Integer.parseInt(ordersidstr[i]);
        }
        Map<String, Object> contentmap = new HashedMap();
        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        if (null != ordersList && ordersList.size() == ordersidstr.length) {//传入的订单均存在且均为待支付的情况下
            String transactionNumber = null;
            for (int i = 0; i < ordersList.size(); i++) {
                //拼接主订单id
                transactionNumber = ordersList.get(i).getPaycode();
            }

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
            String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
            String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关
            String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥（请求接口入参目前未用到）
            String charset = "GBK";
            String format = "json";
            String sign_type = "RSA2";

            AlipayClient alipayClient = new DefaultAlipayClient(alipaygateway, app_id, appprivatekey, format, charset, alipublickey, sign_type);
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{" +
                    "    \"out_trade_no\":\"\"," +//订单支付时传入的商户订单号
                    "    \"trade_no\":\"" + transactionNumber + "\"" +//支付宝交易号
                    "  }");
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
                contentmap.put("code", 200);
                contentmap.put("msg", response.getSubMsg());
                if (response.getTradeStatus() == "WAIT_BUYER_PAY") {
                    contentmap.put("status", "交易创建，等待买家付款");
                } else if (response.getTradeStatus() == "TRADE_CLOSED") {
                    contentmap.put("status", "未付款交易超时关闭，或支付完成后全额退款");
                } else if (response.getTradeStatus() == "TRADE_SUCCESS") {
                    contentmap.put("status", "交易支付成功");
                } else if (response.getTradeStatus() == "TRADE_FINISHED") {
                    contentmap.put("status", "交易结束，不可退款");
                }
                contentmap.put("resault", request);
            } else {
                System.out.println("调用失败");
                contentmap.put("code", 300);
                contentmap.put("msg", response.getSubMsg());
                contentmap.put("resault", request);
            }
        }
        return contentmap;
    }

    /**
     * 支付宝交易退款查询
     *
     * @param tradingAccount
     * @return
     * @throws AlipayApiException
     */
    public Map tradingRefundQuery(String tradingAccount) throws AlipayApiException {

        //根据支付宝交易号查询订单号，
        List ordersList = orderService.queryOrdersListByTradingAccount(tradingAccount);
        Map map = new HashedMap();
        if (null != ordersList) {//传入的订单均存在且均为待支付的情况下
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
            String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
            String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关
            String alipublickey = AlipayPropertiesLoader.getValue("alipay_public_key");//支付宝公钥（请求接口入参目前未用到）
            String charset = "GBK";
            String format = "json";
            String sign_type = "RSA2";
            String outrequestno = "";
            for (int i = 0; i < ordersList.size(); i++) {
                outrequestno = outrequestno + ordersList.get(i).toString() + ",";
            }
            outrequestno.substring(0, outrequestno.length() - 1);


            AlipayClient alipayClient = new DefaultAlipayClient(alipaygateway, app_id, appprivatekey, format, charset, alipublickey, sign_type);
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            request.setBizContent("{" +
                    "    \"trade_no\":\"\"," +
                    "    \"out_trade_no\":\"" + tradingAccount + "\"," +
                    "    \"out_request_no\":\"" + outrequestno + "\"" +
                    "  }");
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
                System.out.println(response.getBody() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                map.put("code", 200);
                map.put("msg", response.getSubMsg());
                map.put("tradeno", response.getTradeNo());//支付宝交易号
                map.put("outtradeno", response.getOutTradeNo());//创建交易传入的商户订单号
                map.put("outrequestno", response.getOutRequestNo());//本笔退款对应的退款请求号
                map.put("refundreason", response.getRefundReason());//发起退款时，传入的退款原因
                map.put("totalamount", response.getTotalAmount());//该笔退款所对应的交易的订单金额
                map.put("refundamount", response.getRefundAmount());//本次退款请求，对应的退款金额
            } else {
                System.out.println(response.getBody() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                map.put("code", 300);
                map.put("msg", response.getSubMsg());
                map.put("tradeno", response.getTradeNo());//支付宝交易号
                map.put("outtradeno", response.getOutTradeNo());//创建交易传入的商户订单号
                map.put("outrequestno", response.getOutRequestNo());//本笔退款对应的退款请求号
                map.put("refundreason", response.getRefundReason());//发起退款时，传入的退款原因
                map.put("totalamount", response.getTotalAmount());//该笔退款所对应的交易的订单金额
                map.put("refundamount", response.getRefundAmount());//本次退款请求，对应的退款金额
            }
        }
        return map;
    }
}

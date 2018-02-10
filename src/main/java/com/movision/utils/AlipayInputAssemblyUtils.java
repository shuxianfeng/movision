package com.movision.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.movision.mybatis.pay.AlipayContent;
import com.movision.utils.propertiesLoader.AlipayPropertiesLoader;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2018/1/31 10:13
 * 封装APP端支付宝支付的请求入参（支付宝支付）
 */
@Service
public class AlipayInputAssemblyUtils {

    @Autowired
    private PropertiesDBLoader propertiesDBLoader;
    /**
     * @param totalamount 总金额
     * @param body        订单号
     * @param subject     商品id逗号分隔
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    public List<AlipayContent> assembleAlipayIintoGinseng(double totalamount, String body, String subject) throws UnsupportedEncodingException, AlipayApiException {

        String app_id = AlipayPropertiesLoader.getValue("app_id");//获取配置文件中的APPID
        String appprivatekey = AlipayPropertiesLoader.getValue("private_key");//应用私钥（商户的私钥）
        String seller_id = AlipayPropertiesLoader.getValue("seller_id");//收款支付宝用户号UID
        String notify_url = propertiesDBLoader.getValue("notify_url");//支付宝支付的回调地址
        List<AlipayContent> alipayContentList = new ArrayList<>();//alipay返回content实体列表

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

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
        String format = "JSON";
        String method = "alipay.trade.app.pay";//App支付接口  alipay.trade.app.pay
        String sign_type = "RSA2";
        String timestamp = df.format(new Date());
        String version = "1.0";

        //手动拼装待签内容
        StringBuffer contentstr = new StringBuffer();
        contentstr.append("app_id=");
        contentstr.append(app_id);
        contentstr.append("&method=");
        contentstr.append(method);
        contentstr.append("format=");
        contentstr.append(format);
        contentstr.append("&charset=");
        contentstr.append(charset);
        contentstr.append("&sign_type");
        contentstr.append(sign_type);
        contentstr.append("&biz_content=");
        contentstr.append(biz_content);
        contentstr.append("&timestamp=");
        contentstr.append(timestamp);
        contentstr.append("&version=");
        contentstr.append(version);
        String content = contentstr.toString();

        //根据签名规则，请求签名（手动签名需要的参数：app_id, biz_content, charset, method, sign_type, timestamp, version, appprivatekey, charset, sign_type）
        String sign = AlipaySignature.rsaSign(content, appprivatekey, charset, sign_type);

        //拼装返回数据结构
        AlipayContent alipayContent = new AlipayContent();
        alipayContent.setApp_id(app_id);
        alipayContent.setMethod(method);
        alipayContent.setFormat(format);
        alipayContent.setCharset(charset);
        alipayContent.setSign_type(sign_type);
        alipayContent.setSign(sign);
        alipayContent.setTimestamp(timestamp);
        alipayContent.setVersion(version);
        alipayContent.setNotify_url(notify_url);
        alipayContent.setBiz_content(biz_content);
        alipayContentList.add(alipayContent);
        return alipayContentList;
    }
}

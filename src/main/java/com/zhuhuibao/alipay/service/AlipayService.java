package com.zhuhuibao.alipay.service;

import com.zhuhuibao.alipay.config.AliPayConfig;
import com.zhuhuibao.alipay.util.AlipaySubmit;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝服务入口
 */
@Service
public class AlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private AliPayConfig config;

    /**
     * 组装基本参数
     *
     * @return paramMap
     */
    private Map<String, String> genBasicParmas() {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("partner", config.getPartner());
        paramMap.put("seller_id", config.getSellerId());
        paramMap.put("_input_charset", config.getInputCharset());
        paramMap.put("payment_type", config.getPaymentType());

        return paramMap;
    }

    /**
     * 建立请求
     *
     * @param msgParam    请求参数集合
     * @param method 提交方式。两个值可选：post、get
     * @return html
     */
    public String execute(Map<String, String> msgParam, String method) {


        //生成订单号,交易流水号
        String orderNo = IdGenerator.createOrderNo();
        msgParam.put("orderNo",orderNo);

        //请求参数校验
        checkParams(msgParam);

        //生成一条订单记录
        genOrderRecord(msgParam);

        //支付操作
        return doPay(msgParam, method);
    }

    /** 生成一条订单记录
     *
     * @param msgParam 请求参数
     */
    private void genOrderRecord(Map<String,String> msgParam){
        log.debug("request params:{}",msgParam.toString());
    }


    /**
     * 支付操作
     * @param msgParam  请求参数
     * @param method 请求方法{get,post}
     * @return
     */
    private String doPay(Map<String, String> msgParam, String method) {

        Map<String, String> basicMap = genBasicParmas();
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.putAll(basicMap);

        //基本参数
        sParaTemp.put("service", msgParam.get("service"));//接口名称
        sParaTemp.put("notify_url", msgParam.get("notifyUrl"));//服务器异步通知页面路径
//        sParaTemp.put("return_url", msgParam.get("returnUrl"));//页面跳转同步通知页面路径

        //业务参数
        sParaTemp.put("out_trade_no",msgParam.get("orderNo"));//商户网站唯一订单号
        sParaTemp.put("subject",msgParam.get("subject"));   //商品名称
        sParaTemp.put("payment_type","1");                    //支付类型
        sParaTemp.put("total_fee",msgParam.get("total_fee"));    //交易金额

        sParaTemp.put("exter_invoke_ip",msgParam.get("exterInvokeIp"));  //客户端IP
        // 防钓鱼时间戳
        String anti_phishing_key = "";
        try {
            log.debug("*****开始使用https获取防钓鱼时间戳:");
            if (config.isEnableTimestamp()) {
                anti_phishing_key = AlipaySubmit.query_timestamp();
            }
            log.debug("anti_phishing_key=" + anti_phishing_key);
        } catch (Exception e1) {
            e1.printStackTrace();
            log.error("获取防钓鱼时间戳异常：" + e1.getMessage());
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "获取防钓鱼时间戳失败");
        }


        log.debug("Alipay 请求参数:{}", sParaTemp.toString());
        return AlipaySubmit.buildRequest(sParaTemp, method, "确认");
    }

    /**
     * 请求参数校验
     *
     * @param msgParam 请求参数
     */
    private void checkParams(Map<String, String> msgParam) {
        String goodsId = msgParam.get("goodsId");//商品ID
        if (StringUtils.isEmpty(goodsId)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品ID不能为空");
        }
        String goodsName = msgParam.get("goodsName");//商品名称
        if (StringUtils.isEmpty(goodsName)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品名称不能为空");
        }
        String buyersid = msgParam.get("buyersId");//创建订单的会员ID
        if (StringUtils.isEmpty(buyersid)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "创建订单会员ID不能为空");
        }
        String number = msgParam.get("number");//订单商品数量
        if (StringUtils.isEmpty(number)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品数量不能为空");
        }

        String amount = msgParam.get("amount");//订单商品总额
        if (StringUtils.isEmpty(amount)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品总额不能为空");
        }

        String mobile = msgParam.get("mobile");//手机号码
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "手机号码不能为空");
        }


        String paymode = msgParam.get("payMode");//支付方式
        if (StringUtils.isEmpty(paymode)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "支付方式不能为空");
        }
        String goodsType = msgParam.get("goodsType");//商品类型
        if (StringUtils.isEmpty(goodsType)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品类型不能为空");
        }

//        String returnUrl = msgParam.get("returnUrl");// 同步通知
//        if (StringUtils.isEmpty(returnUrl)) {
//            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR,"同步通知地址不能为空");
//        }

        String notifyUrl = msgParam.get("notifyUrl");// 异步通知
        if (StringUtils.isEmpty(notifyUrl)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "异步通知地址不能为空");
        }

        String exterInvokeIp = msgParam.get("exterInvokeIp");// 客户端IP
        if (StringUtils.isEmpty(exterInvokeIp)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "客户端IP不能为空");
        }

        String service = msgParam.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "接口名称不能为空");
        }
    }

}

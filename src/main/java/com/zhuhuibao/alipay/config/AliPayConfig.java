package com.zhuhuibao.alipay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 支付宝公共配置
 */
@Component
public class AliPayConfig {

    @Value("${partner}")
    private  String partner;

    @Value("${seller_id}")
    private  String sellerId;

    @Value("${alipay_public_key}")
    private  String alipayPublicKey;

    @Value("${notify_url}")
    private  String notifyUrl;

    @Value("${return_url}")
    private  String returnUrl;

    @Value("${sign_type}")
    private  String signType;

    @Value("${input_charset}")
    private  String inputCharset;

    @Value("${payment_type}")
    private  String paymentType;

    @Value("${service}")
    private  String service;

    @Value("${anti_phishing_key}")
    private  String antiPhishingKey;

    @Value("${exter_invoke_ip}")
    private  String exterInvokeIp;

    @Value("${enable_timestamp}")
    private boolean enableTimestamp;

    public boolean isEnableTimestamp() {
        return enableTimestamp;
    }

    public void setEnableTimestamp(boolean enableTimestamp) {
        this.enableTimestamp = enableTimestamp;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAntiPhishingKey() {
        return antiPhishingKey;
    }

    public void setAntiPhishingKey(String antiPhishingKey) {
        this.antiPhishingKey = antiPhishingKey;
    }

    public String getExterInvokeIp() {
        return exterInvokeIp;
    }

    public void setExterInvokeIp(String exterInvokeIp) {
        this.exterInvokeIp = exterInvokeIp;
    }
}

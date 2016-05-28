package com.zhuhuibao.business.techtrain;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 技术培训 订单请求参数
 */
@ApiModel(value="订单请求参数")
public class TechOrderBean {

    /**
     * buyerId :   下单会员ID
     * goodsId :   商品ID
     * goodsName : 商品名称
     * goodsPrice :商品单价
     * number :    购买数量
     * amount :    订单总价
     * payMode :   支付方式  1支付宝，2银联支付，3微信支付等
     * goodsType : 商品类型 1：VIP服务套餐订单，2：技术培训，3：专家培训
     * mobile :    下单手机号
     * needInvoice : 是否需要发票 true | false
     * invoiceTitle : 发票抬头
     * invoiceType :  发票类型 1普通发票（纸质），2：增值发票
     */
    @ApiModelProperty(value="下单会员ID",required = true)
    private String buyerId;
    @ApiModelProperty(value="商品ID",required = true)
    private String goodsId;
    @ApiModelProperty(value="商品名称",required = true)
    private String goodsName;
    @ApiModelProperty(value="商品单价",required = true)
    private String goodsPrice;
    @ApiModelProperty(value="商品数量",required = true)
    private String number;
    @ApiModelProperty(value="订单总金额",required = true)
    private String amount;
    @ApiModelProperty(value="交易金额",required = true)
    private String payAmount;
    @ApiModelProperty(value="支付方式 1:支付宝",required = true)
    private String payMode;
    @ApiModelProperty(value="商品类型 2:技术培训",required = true)
    private String goodsType;
    @ApiModelProperty(value="下单手机号",required = true)
    private String mobile;
    @ApiModelProperty(value="是否需要发票(true|false)",required = true)
    private String needInvoice;
    @ApiModelProperty(value="发票抬头")
    private String invoiceTitle;
    @ApiModelProperty(value="发票类型 1:普通 2:增值")
    private String invoiceType;


    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNeedInvoice() {
        return needInvoice;
    }

    public void setNeedInvoice(String needInvoice) {
        this.needInvoice = needInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
}

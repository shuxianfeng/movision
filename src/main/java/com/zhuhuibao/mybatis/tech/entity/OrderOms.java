package com.zhuhuibao.mybatis.tech.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 运营管理平台订单管理
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value = "订单",description = "订单")
public class OrderOms {
    @ApiModelProperty(value = "订单编号主键")
    private String orderNo;
    @ApiModelProperty(value = "购买者的ID")
    private Long buyerId;
    @ApiModelProperty(value = "商家ID")
    private String sellerId;
    @ApiModelProperty(value = "订单总额")
    private BigDecimal amount;
    @ApiModelProperty(value = "实付金额")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "下单时间")
    private Date dealTime;
    @ApiModelProperty(value = "订单修改时间。包括订单完成，取消订单，退款等。")
    private Date updateTime;
    @ApiModelProperty(value = "支付方式：1支付宝，2银联支付，3微信支付等")
    private String payMode;
    @ApiModelProperty(value = "订单状态：1未支付，2：已支付，3：退款中，4，退款失败，5：已退款 , 6:已失效")
    private String status;
    @ApiModelProperty(value = "商品类型 1：VIP服务套餐订单，2：技术培训，3：专家培训")
    private String goodsType;
    @ApiModelProperty(value = "订单描述：退款，取消原因描述")
    private String descriptions;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode == null ? null : payMode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions == null ? null : descriptions.trim();
    }
}
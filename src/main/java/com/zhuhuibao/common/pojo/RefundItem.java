package com.zhuhuibao.common.pojo;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 退款信息
 */
public class RefundItem {

    /**
     * orderNo : 订单号
     * payType : 退款方式
     * fee : 应退金额
     */

    @ApiModelProperty(value="订单编号",required = true)
    private String orderNo;
    @ApiModelProperty(value="退款方式 1:支付宝",required = true)
    private String payType;
    @ApiModelProperty(value="应退金额",required = true)
    private String fee;
    @ApiModelProperty(value="退款理由 (不能有“^”、“|”、“$”、“#”等特殊字符)",required = true)
    private String reason;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

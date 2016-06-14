package com.zhuhuibao.common.pojo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 技术培训 支付请求参数
 */
@ApiModel(value="支付请求参数")
public class PayReqBean {

    @ApiModelProperty(value="订单编号",required = true)
    private String orderNo;
    @ApiModelProperty(value="支付方式 1:支付宝",required = true)
    private String tradeMode;

    @ApiModelProperty(value="是否使用筑慧币 true|false",required = true)
    private String userZHB;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(String tradeMode) {
        this.tradeMode = tradeMode;
    }

    public String getUserZHB() {
        return userZHB;
    }

    public void setUserZHB(String userZHB) {
        this.userZHB = userZHB;
    }
}

package com.zhuhuibao.common.pojo;

import com.wordnik.swagger.annotations.ApiModel;
import java.util.List;

/**
 * 退款请求 批量退款
 */
@ApiModel(value = "退款请求参数")
public class RefundReqBean {


    List<RefundItem> items;

    public List<RefundItem> getItems() {
        return items;
    }

    public void setItems(List<RefundItem> items) {
        this.items = items;
    }
}

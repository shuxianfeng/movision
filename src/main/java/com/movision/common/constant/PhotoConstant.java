package com.movision.common.constant;

/**
 * @Author zhanglei
 * @Date 2018/2/2 16:21
 */

/**
 * 约拍订单状态
 */
public class PhotoConstant {

    public enum TYPE {
        bidding(0),    //竞价中
        pendingorder(1),  //待接单
        alreadymadeup (2),  //已成单
        agencyfund(3),  //待收款
        tobeevaluated(4),  //待评价
        ordercompletion(5);  //订单完结

        public final int code;

        TYPE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
    }


}

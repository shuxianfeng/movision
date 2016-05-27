package com.zhuhuibao.common.constant;

/**
 * 常量类
 *
 * @author wangxiang2
 */
public class PayConstants {

    /**
     * 支付方式  1支付宝，2银联支付，3微信支付等
     */
    public enum PayMode {
        ALIPAY("1"), YLPAY("2"), WXPAY("3");
        public final String value;

        PayMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 订单状态  1未支付，2：已支付，3：退款中，4，退款失败，5：已退款
     */
    public enum OrderStatus {
        WZF("1"), YZF("2"), TKZ("3"), TKSB("4"), YTK("5");
        public final String value;

        OrderStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 交易状态  1:成功，2：处理中  3：失败
     */
    public enum TradeStatus {
        SUCCESS("1"), INPROCESS("2"), FAIL("3");
        public final String value;

        TradeStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }


    /**
     * 通知类型  1:  2 :  3:
     */
    public enum NotifyType {
        CDX("1"), DDD("2"), DZ("3");

        private final String value;

        NotifyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * http 状态码
     */
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_BUSINESS_EXCEPTION_CODE = 560;
    public static final int HTTP_SYSTEM_EXCEPTION_CODE = 600;

    /**
     * 支持请求方式类型 get | post
     */
    public static final String ALIPAY_METHOD_GET = "get";
    public static final String ALIPAY_METHOD_POST = "post";
}

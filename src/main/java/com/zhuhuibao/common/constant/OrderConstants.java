package com.zhuhuibao.common.constant;

/**
 * 订单相关常量
 */
public class OrderConstants {

    /**
     * 商品类型 1：技术培训，2：专家培训   3：VIP服务套餐， 4:筑慧币
     */
    public enum GoodsType {
        JSPX("1"), ZJPX("2"),VIP("3"),ZHB("4");
        public final String value;

        GoodsType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 发票类型 1普通发票（纸质），2：增值发票
     */
    public enum InvoiceType {
        PTFP("1"), ZZFP("2");
        public final String value;

        InvoiceType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 短信状态 1：待发送  2：已发送  3：发送失败
     */
    public enum SmsStatus {
        WAITING("1"), SUCCESS("2"),FAIL("3");
        public final String value;

        SmsStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 订单交易方式名称 1:支付宝 2:筑慧币
     */
    public enum OrderFlowTradeModeName {
        ALIPAY("支付宝"), ZHB("筑慧币");

        private final String value;

        OrderFlowTradeModeName(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 订单交易方式 1:支付宝 2:筑慧币
     */
    public enum OrderFlowTradeMode {
        ALIPAY("1"), ZHB("2");

        private final String value;

        OrderFlowTradeMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 发票是否最近被使用 0:否 1：是
     */
    public enum InvoiceIsRecentUsed {
        NO("0"), YES("1");

        private final String value;

        InvoiceIsRecentUsed(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

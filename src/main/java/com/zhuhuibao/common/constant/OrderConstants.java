package com.zhuhuibao.common.constant;

/**
 * 订单相关常量
 */
public class OrderConstants {

    /**
     * 商品类型 1：VIP服务套餐订单，2：技术培训，3：专家培训
     */
    public enum GoodsType {
        VIP("1"), JSPX("2"), ZJPX("3");
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
}

package com.zhuhuibao.common;

/**
 * 常量类
 *
 * @author wangxiang2
 */
public class Constants {

    /**
     * 支付方式 大类
     */
    public enum PayWay {
        ALIPAY("01");
        public final String value;

        PayWay(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 支付接口类型
     *
     * @author wangxiang2
     */
    public enum PayType {
        ALIPAY_CWG("01"), ALIPAY_JS("02"), ALIPAY_KJ("03");

        public final String value;

        PayType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 币种类型
     *
     * @author wangxiang2
     */
    public enum CurrencyType {
        RMB("00"), MY("01");

        public final String value;

        CurrencyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 充值状态
     */
    public enum RechargeStatus {
        WZF("0"), YZF("1");
        public final String value;

        RechargeStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 支付状态
     */
    public enum PayState {
        // 0-未支付，1-已支付，2-支付失败，3-已冲正，4-冲正失败
        WZF("0"), YZF("1"), ZFSB("2"), YCZ("3"), CZSB("4");

        public final String value;

        PayState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 交易类型
     *
     * @author wangxiang2
     */
    public enum TradeType {
        CZ("00"), ZF("01"), TK("02"), DZDJ("03"), DZJD("04"), DZKK("05"), PHDJ(
                "06"), PHJD("07"), PHKK("08"), QTDJ("09"), TX("10");
        private final String value;

        TradeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 通知类型
     *
     * @author wangxiang2
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
     * 退费申请状态
     *
     * @author wangxiang2 0：待处理 1：审批通过 2；审批未通过
     */
    public enum RefundStatus {
        DCL("0"), SPTG("1"), SPWTG("2");
        private final String value;

        RefundStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 退费申请状态
     *
     * @author wanghong7 0：正常 1：停机 2：离网
     */
    public enum ThresholdUserStatus {
        ZC("0"), TJ("1"), LW("2");
        private final String value;

        ThresholdUserStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public enum OrderType {
        GOODS("00"), RECHARGE("01");
        private final String value;

        private OrderType(String value) {
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

    public enum CustCheckStatus {
        DR("0"), DRZ("1"), RZTG("2"), RZWTG("3");
        private final String value;

        private CustCheckStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * TF_F_CUSTOMERS 客户类型
     *
     * @author wangxiang2
     */
    public enum CustType {
        PTYH("0"), SH("1");
        private final String value;

        private CustType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * 代理商级别
     *
     * @author wangxiang2
     */
    public enum AgentHierarchy {
        YJ("00"), EJ("01");
        private final String value;

        private AgentHierarchy(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    public enum ProjectCode {
        ADMIN("/Admin"), ODM("/Odm");
        private final String value;

        private ProjectCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static final String ADMIN = "ADMIN";
    public static final String ODM = "ODM";

    public static final String PROVINCE_CODE = "34";
    public static final String PROVINCE_NAME = "全省";

}

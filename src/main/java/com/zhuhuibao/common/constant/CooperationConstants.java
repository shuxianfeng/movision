package com.zhuhuibao.common.constant;

/**
 * 威客常量
 */
public class CooperationConstants {

    /**
     * 审核状态：0：未审核；1：已审核；2：已拒绝
     */
    public enum Status {
        UNAUDITED ("0"),AUDITED("1"),REFUSED("2");
        public final String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 是否显示联系方式
     * 1：显示我的联系方式，0：不显示
     */
    public enum Show {
        UNSHOW ("0"),SHOW("1");
        public final String value;

        Show(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}

package com.zhuhuibao.common.constant;

/**
 * 工程商 | 供应商 常量
 */
public class ESConstants {

    /**
     * 企业类型：1：厂商；2：代理商；3：渠道商 ; 4:工程商
     */
    public enum Type {
        MANUFACTURER ("1"),AGENT ("2"),CHANNEL ("3"),ENGINEER ("4");
        public final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

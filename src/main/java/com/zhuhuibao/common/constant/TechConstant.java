package com.zhuhuibao.common.constant;

/**
 * 技术频道常量类
 * @author Administrator
 * @version 2016/5/30 0030
 */
public class TechConstant {

    /**
     * 技术合作状态  1:待审核，2：已审核，3：拒绝，4，删除
     */
    public enum TechCooperationnStatus {
        PENDINGAUDIT("1"), AUDITPASS("2"), REJECT("3"), DELETE("4");
        public final String value;

        TechCooperationnStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

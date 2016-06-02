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

    /**
     * 技术合作状态  1:待审核，2：已审核，3：拒绝，4，删除
     */
    public enum TechDataStatus {
        PENDINGAUDIT("1"), AUDITPASS("2"), REJECT("3"), DELETE("4");
        public final String value;

        TechDataStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 上传技术资料附件的路径  /tech
     */
    public static final String UPLOAD_TECH_DATA_URL = "/tech";

    /**
     * 上传技术资料的大小  fileSize
     */
    public static final String FILE_SIZE = "fileSize";

    /**
     * 上传技术资料的格式
     */
    public static final String FILE_FORMAT = "fileFormat";

    /**
     * 课程类型：1：技术培训，2专家培训
     */
    public static final int COURSE_TYPE_TECH = 1;
}

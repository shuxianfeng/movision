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
     * 发布课程的状态  1未上架，2销售中，3待开课，4上课中，5已终止，6已完成
     */
    public enum PublishCourseStatus {
        UNPUBLISH("1"), SALING("2"), PRECLASS("3"), CLASSING("4"),STOP("5"),FINISH("6");
        public final String value;

        PublishCourseStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * SN码状态 1有效，2已使用，3已过期，4已取消，5未生效
     */
    public enum SNStatus{
        EFFECT("1"),USED("2"),EXPIRE("3"),CANCELED("4"),INEFFECT("5");
        public final String value;
        SNStatus(String value){this.value = value;}
        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 上传技术资料附件的路径  /tech/doc
     */
    public static final String UPLOAD_TECH_DOC_URL = "/tech/doc";

    /**
     * 上传发布的培训课程路径  /tech/img
     */
    public static final String UPLOAD_TECH_IMG_URL = "/tech/img";

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

    /**
     * 最新培训课程 默认3条
     */
    public static final int COURSE_LATEST_COUNT_THREE = 3;

    /**
     * 技术频道首页展示的最新培训课程 默认5条
     */
    public static final int INDEX_LATEST_COUNT_FIVE = 5;

    /**
     * 行业解决方案，技术资料，培训资料点击排行数量10条
     */
    public static final int DATA_VIEWS_COUNT_TEN = 10;

    /**
     * 行业解决方案，技术资料，培训资料下载排行数量10条
     */
    public static final int DATA_DOWNLOAD_COUNT_TEN = 10;

    /**
     * 技术合作点击排行数量10条
     */
    public static final int COOP_DOWNLOAD_COUNT_TEN = 10;

    /**
     * 发送验证码时存在session的参数type 申请开课
     */
    public static final String MOBILE_CODE_SESSION_TYPE_CLASS = "techClass";

    /**
     * 发送验证码时存在session的参数type 下单购买培训课程
     */
    public static final String MOBILE_CODE_SESSION_ORDER_CLASS = "techOrder";


}

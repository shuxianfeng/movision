package com.zhuhuibao.common.constant;

/**
 * Created by cxx on 2016/5/30 0030.
 */

/**
 *专家常量类
 **/

public interface ExpertConstant {
    /**
     * 专家频道删除标识 1：删除
     */
    String EXPERT_DELETE_ONE = "1";

    /**
     * 专家频道问题状态标识 2：已关闭
     */
    String EXPERT_QUESTION_STATUS_TWO = "2";

    /**
     * 专家频道区分前台跟会员中心 1：前台
     */
    String EXPERT_TYPE_ONE = "1";

    /**
     * 专家频道技术成果审核状态标识 1：审核通过
     */
    String EXPERT_ACHIEVEMENT_STATUS_ONE = "1";

    /**
     * 专家频道系統分類常量TYPE
     */
    String EXPERT_SYSTEM_TYPE = "19";

    /**
     * 专家频道應用領域常量TYPE
     */
    String EXPERT_USEAREA_TYPE = "18";

    /**
     * 专家频道问题状态标识 3：已屏蔽
     */
    String EXPERT_QUESTION_STATUS_THREE = "3";

    /**
     * 专家频道答案状态标识 1：已屏蔽
     */
    String EXPERT_ANSWER_STATUS_ONE = "1";

    /**
     * 专家频道工作类型 专家type
     */
    String EXPERT_WORKTYPE_EXPERT = "expert";

    /**
     * 课程类型：1：技术培训，2专家培训
     */
    Integer COURSE_TYPE_EXPERT = 2;

    /**
     * 发送验证码时存在session的参数type
     */
    String MOBILE_CODE_SESSION_TYPE_TRAIN = "expertTrain";

    /**
     * 发送验证码时存在session的参数type
     */
    String MOBILE_CODE_SESSION_TYPE_SUPPORT = "expertSupport";
}

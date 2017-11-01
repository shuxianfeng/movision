package com.movision.common.constant;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/1 10:06
 */
public class RobotConstant {

    /**
     * 机器人执行任务类型
     * 1：点赞，2：收藏，3：评论，4：关注
     */
    public enum ROBOT_JOB_TYPE {
        zan_post(1),
        collect_post(2),
        comment_post(3),
        follow_user(4);

        public final int code;

        ROBOT_JOB_TYPE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
    }
}

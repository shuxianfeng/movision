package com.movision.common.constant;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/3 11:17
 */
public class MsgCenterConstant {

    /**
     * 动态消息类型，0：评论， 1：赞， 2：关注
     */
    public enum INSTANT_INFO_TYPE {
        comment(0),
        zan(1),
        follow(2),
        replyComment(3);

        public final int code;

        INSTANT_INFO_TYPE(int code) {
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

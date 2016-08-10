package com.zhuhuibao.common.constant;

/**
 * Created by cxx on 2016/6/13 0013.
 */
public interface MessageConstant {

    /**
     * 留言发送者删除
     */
    String MESSAGE_SEND_DELETE_ONE = "1";

    /**
     * 留言接收者删除
     */
    String MESSAGE_RECEIVE_DELETE_ONE = "1";

    /**
     * 留言状态：1：未读，2：已读
     */
    enum Status {
        WD("1"), YD("2");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

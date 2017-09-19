package com.movision.common.constant;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/25 15:47
 */
public class DiscoverConstant {

    public enum HOT_RANGE_TITLE {
        post_comment_list(0),
        post_view_list(1),
        post_zan_list(2),
        post_collect_list(3),
        author_fans_list(4),
        author_comment_list(5),
        author_post_list(6);

        public final int code;

        HOT_RANGE_TITLE(int code) {
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

    public enum HOT_RANGE_TYPE {
        total_range(0),
        month_range(1);

        public final int code;

        HOT_RANGE_TYPE(int code) {
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

package com.movision.common.constant;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/31 15:33
 */
public class PostLabelConstants {

    public enum TYPE {
        geog(0),
        normal(1),
        circle(2),
        active(3);

        public final int code;

        TYPE(int code) {
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

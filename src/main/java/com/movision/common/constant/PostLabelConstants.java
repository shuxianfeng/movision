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

    /**
     * 默认普通标签头像
     */
    public static final String DEFAULT_NORMAL_PHOTO = "http://pic.mofo.shop/upload/coverIncise/img/biaoqian_morentu.png";
    /**
     * 默认地理标签头像
     */
    public static final String DEFAULT_GEOG_PHOTO = "http://pic.mofo.shop/upload/coverIncise/img/biaoqian_weizhi.png";
}

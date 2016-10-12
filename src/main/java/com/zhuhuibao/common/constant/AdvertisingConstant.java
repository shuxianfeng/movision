package com.zhuhuibao.common.constant;

/**
 * 筑慧币相关静态常量
 *
 * @author liyang
 * @date 2016年10月12日
 */
public class AdvertisingConstant {

    public enum AdvertisingChanType {
        /**
         * 平台主站
         */
        master("1"),
        /**
         * 工程商
         */
        contractors("2"),
        /**
         * 商城
         */
        mall("3"),
        /**
         * 项目
         */
        project("4"),
        /**
         * 威客
         */
        witkey("5"),
        /**
         * 人才
         */
        talented("6"),
        /**
         * 会展
         */
        Exhibition("7"),
        /**
         * 技术
         */
        technology("8"),
        /**
         * 专家
         */
        expert("9"),
        /**
         * 移动端
         */
        mobile("10");
        public final String value;

        AdvertisingChanType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}

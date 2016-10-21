package com.zhuhuibao.common.constant;

/**
 * 筑慧币相关静态常量
 *
 * @author liyang
 * @date 2016年10月12日
 */
public class AdvertisingConstant {

    public enum AdvertisingPosition {

        /**
         * 手机版首页banner
         */
        M_Homepage_Banner(new String[] { "181", "M_Homepage", "M_Homepage_Banner" }),
        /**
         * 移动端首页筑慧头条
         */
        M_Homepage_Headline(new String[] { "181", "M_Homepage", "M_Homepage_Headline" }),
        /**
         * 移动端首页频道推广
         */
        M_Homepage_Marketing(new String[] { "181", "M_Homepage", "M_Homepage_Marketing" }),
        /**
         * 移动端首页盟友邀请
         */
        M_Homepage_Invitation(new String[] { "181", "M_Homepage", "M_Homepage_Invitation" }),
        /**
         * 移动端项目信息首页banner
         */
        M_Project_Banner(new String[] { "181", "M_Project", "M_Project_Banner" }),
        /**
         * 移动端活动家首页banner
         */
        M_Activity_Banner(new String[] { "181", "M_Activity", "M_Activity_Banner" });

        /**
         * chanType,page,advArea
         */
        public final String[] value;

        AdvertisingPosition(String[] value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

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
        mobile("181");
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

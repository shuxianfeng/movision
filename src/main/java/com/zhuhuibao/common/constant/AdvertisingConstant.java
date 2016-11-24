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
        M_Project_Banner(new String[] { "193", "M_Project", "M_Project_Banner" }),
        /**
         * 移动端活动家首页banner
         */
        M_Activity_Banner(new String[] { "236", "M_Activity", "M_Activity_Banner" }),

        /**
         * 移动端工程商首页banner
         */
        M_Contractor_Banner(new String[] { "191", "M_Contractor", "M_Contractor_Banner" }),

        /**
         * 移动端工程商首页banner
         */
        M_Contractor_Join_Us(new String[] { "191", "M_Contractor", "M_Contractor_Join_Us" }),

        /**
         * 移动端工程商首页名企展示
         */
        M_Contractor_Enterprise_Display(new String[] { "191", "M_Contractor", "M_Contractor_Enterprise_Display" }),

        /**
         * 移动端工程商首页风云人物
         */
        M_Contractor_Influential_Man(new String[] { "191", "M_Contractor", "M_Contractor_Influential_Man" }),

        /**
         * 移动端供应链首页banner
         */
        M_Supplychain_Banner(new String[] { "192", "M_Supplychain", "M_Supplychain_Banner" }),

        /**
         * 移动端供应链首页热门品牌
         */
        M_Supplychain_Hotbrand(new String[] { "192", "M_Supplychain", "M_Supplychain_Hotbrand" }),

        /**
         * 移动端供应链首页热门厂商
         */
        M_Supplychain_Manufa(new String[] { "192", "M_Supplychain", "M_Supplychain_Manufa" }),

        /**
         * 移动端供应链首页热门代理商
         */
        M_Supplychain_Agent(new String[] { "192", "M_Supplychain", "M_Supplychain_Agent" }),

        /**
         * 移动端供应链首页渠道商
         */
        M_Supplychain_Canals(new String[] { "192", "M_Supplychain", "M_Supplychain_Canals" }),

        /**
         * 移动端供应链品牌馆-首页banner
         */
        M_Brands_Banner(new String[] { "192", "M_Brands", "M_Brands_Banner" }),

        /**
         * 移动端供应链品牌馆-网络及硬件品牌
         */
        M_Brands_Hardware(new String[] { "192", "M_Brands", "M_Brands_Hardware" }),

        /**
         * 移动端供应链品牌馆-安全防范品牌
         */
        M_Brands_Safety(new String[] { "192", "M_Brands", "M_Brands_Safety" }),

        /**
         * 移动端供应链品牌馆-楼宇自动化品牌
         */
        M_Brands_Bas(new String[] { "192", "M_Brands", "M_Brands_Bas" }),

        /**
         * 移动端供应链品牌馆-数据中心品牌
         */
        M_Brands_Data_Center(new String[] { "192", "M_Brands", "M_Brands_Data_Center" }),

        /**
         * 移动端供应链品牌馆-智能家居品牌
         */
        M_Brands_Smart_Home(new String[] { "192", "M_Brands", "M_Brands_Smart_Home" }),

        /**
         * 移动端供应链品牌馆-影音视频品牌
         */
        M_Brands_Video(new String[] { "192", "M_Brands", "M_Brands_Video" }),

        /**
         * 移动端供应链品牌馆-应用系统品牌
         */
        M_Brands_App_Sys(new String[] { "192", "M_Brands", "M_Brands_App_Sys" }),

        /**
         * 移动端供应链品牌馆-智能照明品牌
         */
        M_Brands_Lighting(new String[] { "192", "M_Brands", "M_Brands_Lighting" }),

        /**
         * 移动端威客-首页-banner
         */
        M_Witkey_Banner(new String[] {"243","M_Witkey","M_Witkey_Banner"}),

        /**
         * 移动端技术&培训-首页-banner
         */
        M_Tech_Banner(new String[] {"246", "M_TechAndTrain", "M_TechAndTrain_Banner"}),


        /**
         * 移动端供应链品牌馆-行业软件品牌
         */
        M_Brands_Software(new String[] { "192", "M_Brands", "M_Brands_Software" }),;

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

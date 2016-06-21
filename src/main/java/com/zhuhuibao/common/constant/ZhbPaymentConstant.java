package com.zhuhuibao.common.constant;

/**
 * 筑慧币支付
 *
 * @author pl
 * @version 2016/6/21 0021
 */
public class ZhbPaymentConstant {
    public enum goodsType {
        /**
         * 员工账号
         */
        YGZH,
        /**
         * 询价发布
         */
        XJFB,
        /**
         * 报价发布
         */
        BJFB,
        /**
         * 查看项目信息
         */
        CKXMXX,
        /**
         * 查看威客任务
         */
        CKWKRW,
        /**
         * 发布威客服务
         */
        FBWKFW,
        /**
         * 发布资质合作
         */
        FBZZHZ,
        /**
         * 发布职位
         */
        FBZW,
        /**
         * 查看/下载简历
         */
        CXXZJL,
        /**
         * 查看技术成果
         */
        CKJSCG,
        /**
         * 查看专家技术成果
         */
        CKZJJSCG,
        /**
         * 发布技术需求
         */
        FBJSXQ,
        /**
         * 查看专家信息
         */
        CKZJXX,
        /**
         * 给专家留言
         */
        GZJLY
    }

    /**
     * 已支付
     */
    public static final int PAY_ZHB_PURCHASE = 1;

    /**
     * 未支付
     */
    public static final int PAY_ZHB_NON_PURCHASE = 0;
}

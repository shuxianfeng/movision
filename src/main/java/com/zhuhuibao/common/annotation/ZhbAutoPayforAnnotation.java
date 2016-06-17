package com.zhuhuibao.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 筑慧币自动扣分服务项注解
 * 
 * @author tongxinglong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ZhbAutoPayforAnnotation {

	public ZhbGoodsType goodsType();

	public enum ZhbGoodsType {
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
}

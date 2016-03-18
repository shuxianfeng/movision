package com.zhuhuibao.common;

public interface Constant {
	/**
	 * 短信验证时间 10分钟
	 */
	String sms_time = "10";
	
	/**
	 * 图片存放路径  upload/img/
	 */
	String upload_img_prifix = "upload/img/";
	
	/**
	 * 产品状态  上架：0
	 */
	Integer product_status_uppublish = 0;
	
	/**
	 * 产品状态  上架：1
	 */
	Integer product_status_publish = 1;
	
	/**
	 * 产品状态  注销：1
	 */
	Integer product_status_delete = 2;
	
	/**
	 * 品牌详情页面展示产品的数量 10
	 */
	Integer brand_page_product_count = 10;
}

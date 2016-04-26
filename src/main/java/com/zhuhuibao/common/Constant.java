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
	
	/**
	 * 产品列表页面推荐产品的数量 40
	 */
	Integer recommend_product_count = 40;
	/**
	 * 产品列表页面热点产品的数量 5
	 */
	Integer hot_product_count = 5;
	
	/**
	 * 产品信息表 字段名 fcateid
	 */
	String product_field_fcateid = "fcateid";
	
	/**
	 * 产品信息表 字段名 fcateName
	 */
	String product_field_fcateName = "fcateName";
	
	/**
	 * 产品信息表 字段名 scateid
	 */
	String product_field_scateid = "scateid";
	
	/**
	 * 产品信息表 字段名 scateName
	 */
	String product_field_scateName = "scateName";
	
	/**
	 * 产品信息表 字段名 brandid
	 */
	String product_field_brandid = "brandid";
	
	/**
	 * 产品信息表 字段名 brandName
	 */
	String product_field_brandName = "brandName";
	
	/**
	 * 产品信息表 字段名 navigation
	 */
	String product_field_navigation = "navigation";
	
	/**
	 * 产品信息表 字段名 defalut
	 */
	String product_field_defalut = "defalut";
	
	/**
	 * 产品信息表 字段名 skuid
	 */
	String product_field_skuid = "skuid";
	
	/**
	 * 产品信息表 字段名 imgUrl
	 */
	String product_field_imgUrl = "imgUrl";
	
	/**
	 * 产品信息表 字段名 imgs
	 */
	String product_field_imgs = "imgs";
	
	/**
	 * 产品信息表 字段名 name
	 */
	String product_field_name = "name";
	
	/**
	 * 产品信息表 字段名 price
	 */
	String product_field_price = "price";
	
	/**
	 * 产品信息表 字段名 number
	 */
	String product_field_number = "number";
	
	/**
	 * 产品信息表 字段名 unit
	 */
	String product_field_unit = "unit";
	
	/**
	 * 产品信息表 字段名 k
	 */
	String product_field_k = "k";
	
	/**
	 * 产品信息表 字段名 v
	 */
	String product_field_v = "v";
	
	/**
	 * 产品信息表 字段名 products
	 */
	String product_field_products = "products";
	
	/**
	 * 产品信息表 字段名 params
	 */
	String product_field_params = "params";
	
	/**
	 * 产品信息表 字段名 key
	 */
	String product_field_key = "key";
	
	/**
	 * 产品信息表 字段名 value
	 */
	String product_field_value = "value";
	
	/**
	 * 产品信息表 字段名 values
	 */
	String product_field_values = "values";
	
	/**
	 * 产品信息表 字段名 id
	 */
	String product_field_id = "id";
	
	/**
	 * 产品信息表 字段名 detailDesc
	 */
	String product_field_detailDesc = "detailDesc";
	
	/**
	 * 产品信息表 字段名 paras
	 */
	String product_field_paras = "paras";
	
	/**
	 * 产品信息表 字段名 service
	 */
	String product_field_service = "service";
	
	/**
	 * 产品计件单位”件“
	 */
	String product_unit = "件";
	
	/**
	 * 产品价格如果不填的情况下，默认“-1”
	 */
	String product_price = "-1";

	/******************price****************/

	/**
	 * id
	 */
	String id = "id";

	/**
	 * name
	 */
	String name = "name";
	/**
	 * logo
	 */
	String logo = "logo";
	/**
	 * webSite
	 */
	String webSite = "webSite";
	/**
	 * address
	 */
	String address = "address";
	/**
	 * 公司名称
	 */
	String companyName = "companyName";
	/**
	 * 公司联系人
	 */
	String linkMan = "linkMan";
	/**
	 * 公司座机
	 */
	String telephone = "telephone";
	/**
	 * 公司联系人手机
	 */
	String mobile = "mobile";
	
	/**
	 * 上传询，报价单的路径  /price
	 */
	String upload_price_document_url = "/price";
	/**
	 * 上传简历附件的路径  /job
	 */
	String upload_job_document_url = "/job";

	/******************end****************/
	/**
	 * 公司性质
	 */
	String enterpriseTypeName = "enterpriseTypeName";
	/**
	 * 公司注册时间
	 */
	String enterpriseCreaterTime = "enterpriseCreaterTime";
	/**
	 * 公司注册资本
	 */
	String registerCapital = "registerCapital";
	/**
	 * 公司人员规模
	 */
	String employeeNumber = "employeeNumber";
	/**
	 * 企业身份
	 */
	String identifyName = "identifyName";
	/**
	 * 企业介绍
	 */
	String enterpriseDesc = "enterpriseDesc";
	/**
	 * 企业主营范围
	 */
	String saleProductDesc = "saleProductDesc";
	/**
	 * 企业所在地区
	 */
	String area = "area";
	/**
	 * 传真
	 */
	String fax = "fax";
	/**
	 * 企业资质
	 */
	String certificateRecord = "certificateRecord";
	/**
	 * 状态
	 */
	String status = "status";
	/**
	 * 排序
	 */
	String sort = "sort";
	/**
	 * 标题
	 */
	String title = "title";
	/**
	 * 类型
	 */
	String type = "type";
	/**
	 * 发布时间
	 */
	String publishTime = "publishTime";
	/**
	 *
	 */
	String offerTime = "offerTime";
	/**
	 *职位名称
	 */
	String position = "position";
	/**
	 *月薪
	 */
	String salary = "salary";

	String code = "code";

	String subPositionList = "subPositionList";

	String realName = "realName";

	String experienceYear = "experienceYear";

	String welfare = "welfare";
}

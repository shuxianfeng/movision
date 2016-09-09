package com.zhuhuibao.common.constant;

/**
 * 消息定义接口常量
 * @author penglong
 *
 */
public interface MsgCodeConstant {

	int response_status_200 = 200;

	int response_status_400 = 400;

	/**
	 * 统一定义成功返回1000
	 */
	int mcode_common_success = 1000;

	/**
	 * 通用错误
	 */
	int mcode_common_failure = 1001;

	/**
	 * 会员错误信息码：100开头
	 * 账户名已经存在
	 */
	int member_mcode_account_exist = 10001;

	/**
	 * 手机验证码不正确
	 */
	int member_mcode_mobile_validate_error = 10002;

	/**
	 * 邮箱验证码不正确
	 */
	int member_mcode_mail_validate_error = 10003;

	/**
	 * 激活码已过期
	 */
	int member_mcode_active_code_expire = 10004;

	/**
	 * 邮箱已激活请登录
	 */
	int member_mcode_mail_actived = 10005;

	/**
	 * 邮箱已被注册
	 */
	int member_mcode_mail_registered = 10006;

	/**
	 * 该邮箱未注册
	 */
	int member_mcode_mail_unregister = 10007;

	/**
	 * 验证身份已过期
	 */
	int member_mcode_mail_validate_expire = 10008;

	/**
	 * 密码找回链接已经失效
	 */
	int member_mcode_mail_url_invalid = 10009;

	/**
	 * 用户名不存在
	 */
	int member_mcode_username_not_exist = 10010;

	/**
	 * 帐户状态异常
	 */
	int member_mcode_account_status_exception = 10011;

	/**
	 * 用户名或密码错误
	 */
	int member_mcode_usernameorpwd_error = 10012;

	/**
	 * 找回密码错误
	 */
	int MEMBER_SEED_PWD_ERROR = 10214;

	/**
	 * 短信验证码超时
	 */
	int member_mcode_sms_timeout = 10013;

	/**
	 * 文件不存在
	 */
	int file_not_exist = 10210;

	/**
	 * 文件名下载异常
	 */
	int file_download_error = 10211;

	/**
	 * 未登录
	 */
	int un_login = 10212;

	/**
	 * 图形验证码不正确
	 */
	int validate_error = 10213;

	/**
	 * 筑慧币余额不足
	 */
	int ZHB_AUTOPAYFOR_FAILED = 10302;

	/**
	 * 充值失败
	 */
	int ZHB_PERPAID_FAILED = 10302;

	/**
	 * 支付宝异常错误码 9000~9999
	 */
	int ALIPAY_JSON_ERROR = 9000;

	/**
	 * 支付参数错误
	 */
	int ALIPAY_PARAM_ERROR = 9001;

	/**
	 * 支付宝校验异常
	 */
	int ALIPAY_VERIFY_ERROR = 9002;

	/**
	 * 支付宝支付失败
	 */
	int ALIPAY_PAY_FAIL = 9003;

	/**
	 * 支付异常
	 */
	int PAY_ERROR = 9004;


	/**
	 * 支付宝支付成功
	 */
	int ALIPAY_PAY_SUCCESS = 9999;


	/**
	 * 解析错误 8000~8999
	 * JSON格式解析错误
	 */
	int JSON_PARSE_ERROR = 8000;

	/**
	 * 数据操作异常 2000~2999
	 * 插入失败
	 */
	int DB_INSERT_FAIL = 2000;
	/**
	 * 数据操作异常 2000~2999
	 * 更新失败
	 */
	int DB_UPDATE_FAIL = 2001;


	int DB_SELECT_FAIL = 2002;
	/**
	 * 参数校验错误
	 */
	int PARAMS_VALIDATE_ERROR = 4000;

	/**
	 * 系统异常
	 */
	int SYSTEM_ERROR = 4001;

	/**
	 * 已是专家
	 */
	int EXPERT_ISEXIST = 10214;

	/**
	 * 扣款
	 */
	int ZHB_PAYMENT_TRUE = 10070;

	/**
	 * 筑慧币支付失败稍后重试或联系客服
	 */
	int ZHB_PAYMENT_FAILURE = 10080;

	/**
	 * 简历增加屏蔽企业关键字超限
	 */
	int FORBID_KEYWORDS_LIMIT = 10215;

	/**
	 * 简历增加屏蔽企业关键字重复
	 */
	int FORBID_KEYWORDS_REPEAT = 10216;

	/**
	 * 期望工作地点不得超过5个
	 */
	int RESUME_JOB_COUNT_LIMIT = 10217;

	/**
	 * 意见反馈图片不得超过4个
	 */
	int SUGGEST_URL_COUNT_LIMIT = 10218;

	/**
	 * 审核实名认证需先审核基本资料
	 */
	int SMRZSH_ERROR = 5001;
	
	/**
	 * 数据存在警告 3000~3099
	 * 该合同编号已存在
	 */
	int EXIST_CONTRACTNO_WARN = 3000;
	/**
	 * 不存在该商品信息
	 */
	int NOT_EXIST_GOODS_ERROR = 3001;
	/**
	 * 不存在该会员信息
	 */
	int NOT_EXIST_VIP = 3002;
	
	/**
	 * 时间异常 3100~3199
	 * 日期转换异常
	 */
	int DATE_CONVERT_WARN = 3100; 
	
	/**
	 * 不支持会员降级处理
	 */
	int DEGRADE_VIP_WARN = 3200;
	
	
}

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

	/**
	 * 参数校验错误
	 */
	int PARAMS_VALIDATE_ERROR = 4000;

	/**
	 * 系统异常
	 */
	int SYSTEM_ERROR = 4001;
}

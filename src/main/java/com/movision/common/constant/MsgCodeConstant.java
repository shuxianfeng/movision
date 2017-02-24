package com.movision.common.constant;

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
	 * 激活码已过期
	 */
	int member_mcode_active_code_expire = 10004;

	/**
	 * 验证身份已过期
	 */
	int member_mcode_mail_validate_expire = 10008;

	/**
	 * 用户名不存在
	 */
	int app_user_not_exist = 10010;

	/**
	 * 帐户状态异常
	 */
	int app_account_status_error = 10011;

	/**
     * 用户名或验证码错误
     */
	int app_account_name_error = 10012;


	/**
	 * 短信验证码超时
	 */
	int member_mcode_sms_timeout = 10013;

	/**
	 * app本地的token丢失
	 */
	int app_token_missing = 10014;

	/**
	 * 服务器存储的token丢失
	 */
	int server_token_missing = 10015;


    /**
     * apptoken和服务器token不相等
     */
    int app_token_not_equal_server_token = 10016;

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
	 * 数据存在警告 3000~3099
	 * 该合同编号已存在
	 */
	int EXIST_CONTRACTNO_WARN = 3000;
	/**
	 * 不存在该商品信息
	 */
	int NOT_EXIST_GOODS_ERROR = 3001;


	
	/**
	 * 时间异常 3100~3199
	 * 日期转换异常
	 */
	int DATE_CONVERT_WARN = 3100;


	/**
	 * 微信支付结果通用通知接口参数错误
	 */
	int NOTIFY_PARAMS_EMPTY_ERROR = 8001;
	
	/**
	 * 统一下单接口错误码
	 */
    enum WXPAY_ERROR_CODE {

        NOAUTH(8002),
        NOTENOUGH(8003),
		ORDERPAID(8004),
		ORDERCLOSED(8005), 
		SYSTEMERROR(8006), 
		APPID_NOT_EXIST(8007), 
		MCHID_NOT_EXIST(8008),
		APPID_MCHID_NOT_MATCH(8009),
		LACK_PARAMS(8010),
		OUT_TRADE_NO_USED(8011),
		SIGNERROR(8012),
		XML_FORMAT_ERROR(8013),
		REQUIRE_POST_METHOD(8014),
		POST_DATA_EMPTY(8015),
		NOT_UTF8(8016);
		
        public final int code;

        WXPAY_ERROR_CODE(Integer code) {
            this.code = code;
        }

        public Integer getCode(){
        	return this.code;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
    }
	
	/**
	 * 微信查询订单接口错误码
	 * @author zhuangyuhao
	 * @time   2016年10月25日 下午4:47:29
	 *
	 */
    enum WXPAY_QUERY_ORDER_ERROR {
        ORDERNOTEXIST(8018),
        SYSTEMERROR(8019);
		
		public final int code;

		WXPAY_QUERY_ORDER_ERROR(Integer code) {
            this.code = code;
        }

        public Integer getCode(){
        	return this.code;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.code);
        }
	}
	
	
	/**
	 * 微信支付回调接口调用时，微信端的请求参数中不存在该订单
	 */
	int NOT_EXIST_ORDER_FOR_WXPAY = 8017;
	
	/**
	 * 不存在该订单
	 */
	int NOT_EXIST_ORDER = 3005;


	/**
	 * 手机号是空
	 */
	int MOBILE_IS_EMPTY = 3007;
	
	/**
	 * 手机短信验证码是空
	 */
	int SMS_VERIFY_CODE_IS_EMPTY = 3008;
	
	/**
	 * 手机格式不正确
	 */
	int MOBILE_PATTERN_ERROR = 3009;
	
	/**
	 * 不存在该会员信息
	 */
	int NOT_EXIST_MEMBER = 3010;
	
	/**
	 * 该订单不存在购买的商品
	 */
	int NOT_EXIST_ORDER_GOODS = 3011;


	/**
	 * boss用户不存在
	 */
	int boss_user_not_exist = 10300;

	/**
	 * 手机号已经存在
	 */
	int phone_is_exist = 10301;

	/**
	 * boss菜单已经存在
	 */
	int boss_menu_name_is_exist = 10302;

	/**
	 * 角色名称已经存在
	 */
	int boss_role_name_is_exist = 10303;

	/**
	 * boss用户名已经存在
	 */
	int boss_username_is_exist = 10304;

}

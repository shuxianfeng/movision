package com.movision.common.constant;

/**
 * 消息定义接口常量
 * @author zhuangyuhao
 *
 */
public interface MsgCodeConstant {

	int response_status_200 = 200;

	int response_status_400 = 400;

	/**
	 * 手机验证码不正确
	 */
	int member_mcode_mobile_validate_error = 10002;

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
     * 该token的app用户不存在
     */
    int app_user_not_exist_with_this_token = 10017;

	/**
	 * 该昵称已经存在
	 */
	int app_nickname_already_exist = 10018;

	/**
	 * 文件名下载异常
	 */
	int file_download_error = 10211;

	/**
	 * 未登录
	 */
	int un_login = 10212;

	/**
	 * 系统异常
	 */
	int SYSTEM_ERROR = 4001;

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
	 * 不存在该会员信息
	 */
	int NOT_EXIST_APP_ACCOUNT = 3010;

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

    /**
     * qq账号不存在
     */
    int app_account_by_qq_not_exist = 10305;

    /**
     * app用户编辑我的地址失败
	 */
	int app_edit_my_address_fail = 20000;

	/**
	 * app积分类型不存在
	 */
	int app_point_type_not_exist = 30000;

    /**
     * 创建云信id失败
     */
    int create_im_accid_fail = 30100;

	/**
	 * 发送系统通知失败
	 */
	int send_system_msg_fail = 30101;

}

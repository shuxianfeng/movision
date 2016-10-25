package com.zhuhuibao.common.constant;

/**
 * 常量类
 *
 * @author wangxiang2
 */
public class PayConstants {

    /**
     * 支付方式  1支付宝，2筑慧币支付, 3微信
     */
    public enum PayMode {
        ALIPAY("1"), ZHBPAY("2"), WXPAY("3");
        public final String value;

        PayMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 
     * 微信查订单接口的交易状态
     * @author zhuangyuhao
     * @time   2016年10月25日 下午4:58:15
     *
     */
    public enum WX_TradeStatus{
    	
    	SUCCESS("支付成功"),
    	REFUND("转入退款"),
    	NOTPAY("未支付"),
    	CLOSED("已关闭"),
    	REVOKED("已撤销（刷卡支付）"),
    	USERPAYING("用户支付中"),
    	PAYERROR("支付失败(其他原因，如银行返回失败)");
    	
    	public final String value;
    	
    	WX_TradeStatus(String value){
    		this.value = value;
    	}
    	
    	@Override
        public String toString() {
            return this.value;
        }
    }
    
    /**
     * 订单状态 1未支付，2：已支付，3：支付失败 ，4：待退款，5：退款中，6：退款失败，7：已退款 ，8:已关闭
     */
    public enum OrderStatus {
        WZF("1"), YZF("2"),FAIL("3"),DTK("4"), TKZ("5"), TKSB("6"), YTK("7"),CLOSED("8");
        public final String value;

        OrderStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 交易状态  1:成功，2：处理中  3：失败
     */
    public enum TradeStatus {
        SUCCESS("1"), INPROCESS("2"), FAIL("3");
        public final String value;

        TradeStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }


    /**
     * 通知类型  1:同步  2 :异步:
     */
    public enum NotifyType {
        SYNC("1"), ASYNC("2");

        private final String value;

        NotifyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 交易类型 1:支付  2:退款
     */
    public enum TradeType {
        PAY("1"), REFUND("2");

        private final String value;

        TradeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * 商品类型 {1表示实物类商品  0表示虚拟类商品}
     */
    public enum GoodsType {
        XNL("0"), SWL("1");
        public final String value;

        GoodsType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * http 状态码
     */
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_BUSINESS_EXCEPTION_CODE = 560;  //业务异常
    public static final int HTTP_SYSTEM_EXCEPTION_CODE = 600;    //系统异常

    /**
     * 支持请求方式类型 get | post
     */
    public static final String ALIPAY_METHOD_GET = "get";
    public static final String ALIPAY_METHOD_POST = "post";
}

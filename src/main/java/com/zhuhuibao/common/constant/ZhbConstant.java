package com.zhuhuibao.common.constant;

/**
 * 筑慧币相关静态常量
 * 
 * @author tongxinglong
 *
 */
public class ZhbConstant {
	
	public enum ZhbCode{
		/**
		 * 扣减筑慧币
		 */
		DEDUCT(1),
		/**
		 * 不扣减筑慧币
		 */
		NOTDEDUCT(2);

		public final int value;

		private ZhbCode(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	/**
	 * 账户状态
	 * 
	 * @author tongxinglong
	 *
	 */
	public enum ZhbAccountStatus {
		/**
		 * 账户未冻结
		 */
		ACTIVE("1"),
		/**
		 * 账户已冻结
		 */
		FROZEN("2");

		private final String value;

		private ZhbAccountStatus(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	/**
	 * 筑慧币流水记录的类型
	 * 
	 * @author tongxinglong
	 *
	 */
	public enum ZhbRecordType {
		/**
		 * 支付
		 */
		PAYFOR("1"),
		/**
		 * 充值
		 */
		PREPAID("2"),
		/**
		 * 退款
		 */
		REFUND("3");

		public final String value;

		private ZhbRecordType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

}

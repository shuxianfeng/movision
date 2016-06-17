package com.zhuhuibao.common.constant;

public class VipConstant {
	/**
	 * 特权类型（有无、折扣率、数量）
	 */
	public enum VipPrivilegeType {
		/**
		 * 有无
		 */
		EXIST("1"),
		/**
		 * 折扣率
		 */
		DISCOUNT("2"),
		/**
		 * 数量
		 */
		NUM("3");

		private final String value;

		private VipPrivilegeType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	/**
	 * 额外自定义特权-个人
	 */
	public final static int EXTRA_PRIVILEGE_LEVEL_PERSONAL = 200;
	/**
	 * 额外自定义特权-企业
	 */
	public final static int EXTRA_PRIVILEGE_LEVEL_ENTERPRISE = 201;
}

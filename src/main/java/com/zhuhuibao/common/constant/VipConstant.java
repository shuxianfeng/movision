package com.zhuhuibao.common.constant;

import java.util.HashMap;
import java.util.Map;

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

	public enum VipLevel {
		/**
		 * 个人免费
		 */
		PERSON_FREE(0),
		/**
		 * 个人黄金
		 */
		PERSON_GOLD(30),
		/**
		 * 个人铂金
		 */
		PERSON_PLATINUM(60),
		/**
		 * 企业免费
		 */
		ENTERPRISE_FREE(100),
		/**
		 * 企业黄金
		 */
		ENTERPRISE_GOLD(130),
		/**
		 * 企业铂金
		 */
		ENTERPRISE_PLATINUM(160);

		public final int value;

		private VipLevel(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	/**
	 * VIP级别对应的名称
	 */
	public final static Map<String, String> VIP_LEVEL_NAME = new HashMap<String, String>() {
		{
			put(VipLevel.PERSON_FREE.toString(), "个人普通会员");
			put(VipLevel.PERSON_GOLD.toString(), "个人黄金VIP会员");
			put(VipLevel.PERSON_PLATINUM.toString(), "个人铂金VIP会员");
			put(VipLevel.ENTERPRISE_FREE.toString(), "企业普通会员");
			put(VipLevel.ENTERPRISE_GOLD.toString(), "企业黄金VIP会员");
			put(VipLevel.ENTERPRISE_PLATINUM.toString(), "企业铂金VIP会员");
		}
	};

	/**
	 * VIP级别对应赠送筑慧币数量
	 */
	public final static Map<String, Long> VIP_LEVEL_ZHB = new HashMap<String, Long>() {
		{
			put(VipLevel.PERSON_FREE.toString(), 0L);
			put(VipLevel.PERSON_GOLD.toString(), 350L);
			put(VipLevel.PERSON_PLATINUM.toString(), 580L);
			put(VipLevel.ENTERPRISE_FREE.toString(), 0L);
			put(VipLevel.ENTERPRISE_GOLD.toString(), 3500L);
			put(VipLevel.ENTERPRISE_PLATINUM.toString(), 6200L);
		}
	};

	/**
	 * 额外自定义特权-个人
	 */
	public final static int EXTRA_PRIVILEGE_LEVEL_PERSONAL = 200;
	/**
	 * 额外自定义特权-企业
	 */
	public final static int EXTRA_PRIVILEGE_LEVEL_ENTERPRISE = 201;
	
	public final static String[] CHARGE_VIP_LEVEL =  {"30","60","130","160"};
}

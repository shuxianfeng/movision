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
	 * VIP级别对应的t_dictionary_zhbgoods中的ID
	 */
	public final static Map<String, Integer> VIP_LEVEL_GOODSID = new HashMap<String, Integer>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = -6418172837829944696L;

		{
			put(VipLevel.PERSON_GOLD.toString(), 15);
			put(VipLevel.PERSON_PLATINUM.toString(), 16);
			put(VipLevel.ENTERPRISE_GOLD.toString(), 17);
			put(VipLevel.ENTERPRISE_PLATINUM.toString(), 18);
		}
	};
	
	
	/**
	 * VIP级别对应的名称
	 */
	public final static Map<String, String> VIP_LEVEL_NAME = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6418172837829944696L;

		{
			put(VipLevel.PERSON_FREE.toString(), "个人普通会员");
			put(VipLevel.PERSON_GOLD.toString(), "个人黄金盟友");
			put(VipLevel.PERSON_PLATINUM.toString(), "个人铂金盟友");
			put(VipLevel.ENTERPRISE_FREE.toString(), "企业普通会员");
			put(VipLevel.ENTERPRISE_GOLD.toString(), "企业黄金盟友");
			put(VipLevel.ENTERPRISE_PLATINUM.toString(), "企业铂金盟友");
		}
	};

	/**
	 * VIP级别对应赠送筑慧币数量
	 */
	public final static Map<String, Long> VIP_LEVEL_ZHB = new HashMap<String, Long>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7063341595544422247L;
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
	/**
	 * 个人黄金+个人铂金
	 */
	public final static String[] PERSON_VIP_LEVEL = {"30","60"};
	/**
	 * 企业黄金+企业铂金
	 */
	public final static String[] ENTERPRISE_VIP_LEVEL = {"130","160"};
	
	
	public final static String PERSON = "1";
	public final static String ENTERPRISE = "2";
	
	
}

package com.zhuhuibao.common.constant;

/**
 * 筑慧币相关静态常量
 * 
 * @author tongxinglong
 *
 */
public class ZhbConstant {

	public enum ZhbCode {
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

	public enum ZhbGoodsType {
		/**
		 * 员工账号
		 */
		YGZH,
		/**
		 * 询价发布
		 */
		XJFB,
		/**
		 * 报价发布
		 */
		BJFB,
		/**
		 * 查看项目信息
		 */
		CKXMXX,
		/**
		 * 查看威客任务
		 */
		CKWKRW,
		/**
		 * 发布威客服务
		 */
		FBWKFW,
		/**
		 * 发布资质合作
		 */
		FBZZHZ,
		/**
		 * 发布职位
		 */
		FBZW,
		/**
		 * 查看/下载简历
		 */
		CXXZJL,
		/**
		 * 查看技术成果
		 */
		CKJSCG,
		/**
		 * 查看专家技术成果
		 */
		CKZJJSCG,
		/**
		 * 发布技术需求
		 */
		FBJSXQ,
		/**
		 * 查看专家信息
		 */
		CKZJXX,
		/**
		 * 给专家留言
		 */
		GZJLY
	}
}

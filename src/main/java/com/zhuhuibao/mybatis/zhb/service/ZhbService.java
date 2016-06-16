package com.zhuhuibao.mybatis.zhb.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.PayConstants.OrderStatus;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbAccountStatus;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbRecordType;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.ZhbGoodsConfig;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;
import com.zhuhuibao.mybatis.zhb.mapper.ZhbMapper;
import com.zhuhuibao.utils.MapUtil;

/**
 * 筑慧币账户service
 * 
 * @author tongxinglong
 *
 */
@Service
@Transactional
public class ZhbService {
	private Logger log = LoggerFactory.getLogger(ZhbService.class);

	@Autowired
	private ZhbMapper zhbMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private OrderService orderService;

	// TODO 判断接口调用者和当前数据属于同一人

	/**
	 * 充值
	 * 
	 * @param orderMap
	 *            :memberId/operaterId/amount/orderNo
	 * @return
	 */
	public int zhbPrepaid(ZhbRecord zhbRecord) {
		int result = 0;
		try {
			// 充值操作流水记录类型设置
			zhbRecord.setType(ZhbRecordType.PREPAID);

			// 若数据正常，则进行充值操作
			if (isNotExistsZhbRecord(zhbRecord.getOrderNo(), ZhbRecordType.PREPAID) && isRightZhbRecord(zhbRecord, OrderStatus.YZF)) {
				// 增加充值流水
				insertZhbRecord(zhbRecord, ZhbRecordType.PREPAID);

				// 增加充值金额
				ZhbAccount zhbAccount = zhbMapper.selectZhbAccount(zhbRecord.getBuyerId());
				if (null != zhbAccount) {
					// 将充值金额更新到账户
					zhbAccount.setAmount(zhbAccount.getAmount().add(zhbRecord.getAmount()));
					zhbMapper.updateZhbAccountEmoney(zhbAccount);
				} else {
					// 初次充值账户为空，将充值金额添加到账户
					insertZhbAccount(zhbRecord.getBuyerId(), zhbRecord.getAmount());
				}

				result = 1;
			}
		} catch (Exception e) {
			String msg = null == zhbRecord ? "null" : "orderNo=" + zhbRecord.getOrderNo() + ",buyerId=" + zhbRecord.getBuyerId();
			log.error("ZhbAccountService::zhbPrepaid::" + msg, e);
		}

		return result;
	}

	/**
	 * 查询筑慧币余额
	 * 
	 * @param memberId
	 * @return
	 */
	public ZhbAccount getZhbAccount(Long memberId) {
		return zhbMapper.selectZhbAccount(memberId);
	}

	/**
	 * 支付
	 * 
	 * @param zhbRecord
	 *            :BuyerId/operaterId/amount/orderNo/goodsId/goodsType
	 * @return 1:支付成功，0：支付失败
	 */
	public int payForOrder(ZhbRecord zhbRecord) {
		int result = 0;
		try {
			zhbRecord.setType(ZhbRecordType.PAYFOR);

			// 验证是否可以支付:余额是否足够 且 zhbRecord对象数据正确
			ZhbAccount account = getZhbAccount(zhbRecord.getBuyerId());
			if (null != account && account.getAmount().compareTo(zhbRecord.getAmount()) >= 0 && isRightZhbRecord(zhbRecord, null)) {
				// 增加流水记录
				insertZhbRecord(zhbRecord, ZhbRecordType.PAYFOR);
				// 修改筑慧币总额
				account.setAmount(account.getAmount().subtract(zhbRecord.getAmount()));

				// TODO where条件，account.getEmoney() = 原值 ;
				// 判断更新条数!=1为异常，事务返回，支付失败
				zhbMapper.updateZhbAccountEmoney(account);

				result = 1;
			}
		} catch (Exception e) {
			String msg = null == zhbRecord ? "null" : "orderNo=" + zhbRecord.getOrderNo() + ",buyerId=" + zhbRecord.getBuyerId();
			log.error("ZhbAccountService::payForOrder::" + msg, e);
		}

		return result;
	}

	/**
	 * 退款
	 * 
	 * @param orderMap
	 *            :memberId/operaterId/amount/orderNo
	 * @return
	 */
	public int refund(ZhbRecord zhbRecord) {
		int result = 0;
		try {
			zhbRecord.setType(ZhbRecordType.REFUND);
			// 确认存在订单号对应的支付流水且不存在退款流水
			List<ZhbRecord> recordList = listZhbRecordByOrderNo(zhbRecord.getOrderNo());
			if (CollectionUtils.isNotEmpty(recordList) && recordList.size() == 1 && ZhbRecordType.PAYFOR == recordList.get(0).getType()
					&& isRightZhbRecord(zhbRecord, OrderStatus.TKZ)) {
				// 添加退款流水记录
				insertZhbRecord(zhbRecord, ZhbRecordType.REFUND);
				// 更新账户数据
				ZhbAccount account = getZhbAccount(zhbRecord.getBuyerId());
				if (null != account) {
					account.setAmount(account.getAmount().add(zhbRecord.getAmount()));
					// TODO where条件，account.getEmoney() = 原值 ;
					zhbMapper.updateZhbAccountEmoney(account);

					result = 1;
				}
			}
		} catch (Exception e) {
			String msg = null == zhbRecord ? "null" : "orderNo=" + zhbRecord.getOrderNo() + ",buyerId=" + zhbRecord.getBuyerId();
			log.error("ZhbAccountService::refund::" + msg, e);
		}
		return result;
	}

	/**
	 * 获取筑慧币物品配置信息
	 * 
	 * @param pinyin
	 * @return
	 */
	@Cacheable(value = "zhbGoodsConfigCache", key = "#pinyin")
	public ZhbGoodsConfig getZhbGoodsConfigByPinyin(String pinyin) {
		if (StringUtils.isNotBlank(pinyin)) {
			return zhbMapper.selectZhbGoodsConfigByPinyin(pinyin.toLowerCase());
		}
		return null;
	}

	/**
	 * 判断 record信息是否正确
	 * 
	 * @param zhbRecord
	 * @param correctStatus
	 * @return
	 */
	private boolean isRightZhbRecord(ZhbRecord zhbRecord, OrderStatus correctStatus) {
		boolean isRight = false;

		if (null != zhbRecord && StringUtils.isNotBlank(zhbRecord.getOrderNo()) && !"0".equals(zhbRecord.getOrderNo()) && null != correctStatus) {
			// 存在订单的情况，判断是否正确
			Order order = orderService.findByOrderNo(zhbRecord.getOrderNo());
			if (null != order && null != zhbRecord.getAmount()) {
				// 订单状态
				boolean rightStatus = order.getStatus().equals(correctStatus.toString());
				if (!rightStatus) {
					return isRight;
				}

				// amount 充值的时候需一致
				boolean rightAmount = zhbRecord.getAmount().compareTo(BigDecimal.ZERO) > 0 && order.getAmount().compareTo(zhbRecord.getAmount()) >= 0;
				if (!rightAmount) {
					return isRight;
				}

				// buyerId和operaterId的关系是否一致
				boolean rightOperaterId = zhbRecord.getOperaterId() == zhbRecord.getBuyerId();
				if (!rightOperaterId && ZhbRecordType.REFUND != zhbRecord.getType()) {
					Member operater = memberService.findMemById(String.valueOf(zhbRecord.getOperaterId()));
					rightOperaterId = null != operater && String.valueOf(zhbRecord.getBuyerId()).equals(operater.getEnterpriseEmployeeParentId());
				}
				if (!rightOperaterId) {
					return isRight;
				}

				// buyerId 一致
				boolean rightBuyerId = order.getBuyerId() == zhbRecord.getBuyerId();
				if (!rightBuyerId) {
					return isRight;
				} else {
					Member member = memberService.findMemById(String.valueOf(zhbRecord.getBuyerId()));
					rightBuyerId = null != member && "0".equals(member.getEnterpriseEmployeeParentId());
				}
				if (!rightBuyerId) {
					return isRight;
				}

				// 最终结果
				isRight = true;
			}
		} else if (null != zhbRecord && "0".equals(zhbRecord.getOrderNo())) {
			// 无订单情况，类型为支付
			if (ZhbRecordType.PAYFOR != zhbRecord.getType()) {
				return isRight;
			}

			// amount 待消费的金额需大于0
			boolean rightAmount = zhbRecord.getAmount().compareTo(BigDecimal.ZERO) > 0;
			if (!rightAmount) {
				return isRight;
			}
			// buyerId和operaterId的关系是否一致
			if (zhbRecord.getOperaterId() != zhbRecord.getBuyerId()) {
				Member operater = memberService.findMemById(String.valueOf(zhbRecord.getOperaterId()));
				boolean rightOperaterId = null != operater && String.valueOf(zhbRecord.getBuyerId()).equals(operater.getEnterpriseEmployeeParentId());
				if (!rightOperaterId) {
					return isRight;
				}
			}

			// 判断BuyerId为管理员账号
			if (zhbRecord.getOperaterId() == zhbRecord.getBuyerId()) {
				Member member = memberService.findMemById(String.valueOf(zhbRecord.getBuyerId()));
				boolean rightBuyerId = null != member && "0".equals(member.getEnterpriseEmployeeParentId());
				if (!rightBuyerId) {
					return isRight;
				}
			}

			// 最终结果
			isRight = true;
		}

		return isRight;
	}

	/**
	 * 判断是否不存在对应流水记录
	 * 
	 * @return
	 */
	private boolean isNotExistsZhbRecord(String orderNo, ZhbRecordType type) {
		return null == getZhbRecordByOrderNoAndType(orderNo, type);
	}

	/**
	 * 添加筑慧币账号
	 * 
	 * @param memberId
	 * @param amount
	 */
	private void insertZhbAccount(Long memberId, BigDecimal amount) {
		ZhbAccount zhbAccount = new ZhbAccount();
		zhbAccount.setMemberId(memberId);
		zhbAccount.setStatus(ZhbAccountStatus.ACTIVE);
		zhbAccount.setAmount(amount);
		Calendar cal = Calendar.getInstance();
		zhbAccount.setAddTime(cal.getTime());
		zhbAccount.setUpdateTime(cal.getTime());

		zhbMapper.insertZhbAccount(zhbAccount);
	}

	/**
	 * 添加筑慧币流水记录
	 * 
	 * @param zhbRecord
	 * @param type
	 */
	private void insertZhbRecord(ZhbRecord zhbRecord, ZhbRecordType type) {
		zhbRecord.setType(type);
		Calendar cal = Calendar.getInstance();
		zhbRecord.setAddTime(cal.getTime());
		zhbRecord.setUpdateTime(cal.getTime());

		zhbMapper.insertZhbRecord(zhbRecord);
	}

	/**
	 * 根据订单编号获取流水记录
	 * 
	 * @param orderNo
	 * @return
	 */
	private List<ZhbRecord> listZhbRecordByOrderNo(String orderNo) {
		return zhbMapper.selectZhbRecordListByOrderNo(orderNo);
	}

	/**
	 * 根据订单号查询筑慧币流水信息
	 * 
	 * @param orderNo
	 * @return
	 */
	private ZhbRecord getZhbRecordByOrderNoAndType(String orderNo, ZhbRecordType type) {

		if (StringUtils.isNotBlank(orderNo) && !"0".equals(orderNo)) {
			Map<String, String> param = MapUtil.convert2HashMap("orderNo", orderNo, "type", type.value);
			return zhbMapper.selectZhbRecordByOrderNoAndType(param);
		}

		return null;
	}

}

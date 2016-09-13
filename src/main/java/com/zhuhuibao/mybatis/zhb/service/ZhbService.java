package com.zhuhuibao.mybatis.zhb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.constant.PayConstants.OrderStatus;
import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbAccountStatus;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbRecordType;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;
import com.zhuhuibao.mybatis.memCenter.mapper.WorkTypeMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import com.zhuhuibao.mybatis.order.entity.OrderGoods;
import com.zhuhuibao.mybatis.order.service.OrderFlowService;
import com.zhuhuibao.mybatis.order.service.OrderGoodsService;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;
import com.zhuhuibao.mybatis.zhb.mapper.ZhbMapper;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

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
	private WorkTypeMapper workTypeMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private OrderFlowService orderFlowService;

	@Autowired
	private VipInfoService vipInfoService;

	@Autowired
	private PaymentGoodsService paymentGoodsService;

	/**
	 * 订单充值
	 * 
	 * @param orderNo
	 * @return
	 */
	public int zhbPrepaidByOrder(String orderNo) throws Exception {
		int result = 0;
		try {
			Order order = orderService.findByOrderNo(orderNo);
			OrderGoods orderGoods = orderGoodsService.findByOrderNo(orderNo);
			// 订单不为空
			// 订单类型为4：筑慧币
			// 订单状态为已支付
			// 订单购买人为当前操作账号的管理员账号
			// 操作人为管理员
			// 不存在该订单号对应的筑慧币流水记录
			// order.getBuyerId().compareTo(ShiroUtil.getCompanyID()) == 0
			// && ShiroUtil.getCompanyID().compareTo(ShiroUtil.getCreateID()) ==
			// 0 &&
			if (null != order && null != orderGoods && "4".equals(order.getGoodsType()) && isNotExistsZhbRecord(orderNo, ZhbRecordType.PREPAID)) {
				DictionaryZhbgoods goods = getZhbGoodsById(String.valueOf(orderGoods.getGoodsId()));
				BigDecimal amount = new BigDecimal(goods.getValue());

				// 订单中amount大于0
				if (BigDecimal.ZERO.compareTo(amount) < 0) {
					// 进行充值
					result = execPrepaid(order.getOrderNo(), order.getBuyerId(), order.getBuyerId(), amount, goods.getPinyin(), goods.getId());
				}
			}
		} catch (Exception e) {
			log.error("ZhbService::zhbPrepaidByOrder::" + "orderNo=" + orderNo + ",buyerId=" + ShiroUtil.getCreateID(), e);
			throw e;
		}

		return result >= 1 ? 1 : 0;
	}

	/**
	 * 订单类型正确\订单状态正确\购买人为当前操作账号的管理员账号
	 * 
	 * @param order
	 * @param goodsType
	 * @param orderStatus
	 * @return
	 */
	private boolean isRightOrder(Order order, String goodsType, String orderStatus) {
		boolean result = false;
		result = null != order && goodsType.equals(order.getGoodsType()) && orderStatus.equals(order.getStatus())
				&& order.getBuyerId().compareTo(ShiroUtil.getCompanyID()) == 0;

		return result;
	}

	/**
	 * 订单类型正确\订单状态正确\购买人为当前操作账号的管理员账号
	 * 
	 * @param order
	 * @param goodsType
	 * @return
	 */
	private boolean isRightOrder(Order order, String goodsType) {
		boolean result = false;
		result = null != order && goodsType.equals(order.getGoodsType());
		// && order.getBuyerId().compareTo(ShiroUtil.getCompanyID()) == 0;

		return result;
	}

	/**
	 * 判断当前登录者是否为企业管理员账号
	 * 
	 * @return
	 */
	/*private boolean isAdminLogin() {
		return ShiroUtil.getCompanyID().compareTo(ShiroUtil.getCreateID()) == 0;
	}*/
	
	/**
	 * 购买VIP服务
	 * 
	 * @param orderNo
	 * @return
	 */
	public int openVipService(String orderNo) throws Exception {
		int result = 0;
		try {
			Order order = orderService.findByOrderNo(orderNo);
			OrderGoods orderGoods = orderGoodsService.findByOrderNo(orderNo);
			// 订单和订单物品不为空
			// 订单类型为3：VIP套餐
			// 订单状态为已支付
			// 订单购买人为当前操作账号的管理员账号 （）
			// 操作人为管理员 (*)
			// 不存在该订单号对应的筑慧币流水记录 && isAdminLogin()
			if (null != order && null != orderGoods && isRightOrder(order, "3") && isNotExistsZhbRecord(orderNo, ZhbRecordType.PREPAID)) {
				DictionaryZhbgoods vipgoods = getZhbGoodsById(String.valueOf(orderGoods.getGoodsId()));
				int buyVipLevel = Integer.parseInt(vipgoods.getValue());
				BigDecimal amount = new BigDecimal(VipConstant.VIP_LEVEL_ZHB.get(String.valueOf(buyVipLevel)));

				// 订单中amount大于0
				// 筑慧币充值
				if (amount.compareTo(BigDecimal.ZERO) > 0 ) {
					// 进行筑慧币充值
					int prepaidResult = execPrepaid(order.getOrderNo(), order.getBuyerId(), order.getBuyerId(), amount, vipgoods.getPinyin(), vipgoods.getId());
					if (0 == prepaidResult) {
						throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "充值失败");
					}
				}
				// VIP升级
				VipMemberInfo vipMemberInfo = vipInfoService.findVipMemberInfoById(order.getBuyerId());
				if (null == vipMemberInfo) {
					vipInfoService.insertVipMemberInfo(order.getBuyerId(), buyVipLevel, 1, null);
				} else if (vipMemberInfo.getVipLevel() <= buyVipLevel) {
					Calendar cal = Calendar.getInstance();
					//若原本是收费会员，则需要在原过期时间上增加1年
					if (ArrayUtils.contains(VipConstant.CHARGE_VIP_LEVEL, String.valueOf(vipMemberInfo.getVipLevel()))) {
						cal.setTime(vipMemberInfo.getExpireTime());
					}
					cal.add(Calendar.YEAR, 1);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.add(Calendar.DATE, 1);
					vipMemberInfo.setExpireTime(cal.getTime());
					vipMemberInfo.setVipLevel(buyVipLevel);
					vipInfoService.updateVipMemberInfo(vipMemberInfo);
				}
				result = 1;
			}
		} catch (Exception e) {
			log.error("ZhbService::openVipService::orderNo=" + orderNo + ",buyerId=" + ShiroUtil.getCreateID(), e);
			throw e;
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
	 * 支付金额大于0，且小于或等于订单金额，筑慧币账户余额足够支付
	 * 
	 * @param zhbAmount
	 * @param orderAmount
	 * @param account
	 * @return
	 */
	private boolean isRightAmount(BigDecimal zhbAmount, BigDecimal orderAmount, ZhbAccount account) {
		return BigDecimal.ZERO.compareTo(zhbAmount) < 0 && zhbAmount.compareTo(orderAmount) <= 0 && null != account
				&& account.getAmount().compareTo(zhbAmount) >= 0 && ZhbConstant.ZhbAccountStatus.ACTIVE.toString().equals(account.getStatus());
	}

	/**
	 * 筑慧币支付订单
	 * 
	 * @param orderNo
	 * @return
	 */
	public int payForOrder(String orderNo) throws Exception {
		int resut = 0;
		try {

			Order order = orderService.findByOrderNo(orderNo);
			OrderFlow orderFlow = orderFlowService.findByOrderNoAndTradeMode(orderNo, PayConstants.PayMode.ZHBPAY.toString());
			ZhbAccount account = getZhbAccount(ShiroUtil.getCompanyID());
			// 订单不为空
			// 订单状态为未支付
			// 订单购买人为当前操作账号的管理员账号
			// 支付金额大于0，且小于或等于订单金额
			// 不存在该订单号对应的筑慧币流水记录
			// 筑慧币账户余额足够支付
			if (null != order && null != orderFlow && isRightOrder(order, order.getGoodsType(), OrderStatus.WZF.value)
					&& isRightAmount(orderFlow.getTradeFee(), order.getPayAmount(), account) && isNotExistsZhbRecord(orderNo, ZhbRecordType.PAYFOR)) {

				insertZhbRecord(orderNo, order.getBuyerId(), ShiroUtil.getCreateID(), orderFlow.getTradeFee(), ZhbRecordType.PAYFOR.toString(), null, "");
				account.setAmount(account.getAmount().subtract(orderFlow.getTradeFee()));
				int updateNum = zhbMapper.updateZhbAccountEmoney(account);
				if (1 != updateNum) {
					throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, "支付失败");
				}
				resut = 1;
			}
		} catch (Exception e) {
			log.error("ZhbService::payForOrder::orderNo=" + orderNo + ",buyerId=" + ShiroUtil.getCreateID(), e);
			throw e;
		}

		return resut;
	}

	/**
	 * 消费自定义特权数量或者筑慧币进行业务操作
	 * 
	 * @param goodsId
	 * @param goodsType
	 * @return
	 */
	public int payForGoods(Long goodsId, String goodsType) throws Exception {
		int result = 0;

		if (vipInfoService.hadExtraPrivilege(ShiroUtil.getCompanyID(), goodsType)) {
			result = vipInfoService.useExtraPrivilege(ShiroUtil.getCompanyID(), goodsType);
		}
		if (0 == result) {
			result = payForGoodsByZhb(goodsId, goodsType);
		}

		// 记录已进行的操作
		paymentGoodsService.insertViewGoods(goodsId, ShiroUtil.getCreateID(), ShiroUtil.getCompanyID(), goodsType);
		return result;
	}

	/**
	 * 判断是否可以使用特权数量或者筑慧币支付
	 * 
	 * @param goodsType
	 * @return
	 */
	public boolean canPayFor(String goodsType) {
		boolean result = false;
		try {
			long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), goodsType);
			if (privilegeNum > 0) {
				return true;
			}

			DictionaryZhbgoods goodsConfig = getZhbGoodsByPinyin(goodsType);
			ZhbAccount account = getZhbAccount(ShiroUtil.getCompanyID());
			result = account.getAmount().compareTo(goodsConfig.getPrice()) >= 0;
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 使用筑慧币支付
	 * 
	 * @param goodsId
	 * @param goodsType
	 * @return 1:成功，0或者exception为失败
	 */
	public int payForGoodsByZhb(Long goodsId, String goodsType) throws Exception {
		int result = 0;

		try {
			DictionaryZhbgoods goods = zhbMapper.selectZhbGoodsByPinyin(goodsType.toUpperCase());
			BigDecimal amount = goods.getPrice();
			// 验证是否可以支付:余额是否足够，amount大于0
			ZhbAccount account = getZhbAccount(ShiroUtil.getCompanyID());
			if (null != account && ZhbConstant.ZhbAccountStatus.ACTIVE.toString().equals(account.getStatus()) && account.getAmount().compareTo(amount) >= 0) {
				// 增加流水记录
				insertZhbRecord("0", ShiroUtil.getCompanyID(), ShiroUtil.getCreateID(), amount, ZhbRecordType.PAYFOR.toString(), goodsId, goodsType);

				// 修改筑慧币总额
				account.setAmount(account.getAmount().subtract(amount));
				int updateNum = zhbMapper.updateZhbAccountEmoney(account);
				if (updateNum != 1) {
					throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "筑慧币余额不足");
				}

				result = 1;
			}
		} catch (Exception e) {
			String msg = "operaterId=" + ShiroUtil.getCreateID() + ",goodsId=" + goodsId + ",goodsType=" + goodsType;
			log.error("ZhbService::payForGoods::" + msg, e);
			throw e;
		}

		return result;
	}

	/**
	 * 运营退款
	 * 
	 * @param orderNo
	 * 
	 * @return
	 */
	public int refundBySystem(String orderNo) throws Exception {
		int result = 0;
		try {
			Order order = orderService.findByOrderNo(orderNo);
			// 订单不为空
			// 订单状态为退款中
			// 不存在该订单号对应的筑慧币退款流水记录
			// 存在该订单号对应的筑慧币支付流水记录
			List<ZhbRecord> recordList = listZhbRecordByOrderNo(orderNo);
			ZhbRecord parForRecord = CollectionUtils.isNotEmpty(recordList) ? recordList.get(0) : null;
			if (null != order && OrderStatus.TKZ.value.equals(order.getStatus()) && null != parForRecord && recordList.size() == 1
					&& ZhbRecordType.PAYFOR.toString().equals(parForRecord.getType())) {
				// 添加退款流水记录
				insertZhbRecord(orderNo, order.getBuyerId(), ShiroUtil.getOmsCreateID(), parForRecord.getAmount(), ZhbRecordType.REFUND.toString(),
						parForRecord.getGoodsId(), parForRecord.getGoodsType());
				// 更新账户数据
				ZhbAccount account = getZhbAccount(parForRecord.getBuyerId());
				if (null != account) {
					account.setAmount(account.getAmount().add(parForRecord.getAmount()));
					zhbMapper.updateZhbAccountEmoney(account);
					result = 1;
				}
			}

		} catch (Exception e) {
			String msg = "orderNo=" + orderNo + ",operaterId=" + ShiroUtil.getOmsCreateID();
			log.error("ZhbService::refund::" + msg, e);
			throw e;
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
	public DictionaryZhbgoods getZhbGoodsByPinyin(String pinyin) {
		if (StringUtils.isNotBlank(pinyin)) {
			return zhbMapper.selectZhbGoodsByPinyin(pinyin.toUpperCase());
		}
		return null;
	}

	/**
	 * 根据类型获取筑慧币物品配置信息
	 * 
	 * @param type
	 * @return
	 */
	public List<DictionaryZhbgoods> listZhbGoodsByType(String type) {
		return zhbMapper.selectZhbGoodsListById(type);
	}

	/**
	 * 获取筑慧币物品配置信息
	 * 
	 * @param id
	 * @return
	 */
//	@Cacheable(value = "zhbGoodsConfigCache", key = "#id")
	public DictionaryZhbgoods getZhbGoodsById(String id) {

		return zhbMapper.selectZhbGoodsById(Long.valueOf(id));
	}

	/**
	 * 按要求查询
	 * 
	 * @param pager
	 * @param recordType
	 *            1:使用，2:获取
	 * @return
	 */
	public List<Map<String, String>> getZhbDetails(Paging<Map<String, String>> pager, String recordType) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			Map<String, Long> param = MapUtil.convert2HashMap("companyId", ShiroUtil.getCompanyID(), "operaterId", ShiroUtil.getCreateID(), "recordType",
					recordType);
			List<Map> recordList = zhbMapper.findAllZhbRecordList(pager.getRowBounds(), param);
			if (CollectionUtils.isNotEmpty(recordList)) {
				for (Map record : recordList) {
					// 行为
					String orderNo = (String) record.get("orderNo");
					if ("0".equals(orderNo)) {
						// 非订单流程购买商品
						DictionaryZhbgoods goods = zhbMapper.selectZhbGoodsByPinyin((String) record.get("goodsType"));
						record.put("behavior", goods.getName());
					} else {
						// 订单流程消费
						OrderGoods goods = orderGoodsService.findByOrderNo(orderNo);
						record.put("behavior", goods.getGoodsName());
					}

					// amount 数量
					if (ZhbRecordType.PAYFOR.toString().equals(record.get("type"))) {
						record.put("amount", "-" + record.get("amount"));
					}
					// 操作人 account ，补充角色
					WorkType workType = workTypeMapper.findWorkTypeById(((Integer) record.get("workType")).toString());
					if (null != workType) {
						record.put("account", record.get("account") + "(" + workType.getName() + ")");
					}
					resultList.add(record);
				}
			}
		} catch (Exception e) {
			log.error("ZhbService::getZhbDetails::memberId=" + ShiroUtil.getCreateID(), e);
			throw e;
		}

		return resultList;
	}

	/**
	 * 添加筑慧币流水记录
	 * 
	 * @param orderNo
	 * @param buyerId
	 * @param operaterId
	 * @param amount
	 * @param type
	 * @param goodsId
	 * @param goodsType
	 */
	public void insertZhbRecord(String orderNo, Long buyerId, Long operaterId, BigDecimal amount, String type, Long goodsId, String goodsType) {
		ZhbRecord zhbRecord = new ZhbRecord();
		zhbRecord.setOrderNo(orderNo);
		zhbRecord.setBuyerId(buyerId);
		zhbRecord.setOperaterId(operaterId);
		zhbRecord.setAmount(amount);
		zhbRecord.setStatus("1");
		zhbRecord.setType(type);
		zhbRecord.setGoodsId(goodsId);
		zhbRecord.setGoodsType(goodsType);

		zhbMapper.insertZhbRecord(zhbRecord);
	}

	/**
	 * 进行充值操作
	 * 
	 * @param orderNo
	 * @param buyerId
	 * @param operaterId
	 * @param amount
	 * @param goodsType
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public int execPrepaid(String orderNo, Long buyerId, Long operaterId, BigDecimal amount, String goodsType, Long goodsId) throws Exception {
		int result = 0;
		ZhbAccount zhbAccount = zhbMapper.selectZhbAccount(buyerId);
		if (null != zhbAccount && !ZhbConstant.ZhbAccountStatus.ACTIVE.toString().equals(zhbAccount.getStatus())) {
			return result;
		}
		// 增加充值流水
		insertZhbRecord(orderNo, buyerId, operaterId, amount, ZhbRecordType.PREPAID.toString(), goodsId, goodsType);

		// 增加充值金额

		if (null != zhbAccount) {
			// 将充值金额更新到账户
			zhbAccount.setAmount(zhbAccount.getAmount().add(amount));
			result = zhbMapper.updateZhbAccountEmoney(zhbAccount);

			if (1 != result) {
				throw new BusinessException(MsgCodeConstant.ZHB_PERPAID_FAILED, "充值失败");
			}

		} else {
			// 初次充值账户为空，将充值金额添加到账户
			insertZhbAccount(buyerId, amount);
			result = 1;
		}

		return result >= 1 ? 1 : 0;
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
		zhbAccount.setStatus(ZhbAccountStatus.ACTIVE.toString());
		zhbAccount.setAmount(amount);
		Calendar cal = Calendar.getInstance();
		zhbAccount.setAddTime(cal.getTime());
		zhbAccount.setUpdateTime(cal.getTime());

		zhbMapper.insertZhbAccount(zhbAccount);
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

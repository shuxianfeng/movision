package com.zhuhuibao.mybatis.vip.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.constant.VipConstant.VipLevel;
import com.zhuhuibao.common.constant.VipConstant.VipPrivilegeType;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbAccountStatus;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbRecordType;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipRecord;
import com.zhuhuibao.mybatis.vip.mapper.VipInfoMapper;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;
import com.zhuhuibao.mybatis.zhb.mapper.ZhbMapper;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * VIP特权服务
 * 
 * @author tongxinglong
 *
 */
@Service
@Transactional
public class VipInfoService {
	private Logger log = LoggerFactory.getLogger(VipInfoService.class);
	@Autowired
	private VipInfoMapper vipInfoMapper;
	@Autowired
	private ZhbMapper zhbMapper;
	@Autowired
	private MemberService memberSV;
	
	/**
	 * 开通尊贵会员
	 * @param contract_id
	 * @param member_account
	 * @param vip_level
	 * @param active_time
	 * @param validity
	 * @return
	 * @throws Exception
	 */
	public int addVipService(String contract_id, String member_account, int vip_level, 
			String active_time, int validity) throws Exception{
		int result = 0;
		try{
			//校验该合同是否已经存在
			if(isNotExistsVipRecord(contract_id)){
				//VIP级别对应赠送筑慧币数量
				BigDecimal amount = new BigDecimal(VipConstant.VIP_LEVEL_ZHB.get(String.valueOf(vip_level)));
				//获取memberID
				Member member = new Member();
				if (member_account.contains("@")) {
		            member.setEmail(member_account);
		        } else {
		            member.setMobile(member_account);
		        }
				Member mem = memberSV.findMember(member);
				if(mem == null){
					throw new BusinessException(MsgCodeConstant.member_mcode_username_not_exist, "该盟友账号不存在");
				}
				String member_id_Str = mem.getId();
				if(StringUtils.isEmpty(member_id_Str)){
					throw new BusinessException(MsgCodeConstant.member_mcode_username_not_exist, "该盟友账号不存在");
				}
				Long member_id = Long.valueOf(mem.getId());
				//获取管理员账号
				Long createid = ShiroUtil.getOmsCreateID();
				if(createid == null) {
					throw new BusinessException(MsgCodeConstant.member_mcode_account_status_exception, "获取当前登录管理员失败");
				}
				// 订单中amount大于0
				// 筑慧币充值
				if (amount.compareTo(BigDecimal.ZERO) > 0 ) {
					// 进行筑慧币充值
					int prepaidResult = execPay(contract_id, member_id, createid, amount,vip_level, active_time, validity);
					if (0 == prepaidResult) {
						throw new BusinessException(MsgCodeConstant.ZHB_AUTOPAYFOR_FAILED, "充值失败");
					}
				}
				// VIP升级
				VipMemberInfo vipMemberInfo = findVipMemberInfoById(member_id);
				if (null == vipMemberInfo) {
					result = insertVipMemberInfo(member_id, vip_level, 1);
					insertVipRecord(contract_id, member_id, createid, amount, vip_level, active_time, validity);
					
				} else if (vipMemberInfo.getVipLevel() <= vip_level) {
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
					vipMemberInfo.setVipLevel(vip_level);
					insertVipRecord(contract_id, member_id, createid, amount, vip_level, active_time, validity);
					result = updateVipMemberInfo(vipMemberInfo);
				}
			}else{
				throw new BusinessException(MsgCodeConstant.EXIST_CONTRACTNO_WARN, "该合同编号已经操作过");
			}
				
		}catch(Exception e){
			log.error("ZhbService::addVipService::contract_id=" + contract_id + ",member_account=" + member_account + ",vip_level="
					+vip_level+",active_time="+active_time+",validity="+validity, e);
			throw e;
		}
		
		return result;
	}
	
	private int execPay(String contractId, Long buyerId, Long operaterId, BigDecimal amount,int vipLevel,
			 String activeTime, int validity) throws Exception {
		int result = 0;
		ZhbAccount zhbAccount = zhbMapper.selectZhbAccount(buyerId);
		if (null != zhbAccount && !ZhbConstant.ZhbAccountStatus.ACTIVE.toString().equals(zhbAccount.getStatus())) {
			return result;
		}
		// 增加ZHB流水记录
		insertZhbRecord(buyerId, operaterId, amount, ZhbRecordType.PREPAID.toString());
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
	
	private void insertZhbRecord(Long buyerId, Long operaterId, BigDecimal amount, String type) {
		ZhbRecord zhbRecord = new ZhbRecord();
		zhbRecord.setOrderNo("0");	//表示未走订单流程
		zhbRecord.setBuyerId(buyerId);
		zhbRecord.setOperaterId(operaterId);
		zhbRecord.setAmount(amount);
		zhbRecord.setStatus("1");	//支付成功
		zhbRecord.setType(type);	//充值

		zhbMapper.insertZhbRecord(zhbRecord);
	}
	/**
	 * 
	 * @param contractId
	 * @param buyerId
	 * @param operaterId
	 * @param amount
	 * @param activeTime
	 * @param validity
	 * @throws ParseException
	 */
	private void insertVipRecord(String contractId, Long buyerId, Long operaterId, BigDecimal amount, 
			int vipLevel, String activeTime, int validity) throws ParseException {
		VipRecord vipRecord = new VipRecord();
		vipRecord.setContractNo(contractId);
		vipRecord.setBuyerId(buyerId);
		vipRecord.setOperaterId(operaterId);
		vipRecord.setAmount(amount);
		vipRecord.setStatus("1");
		vipRecord.setVipLevel(vipLevel);
		//activeDate
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date activeDate=sdf.parse(activeTime);
		//expireDate
		Calendar   calendar   =   new   GregorianCalendar(); 
	    calendar.setTime(activeDate); 
	    calendar.add(calendar.YEAR, validity);//把日期往后增加一年.整数往后推,负数往前移动 
	    Date expireDate=calendar.getTime(); 
		
		vipRecord.setActiveTime(activeDate);
		vipRecord.setExpireTime(expireDate);
		
		Calendar cal = Calendar.getInstance();
		vipRecord.setAddTime(cal.getTime());
		vipRecord.setUpdateTime(cal.getTime());
		
		vipInfoMapper.insertVipRecord(vipRecord);
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
	
	private boolean isNotExistsVipRecord(String contractNo) {
		return null == getVipRecordByContractNo(contractNo);
	}
	
	private VipRecord getVipRecordByContractNo(String contractNo) {

		if (StringUtils.isNotBlank(contractNo) && !"0".equals(contractNo)) {
			return vipInfoMapper.selectVipRecordByContractNo(contractNo);
		}

		return null;
	}

	
	/**
	 * 根据ID获取会员VIP信息
	 * 
	 * @param memberId
	 * @return
	 */
	public VipMemberInfo findVipMemberInfoById(Long memberId) {
		return vipInfoMapper.selectVipMemberInfoById(memberId);
	}

	/**
	 * 根据会员级别获取所有会员特权list
	 * 
	 * @param vipLevel
	 * @return
	 */
	@Cacheable(value = "vipPrivilegeCache", key = "#vipLevel")
	public List<VipPrivilege> listVipPrivilegeByLevel(int vipLevel) {
		return vipInfoMapper.selectVipPrivilegeListByLevel(vipLevel);
	}

	/**
	 * 根据会员级别获取会员特权map
	 * 
	 * @param vipLevel
	 * @return
	 */
	public Map<String, VipPrivilege> findVipPrivilegeMap(int vipLevel) {
		Map<String, VipPrivilege> privilegeMap = new HashMap<String, VipPrivilege>();
		List<VipPrivilege> list = listVipPrivilegeByLevel(vipLevel);
		if (CollectionUtils.isNotEmpty(list)) {
			for (VipPrivilege p : list) {
				privilegeMap.put(p.getPinyin(), p);
			}
		}

		return privilegeMap;
	}

	// /**
	// * 判断当前VIP级别是否具有相应特权
	// *
	// * @param vipLevel
	// * @param privilegePinyin
	// * @return
	// */
	// public boolean vipHadPrivilege(int vipLevel, String privilegePinyin) {
	// boolean hadVipPrivilege = false;
	// Map<String, VipPrivilege> privilegeMap = getVipPrivilegeMap(vipLevel);
	// if (MapUtils.isNotEmpty(privilegeMap)) {
	// VipPrivilege privilege = privilegeMap.get(privilegePinyin);
	// if (null != privilege && (VipPrivilegeType.DISCOUNT ==
	// privilege.getType() || (VipPrivilegeType.EXIST == privilege.getType() &&
	// "1".equals(privilege.getValue())))) {
	// hadVipPrivilege = true;
	// }
	// }
	//
	// return hadVipPrivilege;
	// }

	/**
	 * 获取用户自定义特权信息
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public VipMemberPrivilege findVipMemberPrivilege(Long memberId, String privilegePinyin) {
		privilegePinyin = StringUtils.isNotBlank(privilegePinyin) ? privilegePinyin.toUpperCase() : "";
		Map<String, Object> param = MapUtil.convert2HashMap("memberId", memberId, "pinyin", privilegePinyin);
		return vipInfoMapper.selectVipMemberPrivilege(param);
	}

	/**
	 * 查询会员剩余额外特权数量
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public long getExtraPrivilegeNum(Long memberId, String privilegePinyin) {
		if (null != memberId && StringUtils.isNotBlank(privilegePinyin)) {
			VipMemberPrivilege extraPrivilege = findVipMemberPrivilege(memberId, privilegePinyin.toUpperCase());
			if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType())) {
				return extraPrivilege.getValue();
			}
		}

		return 0;
	}

	/**
	 * 存在自定义特权
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return
	 */
	public boolean hadExtraPrivilege(Long memberId, String privilegePinyin) {
		boolean hadExtraPrivilege = false;
		VipMemberPrivilege extraPrivilege = findVipMemberPrivilege(memberId, privilegePinyin);
		if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType()) && extraPrivilege.getValue() > 0) {
			hadExtraPrivilege = true;
		} else if (null != extraPrivilege && !VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType())) {
			hadExtraPrivilege = true;
		}

		return hadExtraPrivilege;
	}

	/**
	 * 使用自定义特权（数量类型）
	 * 
	 * @param memberId
	 * @param privilegePinyin
	 * @return 0:失败，1:成功
	 */
	public int useExtraPrivilege(Long memberId, String privilegePinyin) {
		int result = 0;
		Map<String, Object> param = MapUtil.convert2HashMap("memberId", memberId, "pinyin", privilegePinyin);
		VipMemberPrivilege extraPrivilege = vipInfoMapper.selectVipMemberPrivilege(param);
		if (null != extraPrivilege && VipConstant.VipPrivilegeType.NUM.toString().equals(extraPrivilege.getType()) && extraPrivilege.getValue() > 0) {
			extraPrivilege.setValue(extraPrivilege.getValue() - 1);
			extraPrivilege.setOldUpdateTime(extraPrivilege.getUpdateTime());
			result = vipInfoMapper.updateVipMemberPrivilegeValue(extraPrivilege);
		}

		return result;
	}

	/**
	 * 注册时初始化特权特权内容
	 * 
	 * @param memberId
	 * @param identify
	 */
	public void initDefaultExtraPrivilege(Long memberId, String identify) {
		int defaultPrivilegeLevel = StringUtils.contains(identify, "2") ? VipConstant.EXTRA_PRIVILEGE_LEVEL_PERSONAL
				: VipConstant.EXTRA_PRIVILEGE_LEVEL_ENTERPRISE;
		int freeLevel = StringUtils.contains(identify, "2") ? VipConstant.VipLevel.PERSON_FREE.value : VipLevel.ENTERPRISE_FREE.value;
		VipMemberInfo vipMemberInfo = vipInfoMapper.selectVipMemberInfoById(memberId);
		if (null == vipMemberInfo) {
			insertVipMemberInfo(memberId, freeLevel, 50);
		}

		List<VipMemberPrivilege> memberPrivilegeList = vipInfoMapper.selectVipMemberPrivilegeList(memberId);
		if (CollectionUtils.isEmpty(memberPrivilegeList)) {
			insertExtraPrivilege(memberId, defaultPrivilegeLevel);
		}
	}

	/**
	 * 自定义特权列表
	 * 
	 * @param memberId
	 * @return
	 */
	public List<VipMemberPrivilege> listVipMemberPrivilege(Long memberId) {
		return vipInfoMapper.selectVipMemberPrivilegeList(memberId);
	}

	/**
	 * 添加会员VIP信息
	 * 
	 * @param memberId
	 * @param vipLevel
	 * @param activeYears
	 *            生效年份
	 */
	public int insertVipMemberInfo(Long memberId, int vipLevel, int activeYears) {
		VipMemberInfo vipMemberInfo = new VipMemberInfo();
		vipMemberInfo.setMemberId(memberId);
		vipMemberInfo.setVipLevel(vipLevel);

		Calendar cal = Calendar.getInstance();
		vipMemberInfo.setActiveTime(cal.getTime());

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.YEAR, activeYears);
		cal.add(Calendar.DATE, 1);
		vipMemberInfo.setExpireTime(cal.getTime());
		try{
			vipInfoMapper.insertVipMemberInfo(vipMemberInfo);
		}catch(Exception e){
			return 0;
		}
		return 1;
	}

	/**
	 * 修改会员VIP信息
	 * 
	 * @param vipMemberInfo
	 */
	public int updateVipMemberInfo(VipMemberInfo vipMemberInfo) {
		return vipInfoMapper.updateVipMemberInfo(vipMemberInfo);
	}

	/**
	 * 添加对应级别的自定义特权
	 * 
	 * @param memberId
	 * @param vipLevel
	 */
	private void insertExtraPrivilege(Long memberId, int vipLevel) {
		List<VipPrivilege> privilegeList = listVipPrivilegeByLevel(vipLevel);
		if (CollectionUtils.isNotEmpty(privilegeList)) {
			for (VipPrivilege p : privilegeList) {
				if (VipConstant.VipPrivilegeType.NUM.toString().equals(p.getType())) {
					VipMemberPrivilege memberPrivilege = buildVipMemberPrivilege(memberId, p);
					vipInfoMapper.insertVipMemberPrivilege(memberPrivilege);
				}
			}
		}
	}

	/**
	 * 根据vipPrivilege初始化VipMemberPrivilege
	 * 
	 * @param memberId
	 * @param vipPrivilege
	 * @return
	 */
	private VipMemberPrivilege buildVipMemberPrivilege(Long memberId, VipPrivilege vipPrivilege) {
		VipMemberPrivilege memberPrivilege = new VipMemberPrivilege();

		memberPrivilege.setMemberId(memberId);
		memberPrivilege.setType(vipPrivilege.getType());
		memberPrivilege.setPinyin(vipPrivilege.getPinyin());
		memberPrivilege.setName(vipPrivilege.getName());
		memberPrivilege.setValue(vipPrivilege.getValue());
		Calendar cal = Calendar.getInstance();
		memberPrivilege.setAddTime(cal.getTime());
		memberPrivilege.setUpdateTime(cal.getTime());

		return memberPrivilege;
	}

	/**
	 * 运营管理系统-VIP会员
	 * 
	 * @param account
	 * @param name
	 * @param vipLevel
	 * @param status
	 * @param pager
	 * @return
	 */
	public List<Map<String, String>> listAllVipInfo(String account, String name, String vipLevel, String status, Paging<Map<String, String>> pager) {
		List<Map<String, String>> viplist = new ArrayList<Map<String, String>>();
		Map<String, String> param = MapUtil.convert2HashMap("account", account, "name", name, "vipLevel", vipLevel, "status", status);
		viplist = vipInfoMapper.findAllVipInfoList(pager.getRowBounds(), param);
		if (CollectionUtils.isNotEmpty(viplist)) {
			for (Map<String, String> vip : viplist) {
				String level = String.valueOf(vip.get("vip_level"));
				if (StringUtils.isNotBlank(level) && ArrayUtils.contains(VipConstant.CHARGE_VIP_LEVEL, level)) {
					vip.put("status", "1");
				} else {
					vip.put("status", "0");
				}
			}
		}

		return viplist;
	}

	/**
	 * 根据商品查询VIP信息
	 * @param goodsId
	 * @return
     */
	public List<Map<String, String>> findVipInfoByID(Long goodsId) {

		return vipInfoMapper.findVipInfoByID(goodsId);
	}
}

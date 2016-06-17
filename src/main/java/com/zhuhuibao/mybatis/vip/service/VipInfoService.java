package com.zhuhuibao.mybatis.vip.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.constant.VipConstant.VipPrivilegeType;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipPrivilege;
import com.zhuhuibao.mybatis.vip.mapper.VipInfoMapper;
import com.zhuhuibao.utils.MapUtil;

/**
 * VIP特权服务
 * 
 * @author tongxinglong
 *
 */
@Service
@Transactional
public class VipInfoService {

	@Autowired
	private VipInfoMapper vipInfoMapper;

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
	public List<VipPrivilege> listVipPrivilegeByLevel(int vipLevel) {
		return vipInfoMapper.selectVipPrivilegeListByLevel(vipLevel);
	}

	/**
	 * 根据会员级别获取会员特权map
	 * 
	 * @param vipLevel
	 * @return
	 */
	@Cacheable(value = "vipPrivilegeCache", key = "#vipLevel")
	public Map<String, VipPrivilege> getVipPrivilegeMap(int vipLevel) {
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
	public VipMemberPrivilege getVipMemberPrivilege(Long memberId, String privilegePinyin) {
		privilegePinyin = StringUtils.isNotBlank(privilegePinyin) ? privilegePinyin.toLowerCase() : "";
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
			VipMemberPrivilege extraPrivilege = getVipMemberPrivilege(memberId, privilegePinyin.toLowerCase());
			if (null != extraPrivilege && VipPrivilegeType.NUM == extraPrivilege.getType()) {
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
		VipMemberPrivilege extraPrivilege = getVipMemberPrivilege(memberId, privilegePinyin);

		if (null != extraPrivilege && VipPrivilegeType.NUM == extraPrivilege.getType() && extraPrivilege.getValue() > 0) {
			hadExtraPrivilege = true;
		} else if (null != extraPrivilege && VipPrivilegeType.NUM != extraPrivilege.getType()) {
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
		if (null != extraPrivilege && VipPrivilegeType.NUM == extraPrivilege.getType() && extraPrivilege.getValue() > 0) {
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

		List<VipMemberPrivilege> memberPrivilegeList = vipInfoMapper.selectVipMemberPrivilegeList(memberId);
		if (CollectionUtils.isNotEmpty(memberPrivilegeList)) {
			// 当会员自定义特权不为空时表示已存在，无需初始化
			return;
		}

		int defaultLevel = StringUtils.contains(identify, "2") ? VipConstant.EXTRA_PRIVILEGE_LEVEL_PERSONAL : VipConstant.EXTRA_PRIVILEGE_LEVEL_PERSONAL;
		List<VipPrivilege> privilegeList = listVipPrivilegeByLevel(defaultLevel);
		if (CollectionUtils.isNotEmpty(privilegeList)) {
			for (VipPrivilege p : privilegeList) {
				if (VipPrivilegeType.NUM == p.getType()) {
					VipMemberPrivilege memberPrivilege = initVipMemberPrivilege(memberId, p);
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
	private VipMemberPrivilege initVipMemberPrivilege(Long memberId, VipPrivilege vipPrivilege) {
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
}

package com.zhuhuibao.mybatis.vip.mapper;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.entity.VipPrivilege;

/**
 * 
 * @author tongxinglong
 *
 */
public interface VipInfoMapper {

	/**
	 * 根据ID获取会员VIP信息
	 * 
	 * @param memberId
	 * @return
	 */
	VipMemberInfo selectVipMemberInfoById(Long memberId);

	/**
	 * 根据会员级别获取所有会员特权
	 * 
	 * @param vipLevel
	 * @return
	 */
	List<VipPrivilege> selectVipPrivilegeListByLevel(Integer vipLevel);

	/**
	 * 查询会员特定自定义特权
	 * 
	 * @param param
	 *            memberId,pinyin
	 * @return
	 */
	VipMemberPrivilege selectVipMemberPrivilege(Map<String, Object> param);

	/**
	 * 根据memberID获取自定义特权
	 * 
	 * @param memberId
	 * @return
	 */
	List<VipMemberPrivilege> selectVipMemberPrivilegeList(Long memberId);

	/**
	 * 添加会员自定义特权信息
	 * 
	 * @param vipMemberPrivilege
	 */
	void insertVipMemberPrivilege(VipMemberPrivilege vipMemberPrivilege);

	/**
	 * 根据主键ID更新会员VIP自定义特权
	 * 
	 * @param vipMemberPrivilege
	 */
	void updateVipMemberPrivilegeById(VipMemberPrivilege vipMemberPrivilege);
}

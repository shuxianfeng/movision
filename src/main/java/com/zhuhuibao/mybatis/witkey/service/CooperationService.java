package com.zhuhuibao.mybatis.witkey.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.CooperationConstants;
import com.zhuhuibao.common.constant.MessageTextConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.entity.CooperationType;
import com.zhuhuibao.mybatis.witkey.mapper.CooperationMapper;
import com.zhuhuibao.mybatis.witkey.mapper.CooperationTypeMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/5/4 0004.
 */
@Service
@Transactional
public class CooperationService {
	private static final Logger log = LoggerFactory.getLogger(CooperationService.class);

	@Autowired
	private CooperationMapper cooperationMapper;

	@Autowired
	private CooperationTypeMapper cooperationTypeMapper;

	@Autowired
	ZhbService zhbService;

	@Autowired
	SiteMailService siteMailService;

	/**
	 * 发布任务
	 */
	public void publishCooperation(Cooperation cooperation) throws Exception {
		try {
			// 发布威客服务需要付费
			if (ArrayUtils.contains(CooperationConstants.COOPERATION_SERVICE, cooperation.getType())) {
				//发布威客服务
				doWitkeyActivity(cooperation, ZhbPaymentConstant.goodsType.FBWKFW.toString());
				
			} else if (ArrayUtils.contains(CooperationConstants.COOPERATION_QUALIFICATION, cooperation.getType())) {// 发布资质合作需要付费
				//发布资质合作
				doWitkeyActivity(cooperation, ZhbPaymentConstant.goodsType.FBZZHZ.toString());
			} else {
				
				cooperationMapper.publishCooperation(cooperation);
				
			}
		} catch (Exception e) {
			log.error("CooperationService::publishCooperation::"+"publisher="+ShiroUtil.getCreateID(),e);
			throw e;
		}
	}

	/**
	 * 发布威客的一些活动：威客服务，资质合作
	 * @param cooperation
	 * @param witkeyType
	 * @throws Exception
	 */
	private void doWitkeyActivity(Cooperation cooperation, String witkeyType) throws Exception {
		boolean bool = zhbService.canPayFor(witkeyType);
		if (bool) {
			cooperationMapper.publishCooperation(cooperation);
			zhbService.payForGoods(Long.parseLong(cooperation.getId()), witkeyType);
		} else {// 支付失败稍后重试，联系客服
			throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String
					.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
		}
	}

	/**
	 * 合作类型(大类，子类)
	 */
	public List cooperationType() {
		try {
			List<CooperationType> cooperationTypeList = cooperationTypeMapper.findCooperationType();
			List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
			List list1 = new ArrayList();
			for (int i = 0; i < cooperationTypeList.size(); i++) {
				CooperationType cooperationType = cooperationTypeList.get(i);
				Map map = new HashMap();
				map.put(Constants.code, cooperationType.getId());
				map.put(Constants.name, cooperationType.getName());
				List list = new ArrayList();
				for (int y = 0; y < subCooperationTypeList.size(); y++) {
					CooperationType subCooperation = subCooperationTypeList.get(y);
					if (cooperationType.getId().equals(subCooperation.getParentId())) {
						Map map1 = new HashMap();
						map1.put(Constants.code, subCooperation.getId());
						map1.put(Constants.name, subCooperation.getName());
						list.add(map1);
					}
				}
				map.put("subCooperationList", list);
				list1.add(map);
			}
			return list1;
		} catch (Exception e) {
			log.error("CooperationService::cooperationType",e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 合作类型(子类)
	 */
	public List subCooperationType() {
		try {
			List<CooperationType> subCooperationTypeList = cooperationTypeMapper.findSubCooperationType();
			List list = new ArrayList();
			for (int y = 0; y < subCooperationTypeList.size(); y++) {
				CooperationType subCooperation = subCooperationTypeList.get(y);
				Map map = new HashMap();
				map.put(Constants.code, subCooperation.getId());
				map.put(Constants.name, subCooperation.getName());
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			log.error("CooperationService::subCooperationType",e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 编辑任务
	 */
	public int updateCooperation(Cooperation cooperation) {
		int result = 0;
		try {
			result = cooperationMapper.updateCooperation(cooperation);
			if ("2".equals(cooperation.getStatus())) {
				
				siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(),
						Long.parseLong(cooperation.getCreateId()), cooperation.getReason(),
						MessageTextConstant.WITKEY, cooperation.getTitle());
			}
		} catch (Exception e) {
			log.error("CooperationService::updateCooperation",e);
			//e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 更新点击率
	 */
	public int updateCooperationViews(Cooperation cooperation) {
		try {
			return cooperationMapper.updateCooperationViews(cooperation);
		} catch (Exception e) {
			log.error("CooperationService::updateCooperationViews",e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 批量删除任务
	 */
	public void deleteCooperation(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				cooperationMapper.deleteCooperation(id);
			}
		} catch (Exception e) {
			log.error("CooperationService::deleteCooperation",e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 查询一条任务的信息
	 */
	public Map<String,Object> queryCooperationInfoById(String id) {
		try {
			Map<String,Object> cooperation = cooperationMapper.queryCooperationInfoById(id);
			return cooperation;
		} catch (Exception e) {
			log.error("CooperationService::queryCooperationInfoById::"+"witkeyId="+id,e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 未登录时查询威客任务信息
	 */
	public Map<String, Object> queryUnloginCooperationInfo(String id) {
		try {
			Map<String, Object> cooperation = cooperationMapper.queryUnloginCooperationInfo(id);
			return cooperation;
		} catch (Exception e) {
			log.error("CooperationService::queryUnloginCooperationInfo::"+"witkeyId="+id,e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 根据条件查询任务信息列表（分页）
	 */
	public List<Map<String, String>> findAllCooperationByPager(Paging<Map<String, String>> pager, Cooperation cooperation) {
		try {
			return cooperationMapper.findAllCooperationByPager(pager.getRowBounds(), cooperation);
		} catch (Exception e) {
			log.error("CooperationService::findAllCooperationByPager",e);
			//e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 最热合作信息
	 */
	public List<Map<String, String>> queryHotCooperation(Map<String, Object> map) {
		try {
			return cooperationMapper.queryHotCooperation(map);
		} catch (Exception e) {
			log.error("CooperationService::queryHotCooperation",e);
			//e.printStackTrace();
			throw e;
		}
	}

	public static void main(String[] args) {
		String str = "2";
		System.out.println(str.matches("[1,2,3,4,5]"));
	}

	public int queryMyWitkeyListSize(Map<String, Object> map) {
		try {
			return cooperationMapper.queryMyWitkeyListSize(map);
		} catch (Exception e) {
			log.error("CooperationService::queryMyWitkeyListSize",e);
			//e.printStackTrace();
			throw e;
		}
	}

	public List<Map<String,String>> findAllWitkeyTaskList(Paging<Map<String, String>> pager, Map<String, Object> map) {
		try {
			return cooperationMapper.findAllWitkeyTaskList(pager.getRowBounds(),map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public int deleteWitkeyTask(String id) {
		try {
			return cooperationMapper.deleteWitkeyTask(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public Map<String,Object> queryUnloginCooperationInfoById(String id) {
		try {
			return cooperationMapper.queryUnloginCooperationInfoById(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public List<Map<String,String>> findAllWitkeyByCompanyId(Paging<Map<String, String>> pager, Map<String, Object> map) {
		try {
			return cooperationMapper.findAllWitkeyByCompanyId(pager.getRowBounds(),map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
}

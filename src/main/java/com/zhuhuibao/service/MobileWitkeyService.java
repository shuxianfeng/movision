package com.zhuhuibao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;


@Service
@Transactional
public class MobileWitkeyService {

	@Autowired
	private CooperationService cooperationService;

	@Autowired
	private MemberService memberService;


	/**
	 * 查询我发布的任务
	 * @param pageNo
	 * @param pageSize
	 * @param title
	 * @param type
	 * @param status
	 * @return
	 */
	public Response findAllMyCooperationByPager(String pageNo, String pageSize,
			String title, String type, String status){

		Long createId = ShiroUtil.getCreateID();
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Response response = new Response();
		Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo),
				Integer.valueOf(pageSize));

		Cooperation cooperation = genCooperation(title, type, status, createId);
		if (createId != null) {
			List<Map<String, String>> cooperationList = cooperationService
					.findAllCooperationByPager(pager, cooperation);
			pager.result(cooperationList);
			response.setData(pager);
		} else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
					.valueOf(MsgCodeConstant.un_login)));
		}
		return response;
	}

	/**
	 * 生成合作对象
	 * @param title
	 * @param type
	 * @param status
	 * @param createId
	 * @return
	 */
	private Cooperation genCooperation(String title, String type,
			String status, Long createId) {
		Cooperation cooperation = new Cooperation();
		cooperation.setCreateId(String.valueOf(createId));
		cooperation.setType(type);
		cooperation.setTitle(title);
		cooperation.setStatus(status);
		return cooperation;
	}

	/**
	 * 发布任务,/发布威客服务，/发布威客资质合作
	 * @param cooperation
	 * @return
	 * @throws Exception
	 */
	public Response publishCooperation(Cooperation cooperation) throws Exception{
		Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            cooperation.setCreateId(String.valueOf(createId));
            cooperationService.publishCooperation(cooperation);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
	}

	/**
	 * 查询威客信息詳情
	 * @param id
	 * @return
	 */
	public Response cooperationInfo(String id){
		Response response = new Response();
        Map<String, Object> cooperation = cooperationService.queryCooperationInfoById(id);
        if (!"1".equals(cooperation.get("parentId").toString())) {
            Cooperation result = new Cooperation();
            result.setId(cooperation.get("id").toString());
            result.setViews(String.valueOf(Integer.parseInt(cooperation.get("views").toString()) + 1));
            cooperationService.updateCooperationViews(result);
            response.setData(cooperation);
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }
        return response;
	}

	/**
	 * 我查看的威客任务
	 * @param pageNo
	 * @param pageSize
	 * @param type
	 * @return
	 */
	public Response sel_witkey_task(String pageNo, String pageSize, String type){
        Response response = new Response();
        Paging<Map<String, String>> pager = getMapPaging(pageNo, pageSize);

		Map<String, Object> map = new HashMap<>();
		if(type == null || type.length() <= 0){
			map.put("type", null);
		}else{
			map.put("type", type);
		}
        Long createId = ShiroUtil.getCreateID();
		if (createId != null) {
			Member member = memberService.findMemById(String.valueOf(createId));
			if ("100".equals(member.getWorkType())) {
				map.put("companyId", createId);
			} else {
				map.put("viewerId", createId);
			}
			List<Map<String, String>> cooperationList = cooperationService.findAllWitkeyTaskList(pager, map);
			pager.result(cooperationList);
			response.setData(pager);
		} else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
					.valueOf(MsgCodeConstant.un_login)));
		}
		return response;
	}

    /**
     * 获取分页map
     * @param pageNo
     * @param pageSize
     * @return
     */
    private Paging<Map<String, String>> getMapPaging(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        return new Paging<Map<String, String>>(Integer.valueOf(pageNo),
                Integer.valueOf(pageSize));
    }

    /**
	 * 批量删除我查看的威客任务
	 * @param ids
	 * @return
	 */
	public Response del_witkey_task(String ids){
		Response response = new Response();
		String idlist[] = ids.split(",");
		for (String id : idlist) {
			cooperationService.deleteWitkeyTask(id);
		}
		return response;
	}

	/**
	 * 发布任务时，自动获取发布者的联系方式
	 * @return
	 */
	public Response sel_connection(){
		Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        Map map = new HashMap();
        if (createId != null) {
            Member member = memberService.findMemById(String.valueOf(createId));
            map.put("telephone", member.getFixedTelephone());
            map.put("mobile", member.getFixedMobile());
            map.put("email", member.getEmail());
            if ("2".equals(member.getIdentify())) {
                map.put("companyName", "");
                map.put("name", member.getNickname());
            } else {
                map.put("companyName", member.getEnterpriseName());
                map.put("name", member.getEnterpriseLinkman());
            }
            response.setData(map);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
	}

    /**
     * 查询任务列表（前台分页）
     * @param pageNo
     * @param pageSize
     * @param type
     * @param category
     * @param systemType
     * @param province
     * @param smart
     * @param parentId
     * @return
     */
    public Paging<Map<String, String>> getPager(String pageNo, String pageSize, String type, String category,
                                                String systemType, String province, String smart, String parentId)
    {
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Cooperation cooperation = getCooperation(type, category, systemType, province, smart, parentId);

        List<Map<String, String>> cooperationList = cooperationService.findAllCooperationByPager(pager, cooperation);
        pager.result(cooperationList);
        return pager;
    }

    /**
     * 获取合作实体bean
     * @param type
     * @param category
     * @param systemType
     * @param province
     * @param smart
     * @param parentId
     * @return
     */
    private Cooperation getCooperation(String type, String category, String systemType, String province, String smart, String parentId) {
        Cooperation cooperation = new Cooperation();
        cooperation.setSmart(smart);
        cooperation.setType(type);
        //区分前台跟后台
        cooperation.setDistinction("1");
        cooperation.setCategory(category);
        cooperation.setProvince(province);
        cooperation.setParentId(parentId);
        cooperation.setSystemType(systemType);
        return cooperation;
    }


}

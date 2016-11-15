package com.zhuhuibao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.mybatis.tech.service.TechDataService;
import com.zhuhuibao.mybatis.tech.service.TechDownloadDataService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

@Service
@Transactional
public class MobileTechService {
	
	@Autowired
    TechDataService techDataService;
	
	@Autowired
    TechCooperationService techService;
	
	@Autowired
    TechDownloadDataService downloadService;
	
	
	/**
	 * 搜索技术资料
	 * @param fCategory
	 * @param title
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Response findAllTechDataPager(
			String fCategory,
			String title,
			String status,
			String pageNo,
			String pageSize
			){
		
		Response response = new Response();
        Map<String, Object> condition = new HashMap<>();
        condition.put("fCategory", fCategory);

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (title != null && !"".equals(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        condition.put("status", status);

        //登录用户
        Long createid = ShiroUtil.getCreateID();

        condition.put("createid", createid);

        List<Map<String, String>> techList = techDataService.findAllOMSTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
	}
	
	/**
	 * 查询我的技术合作列表
	 * @param title
	 * @param type
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Response findAllTechCooperationPager(String title, String type, String status, String pageNo, String pageSize){
		
		Response response = new Response();
        Map<String, Object> condition = new HashMap<>();
        Long createID = ShiroUtil.getCreateID();

        condition.put("createID", createID);
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (StringUtil.isNotEmpty(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        if (StringUtil.isNotEmpty(type)) {
            condition.put("type", type);
        }
        if (StringUtil.isNotEmpty(status)) {
            condition.put("status", status);
        }

        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
	}
	
	/**
	 * 资料下载记录列表
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Response findAllDownloadTechDataPager(String pageNo, String pageSize){
		Response response = new Response();
        Map<String, String> condition = new HashMap<String, String>();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            condition.put("downloaderId", String.valueOf(createId));
            if (StringUtils.isEmpty(pageNo)) {
                pageNo = "1";
            }
            if (StringUtils.isEmpty(pageSize)) {
                pageSize = "10";
            }
            Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            List<Map<String, Object>> techList = downloadService.findAllDownloadData(pager, condition);
            pager.result(techList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
	}
	
	/**
	 * 我查看的技术成果列表
	 * @param systemCategory
	 * @param applicationArea
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Response findSiteAllTechCooperationPager(String systemCategory, String applicationArea, 
			String type, String pageNo, String pageSize){
		
		Response response = new Response();
        Map<String, Object> condition = new HashMap<>();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("type", type);
        condition.put("systemCategory", systemCategory);
        condition.put("applicationArea", applicationArea);
        condition.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
	}
	
	/**
	 * 我查看的技术成果详情
	 * @param techCoopId
	 * @return
	 */
	public Response previewSiteTechCooperation(String techCoopId){
		
		Map<String, Object> map = new HashMap<>();
        map.put("id", techCoopId);
        map.put("type", 2);
        Map<String, Object> techCoop = techService.previewTechCooperationDetail(map);
        techService.updateTechCooperationViews(techCoopId);
        Response response = new Response();
        response.setData(techCoop);
        return response;
	}
}

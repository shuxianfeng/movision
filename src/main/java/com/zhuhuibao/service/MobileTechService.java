package com.zhuhuibao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.*;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.utils.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.fsearch.utils.StringUtil;
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

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    PublishTCourseService ptCourseService;

    @Autowired
    private ExpertService expertService;

    @Autowired
    TechExpertCourseService techCourseService;

    @Autowired
    private MobileSysAdvertisingService advertisingService;

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


    /**
     * 批量删除我的技术合作
     * @param idStr
     */
    public void batchDeleteTechCoop(String idStr)
    {
        String ids[] = idStr.split(",");
        if(null != ids && ids.length>0 ){
            Map<String, Object> condition = new HashMap<>();
            condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
            int len = ids.length;
            for (int i = 0; i < len; i++) {
                condition.put("id", ids[i]);
                int result = techService.deleteTechCooperation(condition);
                if (result != 1) {
                    throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "删除失败");
                }
            }
        }else{
            throw new BusinessException(MsgCodeConstant.TECH_COOP_IS_EMPTY, "技术合作id的数组为空");
        }
    }

    /**
     * 获取技术合作：技术成果 1，技术需求 2 数据
     * @param type 技术成果 1，技术需求 2
     * @return
     */
    public List<Map<String, String>> getTechCoop(Integer type, Integer count){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        map.put("type", type);
        map.put("count", count);
        return techService.findIndexTechCooperation(map);
    }

    /**
     * 查询新技术 数据
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, Object>> getNewTechList(String pageNo, String pageSize){
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "5";
        }
        Paging<Map<String, Object>> pager = new Paging<Map<String, Object>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        Map<String, Object> map = new HashMap<>();
        map.put("channelid", 11);
        map.put("sort", 1);
        map.put("status", 1);
        List<Map<String, Object>> channelList = newsService.findAllTechNewsList(pager, map);
        pager.result(channelList);

        return pager;
    }


    /**
     * 获取手机端-技术主页-新技术数据
     * @param num 获取的数量
     * @return
     */
    public List<Map<String, Object>> getNewTechList4Mobile(Integer num){

        Map<String, Object> map = new HashMap<>();
        map.put("channelid", 11);
        map.put("sort", 1);
        map.put("status", 1);
        map.put("num", num);

        return newsService.findAllTechNewsList4Mobile(map);
    }


    /**
     * 查询新技术详情
     * @param newsId
     * @return
     */
    public Map<String, Object> getNewTechDetail(String newsId){
        Map<String, Object> map = new HashMap<>();
        map.put("id", newsId);
        newsService.updateViews(Long.valueOf(newsId));

        return newsService.previewNewsInfo(map);
    }

    /**
     * 获取技术资料列表数据
     * @param fcateId
     * @param scateId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, String>> getTechDataList(
            String fcateId, String scateId, String pageNo, String pageSize
    ){
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("fCategory", fcateId);
        if (StringUtils.isEmpty(scateId)) {
            conditionMap.put("sCategory", scateId);
        }
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> techList = techDataService.findAllTechDataOnlyPager(pager, conditionMap);
         pager.result(techList);
        return pager;
    }

    /**
     * 查询技术资料详情
     * @param techDataId
     * @return
     */
    public List<Map<String, String>> getTechDataDetail(String techDataId){

        //更新热度
        Map<String, Object> map = new HashMap<>();
        map.put("views", "views");
        map.put("id", techDataId);
        techDataService.updateTechDataViewsOrDL(map);

        return techDataService.previewTechDataDetail(Long.parseLong(techDataId));
    }

    /**
     * 获取培训课程列表
     * @return
     */
    public List<Map<String, String>> getTechCourseList(Integer count){
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
        condition.put("courseType", TechConstant.COURSE_TYPE_TECH);
        condition.put("count", count);
        return ptCourseService.findLatestPublishCourse(condition);
    }


    public Paging<Map<String, String>> getTechCoursePageList(String pageNo, String pageSize,String province){
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
        condition.put("courseType", TechConstant.COURSE_TYPE_TECH);
        condition.put("province", province);    //地区筛选

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> list = ptCourseService.findAllPublishCoursePager(pager,condition);
        pager.result(list);
        return pager;
    }









    /**
     * 获取培训课程详情
     * @param courseId
     * @return
     */
    public Map<String, String> getTechCourseDetail(Long courseId){
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("courseid", courseId);
        return ptCourseService.previewTrainCourseDetail(condition);
    }

    /**
     * 开课申请
     * @param techCourse
     */
    public void addCourse(TechExpertCourse techCourse){
        expertService.checkMobileCode(techCourse.getCode(),techCourse.getMobile(), TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        Long createId = ShiroUtil.getCreateID();
        if(createId != null) {
            techCourse.setProposerId(createId);
            techCourseService.insertTechExpertCourse(techCourse);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    /**
     * 获取手机端技术&培训-首页-信息
     * @return
     */
    public Map<String,Object> getTechIndexInfo(){

        Map<String, Object> result = new HashMap<>();
        result.put("jscglist", getTechCoop(1, 2));  //技术成果
        result.put("jsxqlist", getTechCoop(2, 2));  //技术需求
        result.put("pxkclist", getTechCourseList(3));   //培训课程
        result.put("yzzllist", techDataService.findAllTechData4Mobile(3));  //优质资料
        result.put("xjslist",getNewTechList4Mobile(3)); //新技术
        //首页广告banner
        result.put("bannerAdvList",advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Tech_Banner.value) );
        return result;
    }



}

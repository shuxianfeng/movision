package com.zhuhuibao.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 人才网业务层
 * <p/>
 * Created by Administrator on 2016/11/24 0024.
 */


@Service
@Transactional
public class MobileJobAndResumeService {

    private static final Logger log = LoggerFactory.getLogger(MobileJobAndResumeService.class);
    @Autowired
    private SysAdvertisingService advService;

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    JobPositionService jobPositionService;

    @Autowired
    JobPositionService job;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    ChannelNewsService channelNewsService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    ResumeService resume;

    @Autowired
    private MobileSysAdvertisingService advertisingService;

    /**
     * 公司详情
     *
     * @param id
     * @return
     */
    public MemberDetails queryCompanyInfo(long id) {
        return jobMapper.queryCompanyInfo(id);
    }

    /**
     * 职位详情
     *
     * @param id
     * @return
     */
    public Job getPositionByPositionId(String id) {
        return jobPositionService.getPositionByPositionId(id);
    }


    /**
     * 资讯详情
     *
     * @param aLong
     * @return
     */
    public ChannelNews selectByID(Long aLong) {
        return channelNewsService.selectByID(aLong);
    }

    /**
     * 公司招聘的其他职位
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> queryJobByCompany(String id) {
        return jobMapper.findAllOtherPositionById(id);
    }


    /**
     * 收藏简历
     *
     * @param id
     * @return
     */
    public Response selCollectionResume(String id) {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            int collCount = resumeService.getMaxCollCount(memberId);
            if (collCount >= JobConstant.MAX_COLL_COUNT) {
                response.setCode(400);
                response.setMessage("您的简历收藏夹已满" + JobConstant.MAX_COLL_COUNT + "，请先清空收藏夹，然后再进行简历收藏！");
                return response;
            }
            int result = resumeService.insertCollRecord(id);
            if (result > 0) {
                response.setCode(200);
            } else {
                response.setCode(400);
            }

        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

    /**
     * 获取职位搜索Pager
     * @param name
     * @param province
     * @param city
     * @param area
     * @param employeeNumber
     * @param enterpriseType
     * @param days
     * @param salary
     * @param positionType
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String,Object>>  getJobSearchResultPager(String name,String province,String city,String area, String employeeNumber,
                                    String enterpriseType,String days,String salary,String positionType,String pageNo,
                                    String pageSize){
        log.info("query position info by id");
        Map<String, Object> map = getStringObjectMap(name, city, employeeNumber, enterpriseType, days, salary, positionType);

        Paging<Map<String,Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        pager.result(job.findAllOtherPosition(pager, map));
        return pager;
    }

    /**
     * 职位搜索-准备参数
     * @param name
     * @param city
     * @param employeeNumber
     * @param enterpriseType
     * @param days
     * @param salary
     * @param positionType
     * @return
     */
    private Map<String, Object> getStringObjectMap(String name, String city, String employeeNumber, String enterpriseType, String days, String salary, String positionType) {
        Map<String, Object> map = new HashMap<>();
        if (name != null && !"".equals(name)) {
            map.put("name", name.replace("_", "\\_"));
        }
        map.put("positionType", positionType);
        map.put("city", city);
        map.put("employeeNumber", employeeNumber);
        map.put("enterpriseType", enterpriseType);
        //发布时间一周内，一天内
        if (days != null && !"".equals(days)) {
            Date date = DateUtils.date2Sub(new Date(), 5, -Integer.parseInt(days));
            String publishTime = DateUtils.date2Str(date, "yyyy-MM-dd");
            map.put("publishTime", publishTime);
        }
        map.put("salary", salary);
        return map;
    }

    /**
     * 获取简历搜索pager
     * @param title
     * @param jobCity
     * @param expYearBefore
     * @param expYearBehind
     * @param education
     * @param positionType
     * @param isPublic
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, Object>> getResumePager(String title,String jobCity,String expYearBefore,String expYearBehind,
                                                     String education, String positionType,String isPublic, String pageNo,
                                                     String pageSize){
        log.info("find all resume!!");
        Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        Map<String, Object> map = getResumeParamsMap(title, jobCity, expYearBefore, expYearBehind, education, positionType, isPublic);

        return resume.findAllResume(pager, map);
    }


    /**
     * 手机端-人才-首页-获取最新简历
     * @param title
     * @param jobCity
     * @param expYearBefore
     * @param expYearBehind
     * @param education
     * @param positionType
     * @param isPublic
     * @return
     */
    public List<Map<String, Object>>  getMLatestResume(String title,String jobCity,String expYearBefore,String expYearBehind,
                                                      String education, String positionType,String isPublic){
        log.info("find all resume!!");
        Map<String, Object> map = getResumeParamsMap(title, jobCity, expYearBefore, expYearBehind, education, positionType, isPublic);

        return resume.findMIndexResume(map);
    }

    /**
     * 获取简历搜索准备参数map
     * @param title
     * @param jobCity
     * @param expYearBefore
     * @param expYearBehind
     * @param education
     * @param positionType
     * @param isPublic
     * @return
     */
    private Map<String, Object> getResumeParamsMap(String title, String jobCity, String expYearBefore, String expYearBehind, String education, String positionType, String isPublic) {
        Map<String, Object> map = new HashMap<>();
        if (title != null && !"".equals(title)) {
            map.put("title", title.replace("_", "\\_"));
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            //简历屏蔽
            map.put("company_id", ShiroUtil.getCreateID());
        }
        map.put("jobCity", jobCity);
        map.put("expYearBefore", expYearBefore);
        map.put("expYearBehind", expYearBehind);
        map.put("education", education);
        if (isPublic == null) {
            map.put("isPublic", "1");	//默认公开
        } else {
            if (!"2".equals(isPublic)) {
                map.put("isPublic", isPublic);
            }
        }
        map.put("status", JobConstant.JOB_MEMBER_STATUS_LOGOUT);
        if (positionType != null && positionType.length() > 0) {
            String[] positionTypes = positionType.split(",");
            List<String> positionList = Arrays.asList(positionTypes);
            map.put("positionList", positionList);
        }
        return map;
    }

    /**
     * 职场资讯列表
     * @param type
     * @param count
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, String>> getJobNewsList( String type,String count, String pageNo,String pageSize){
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> params = getJobNewsListMap(type, count);
        List<Map<String, String>> list = newsService.findJobNews4Mobile(pager, params);
        pager.result(list);
        return pager;
    }

    /**
     * 手机端-人才-首页-获取筑慧资讯
     * @return
     */
    public List<Map<String, String>> getJobNews(){
        return newsService.findIndexNews(getJobNewsListMap(null, "4"));
    }


    /**
     * 获取职位资讯列表的准备参数
     * @param type
     * @param count
     * @return
     */
    private Map<String, Object> getJobNewsListMap(String type, String count) {
        Map<String, Object> params = new HashMap<>();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(type)){
            params.put("sort",getSortByType(type));
        }
        params.put("channelid", "13");     //人才网
        params.put("status", "1");   //已审核
        if (!StringUtils.isEmpty(count)) {
            params.put("count", Integer.valueOf(count));
        }
        return params;
    }

    /**
     * 根据类型排序
     * @param type
     * @return
     */
    public String getSortByType(String type) {
        String sort = "";
        switch (type) {
            case "14":
                sort = "1";    //面试技巧
                break;
            case "15":
                sort = "2";     //职场动态
                break;
            case "16":
                sort = "3";   //行业资讯
                break;
        }
        return sort;
    }

    /**
     * 获取最热招聘广告
     * @return
     */
    public List<Map<String, Object>> getHotZpAdv(){
        String[] arr = AdvertisingConstant.AdvertisingPosition.M_Rencai_Banner.value;
        List<SysAdvertising> advertisings = advService.findHottestPosition(arr[0], arr[1], arr[2]);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysAdvertising item : advertisings) {
            String jobID = item.getConnectedId();
            Map<String, Object> map = job.findJobByID(jobID);
            map.put("logo",item.getImgUrl());
            list.add(map);
        }
        return list;
    }
    /**
     * 获取人才网首页数据
     * @return
     */
    public Map getIndexInfo() throws Exception{
        Map result = new HashMap();
        //banner广告
        result.put("banner_advs", advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Rencai_Banner.value));
        //热门招聘
        result.put("rmzp_advs",getHotZpAdv());
        //名企招聘（广告）
        result.put("mqzp_advs",advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Rencai_Banner.value));
        //最新招聘
        result.put("zxzp",job.searchNewPosition(3));
        //最新求职
        result.put("zxqz",getMLatestResume(null,null,null,null,null,null,null));
        //筑慧职场
        result.put("zhzc",getJobNews());

        return result;
    }

    public String getSortByType(String type, String sort) {
        switch (type) {
            case "14":
                sort = "1";    //面试技巧
                break;
            case "15":
                sort = "2";     //职场动态
                break;
            case "16":
                sort = "3";   //行业资讯
                break;
        }
        return sort;
    }


    public List<SysAdvertising> getHuntingCompany() {
        return advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Rencai_Hunting.value);
    }
}

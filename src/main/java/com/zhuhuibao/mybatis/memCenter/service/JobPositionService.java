package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.entity.Position;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.PositionMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.SalaryUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by cxx on 2016/4/18 0018.
 */
@Service
@Transactional
public class JobPositionService {
    private static final Logger log = LoggerFactory.getLogger(JobPositionService.class);

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    ZhbService zhbService;

    @Autowired
    private SysAdvertisingService advService;

    @Autowired
    DictionaryService dictionaryService;


    /**
     * 发布职位
     */
    public void publishPosition(Job job) throws Exception {
        try {
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.FBZW.toString());
            if (bool) {
                jobMapper.publishPosition(job);
                zhbService.payForGoods(Long.parseLong(job.getId()), ZhbPaymentConstant.goodsType.FBZW.toString());
            } else {// 支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询公司已发布的职位
     */
    public List<Map<String, Object>> findAllPositionByMemId(Paging<Map<String, Object>> pager, String id) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<Map<String, Object>> jobList = jobMapper.findAllByPager(pager.getRowBounds(), id);

            for (Map<String, Object> map : jobList) {
                Map<String, Object> tmpMap = genPosiMap("id", map.get("id"), Constants.position, map.get("name"), Constants.salary, map.get("salaryName"), Constants.area, map.get("workArea"),
                        "companyId", map.get("createID"), "positionType", map.get("positionType"), Constants.publishTime, map.get("publishTime"), Constants.updateTime, map.get("updateTime"));
                list.add(tmpMap);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }
        return list;
    }

    private Map<String, Object> genPosiMap(String id, Object id2, String position, Object name, String salary, Object salaryName, String area, Object workArea, String companyId, Object createID,
                                           String positionType, Object positionType2, String publishTime, Object publishTime2, String updateTime, Object updateTime2) {
        Map<String, Object> tmpMap = new HashMap<>();
        tmpMap.put(id, id2);
        tmpMap.put(position, name);
        tmpMap.put(salary, salaryName);
        tmpMap.put(area, workArea);
        tmpMap.put(companyId, createID);
        tmpMap.put(positionType, positionType2);
        tmpMap.put(publishTime, publishTime2);
        tmpMap.put(updateTime, updateTime2);
        return tmpMap;
    }

    /**
     * 查询公司发布的某条职位的信息
     */
    public Job getPositionByPositionId(String id) {
        try {
            return jobMapper.getPositionByPositionId(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 删除已发布的职位
     */
    public Response deletePosition(String ids) {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            try {
                jobMapper.deletePosition(id);
            } catch (Exception e) {
                log.error("执行异常>>>", e);
                throw e;
            }
        }
        return response;
    }

    /**
     * 刷新已发布的职位
     */
    public Response refreshPosition(String ids) {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            try {
                Job job = new Job();
                job.setId(id);
                jobMapper.updatePosition(job);
            } catch (Exception e) {
                log.error("执行异常>>>", e);
                throw e;
            }
        }
        return response;
    }

    /**
     * 更新编辑已发布的职位
     */
    public Response updatePosition(Job job) {
        Response result = new Response();
        try {
            jobMapper.updatePosition(job);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return result;
    }

    /**
     * 查询最新招聘职位
     */
    public List<Map<String, Object>> searchNewPosition(int count) {
        List<Map<String, Object>> jobList;
        try {
            jobList = jobMapper.searchNewPosition(count);
            for (Map<String, Object> job : jobList) {
                handleSalary(job);
                handleCity(job);
                if (job.get("education") != null) {
                    job = ConvertUtil.execute(job, "education", "constantService", "findByTypeCode", new Object[]{"2", String.valueOf(job.get("education"))});
                    job.put("educationName", job.get("educationName"));
                } else {
                    job.put("educationName", "");
                }
            }
        } catch (Exception e) {
            log.error("查询异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败'");
        }

        return jobList;
    }

    private void handleSalary(Map<String, Object> job) {
        job = ConvertUtil.execute(job, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(job.get("salary"))});
        job.put("salary", job.get("salaryName"));
        job.put("m_salary", SalaryUtil.convertSalary((String) job.get("salaryName")));
    }

    /**
     * 查询推荐职位
     */
    public Response searchRecommendPosition(String id, int count) {
        Response result = new Response();
        List<Job> jobList = jobMapper.searchRecommendPosition(id, count);
        result.setData(jobList);
        result.setCode(200);
        return result;
    }

    /**
     * 查询不同公司发布的相同职位
     */
    public Response searchSamePosition(Map<String, Object> map) {
        log.info("search same position form different company postID = " + StringUtils.mapToString(map));
        Response result = new Response();
        List<Job> jobList = jobMapper.searchSamePosition(map);
        result.setData(jobList);
        result.setCode(200);
        return result;
    }

    /**
     * 查询最新发布的职位
     */
    public Response searchLatestPublishPosition() {
        List<Job> jobList = jobMapper.searchLatestPublishPosition();
        return new Response(jobList);
    }

    /**
     * 职位类别
     */
    public List<Map<String, Object>> getPositionTypes() {
        List<Map<String, Object>> retList = new ArrayList<>();

        List<Position> positionList = dictionaryService.findParentTypes(7);

        for (Position position : positionList) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.code, position.getId());
            map.put(Constants.name, position.getName());

            List<Position> subpositionList = dictionaryService.findPositionByParentId(String.valueOf(position.getId()));
            List<Map<String, Object>> subList = new ArrayList<>();
            List<Map<String, String>> advList = new ArrayList<>();

            for (Position sub : subpositionList) {
                Map<String, Object> subMap = new HashMap<>();
                subMap.put(Constants.code, sub.getId());
                subMap.put(Constants.name, sub.getName());
                subMap.put(Constants.hot, sub.getHot());
                subList.add(subMap);
            }

            map.put(Constants.subPositionList, subList);

            // 广告项
            String chanType = Constants.AdvChannType.JOB.toString(); // 招聘频道
            String page = "index";// 首页
            String advArea = "A" + position.getId();
            List<SysAdvertising> advertisings = advService.findListByCondition(chanType, page, advArea);
            for (SysAdvertising adv : advertisings) {
                Map<String, String> tmpMap = new HashMap<>();
                tmpMap.put("title", adv.getTitle());
                tmpMap.put("imgUrl", adv.getImgUrl());
                tmpMap.put("linkUrl", adv.getLinkUrl());
                tmpMap.put("id", adv.getConnectedId());
                advList.add(tmpMap);
            }
            map.put("advList", advList);

            retList.add(map);
        }
        return retList;
    }

    /**
     * 查询发布职位公司信息
     *
     * @param id
     * @return
     */
    public Response queryCompanyInfo(Long id) throws Exception {
        MemberDetails member;
        try {
            member = jobMapper.queryCompanyInfo(id);
        } catch (Exception e) {
            log.error("查询异常>>>", e);
            throw e;
        }
        return new Response(member);
    }

    /**
     * 查询企业发布的职位详情
     *
     * @param map 职位搜索
     * @return
     */
    public Map<String, Object> queryPositionInfoByID(Map<String, Object> map) {
        log.info("query position info by id = " + StringUtils.mapToString(map));
        Map<String, Object> job;

        job = jobMapper.queryPositionInfoByID(map);
        String id = String.valueOf(job.get("id"));
        if (!StringUtils.isEmpty(id)) {
            try {
                Long isApply = (Long) job.get("isApply");
                job.put("isApply", (isApply != 0));

                String workArea = "";
                String provinceName = String.valueOf(job.get("provinceName"));
                if (!StringUtils.isEmpty(provinceName)) {
                    workArea += provinceName;
                }
                String cityName = String.valueOf(job.get("cityName"));
                if (!StringUtils.isEmpty(cityName)) {
                    workArea += " " + cityName;
                }
                String areaName = String.valueOf(job.get("areaName"));
                if (!StringUtils.isEmpty(areaName)) {
                    workArea += " " + areaName;
                }
                job.put("workArea", workArea);
            } catch (Exception e) {
                log.error("执行异常>>>", e);
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
            }
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }

        return job;
    }

    /**
     * 查询企业发布的其它求职位
     *
     * @param map 查询条件
     * @return
     */
    public List<Map<String, Object>> findAllOtherPosition(Paging<Map<String, Object>> pager, Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            log.info("map:"+map.toString());
            List<Map<String, Object>> jobList = jobMapper.findAllPositionForMobile(pager.getRowBounds(), map);
            for (Map<String, Object> job : jobList) {
                Map<String, Object> result = job;

                handleWelfare2(job, result);

                job = handleSalary(job, result);

                job = addWorlArea(job, result);

                handleOtherJobInfo(job, result);

                list.add(result);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return list;
    }

    private void handleWelfare2(Map<String, Object> job, Map<String, Object> result) {
        String welfare = (String) job.get("welfare");
        String welfarename = "";
        genWelfaceName(result, welfare, welfarename);
    }

    private void handleOtherJobInfo(Map<String, Object> job, Map<String, Object> result) {
        job = ConvertUtil.execute(job, "education", "constantService", "findByTypeCode", new Object[]{"2", String.valueOf(job.get("education"))});
        result.put("educationName", job.get("educationName"));
        job = ConvertUtil.execute(job, "experience", "constantService", "findByTypeCode", new Object[]{"3", String.valueOf(job.get("experience"))});
        result.put("experienceName", job.get("experienceName"));
        result.put("id", job.get("id"));
        result.put("createid", job.get("createid"));
        result.put("name", job.get("name"));
        result.put("positionType", job.get("positionType"));
        result.put("publishTime", job.get("publishTime"));
        result.put("updateTime", job.get("updateTime"));
        result.put("enterpriseName", job.get("enterpriseName"));
    }

    private Map<String, Object> handleSalary(Map<String, Object> job, Map<String, Object> result) {
        job = ConvertUtil.execute(job, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(job.get("salary"))});
        result.put("salaryName", job.get("salaryName"));
        result.put("m_salary", SalaryUtil.convertSalary((String) job.get("salaryName")));
        return job;
    }

    private Map<String, Object> addWorlArea(Map<String, Object> job, Map<String, Object> result) {
        String cityCode = (String) job.get("city");
        if (!StringUtils.isEmpty(cityCode)) {
            job = ConvertUtil.execute(job, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
            result.put("workArea", job.get("cityName"));
        } else {
            String provinceCode = (String) job.get("province");
            if (!StringUtils.isEmpty(provinceCode)) {
                job = ConvertUtil.execute(job, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
                result.put("workArea", job.get("provinceName"));
            } else {
                result.put("workArea", "");
            }
        }
        return job;
    }

    /**
     * 我申请的职位
     */
    public Response myApplyPosition(Paging<Job> pager, String id) {
        Response response = new Response();
        List<Map<String, Object>> jobList = jobMapper.findAllMyApplyPosition(pager.getRowBounds(), id);
        List list = new ArrayList();
        for (Map<String, Object> result : jobList) {
            Map map = new HashMap();
            map.put(Constants.id, result.get("id"));
            map.put(Constants.name, result.get("name"));
            map.put(Constants.companyName, result.get("enterpriseName"));
            map.put(Constants.salary, result.get("salaryName"));
            map.put(Constants.publishTime, result.get("publishTime"));
            map.put(Constants.area, result.get("workArea"));
            map.put(Constants.welfare, result.get("welfare"));
            map.put("is_deleted", result.get("is_deleted"));
            map.put("companyId", result.get("companyId"));
            map.put("positionType", result.get("positionType"));
            list.add(map);
        }
        pager.result(list);
        response.setCode(200);
        response.setData(pager);
        return response;
    }

    /**
     * 人才网首页热门招聘
     *
     * @param condition 查询的条件
     * @return
     * @throws Exception
     */
    public List queryHotPosition(Map<String, Object> condition) throws Exception {
        List list = new ArrayList();
        try {
            List<Job> jobList = jobMapper.queryHotPosition(condition);
            for (int i = 0; i < jobList.size(); i++) {
                Job job = jobList.get(i);
                Map map = new HashMap();
                map.put(Constants.id, job.getId());
                map.put(Constants.name, job.getName());
                map.put(JobConstant.JOB_KEY_POSITIONTYPE, job.getPositionType());
                map.put(Constants.createid, job.getCreateid());
                map.put(Constants.companyName, job.getEnterpriseName());
                map.put(Constants.salary, job.getSalaryName());
                map.put(Constants.publishTime, job.getPublishTime().substring(0, 10));
                map.put(Constants.updateTime, job.getUpdateTime().substring(0, 10));
                map.put(Constants.area, job.getWorkArea());
                map.put(Constants.welfare, job.getWelfare());
                map.put(Constants.logo, job.getEnterpriseLogo());
                list.add(map);
            }

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return list;
    }

    /**
     * 最新招聘（按分类一起查询）
     *
     * @param count 数量
     * @return
     */
    public List queryLatestJob(int count) throws Exception {
        List list = new ArrayList();
        try {
            // 获取研发类的职位
            List<Position> positionList = positionMapper.findPosition(6);
            for (Position position : positionList) {
                Map map = new HashMap();
                map.put(Constants.name, position.getName());
                List<Map<String, Object>> jobList = jobMapper.queryLatestJob(position.getId(), count);
                List list1 = new ArrayList();
                for (Map<String, Object> job : jobList) {
                    Map map1 = getDisplayMap(job);
                    list1.add(map1);
                }
                map.put("jobList", list1);
                list.add(map);
            }

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return list;
    }

    /**
     * 获取展示的职位map
     *
     * @param job
     * @return
     */
    private Map getDisplayMap(Map<String, Object> job) {
        Map map1 = new HashMap();
        map1.put(Constants.id, job.get("id"));
        map1.put(Constants.name, job.get("name"));
        map1.put(Constants.createid, job.get("createid"));
        if (job.get("salary") != null) {
            job = ConvertUtil.execute(job, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(job.get("salary"))});
            map1.put(Constants.salary, job.get("salaryName"));
        } else {
            map1.put(Constants.salary, "");
        }
        job = ConvertUtil.execute(job, "city", "dictionaryService", "findCityByCode", new Object[]{job.get("city")});
        job = ConvertUtil.execute(job, "province", "dictionaryService", "findProvinceByCode", new Object[]{job.get("province")});
        if (!"".equals(job.get("cityName"))) {
            map1.put(Constants.area, job.get("cityName"));
        } else {
            map1.put(Constants.area, job.get("provinceName"));
        }
        map1.put(JobConstant.JOB_KEY_POSITIONTYPE, job.get("positionType"));
        return map1;
    }

    /**
     * 名企招聘
     *
     * @return
     * @throws Exception
     */
    public List greatCompanyPosition() throws Exception {
        log.info("advertising greatest enterprise job");
        List list = new ArrayList();
        try {
            List<ResultBean> companyList = jobMapper.greatCompanyPosition();
            for (ResultBean company : companyList) {
                Map map = new HashMap();
                map.put(Constants.id, company.getCode());
                map.put(Constants.logo, company.getName());
                list.add(map);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return list;
    }

    /**
     * 更新点击率
     */
    public void updateViews(Long jobID) {
        try {
            jobMapper.updateViews(jobID);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 相似企业
     */
    public List<Map<String, Object>> querySimilarCompany(String id, int count) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Member member = memberMapper.findMemById(id);
            String employeeNumber = member.getEmployeeNumber();
            String enterpriseType = member.getEnterpriseType();

            List<String> companyList = jobMapper.querySimilarCompany(member.getEmployeeNumber() == null ? "" : employeeNumber,
                    member.getEnterpriseType() == null ? 0 : Integer.parseInt(enterpriseType), id, count);

            for (String createid : companyList) {
                Job companyInfo = jobMapper.querySimilarCompanyInfo(createid);
                Map<String, Object> map = new HashMap<>();
                map.put(Constants.id, createid);
                map.put(Constants.companyName, companyInfo.getEnterpriseName());
                map.put(Constants.size, companyInfo.getSize());
                map.put(Constants.area, companyInfo.getCity());
                list.add(map);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }

        return list;
    }

    /**
     * 查询名企发布的热门职位
     *
     * @param map 查询条件：recommend 是否名企（1：是），count 条数
     * @return 发布职位集合
     */
    public List<Job> queryEnterpriseHotPosition(Map<String, Object> map) {
        List<Job> jobList;
        try {
            jobList = jobMapper.queryEnterpriseHotPosition(map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return jobList;
    }

    /**
     * 查询名企发布的热门职位
     *
     * @param map 查询条件：recommend 是否名企（1：是），count 条数
     * @return 发布职位集合
     */
    public List<Map<String, String>> queryPublishJobCity(Map<String, Object> map) throws Exception {
        List<Map<String, String>> jobList;
        try {
            jobList = jobMapper.queryPublishJobCity(map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return jobList;
    }

    /**
     * 根据ID查询
     *
     * @param id {id}
     * @return
     */
    public Map<String, Object> findById(String id) {
        return positionMapper.findById(id);
    }

    /**
     * 设置为热门职位
     *
     * @param positionId
     */
    public void updateHot(String positionId, String hot) {

        int count;
        try {
            count = positionMapper.setupHotPosition(positionId, hot);
            if (count != 1) {
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新t_dictionary_position失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查询职位信息
     *
     * @param jobID
     * @return
     */
    public Map<String, Object> findJobByID(String jobID) {
        Map<String, Object> map;
        try {
            map = jobMapper.findJobByJobID(jobID);
            if (map != null) {
                handleWelfare(map);
                handleSalary(map);
                String cityCode = (String) map.get("city");
                String provinceCode = (String) map.get("province");
                if (!StringUtils.isEmpty(cityCode)) {
                    map = ConvertUtil.execute(map, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
                    map.put("cityName", map.get("cityName"));
                }
                if (!StringUtils.isEmpty(provinceCode)) {
                    map = ConvertUtil.execute(map, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
                    map.put("provinceName", map.get("provinceName"));
                }
                handleCity(map);
            } else {
                map = new HashMap<>();
            }

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return map;
    }

    private void handleWelfare(Map<String, Object> map) {
        String welfarename = "";
        String welfare = (String) map.get("welfare");
        genWelfaceName(map, welfare, welfarename);
    }

    private void handleCity(Map<String, Object> map) {
        String cityCode = (String) map.get("city");
        if (!StringUtils.isEmpty(cityCode)) {
            map = ConvertUtil.execute(map, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
            map.put("city", map.get("cityName"));
        } else {
            String provinceCode = (String) map.get("province");
            if (!StringUtils.isEmpty(provinceCode)) {
                map = ConvertUtil.execute(map, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
                map.put("city", map.get("provinceName"));
            } else {
                map.put("city", "");
            }
        }
    }

    public List<Map<String, Object>> findNewPositions(int count) {
        List<Map<String, Object>> list;
        try {
            list = jobMapper.findNewPositions(count);

            for (Map<String, Object> map : list) {
                String createID = String.valueOf(map.get("createID"));
                if (!StringUtils.isEmpty(createID)) {
                    Member member = memberMapper.findMemById(createID);
                    if (member != null) {
                        String enterpriseName = member.getEnterpriseName();
                        map.put("enterpriseName", StringUtils.isEmpty(enterpriseName) ? "" : enterpriseName);
                    } else {
                        map.put("enterpriseName", "");
                    }
                } else {
                    map.put("enterpriseName", "");
                }
                String salary = String.valueOf(map.get("salary"));
                if (!StringUtils.isEmpty(salary)) {
                    map = ConvertUtil.execute(map, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(map.get("salary"))});
                } else {
                    map.put("salaryName", "");
                }
                handleWelfare2(map, map);

                String cityCode = String.valueOf(map.get("city"));
                if (!StringUtils.isEmpty(cityCode)) {
                    map = ConvertUtil.execute(map, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
                    map.put("city", map.get("cityName"));
                } else {
                    String provinceCode = String.valueOf(map.get("province"));
                    if (!StringUtils.isEmpty(provinceCode)) {
                        map = ConvertUtil.execute(map, "province", "dictionaryService", "findProvinceByCode", new Object[]{provinceCode});
                        map.put("city", map.get("provinceName"));
                    } else {
                        map.put("city", "");
                    }
                }

            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }

    private void genWelfaceName(Map<String, Object> map, String welfare, String welfarename) {
        if (!StringUtils.isEmpty(welfare)) {
            String[] welfares = welfare.split(",");
            StringBuilder sb = new StringBuilder();
            for (String wf : welfares) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("welfare", wf);
                tmp = ConvertUtil.execute(tmp, "welfare", "constantService", "findByTypeCode", new Object[]{"5", String.valueOf(tmp.get("welfare"))});
                String welfaceName = (String) tmp.get("welfareName");
                sb.append(welfaceName).append(",");
            }
            welfarename = sb.toString();
            welfarename = welfarename.substring(0, welfarename.length() - 1);
        }
        map.put("welfare", welfarename);
    }

    public List<Map<String, String>> findAllJobByCompanyId(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return jobMapper.findAllJobByCompanyId(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("JobPositionService::findAllJobByCompanyId", e);
            throw e;
        }
    }

    /**
     * 解压
     *
     * @param filePath
     * @throws IOException
     */
    public boolean selDecompression(String filePath, String chann) throws Exception {
        boolean isSucc = false;
        filePath = "/home/app/upload/" + chann + "/" + filePath;
        File source = new File(filePath);
        if (source.exists()) {
            try {
                ZipInputStream zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.close();
                }
                zis.close();
                source.delete();
                isSucc = true;
            } catch (IOException e) {
                log.error("解压异常", e);
            }
        }
        return isSucc;
    }

    /**
     * 简历的解析
     *
     * @param name
     * @param chann
     */
    public void analysisResume(String name, String chann) {
        File fileDir = new File("/home/app/upload/" + chann + "/" + name);
        //读Spring配置文
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        if (chann.equals("51job")) {
            AnalysisJobService analysisJobService = (AnalysisJobService) context.getBean("analysisJobService");
            List<File> files = getFiles(fileDir, "htm");
            List<Map<String, String>> list = new ArrayList<>();
            for (File file : files) {
                String fileName = file.getName();
                //输出文件名
                System.out.println(fileName);
                Map<String, String> map = analysisJobService.parseHtmlFile(file);
                System.out.println(map);
                list.add(map);
            }
        } else if (chann.equals("zhilian")) {
            AnalysisZhiLianService analysisZhiLianService = (AnalysisZhiLianService) context.getBean("analysisZhiLianService");
            List<File> files = getFiles(fileDir, "html");
            List<Map<String, String>> list = new ArrayList<>();
            for (File file : files) {
                String fileName = file.getName();
                System.out.println(fileName);
                if (fileName.contains("中文")) {
                    Map<String, String> map = analysisZhiLianService.parseHtmlFile(file);
                    System.out.println(map);
                    list.add(map);
                }
            }
        } else if (chann.equals("rencai")) {
            AnalysisRenCaiService analysisRenCaiService = (AnalysisRenCaiService) context.getBean("analysisRenCaiService");
            List<File> files = getFiles(fileDir, "html");
            List<Map<String, String>> list = new ArrayList<>();
            for (File file : files) {
                System.out.println(file.getName());
                Map<String, String> map = analysisRenCaiService.parseHtmlFile(file);
                list.add(map);
            }
        } else if (chann.equals("liepin")) {
            AnalysisLiePinService analysisLiePinService = (AnalysisLiePinService) context.getBean("analysisLiePinService");
            List<File> files = getFiles(fileDir, "html");
            List<Map<String, String>> list = new ArrayList<>();
            for (File file : files) {
                System.out.println(file.getName());
                Map<String, String> map = analysisLiePinService.parseHtmlFile(file);
                list.add(map);
            }
        }
    }


    /**
     * 文件的递归
     *
     * @param fileDir
     * @param type
     * @return
     */
    public List<File> getFiles(File fileDir, String type) {
        List<File> list = new ArrayList<>();
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    int index = name.lastIndexOf(".");
                    String suffix = name.substring(index + 1, name.length());
                    if (type.equals(suffix)) {
                        list.add(file);
                    }
                } else {
                    System.out.println("不是文件");
                    //递归扫描
                    List<File> list2 = getFiles(file, type);
                    list.addAll(list2);
                }
            }
        }
        return list;
    }
}

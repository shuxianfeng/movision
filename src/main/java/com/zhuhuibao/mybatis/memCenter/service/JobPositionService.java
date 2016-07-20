package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.PositionMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
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

    /**
     * 发布职位
     */
    public void publishPosition(Job job) throws Exception {
        try {
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.FBZW.toString());
            if (bool) {
                jobMapper.publishPosition(job);
                zhbService.payForGoods(Long.parseLong(job.getId()), ZhbPaymentConstant.goodsType.FBZW.toString());
            } else {//支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询公司已发布的职位
     */
    public List findAllPositionByMemId(Paging<Job> pager, String id) {
        List<Job> jobList = jobMapper.findAllByPager(pager.getRowBounds(), id);
        List list = new ArrayList();
        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            Map map = new HashMap();
            map.put(Constants.position, job.getName());
            map.put(Constants.salary, job.getSalaryName());
            map.put(Constants.area, job.getWorkArea());
            map.put(Constants.id, job.getId());
            map.put("companyId", job.getCreateid());
            map.put("positionType", job.getPositionType());
            map.put(Constants.publishTime, job.getPublishTime());
            map.put(Constants.updateTime, job.getUpdateTime());
            list.add(map);
        }
        return list;
    }

    /**
     * 查询公司发布的某条职位的信息
     */
    public Job getPositionByPositionId(String id) {
        try {
            return jobMapper.getPositionByPositionId(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
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
                log.error(e.getMessage());
                e.printStackTrace();
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
                log.error(e.getMessage());
                e.printStackTrace();
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
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询最新招聘职位
     */
    public Response searchNewPosition(int count) {
        Response result = new Response();
        List<Job> jobList = jobMapper.searchNewPosition(count);
        result.setData(jobList);
        result.setCode(200);
        return result;
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
        Response result = new Response();
        List<Job> jobList = jobMapper.searchLatestPublishPosition();
        result.setData(jobList);
        result.setCode(200);
        return result;
    }

    /**
     * 职位类别
     */
    public List positionType() {
        List<Position> positionList = positionMapper.findPosition(7);
        List<Position> subPositionList = positionMapper.findSubPosition();
        List list1 = new ArrayList();
        for (int i = 0; i < positionList.size(); i++) {
            Position position = positionList.get(i);
            Map map = new HashMap();
            map.put(Constants.code, position.getId());
            map.put(Constants.name, position.getName());
            List list = new ArrayList();
            for (int y = 0; y < subPositionList.size(); y++) {
                Position subPosition = subPositionList.get(y);
                if (position.getId().equals(subPosition.getParentId())) {
                    Map map1 = new HashMap();
                    map1.put(Constants.code, subPosition.getId());
                    map1.put(Constants.name, subPosition.getName());
                    map1.put(Constants.hot, subPosition.getHot());
                    list.add(map1);
                }
            }
            map.put(Constants.subPositionList, list);
            list1.add(map);
        }
        return list1;
    }

    /**
     * 查询发布职位公司信息
     *
     * @param id
     * @return
     */
    public Response queryCompanyInfo(Long id) throws Exception {
        Response response = new Response();
        try {
            MemberDetails member = jobMapper.queryCompanyInfo(id);
            response.setData(member);
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 查询招聘栏目的广告位
     * @param map 查询条件
     * @return
     */
/*    public Response queryAdvertisingPosition(Map<String,Object> map)
    {
        Response response = new Response();
        try
        {
            List<MemberDetails> memberList = jobMapper.queryAdvertisingPosition(map);
            response.setData(memberList);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return response;
        }
        return response;
    }*/

    /**
     * 查询企业发布的职位详情
     *
     * @param map 职位搜索
     * @return
     */
    public Response queryPositionInfoByID(Map<String, Object> map) {
        log.info("query position info by id = " + StringUtils.mapToString(map));
        Response response = new Response();
        try {
            Map<String, Object> job = jobMapper.queryPositionInfoByID(map);
            Long isApply = (Long) job.get("isApply");
            job.put("isApply", (isApply == 0 ? false : true));
            response.setData(job);
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw e;
        }
        return response;
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
            List<Map<String, Object>> jobList = jobMapper.findAllOtherPosition(pager.getRowBounds(), map);
            for (Map<String, Object> job : jobList) {
                Map<String, Object> result = new HashMap<>();
                String welfare = (String) job.get("welfare");
                String welfarename = "";
                genWelfaceName(result, welfare, welfarename);

                job = ConvertUtil.execute(job, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(job.get("salary"))});
                result.put("salaryName", job.get("salaryName"));

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
                list.add(result);
            }
        } catch (Exception e) {
            log.error("error!", e);
        }
        return list;
    }

    /**
     * 我申请的职位
     */
    public Response myApplyPosition(Paging<Job> pager, String id) {
        Response response = new Response();
        List<Job> jobList = jobMapper.findAllMyApplyPosition(pager.getRowBounds(), id);
        List list = new ArrayList();
        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            Map map = new HashMap();
            map.put(Constants.id, job.getId());
            map.put(Constants.name, job.getName());
            map.put(Constants.companyName, job.getEnterpriseName());
            map.put(Constants.salary, job.getSalaryName());
            map.put(Constants.publishTime, job.getPublishTime());
            map.put(Constants.area, job.getWorkArea());
            map.put(Constants.welfare, job.getWelfare());
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
            log.error("query hot postion error!!");
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
            List<Position> positionList = positionMapper.findPosition(6);
            for (int a = 0; a < positionList.size(); a++) {
                Position position = positionList.get(a);
                Map map = new HashMap();
                map.put(Constants.name, position.getName());
                List<Job> jobList = jobMapper.queryLatestJob(position.getId(), count);
                List list1 = new ArrayList();
                for (int i = 0; i < jobList.size(); i++) {
                    Job job = jobList.get(i);
                    Map map1 = new HashMap();
                    map1.put(Constants.id, job.getId());
                    map1.put(Constants.name, job.getName());
                    map1.put(Constants.createid, job.getCreateid());
                    map1.put(Constants.salary, job.getSalaryName());
                    map1.put(Constants.area, job.getCity());
                    map1.put(JobConstant.JOB_KEY_POSITIONTYPE, job.getPositionType());
                    list1.add(map1);
                }
                map.put("jobList", list1);
                list.add(map);
            }

        } catch (Exception e) {
            log.error("query latest job error", e);
            throw e;
        }
        return list;
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
            for (int i = 0; i < companyList.size(); i++) {
                ResultBean company = companyList.get(i);
                Map map = new HashMap();
                map.put(Constants.id, company.getCode());
                map.put(Constants.logo, company.getName());
                list.add(map);
            }
        } catch (Exception e) {
            log.error("advertising greatest enterprise job error!", e);
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
            log.error("update position info error!", e);
        }
    }

    /**
     * 相似企业
     */
    public List<Map<String, Object>> querySimilarCompany(String id, int count) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Member member = memberMapper.findMemById(id);
            List<Job> companyList = jobMapper.querySimilarCompany(member.getEmployeeNumber(), Integer.parseInt(member.getEnterpriseType()), id, count);

            for (Job job : companyList) {
                Job companyInfo = jobMapper.querySimilarCompanyInfo(job.getCreateid());
                Map<String, Object> map = new HashMap<>();
                map.put(Constants.id, job.getCreateid());
                map.put(Constants.companyName, companyInfo.getEnterpriseName());
                map.put(Constants.size, companyInfo.getSize());
                map.put(Constants.area, companyInfo.getCity());
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"查询失败");
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
        List<Job> jobList = new ArrayList<Job>();
        try {
            jobList = jobMapper.queryEnterpriseHotPosition(map);
        } catch (Exception e) {
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
        List<Map<String, String>> jobList = new ArrayList<Map<String, String>>();
        try {
            jobList = jobMapper.queryPublishJobCity(map);
        } catch (Exception e) {
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

        int count = 0;
        try {
            count = positionMapper.setupHotPosition(positionId, hot);
            if (count != 1) {
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新t_dictionary_position失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
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
            String welfare = (String) map.get("welfare");
            String welfarename = "";
            genWelfaceName(map, welfare, welfarename);

            map = ConvertUtil.execute(map, "salary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(map.get("salary"))});
            map.put("salary", map.get("salaryName"));

            String cityCode = (String) map.get("city");
            if (!StringUtils.isEmpty(cityCode)) {
                map = ConvertUtil.execute(map, "city", "dictionaryService", "findCityByCode", new Object[]{cityCode});
                map.put("city", map.get("cityName"));

            } else {
                map.put("city", "");
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return map;
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
                String welfare = (String) map.get("welfare");
                String welfarename = "";
                genWelfaceName(map, welfare, welfarename);

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
            e.printStackTrace();
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
}

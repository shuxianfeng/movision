package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ConvertUtil;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.CollectRecord;
import com.zhuhuibao.mybatis.memCenter.entity.DownloadRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.mapper.CollectRecordMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.DownloadRecordMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeLookRecordMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeMapper;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/19 0019.
 */
@Service
@Transactional
public class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    ApiConstants apiConstants;
    @Autowired
    ZhbService zhbService;

    @Autowired
    private ResumeLookRecordMapper resumeLookRecordMapper;

    @Autowired
    private DownloadRecordMapper downloadRecordMapper;

    @Autowired
    private CollectRecordMapper collectRecordMapper;

    @Autowired
    PaymentGoodsService goodsService;


    /**
     * 发布简历
     */
    public Response setUpResume(Resume resume) {
        Response response = new Response();
        checkResumeParams(resume);
        int isSetUp = resumeMapper.setUpResume(resume);
        try {
            if (isSetUp == 1) {
                response.setData(resume.getId());
            } else {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "创建失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return response;
    }

    /**
     * 创建简历字段校验
     *
     * @param resume
     */
        //简历名称
    public void checkResumeParams(Resume resume) {
        if (StringUtils.isEmpty(resume.getTitle())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "简历名称不能为空");
        }
        //姓名
        if (StringUtils.isEmpty(resume.getRealName())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "姓名不能为空");
        }
        //性别
        if (StringUtils.isEmpty(resume.getSex())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "性别不能为空");
        }
        //参加工作年份
        if (StringUtils.isEmpty(resume.getWorkYear())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "参加工作年份不能为空");
        }
        //最高学历
        if (StringUtils.isEmpty(resume.getEducation())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "最高学历不能为空");
        }
        //工作经验
        if (StringUtils.isEmpty(resume.getExperienceYear())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "工作经验不能为空");
        }
        //出生年份
        if (StringUtils.isEmpty(resume.getBirthYear())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "出生年份不能为空");
        }
        //婚姻状况
        if (StringUtils.isEmpty(resume.getMarriage())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "婚姻状况不能为空");
        }
        //现居地
        if (StringUtils.isEmpty(resume.getLiveProvince())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "现居地不能为空");
        }
        //手机
        if (StringUtils.isEmpty(resume.getMobile())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "手机不能为空");
        }
        //邮箱
        if (StringUtils.isEmpty(resume.getEmail())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "邮箱不能为空");
        }
    }

    /**
     * 查询我创建的简历
     */
    public Response searchMyResume(String id) {
        Response response = new Response();
        Resume resume = resumeMapper.searchMyResume(id);
        Map<String,Object> resume1 = resumeMapper.searchMyResumeAllInfo(id);
        if (resume1 != null) {
            Integer i = 0;
            if (resume1.get("jobExperience") != null && !"".equals(resume1.get("jobExperience"))) {
                i = i + 1;
            }
            if (resume1.get("eduExperience") != null && !"".equals(resume1.get("eduExperience"))) {
                i = i + 1;
            }
            if (resume1.get("projectExperience") != null && !"".equals(resume1.get("projectExperience"))) {
                i = i + 1;
            }
            if (resume1.get("attach") != null && !"".equals(resume1.get("attach"))) {
                i = i + 1;
            }
            if (resume1.get("photo") != null && !"".equals(resume1.get("photo"))) {
                i = i + 1;
            }
            /*if(resume1.getExperienceYear()!=null && !"".equals(resume1.getExperienceYear())){
                i=i+1;
            }*/
            if (resume1.get("company") != null && !"".equals(resume1.get("company"))) {
                i = i + 1;
            }
            if (resume1.get("positionName") != null && !"".equals(resume1.get("positionName"))) {
                i = i + 1;
            }
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            String result = numberFormat.format((float) (i + 16) / (float) 23 * 100);
            String b = result + "%";
            Map<String, Object> map = new HashMap<>();
            map.put("info", resume);
            map.put("percent", b);
            response.setData(map);
        } else {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "简历不存在");
        }
        return response;
    }

    /**
     * 查询我创建的简历的全部信息
     */
    public Map<String,Object> searchMyResumeAllInfo(String id) {
        Map<String,Object> resume =  resumeMapper.searchMyResumeAllInfo(id);
        String jobCity = (String) resume.get("jobCity");
        String jobCityName = "";
        if (!StringUtils.isEmpty(jobCity)) {
            String[] jobCitys = jobCity.split(",");
            StringBuilder sb = new StringBuilder();
            for (String jc : jobCitys) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("jobCity", jc);
                tmp = ConvertUtil.execute(tmp, "jobCity", "dictionaryService", "findCityByCode", new Object[]{String.valueOf(tmp.get("jobCity"))});
                String jcName = (String) tmp.get("jobCityName");
                if (!"".equals(jcName)) {
                    sb.append(jcName).append(",");
                }
            }
            jobCityName = sb.toString();
            if (jobCityName.length() != 0) {
                jobCityName = jobCityName.substring(0, jobCityName.length() - 1);
            }
        }
        resume.put("jobCityName", jobCityName);

        String jobProvince = (String) resume.get("jobProvince");
        String jobProvinceName = "";
        if (!StringUtils.isEmpty(jobProvince)) {
            String[] jobProvinces = jobProvince.split(",");
            StringBuilder sb = new StringBuilder();
            for (String jp : jobProvinces) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("jobProvince", jp);
                tmp = ConvertUtil.execute(tmp, "jobProvince", "dictionaryService", "findProvinceByCode", new Object[]{String.valueOf(tmp.get("jobProvince"))});
                String jpName = (String) tmp.get("jobProvinceName");
                if (!"".equals(jpName)) {
                    sb.append(jpName).append(",");
                }
            }
            jobProvinceName = sb.toString();
            if (jobProvinceName.length() != 0) {
                jobProvinceName = jobProvinceName.substring(0, jobProvinceName.length() - 1);
            }
        }
        resume.put("jobProvinceName", jobProvinceName);

        if (resume.get("hopeSalary") != null) {
            resume = ConvertUtil.execute(resume, "hopeSalary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(resume.get("hopeSalary"))});
            resume.put("hopeSalaryName", resume.get("hopeSalaryName"));
        } else {
            resume.put("hopeSalaryName", "");
        }

        if (resume.get("curSalary") != null) {
            resume = ConvertUtil.execute(resume, "curSalary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(resume.get("curSalary"))});
            resume.put("curSalaryName", resume.get("curSalaryName"));
        } else {
            resume.put("curSalaryName", "");
        }

        String post = (String) resume.get("post");
        String postName = "";
        if (!StringUtils.isEmpty(post)) {
            String[] posts = post.split(",");
            StringBuilder sb = new StringBuilder();
            for (String p : posts) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("post", p);
                tmp = ConvertUtil.execute(tmp, "post", "constantService", "findPositionById", new Object[]{String.valueOf(tmp.get("post"))});
                String pName = (String) tmp.get("postName");
                if (!"".equals(pName)) {
                    sb.append(pName).append(",");
                }
            }
            postName = sb.toString();
            if (postName.length() != 0) {
                postName = postName.substring(0, postName.length() - 1);
            }
        }
        resume.put("postName", postName);
        return resume;
    }

    /**
     * 更新简历,刷新简历
     */
    public Response updateResume(Resume resume) {
        Response response = new Response();


        try {
            int isUpdate = resumeMapper.updateResume(resume);
            if (isUpdate == 1) {
                response.setCode(200);
                response.setData(resume.getId());
            } else {
                response.setCode(400);
                response.setMessage("更新失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return response;
    }

    /**
     * 预览简历
     */
    public Resume previewResume(Map<String,Object> map) {
        try {
            return resumeMapper.previewResume(map);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 人才库搜索
     *
     * @param pager 分页条件
     * @param map   查询条件
     * @return 分页结果
     */
    public Paging<Map<String, Object>> findAllResume(Paging<Map<String, Object>> pager, Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<Map<String, Object>> resumeList = resumeMapper.findAllResume(pager.getRowBounds(), map);
            for (Map<String, Object> resume : resumeList) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", resume.get("id"));
                result.put("createid", resume.get("createid"));
                result.put("title", resume.get("title"));
                result.put("realName", resume.get("realName"));
                result.put("photo", resume.get("photo"));
                result.put("publishTime", resume.get("publishTime"));
                result.put("updateTime", resume.get("updateTime"));
                result.put("experienceYear", resume.get("experienceYear"));
                result.put("education", resume.get("education"));
                result.put("isPublic", resume.get("isPublic"));
                result.put("birthYear", resume.get("birthYear"));
                result.put("workYear", resume.get("workYear"));
                if (resume.get("jobNature") != null) {
                    resume = ConvertUtil.execute(resume, "jobNature", "constantService", "findByTypeCode", new Object[]{"7", String.valueOf(resume.get("jobNature"))});
                    result.put("jobNature", resume.get("jobNatureName"));
                } else {
                    result.put("jobNature", "");
                }

                if (resume.get("hopeSalary") != null) {
                    resume = ConvertUtil.execute(resume, "hopeSalary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(resume.get("hopeSalary"))});
                    result.put("hopeSalary", resume.get("hopeSalaryName"));
                } else {
                    result.put("hopeSalary", "");
                }

                if (resume.get("education") != null) {
                    resume = ConvertUtil.execute(resume, "education", "constantService", "findByTypeCode", new Object[]{"2", String.valueOf(resume.get("education"))});
                    result.put("name", resume.get("educationName"));
                } else {
                    result.put("name", "");
                }

                String post = (String) resume.get("post");
                String postName = "";
                if (!StringUtils.isEmpty(post)) {
                    String[] posts = post.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String p : posts) {
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("post", p);
                        tmp = ConvertUtil.execute(tmp, "post", "constantService", "findPositionById", new Object[]{String.valueOf(tmp.get("post"))});
                        String pName = (String) tmp.get("postName");
                        if (!"".equals(pName)) {
                            sb.append(pName).append(",");
                        }
                    }
                    postName = sb.toString();
                    if (postName.length() != 0) {
                        postName = postName.substring(0, postName.length() - 1);
                    }
                }
                result.put("post", postName);
                list.add(result);
            }
            pager.result(list);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return pager;
    }

    /**
     * 我收到的简历
     */
    public Response receiveResume(Paging<Map<String, String>> pager, String id) {
        Response response = new Response();
        List<Map<String, String>> resumeList = resumeMapper.findAllReceiveResume(pager.getRowBounds(), id);
        pager.result(resumeList);
        response.setCode(200);
        response.setData(pager);
        return response;
    }

    /**
     * 我下载的简历
     */
    public Response findAllDownloadResume(Paging<Map<String, String>> pager, String id) {
        Response response = new Response();
        List<Map<String, String>> resumeList = resumeMapper.findAllDownloadResume(pager.getRowBounds(), id);
        pager.result(resumeList);
        response.setData(pager);
        return response;
    }

    /**
     * 我收藏的简历
     */
    public Response findAllCollectResume(Paging<Map<String, String>> pager, String id) {
        Response response = new Response();
        List<Map<String, String>> resumeList = resumeMapper.findAllCollectResume(pager.getRowBounds(), id);
        pager.result(resumeList);
        response.setData(pager);
        return response;
    }

    /**
     * 导出简历
     */
    public Map<String, String> exportResume(String id) {
        Map<String, String> resumeMap = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        Resume resume = resumeMapper.previewResume(map);
        if (resume != null) {
            resumeMap.put("realName", resume.getRealName() != null && !StringUtils.isEmpty(resume.getRealName()) ? resume.getRealName() : "");
            resumeMap.put("title", resume.getTitle() != null && !StringUtils.isEmpty(resume.getTitle()) ? resume.getTitle() : "");
            resumeMap.put("name", resume.getRealName() != null && !StringUtils.isEmpty(resume.getRealName()) ? resume.getRealName() : "");
            resumeMap.put("sex", resume.getSex() != null && !StringUtils.isEmpty(resume.getSex()) ? resume.getSex() : "");
            resumeMap.put("marriage", resume.getMarriage() != null && !StringUtils.isEmpty(resume.getMarriage()) ? resume.getMarriage() : "");
            resumeMap.put("birthYear", resume.getBirthYear() != null && !StringUtils.isEmpty(resume.getBirthYear()) ? resume.getBirthYear() : "");
            resumeMap.put("education", resume.getEducation() != null && !StringUtils.isEmpty(resume.getEducation()) ? resume.getEducation() : "");
            resumeMap.put("liveArea", resume.getLiveArea() != null && !StringUtils.isEmpty(resume.getLiveArea()) ? resume.getLiveArea() : "");
            resumeMap.put("workYear", resume.getWorkYear() != null && !StringUtils.isEmpty(resume.getWorkYear()) ? resume.getWorkYear() : "");
            resumeMap.put("mobile", resume.getMobile() != null && !StringUtils.isEmpty(resume.getMobile()) ? resume.getMobile() : "");
            resumeMap.put("email", resume.getEmail() != null && !StringUtils.isEmpty(resume.getEmail()) ? resume.getEmail() : "");
            resumeMap.put("jobNature", resume.getJobNature() != null && !StringUtils.isEmpty(resume.getJobNature()) ? resume.getJobNature() : "");
            resumeMap.put("post", resume.getPost() != null && !StringUtils.isEmpty(resume.getPost()) ? resume.getPost() : "");
            resumeMap.put("jobArea", resume.getJobArea() != null && !StringUtils.isEmpty(resume.getJobArea()) ? resume.getJobArea() : "");
            resumeMap.put("hopeSalary", resume.getHopeSalary() != null && !StringUtils.isEmpty(resume.getHopeSalary()) ? resume.getHopeSalary() : "");
            resumeMap.put("status", resume.getStatus() != null && !StringUtils.isEmpty(resume.getStatus()) ? resume.getStatus() : "");
            //表格内的使用“(char)11”换行，ascii码的制表符.表格外的参数使用“\r”换行
            if (resume.getEduExperience() != null && !StringUtils.isEmpty(resume.getEduExperience())) {
                resumeMap.put("eduExperience", resume.getEduExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("eduExperience", "");
            }
            if (resume.getJobExperience() != null && !StringUtils.isEmpty(resume.getJobExperience())) {
                resumeMap.put("jobExperience", resume.getJobExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("jobExperience", "");
            }
            if (resume.getProjectExperience() != null && !StringUtils.isEmpty(resume.getProjectExperience())) {
                resumeMap.put("projectExperience", resume.getProjectExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("projectExperience", "");
            }

        }
        return resumeMap;
    }


    /**
     * 导入简历记录，记录现在数据扣筑慧币
     *
     * @param id
     * @return
     * @throws Exception
     * @throws NumberFormatException
     */
    public Map<String, String> exportResumeNew(String id) throws Exception {
        Map<String, String> resumeMap = new HashMap<>();
        Map<String, String> recordMap;
        Map<String, String> viewMap;
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        Resume resume = resumeMapper.previewResume(map);
        Long createId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        if (resume != null) {
            recordMap = new HashMap<>();
            viewMap = new HashMap<>();
            resumeMap.put("title", resume.getTitle() != null && !StringUtils.isEmpty(resume.getTitle()) ? resume.getTitle() : "");
            resumeMap.put("name", resume.getRealName() != null && !StringUtils.isEmpty(resume.getRealName()) ? resume.getRealName() : "");
            resumeMap.put("sex", resume.getSex() != null && !StringUtils.isEmpty(resume.getSex()) ? resume.getSex() : "");
            resumeMap.put("marriage", resume.getMarriage() != null && !StringUtils.isEmpty(resume.getMarriage()) ? resume.getMarriage() : "");
            resumeMap.put("birthYear", resume.getBirthYear() != null && !StringUtils.isEmpty(resume.getBirthYear()) ? resume.getBirthYear() : "");
            resumeMap.put("education", resume.getEducation() != null && !StringUtils.isEmpty(resume.getEducation()) ? resume.getEducation() : "");
            resumeMap.put("liveArea", resume.getLiveArea() != null && !StringUtils.isEmpty(resume.getLiveArea()) ? resume.getLiveArea() : "");
            resumeMap.put("workYear", resume.getWorkYear() != null && !StringUtils.isEmpty(resume.getWorkYear()) ? resume.getWorkYear() : "");
            resumeMap.put("mobile", resume.getMobile() != null && !StringUtils.isEmpty(resume.getMobile()) ? resume.getMobile() : "");
            resumeMap.put("email", resume.getEmail() != null && !StringUtils.isEmpty(resume.getEmail()) ? resume.getEmail() : "");
            resumeMap.put("jobNature", resume.getJobNature() != null && !StringUtils.isEmpty(resume.getJobNature()) ? resume.getJobNature() : "");
            resumeMap.put("post", resume.getPost() != null && !StringUtils.isEmpty(resume.getPost()) ? resume.getPost() : "");
            resumeMap.put("jobArea", resume.getJobArea() != null && !StringUtils.isEmpty(resume.getJobArea()) ? resume.getJobArea() : "");
            resumeMap.put("hopeSalary", resume.getHopeSalary() != null && !StringUtils.isEmpty(resume.getHopeSalary()) ? resume.getHopeSalary() : "");
            resumeMap.put("status", resume.getStatus() != null && !StringUtils.isEmpty(resume.getStatus()) ? resume.getStatus() : "");
            //表格内的使用“(char)11”换行，ascii码的制表符.表格外的参数使用“\r”换行
            if (resume.getEduExperience() != null && !StringUtils.isEmpty(resume.getEduExperience())) {
                resumeMap.put("eduExperience", resume.getEduExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("eduExperience", "");
            }
            if (resume.getJobExperience() != null && !StringUtils.isEmpty(resume.getJobExperience())) {
                resumeMap.put("jobExperience", resume.getJobExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("jobExperience", "");
            }
            if (resume.getProjectExperience() != null && !StringUtils.isEmpty(resume.getProjectExperience())) {
                resumeMap.put("projectExperience", resume.getProjectExperience().replaceAll("<br/>", String.valueOf((char) 11)));
            } else {
                resumeMap.put("projectExperience", "");
            }

            //记录下载状态
            recordMap.put("resumeID", id);
            recordMap.put("companyID", companyId.toString());
            recordMap.put("createId", createId.toString());

            viewMap.put("viewerId", createId.toString());
            viewMap.put("goodsId", id);
            viewMap.put("companyId", companyId.toString());
            viewMap.put("type", ZhbPaymentConstant.goodsType.CXXZJL.toString());

            resumeMapper.insertDownRecord(recordMap);


            //删除收藏夹子
            resumeMapper.delCollRecord(recordMap);

            Map<String, Object> con = new HashMap<>();
            //商品ID
            con.put("goodsId", id);
            con.put("companyId", companyId);
            con.put("type", ZhbPaymentConstant.goodsType.CXXZJL.toString());

            //项目是否已经被同企业账号查看过
            int viewNumber = goodsService.checkIsViewGoods(con);
            if (viewNumber == 0) {
                //记录下载预览
                resumeMapper.insertViewGoods(viewMap);
                // 扣除筑慧币
                zhbService.payForGoods(Long.valueOf(id), ZhbPaymentConstant.goodsType.CXXZJL.toString());
            }
        }
        return resumeMap;
    }

    public String downloadBill(String id) {
        Resume resume = resumeMapper.searchResumeById(id);
        String fileUrl = "";
        if (resume != null) {
            fileUrl = resume.getAttach();
        }
        return fileUrl;
    }

    /**
     * 根据会员ID查询出他自己的简历
     *
     * @param createID
     * @return
     */
    public Resume queryResumeByCreateId(Long createID) {
        Resume resume = null;
        try {
            List<Resume> resumeList = resumeMapper.queryResumeByCreateId(createID);
            if (!resumeList.isEmpty()) {
                resume = resumeList.get(0);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return resume;
    }

    /**
     * 最新求职
     *
     * @param condition 查询条件
     * @return
     */
    public List<Map<String,Object>> queryLatestResume(Map<String, Object> condition) throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            List<Map<String,Object>> resumeList = resumeMapper.queryLatestResume(condition);

            for (Map<String,Object> resume : resumeList) {

                Map<String,Object> map = new HashMap<>();

                if (resume.get("hopeSalary") != null) {
                    resume = ConvertUtil.execute(resume, "hopeSalary", "constantService", "findByTypeCode", new Object[]{"1", String.valueOf(resume.get("hopeSalary"))});
                    map.put(Constants.salary, resume.get("hopeSalaryName"));
                } else {
                    map.put(Constants.salary, "");
                }

                String post = (String) resume.get("post");
                String postName = "";
                if (!StringUtils.isEmpty(post)) {
                    String[] posts = post.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String p : posts) {
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("post", p);
                        tmp = ConvertUtil.execute(tmp, "post", "constantService", "findPositionById", new Object[]{String.valueOf(tmp.get("post"))});
                        String pName = (String) tmp.get("postName");
                        if (!"".equals(pName)) {
                            sb.append(pName).append(",");
                        }
                    }
                    postName = sb.toString();
                    if (postName.length() != 0) {
                        postName = postName.substring(0, postName.length() - 1);
                    }
                }

                String area = "";
                String jobCity = (String) resume.get("jobCity");
                String jobCityName = "";
                if (!StringUtils.isEmpty(jobCity)) {
                    String[] jobCitys = jobCity.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String jc : jobCitys) {
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("jobCity", jc);
                        tmp = ConvertUtil.execute(tmp, "jobCity", "dictionaryService", "findCityByCode", new Object[]{String.valueOf(tmp.get("jobCity"))});
                        String jcName = (String) tmp.get("jobCityName");
                        if (!"".equals(jcName)) {
                            sb.append(jcName).append(",");
                        }
                    }
                    jobCityName = sb.toString();
                    if (jobCityName.length() != 0) {
                        jobCityName = jobCityName.substring(0, jobCityName.length() - 1);
                    }
                }

                String jobProvince = (String) resume.get("jobProvince");
                String jobProvinceName = "";
                if (!StringUtils.isEmpty(jobProvince)) {
                    String[] jobProvinces = jobProvince.split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String jp : jobProvinces) {
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("jobProvince", jp);
                        tmp = ConvertUtil.execute(tmp, "jobProvince", "dictionaryService", "findProvinceByCode", new Object[]{String.valueOf(tmp.get("jobProvince"))});
                        String jpName = (String) tmp.get("jobProvinceName");
                        if (!"".equals(jpName)) {
                            sb.append(jpName).append(",");
                        }
                    }
                    jobProvinceName = sb.toString();
                    if (jobProvinceName.length() != 0) {
                        jobProvinceName = jobProvinceName.substring(0, jobProvinceName.length() - 1);
                    }
                }

                resume = ConvertUtil.execute(resume, "jobCity", "dictionaryService", "findCityByCode", new Object[]{resume.get("jobCity")});
                resume = ConvertUtil.execute(resume, "jobProvince", "dictionaryService", "findProvinceByCode", new Object[]{resume.get("jobProvince")});
                if (!"".equals(jobCityName)) {
                    if (!"".equals(jobProvinceName)) {
                        area = jobProvinceName + "," + jobCityName;
                    } else {
                        area = jobCityName;
                    }
                } else {
                    if (!"".equals(jobProvinceName)) {
                        area = jobProvinceName;
                    }
                }

                map.put(Constants.id, resume.get("id"));
                map.put(Constants.logo, resume.get("photo"));
                map.put(Constants.status, resume.get("status"));
                map.put(Constants.area, area);
                map.put(Constants.name, resume.get("realName").toString().subSequence(0, 1) + "**");
                map.put(Constants.position, postName);
                map.put(Constants.experienceYear, resume.get("workYear"));
                map.put(Constants.age, resume.get("birthYear"));
                list.add(map);
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"查询失败");
        }
        return list;
    }

    /**
     * 判断某个人是否已经创建简历
     *
     * @param createID 创建者ID或者会员ID
     * @return true:存在，false:不存在
     */
    public Boolean isExistResume(Long createID) {
        log.info("isExistResume createID = " + createID);
        Boolean isExist = false;
        try {
            int count = resumeMapper.isExistResume(createID);
            if (count > 0) {
                isExist = true;
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return isExist;
    }

    public int addLookRecord(Map<String, Object> map) {
        try {
            return resumeLookRecordMapper.addLookRecord(map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    public List<Map<String, String>> findAllMyResumeLookRecord(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return resumeLookRecordMapper.findAllMyResumeLookRecord(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    /**
     * 会员中心首页查询招聘信息
     *
     * @param createId
     * @return
     */
    public Map<String, Object> queryJobCount(Long createId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Map<String, Object>> resultList = resumeMapper.queryJobCount(createId);
            Map<String, Object> map1 = resultList.get(0);
            resultMap.put("positionCount", map1.get("count"));
            Map<String, Object> map2 = resultList.get(1);
            resultMap.put("resumeCount", map2.get("count"));
        } catch (Exception e) {
            log.error("find data upload download error!", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
        return resultMap;
    }

    public int del_downloadResume(DownloadRecord record) {
        try {
            return downloadRecordMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    public int del_collectResume(CollectRecord record) {
        try {
            return collectRecordMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    /**
     * 简历预览
     *
     * @param id
     * @return
     */
    public Resume previewResumeNew(String id) {
        try {
            return resumeMapper.previewResumeNew(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    /**
     * 简历是否收藏或下载
     *
     * @param con
     * @return
     */
    public Map isDownOrColl(Map<String, Object> con) {
        try {
            return resumeMapper.isDownOrColl(con);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "操作失败");
        }
    }

    /**
     * 收藏简历
     *
     * @param id
     */
    public int insertCollRecord(String id) {
        try {

            Map<String, Object> resumeMap = new HashMap<>();
            Long createId = ShiroUtil.getCreateID();
            Long companyId = ShiroUtil.getCompanyID();
            resumeMap.put("resumeID", id);
            resumeMap.put("companyID", companyId.toString());
            resumeMap.put("createId", createId.toString());
            return resumeMapper.insertCollRecord(resumeMap);

        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入失败");
        }
    }

    public List<String> selectIdsByCreateId(Long createid) {
        List<String> ids;
        try {
            ids = resumeMapper.selectIdsByCreateId(createid);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }

        return ids;

    }
    //用户收藏最大值值
	public int getMaxCollCount(Long memberId) {
	 
		  return resumeMapper.getMaxCollCount(memberId);
	}
}

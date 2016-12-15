package com.zhuhuibao.service;

import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.CollectRecord;
import com.zhuhuibao.mybatis.memCenter.entity.DownloadRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeMapper;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触屏端招聘简历相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileResumeService {

    private static final Logger log = LoggerFactory.getLogger(MobileResumeService.class);

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobRelResumeService jobRelResumeService;

    @Autowired
    private ResumeService resumeService;

    /**
     * 查询公司收到的简历
     * 
     * @param pager
     * @param id
     * @return
     */
    public List<Map<String, String>> receiveResume(Paging<Map<String, String>> pager, String id) {
        return resumeMapper.findAllReceiveResume(pager.getRowBounds(), id);
    }

    /**
     * 查看公司收到的简历详情
     * 
     * @param id
     * @param recordId
     * @return
     */
    public Map<String, String> queryMyReceiveResume(String id, String recordId) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("id", id);
        queryMap.put("recordId", recordId);
        return jobRelResumeService.queryMyReceiveResume(queryMap);
    }

    /**
     * 更新简历状态
     * 
     * @param recordId
     * @return
     */
    public int updateJobRelResume(String recordId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", recordId);
        map.put("status", JobConstant.RESUME_STATUS_TWO);
        return jobRelResumeService.updateJobRelResume(map);
    }

    /**
     * 更新简历view
     * 
     * @param id
     */
    public void updateResume(String id) {
        Resume resume = new Resume();
        resume.setViews("1");
        resume.setId(id);
        resumeMapper.updateResume(resume);
    }

    /**
     * 简历详情信息
     * 
     * @param id
     * @return
     */
    public Resume previewResume(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return resumeService.previewResume(map);
    }

    /**
     * 浏览记录
     * 
     * @return
     */
    public int addLookRecord(String id, Long companyId, String createId) {
        Map<String, Object> map = new HashMap<>();
        map.put("resumeID", id);
        map.put("companyID", companyId);
        map.put("createId", createId);
        return resumeService.addLookRecord(map);
    }

    /**
     * 查询我收藏的简历
     * 
     * @param pager
     * @param id
     * @return
     */
    public List<Map<String, String>> findAllCollectResume(Paging<Map<String, String>> pager, String id) {
        return resumeMapper.findAllCollectResume(pager.getRowBounds(), id);
    }

    /**
     * 批量删除我收藏的简历
     * 
     * @param ids
     */
    public void batchDelCollectResume(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            CollectRecord r = new CollectRecord();
            r.setId(Long.parseLong(id));
            r.setIs_deleted(1);
            resumeService.del_collectResume(r);
        }
    }

    /**
     * 我下载的简历列表
     * 
     * @param pager
     * @param id
     * @return
     */
    public List<Map<String, String>> findAllDownloadResume(Paging<Map<String, String>> pager, String id) {
        return resumeMapper.findAllDownloadResume(pager.getRowBounds(), id);
    }

    /**
     * 批量删除我下载的简历
     *
     * @param ids
     */
    public void batchDelDownloadResume(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            DownloadRecord r = new DownloadRecord();
            r.setId(Long.parseLong(id));
            r.setIs_deleted(1);
            resumeService.del_downloadResume(r);
        }
    }

    /**
     * 查看我创建的简历信息
     * 
     * @param createId
     * @return
     */
    public List<String> selectIdsByCreateId(Long createId) {
        return resumeService.selectIdsByCreateId(createId);
    }

    /**
     * 创建简历
     * 
     * @param resume
     */
    public String setUpResume(Resume resume) {
        checkResumeParams(resume);
        resumeMapper.setUpResume(resume);
        return resume.getId();
    }

    /**
     * 更新简历
     * 
     * @param resume
     */
    public void updateResume(Resume resume) {
        resumeMapper.updateResume(resume);
    }

    /**
     * 更新简历是否公开接口
     *
     * @param resume
     */
    public void updateResumeIsPublic(Resume resume) {
        resumeMapper.updateResumeIsPublic(resume);
    }

    /**
     * 查看自己简历详情信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> previewMyResume(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return resumeMapper.previewMyResume(map);
    }

    /**
     * 创建简历字段校验
     *
     * @param resume
     */
    public void checkResumeParams(Resume resume) {
        if (StringUtils.isEmpty(resume.getTitle())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "简历名称不能为空");
        }
        // 姓名
        if (StringUtils.isEmpty(resume.getRealName())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "姓名不能为空");
        }
        // 性别
        if (StringUtils.isEmpty(resume.getSex())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "性别不能为空");
        }
        // 最高学历
        if (StringUtils.isEmpty(resume.getEducation())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "最高学历不能为空");
        }
        // 工作经验
        if (StringUtils.isEmpty(resume.getExperienceYear())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "工作经验不能为空");
        }
        // 出生年份
        if (StringUtils.isEmpty(resume.getBirthYear())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "出生年份不能为空");
        }
        // 手机
        if (StringUtils.isEmpty(resume.getMobile())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "手机不能为空");
        }
        // 邮箱
        if (StringUtils.isEmpty(resume.getEmail())) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "邮箱不能为空");
        }
    }
}

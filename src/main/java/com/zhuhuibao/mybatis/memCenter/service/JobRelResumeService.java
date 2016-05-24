package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.pojo.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.JobRelResume;
import com.zhuhuibao.mybatis.memCenter.mapper.JobRelResumeMapper;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
@Service
@Transactional
public class JobRelResumeService {

    private final static Logger log = LoggerFactory.getLogger(JobRelResumeService.class);

    /**
     *
     */
    @Autowired
    private JobRelResumeMapper jrrMapper;

    /**
     * 插入职位与简历的关系
     * @param jobID  职位ID
     * @param resumeID 简历ID
     * @return
     */
    public JsonResult insert(Long jobID,Long resumeID)
    {
        JsonResult jsonResult = new JsonResult();
        log.info("insert job relation resume");
        try {
            JobRelResume jrr = new JobRelResume();
            jrr.setJobID(jobID);
            jrr.setResumeID(resumeID);
            jrrMapper.insertSelective(jrr);
        }catch(Exception e)
        {
            log.error("insert job relation resume error",e);
        }
        return jsonResult;
    }

    /**
     * 查询此职位是否已被同一个人应聘，10天后可以再次申请职位
     * @param map
     * @return
     * @throws Exception
     */
    public Integer isExistApplyPosition(Map<String,Object> map) throws Exception
    {
        log.info("apply position count"+ StringUtils.mapToString(map));
        int count = 0;
        try {
            count = jrrMapper.isExistApplyPosition(map);
        }catch(Exception e)
        {
            log.error("apply position count",e);
            throw e;
        }
        return count;
    }
}

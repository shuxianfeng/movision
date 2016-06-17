package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
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
    public Response insert(Long jobID, Long resumeID,Long createid)
    {
        Response response = new Response();
        log.info("insert job relation resume jobID = "+jobID+" resumeID = "+resumeID+" createid = "+createid);
        try {
            JobRelResume jrr = new JobRelResume();
            jrr.setJobID(jobID);
            jrr.setResumeID(resumeID);
            jrr.setApplicantId(createid);
            jrrMapper.insertSelective(jrr);
        }catch(Exception e)
        {
            log.error("insert job relation resume error",e);
        }
        return response;
    }

    /**
     * 查询此职位是否已被同一个人应聘，10天后可以再次申请职位
     * @param map
     * @return
     * @throws Exception
     */
    public Integer isExistApplyPosition(Map<String,Object> map)
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

    /**
     * 删除职位和简历的关联关系
     * @param map
     * @return
     */
    public int deleteJobRelResume(Map<String,Object> map)
    {
        log.info("delete resume Relation job "+ StringUtils.mapToString(map));
        int result = 0;
        try {
               result = jrrMapper.deleteJobRelResume(map);
        }catch(Exception e)
        {
            log.error("delete resume relation job error!");
            throw e;
        }
        return result;
    }

    public int updateJobRelResume(Map<String,Object> map){
        try {
            return jrrMapper.updateJobRelResume(map);
        }catch(Exception e)
        {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public JobRelResume selectByPrimaryKey(String id){
        try {
            return jrrMapper.selectByPrimaryKey(Long.parseLong(id));
        }catch(Exception e)
        {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

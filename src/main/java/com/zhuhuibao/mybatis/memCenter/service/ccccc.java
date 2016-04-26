package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.aaa;
import com.zhuhuibao.mybatis.memCenter.mapper.bbbb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
@Service
@Transactional
public class Ccccc {

    private final static Logger log = LoggerFactory.getLogger(Ccccc.class);

    /**
     *
     */
    @Autowired
    private bbbb jrrMapper;

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
            aaa jrr = new aaa();
            jrr.setJobID(jobID);
            jrr.setResumeID(resumeID);
            jrrMapper.insertSelective(jrr);
        }catch(Exception e)
        {
            log.error("insert job relation resume error",e);
        }
        return jsonResult;
    }
}

package com.zhuhuibao.mybatis.tech.service;/**
 * @author Administrator
 * @version 2016/6/1 0001
 */

import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.mapper.TechExpertCourseMapper;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *技术培训，专家培训申请的课程
 *@author pl
 *@create 2016/6/1 0001
 **/
@Service
@Transactional
public class TechExpertCourseService {

    private final static Logger log = LoggerFactory.getLogger(TechExpertCourseService.class);

    @Autowired
    TechExpertCourseMapper tecMapper;

    /**
     * 插入专家培训，技术培训申请的课程
     * @param techExpertCourse 专家或者技术申请的课程
     * @return
     */
    public int insertTechExpertCourse(TechExpertCourse techExpertCourse)
    {
        int result = 0;
        log.info("insert tech data info "+ StringUtils.beanToString(techExpertCourse));
        try {
            result = tecMapper.insertSelective(techExpertCourse);
        }catch(Exception e)
        {
            log.error("insert tech expert course error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新专家培训，技术培训申请的课程
     * @param techExpertCourse 专家或者技术申请的课程
     * @return
     */
    public int updateTechExpertCourse(TechExpertCourse techExpertCourse)
    {
        int result = 0;
        log.info("update tech data info "+ StringUtils.beanToString(techExpertCourse));
        try {
            result = tecMapper.updateByPrimaryKeySelective(techExpertCourse);
        }catch(Exception e)
        {
            log.error("update tech expert course error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 查看专家培训，技术培训申请的课程
     * @param techExpertCourseID 专家或者技术申请的课程ID
     * @return
     */
    public TechExpertCourse selectTechExpertCourseInfo(Long techExpertCourseID)
    {
        TechExpertCourse techExpertCourse;
        log.info("update tech data info "+ techExpertCourseID);
        try {
            techExpertCourse = tecMapper.selectByPrimaryKey(techExpertCourseID);
        }catch(Exception e)
        {
            log.error("update tech expert course error!",e);
            throw e;
        }
        return techExpertCourse;
    }
}

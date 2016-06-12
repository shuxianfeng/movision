package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.tech.entity.TrainPublishCourse;
import com.zhuhuibao.mybatis.tech.mapper.PublishTCourseMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *技术，专家培训课程业务处理类
 *@author pl
 *@create 2016/6/2 0002
 **/
@Service
@Transactional
public class PublishTCourseService {

    private final static Logger log = LoggerFactory.getLogger(PublishTCourseService.class);

    @Autowired
    PublishTCourseMapper pCourseMapper;

    /**
     * 插入专家培训，技术培训发布的课程
     * @param course 专家或者技术发布的课程
     * @return
     */
    public int insertPublishCourse(TrainPublishCourse course)
    {
        int result = 0;
        log.info("insert publish course info "+ StringUtils.beanToString(course));
        try {
            //插入库存
            course.setStorageNumber(Integer.parseInt(course.getMaxBuyNumber()));
            //截止日期
            course.setExpiryDate(course.getExpiryDate()+" 23:59:59");
            result = pCourseMapper.insertSelective(course);
        }catch(Exception e)
        {
            log.error("insert publish course info  error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 查看已发布课程的详情
     * @param condition 搜索条件
     * @return
     */
    public TrainPublishCourse selectTrainCourseInfo(Map<String,Object> condition)
    {
        log.info("select train course info id = "+StringUtils.mapToString(condition));
        TrainPublishCourse course;
        try{
            course = pCourseMapper.selectByPrimaryKey(condition);
        }catch(Exception e)
        {
            log.error("select train course info error!",e);
            throw e;
        }
        return course;
    }

    /**
     * 更新发布的课程信息
     * @param course 课程信息
     * @return
     */
    public int updatePublishCourse(TrainPublishCourse course)
    {
        int result = 0;
        log.info("update publish course info "+StringUtils.beanToString(course));
        try {
        	
        	if(course.getMaxBuyNumber()!=null)
        	{
                //插入库存
                course.setStorageNumber(Integer.parseInt(course.getMaxBuyNumber()));
        	}
        	if(course.getExpiryDate()!=null)
        	{
                //截止日期
                course.setExpiryDate(course.getExpiryDate()+" 23:59:59");
        	}
            result = pCourseMapper.updateByPrimaryKeySelective(course);
        }catch(Exception e)
        {
            log.error("update publish course info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 课程上架
     * @param courseId 课程ID
     * @return
     */
    public int publishCourse(String courseId,Long omsOperateId)
    {
        int result = 0;
        log.info("publish courseId "+courseId);
        try {
            TrainPublishCourse course = new TrainPublishCourse();
            course.setPublisherid(omsOperateId);
            course.setCourseid(Long.valueOf(courseId));
            course.setStatus(Integer.valueOf(TechConstant.PublishCourseStatus.SALING.toString()));
            result = pCourseMapper.updateByPrimaryKeySelective(course);
        }catch(Exception e)
        {
            log.error("update publish course info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 查询申请的课程
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String,String>> findAllTrainCoursePager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all OMS tech data for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> courseList = null;
        try{
            courseList = pCourseMapper.findAllTrainCoursePager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all OMS tech data for pager error!",e);
            throw e;
        }
        return courseList;
    }

    /**
     * 查询已发布的课程
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String,String>> findAllPublishCoursePager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all publish train course for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> courseList = null;
        try{
            courseList = pCourseMapper.findAllPublishCoursePager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all publish train course for pager error!",e);
            throw e;
        }
        return courseList;
    }

    /**
     * 查询最新发布的培训课程
     * @param condition
     * @return
     */
    public List<Map<String,String>> findLatestPublishCourse(Map<String,Object> condition)
    {
        log.info("find Latest publish train course "+ StringUtils.mapToString(condition));
        List<Map<String,String>> courseList = null;
        try{
            courseList = pCourseMapper.findLatestPublishCourse(condition);
        }catch(Exception e)
        {
            log.error("find Latest publish train course error!",e);
            throw e;
        }
        return courseList;
    }

    /**
     * 查看培训课程详情
     * @param condition
     * @return
     */
    public List<Map<String,String>> previewTrainCourseDetail(Map<String,Object> condition)
    {
        log.info("find Latest publish train course "+ StringUtils.mapToString(condition));
        List<Map<String,String>> courseList = null;
        try{
            courseList = pCourseMapper.previewTrainCourseDetail(condition);
        }catch(Exception e)
        {
            log.error("find Latest publish train course error!",e);
            throw e;
        }
        return courseList;
    }

}

package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
/**
 * 技术培训，专家培训申请的课程
 * @author  penglong
 * @create 2016-05-27
 */
public interface TechExpertCourseMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TechExpertCourse record);

    TechExpertCourse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TechExpertCourse record);

}
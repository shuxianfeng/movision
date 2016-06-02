package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TrainPublishCourse;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 技术、专家培训课程数据层
 * @author  penglong
 * @create 2016-05-27
 */
public interface PublishTCourseMapper {
    int deleteByPrimaryKey(Long courseid);

    int insertSelective(TrainPublishCourse record);

    TrainPublishCourse selectByPrimaryKey(Long courseid);

    int updateByPrimaryKeySelective(TrainPublishCourse record);

    List<Map<String,String>> findAllPublishCoursePager(RowBounds rowBounds, Map<String,Object> condition);
}
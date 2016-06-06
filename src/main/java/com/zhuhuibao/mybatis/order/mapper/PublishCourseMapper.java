package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.PublishCourse;
import org.apache.ibatis.annotations.Param;

public interface PublishCourseMapper {
    int deleteByPrimaryKey(Long courseid);

    int insert(PublishCourse record);

    int insertSelective(PublishCourse record);

    PublishCourse selectByPrimaryKey(Long courseid);

    int updateByPrimaryKeySelective(PublishCourse record);

    int updateByPrimaryKeyWithBLOBs(PublishCourse record);

    int updateByPrimaryKey(PublishCourse record);

    int updateSubStockNum(@Param("courseId") Long courseId, @Param("number") int number);

    int updateAddStockNum(@Param("courseId") Long courseId, @Param("number") int number);

    int updateStatus(@Param("courseid") Long courseid, @Param("status") String status);
}
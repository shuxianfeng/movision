package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Job;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface JobMapper {
    //发布职位
    int publishPosition(Job job);

    //查询公司已发布的职位
    List<Job> findAllByPager(RowBounds rowBounds, @Param("id") String id);

    //查询公司发布的某条职位的信息
    Job getPositionByPositionId(@Param("id") String id);

    //删除已发布的职位
    int deletePosition(@Param("id") String id);

    //更新编辑已发布的职位
    int updatePosition(Job job);
}
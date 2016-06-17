package com.zhuhuibao.mybatis.project.mapper;

import com.zhuhuibao.mybatis.project.entity.ProjectLinkman;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目联系人信息 甲方乙方信息
 * @author  pl
 * @create  pl
 */
public interface ProjectLinkmanMapper {
    int insertSelective(ProjectLinkman record);

    ProjectLinkman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectLinkman record);

    List<ProjectLinkman> queryProjectLinkmanByProjectID(@Param(value = "projectid") Long projectid);

}
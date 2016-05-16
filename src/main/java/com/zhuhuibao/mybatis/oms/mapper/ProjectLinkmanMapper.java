package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ProjectLinkman;

/**
 * 项目联系人信息 甲方乙方信息
 * @author  pl
 * @create  pl
 */
public interface ProjectLinkmanMapper {
    int insertSelective(ProjectLinkman record);

    ProjectLinkman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectLinkman record);

}
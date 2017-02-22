package com.movision.mybatis.manager.mapper;

import com.movision.mybatis.manager.entity.Manager;

import java.util.Map;

public interface ManagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Manager record);

    int insertSelective(Manager record);

    Manager selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Manager record);

    int updateByPrimaryKey(Manager record);

    int deleteManagerToCircleid(Integer circleid);

    int addManagerToCircleAndUserid(Map<String, Integer> map);
}
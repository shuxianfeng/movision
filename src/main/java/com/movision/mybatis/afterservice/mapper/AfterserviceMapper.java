package com.movision.mybatis.afterservice.mapper;

import com.movision.mybatis.afterservice.entity.Afterservice;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterserviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Afterservice record);

    int insertSelective(Afterservice record);

    Afterservice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Afterservice record);

    int updateByPrimaryKey(Afterservice record);
}
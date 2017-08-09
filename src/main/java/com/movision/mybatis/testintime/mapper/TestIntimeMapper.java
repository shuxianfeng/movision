package com.movision.mybatis.testintime.mapper;

import com.movision.mybatis.testintime.entity.TestIntime;

public interface TestIntimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TestIntime record);

    int insertSelective(TestIntime record);

    TestIntime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestIntime record);

    int updateByPrimaryKey(TestIntime record);
}
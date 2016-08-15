package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import org.apache.ibatis.annotations.Param;

public interface MemInfoCheckMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MemInfoCheck record);

    int insertSelective(MemInfoCheck record);

    MemInfoCheck selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemInfoCheck record);

    int updateByPrimaryKeyWithBLOBs(MemInfoCheck record);

    int updateByPrimaryKey(MemInfoCheck record);

    MemInfoCheck findMemById(@Param("id") String id);

    String getStatusById(@Param("id") Long id);
}
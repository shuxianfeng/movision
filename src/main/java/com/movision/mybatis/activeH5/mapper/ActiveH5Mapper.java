package com.movision.mybatis.activeH5.mapper;

import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.activeH5.entity.ActiveH5Vo;
import com.movision.mybatis.take.entity.TakeVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ActiveH5Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActiveH5 record);

    int insertSelective(ActiveH5 record);

    ActiveH5 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActiveH5 record);

    int updateByPrimaryKey(ActiveH5 record);

    int deleteActive(int id);

    List<ActiveH5> findAllActive(ActiveH5 activeH5, RowBounds rowBounds);

    ActiveH5 queryActivityById(Integer id);

    int updatePageView(int activeid);

    ActiveH5Vo querySum(int activeid);

    ActiveH5 queryH5Describe(int activeid);

}
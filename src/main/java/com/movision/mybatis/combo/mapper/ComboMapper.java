package com.movision.mybatis.combo.mapper;

import com.movision.mybatis.combo.entity.Combo;
import com.movision.mybatis.combo.entity.ComboVo;

import java.util.List;

public interface ComboMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Combo record);

    int insertSelective(Combo record);

    Combo selectByPrimaryKey(Integer id);

    List<ComboVo> queryCombo(int goodsid);

    int queryComboStork(int comboid);

    int updateByPrimaryKeySelective(Combo record);

    int updateByPrimaryKey(Combo record);
}
package com.movision.mybatis.dinyuehao.mapper;

import com.movision.mybatis.dinyuehao.entity.Dinyuehao;
import com.movision.mybatis.fuwuhao.entity.Fuwuhao;

import java.util.Map;

public interface DinyuehaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dinyuehao record);

    int insertSelective(Dinyuehao record);

    Dinyuehao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dinyuehao record);

    int updateByPrimaryKey(Dinyuehao record);


    int unionidByOpenid(String unionid);
    String unionidByOpenids(String unionid);

    int updateFU(Fuwuhao fuwuhao);
    int selectO(String openid);

    Fuwuhao selectOc(String code);
}
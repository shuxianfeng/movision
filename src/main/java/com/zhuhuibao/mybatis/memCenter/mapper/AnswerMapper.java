package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Answer;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    int insertSelective(Answer record);

    Answer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Answer record);

    int updateByPrimaryKey(Answer record);
}
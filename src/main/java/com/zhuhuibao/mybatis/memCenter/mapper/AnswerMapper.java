package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Answer;

import java.util.List;
import java.util.Map;

public interface AnswerMapper {

    int answerQuestion(Answer answer);

    List<Map<String,String>> queryAnswerByQuestionId(String id);

}
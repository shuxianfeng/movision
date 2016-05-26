package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Answer;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AnswerMapper {

    int answerQuestion(Answer answer);

    List<Map<String,String>> queryAnswerByQuestionId(String id);

    List<Map<String,String>> findAllExpertAnswerListOms(RowBounds rowBounds);

}
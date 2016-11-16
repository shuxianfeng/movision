package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.Answer;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AnswerMapper {

    int answerQuestion(Answer answer);

    List<Map<String,Object>> queryAnswerByQuestionId(String id);

    List<Map<String,String>> findAllExpertAnswerListOms(RowBounds rowBounds);

    int updateAnswerInfo(Answer answer);

}
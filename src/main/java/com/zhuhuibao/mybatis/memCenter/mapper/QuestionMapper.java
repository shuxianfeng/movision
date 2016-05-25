package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Question;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface QuestionMapper {

    int askExpert(Question question);

    Map<String,String> queryMyQuestionById(String id);

    List<Map<String,String>> findAllExpertQuestion(RowBounds rowBounds,Map<String, Object> map);

    List<Map<String,String>> findAllMyAnswerQuestion(RowBounds rowBounds,Map<String, Object> map);

    List<Map<String,String>> findAllMyQuestion(RowBounds rowBounds,Map<String, Object> map);

}
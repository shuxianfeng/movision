package com.movision.mybatis.suggestion.service;

import com.movision.mybatis.suggestion.entity.Suggestion;
import com.movision.mybatis.suggestion.mapper.SuggestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/1/26 9:46
 */
@Service
public class SuggestionService {
    @Autowired
    SuggestionMapper suggestionMapper;

    private static Logger logger = LoggerFactory.getLogger(SuggestionService.class);

    public int insertSuggestion(Suggestion suggestion) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("用户对于平台等反馈信息开始提交");
            }
            return suggestionMapper.insertSelective(suggestion);
        } catch (Exception e) {
            logger.error("用户的反馈信息提交异常");
            throw e;
        }
    }
}

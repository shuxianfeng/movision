package com.movision.facade.index;

import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.suggestion.entity.Suggestion;
import com.movision.mybatis.suggestion.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author zhurui
 * @Date 2017/1/26 9:41
 */
@Service
public class SuggestionFacade {
    @Autowired
    SuggestionService suggestionService;

    /**
     * 提交用户反馈信息
     *
     * @param phone
     * @param content
     * @return
     */
    public int insertSuggestion(String phone, String content) {
        Suggestion suggestion = new Suggestion();
        suggestion.setUserid(ShiroUtil.getAppUserID());
        suggestion.setContent(content);
        suggestion.setPhone(phone);
        return suggestionService.insertSuggestion(suggestion);//插入用户反馈信息数据
    }
}

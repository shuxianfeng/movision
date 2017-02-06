package com.movision.facade.index;

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
public class FacadeSuggestion {
    @Autowired
    SuggestionService suggestionService;

    /**
     * 用户信息反馈
     *
     * @param userid
     * @param phone
     * @param content
     * @return
     */
    public int insertSuggestion(String userid, String phone, String content) {
        Suggestion suggestion = new Suggestion();
        suggestion.setUserid(Integer.parseInt(userid));
        suggestion.setContent(content);
        suggestion.setPhone(phone);
        suggestion.setIntime(new Date());
        return suggestionService.insertSuggestion(suggestion);//插入用户反馈信息数据
    }
}

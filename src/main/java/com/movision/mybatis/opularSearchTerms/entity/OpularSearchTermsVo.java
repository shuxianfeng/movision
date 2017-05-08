package com.movision.mybatis.opularSearchTerms.entity;

import org.springframework.data.annotation.Id;

/**
 * @Author zhanglei
 * @Date 2017/5/8 10:34
 */
public class OpularSearchTermsVo {

    @Id
    private String keywords;
    private Integer count;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

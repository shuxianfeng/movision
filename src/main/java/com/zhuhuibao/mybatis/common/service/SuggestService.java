package com.zhuhuibao.mybatis.common.service;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.common.entity.Suggest;
import com.zhuhuibao.mybatis.common.mapper.SuggestMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/7/21 0021.
 */
@Service
public class SuggestService {
    private static final Logger log = LoggerFactory.getLogger(SuggestService.class);

    @Autowired
    SuggestMapper suggestmapper;

    public int addSuggest(Suggest suggest) {
        try {
            Long createId = null != ShiroUtil.getCreateID() ? ShiroUtil.getCreateID() : 0L;
            suggest.setCreateId(createId);

            if (StringUtils.isBlank(suggest.getUserName())) {
                String userName = null != ShiroUtil.getCreateID() ? ShiroUtil.getMember().getCompanyName() : "匿名";
                suggest.setUserName(userName);
            }

            return suggestmapper.insertSelective(suggest);
        } catch (Exception e) {
            log.error("addSuggest error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, String>> findAllSuggest(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return suggestmapper.findAllSuggest(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("findAllSuggest error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public Map<String, String> querySuggestById(String id) {
        try {
            return suggestmapper.querySuggestById(id);
        } catch (Exception e) {
            log.error("findAllSuggest error,id=" + id + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public int updateSuggest(Map<String, Object> map) {
        try {
            return suggestmapper.updateSuggest(map);
        } catch (Exception e) {
            log.error("findAllSuggest error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }
}

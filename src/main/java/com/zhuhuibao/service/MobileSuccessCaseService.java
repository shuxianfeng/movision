package com.zhuhuibao.service;

import java.util.*;

import com.zhuhuibao.mybatis.memCenter.entity.SuccessCase;
import com.zhuhuibao.mybatis.memCenter.mapper.SuccessCaseMapper;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.news.entity.News;
import com.zhuhuibao.mybatis.news.entity.NewsRecommendPlace;
import com.zhuhuibao.mybatis.news.form.NewsForm;
import com.zhuhuibao.mybatis.news.mapper.NewsMapper;
import com.zhuhuibao.mybatis.news.mapper.NewsRecommendPlaceMapper;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 成功案例接口实现类
 *
 * @author liyang
 * @date 2016年10月17日
 */
@Service
@Transactional
public class MobileSuccessCaseService {

    private static final Logger log = LoggerFactory.getLogger(MobileSuccessCaseService.class);

    @Autowired
    private SuccessCaseMapper successCaseMapper;

    @Autowired
    private SuccessCaseService successCaseService;

    /**
     * 检索供应商对应的成功案例
     *
     * @param pager
     * @param id
     * @return
     */
    public List<Map<String, String>> findAllSuccessCaseList(Paging<Map<String, String>> pager, String id) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("status", "1");
        queryMap.put("createid", id);
        try {
            return successCaseMapper.findAllSuccessCaseList(pager.getRowBounds(), queryMap);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 成功案例详情
     *
     * @param id
     * @return
     */
    public SuccessCase querySuccessCaseById(String id) {
        SuccessCase sc = successCaseService.querySuccessCaseById(id);
        // 点击率加1
        sc.setViews(String.valueOf(Integer.parseInt(sc.getViews()) + 1));
        successCaseService.updateSuccessCase(sc);
        return sc;
    }
}

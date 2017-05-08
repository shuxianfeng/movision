package com.movision.mybatis.activityContribute.service;

import com.movision.mybatis.activityContribute.entity.ActivityContribute;
import com.movision.mybatis.activityContribute.entity.ActivityContributeVo;
import com.movision.mybatis.activityContribute.mapper.ActivityContributeMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/5/8 15:22
 */
@Service
public class ActivityContributeService {

    @Autowired
    private ActivityContributeMapper activityContributeMapper;

    private static Logger log = LoggerFactory.getLogger(ActivityContributeService.class);

    public List<ActivityContributeVo> findAllQueryActivityContribute(Map map, Paging<ActivityContributeVo> pager) {
        try {
            log.info("查询活动投稿");
            return activityContributeMapper.findAllQueryActivityContribute(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询活动投稿异常", e);
            throw e;
        }
    }

    /**
     * 查询活动投稿详情
     *
     * @param id
     * @return
     */
    public ActivityContribute queryContributeExplain(String id) {
        try {
            log.info("查询活动投稿详情");
            return activityContributeMapper.queryContributeExplain(id);
        } catch (Exception e) {
            log.error("查询活动投稿详情异常", e);
            throw e;
        }
    }
}

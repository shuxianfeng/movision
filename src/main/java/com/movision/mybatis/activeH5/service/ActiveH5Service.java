package com.movision.mybatis.activeH5.service;

import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.activeH5.entity.ActiveH5Vo;
import com.movision.mybatis.activeH5.mapper.ActiveH5Mapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/8/29 14:57
 */
@Service
@Transactional

public class ActiveH5Service {
    private static Logger log = LoggerFactory.getLogger(ActiveH5Service.class);
    @Autowired
    private ActiveH5Mapper activeH5Mapper;

    /**
     * 添加活动
     *
     * @param activeH5
     * @return
     */
    public int insertSelective(ActiveH5 activeH5) {
        try {
            log.info("添加活动");
            return activeH5Mapper.insertSelective(activeH5);
        } catch (Exception e) {
            log.error("添加活动失败", e);
            throw e;
        }
    }

    /**
     * 查询活动详情
     *
     * @param id
     * @return
     */
    public ActiveH5 queryActivityById(Integer id) {
        try {
            log.info("查询活动详情");
            return activeH5Mapper.queryActivityById(id);
        } catch (Exception e) {
            log.error("查询活动详情接口", e);
            throw e;
        }
    }


    /**
     * 删除活动
     *
     * @param id
     * @return
     */
    public int deleteActive(int id) {
        try {
            log.info("删除活动");
            return activeH5Mapper.deleteActive(id);
        } catch (Exception e) {
            log.error("删除活动失败", e);
            throw e;
        }
    }

    /**
     * 更新活动
     *
     * @param activeH5
     */
    public void updateActivity(ActiveH5 activeH5) {
        try {
            log.info("更新活动");
            activeH5Mapper.updateByPrimaryKeySelective(activeH5);
        } catch (Exception e) {
            log.error("更新活动异常", e);
            throw e;
        }
    }

    /**
     * 查询活动
     *
     * @param paging
     * @return
     */
    public List<ActiveH5> findAllActive(ActiveH5 activeH5, Paging<ActiveH5> paging) {
        try {
            log.info("查询活动");
            return activeH5Mapper.findAllActive(activeH5, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询活动失败", e);
            throw e;
        }
    }


    /**
     * 修改访问量
     *
     * @param activeid
     * @return
     */
    public int updatePageView(int activeid) {
        try {
            log.info("修改访问量");
            return activeH5Mapper.updatePageView(activeid);
        } catch (Exception e) {
            log.error("修改访问量失败", e);
            throw e;
        }
    }


    /**
     * 首页数据
     *
     * @param activeid
     * @return
     */
    public ActiveH5Vo querySum(int activeid) {
        try {
            log.info("首页数据");
            return activeH5Mapper.querySum(activeid);
        } catch (Exception e) {
            log.error("修首页数据失败", e);
            throw e;
        }
    }

    /**
     * 查询活动说名
     *
     * @param activeid
     * @return
     */
    public ActiveH5 queryH5Describe(int activeid) {
        try {
            log.info("查询活动说名");
            return activeH5Mapper.queryH5Describe(activeid);
        } catch (Exception e) {
            log.error("查询活动说名失败", e);
            throw e;
        }
    }


}

package com.movision.mybatis.accusation.service;

import com.movision.mybatis.accusation.entity.Accusation;
import com.movision.mybatis.accusation.mapper.AccusationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/25 11:07
 */
@Service
public class AccusationService {
    @Autowired
    private AccusationMapper accusationMapper;

    private static Logger LOGGER = LoggerFactory.getLogger(AccusationService.class);

    /**
     * 帖子举报
     *
     * @param acc
     * @return
     */
    public int insertPostByAccusation(Accusation acc) {
        try {
            LOGGER.info("举报帖子");
            return accusationMapper.insertAccusation(acc);
        } catch (Exception e) {
            LOGGER.error("举报帖子异常");
            throw e;
        }
    }

    /**
     * 查询用户是否举报过该帖子
     *
     * @param map
     * @return
     */
    public List<Accusation> queryAccusationByUserSum(Map map) {
        try {
            LOGGER.info("查询用户是否举报过该帖子");
            return accusationMapper.queryAccusationByUserSum(map);
        } catch (Exception e) {
            LOGGER.error("查询用户举报帖子异常");
            throw e;
        }
    }

    public Integer queryAccusationBySum(Integer posid) {
        try {
            LOGGER.info("查询帖子被举报次数");
            return accusationMapper.queryAccusationBySum(posid);
        } catch (Exception e) {
            LOGGER.error("查询帖子被举报次数失败");
            throw e;
        }
    }
}

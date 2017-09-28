package com.movision.mybatis.votingrecords.service;


import com.movision.mybatis.votingrecords.entity.Votingrecords;
import com.movision.mybatis.votingrecords.mapper.VotingrecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/8/30 10:17
 */
@Service
@Transactional
public class VotingrecordsService {
    private static Logger log = LoggerFactory.getLogger(VotingrecordsService.class);
    @Autowired
    private VotingrecordsMapper votingrecordsMapper;

    public int insertSelective(Votingrecords votingrecords) {
        try {
            log.info("添加投票记录");
            return votingrecordsMapper.insertSelective(votingrecords);
        } catch (Exception e) {
            log.error("添加投票记录失败", e);
            throw e;
        }
    }

    /**
     * 有没有透过
     *
     * @param
     * @return
     */
    public int queryHave(Map map) {
        try {
            log.info("有没有透过");
            return votingrecordsMapper.queryHave(map);
        } catch (Exception e) {
            log.error("有没有透过失败", e);
            throw e;
        }
    }


    public int activeHowToVote(int activeid) {
        try {
            log.info("什么投票方式");
            return votingrecordsMapper.activeHowToVote(activeid);
        } catch (Exception e) {
            log.error("什么投票方式失败", e);
            throw e;
        }
    }

    /**
     * 查询用户当天是否投票
     *
     * @param votingrecords
     * @return
     */
    public int queryUserByDye(Votingrecords votingrecords) {
        try {
            log.info("查询当天用户是否投票");
            return votingrecordsMapper.queryUserByDye(votingrecords);
        } catch (Exception e) {
            log.error("查询当天用户是否投票异常", e);
            throw e;
        }
    }

}

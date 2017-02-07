package com.movision.mybatis.rewarded.service;

import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.mapper.RewardedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/1/24 9:26
 */
@Service
public class RewardedService {

    private Logger log = LoggerFactory.getLogger(RewardedService.class);

    @Autowired
    RewardedMapper rewardedMapper;

    public int insertRewarded(Rewarded rewarded) {
        try {
            log.info("添加打赏记录");
            return rewardedMapper.insertSelective(rewarded);
        } catch (Exception e) {
            log.error("添加打赏记录异常");
            throw e;
        }
    }

    /**
     * 获取帖子总打赏积分
     *
     * @param postid
     * @return
     */
    public Integer queryRewardedBySum(Integer postid) {
        try {
            log.info("获取打赏积分");
            return rewardedMapper.queryRewardedBySum(postid);
        } catch (Exception e) {
            log.error("获取打赏积分失败");
            throw e;
        }
    }

}

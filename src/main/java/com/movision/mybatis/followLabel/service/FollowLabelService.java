package com.movision.mybatis.followLabel.service;

import com.movision.mybatis.followLabel.entity.FollowLabel;
import com.movision.mybatis.followLabel.mapper.FollowLabelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/25 9:57
 */
@Service
public class FollowLabelService {
    private static Logger log = LoggerFactory.getLogger(FollowLabelService.class);
    @Autowired
    private FollowLabelMapper followLabelMapper;

    public int insertSelective(FollowLabel record) {
        try {
            log.info("关注标签");
            return followLabelMapper.insertSelective(record);
        } catch (Exception e) {
            log.error("关注标签失败");
            throw e;
        }
    }


    public int yesOrNo(Map record) {
        try {
            log.info("是否关注标签");
            return followLabelMapper.yesOrNo(record);
        } catch (Exception e) {
            log.error("是否关注标签失败");
            throw e;
        }
    }

}

package com.movision.mybatis.share.service;

import com.movision.mybatis.share.mapper.SharesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/2/7 10:50
 */
@Service
public class SharesService {
    Logger log = LoggerFactory.getLogger(SharesService.class);
    @Autowired
    SharesMapper sharesMapper;

    /**
     * 获取活动或帖子分享次数
     *
     * @param postid
     * @return
     */
    public Integer querysum(Integer postid) {
        try {
            log.info("查询帖子分享次数");
            return sharesMapper.querysum(postid);
        } catch (Exception e) {
            log.error("查询帖子分享次数异常");
            throw e;
        }
    }
}

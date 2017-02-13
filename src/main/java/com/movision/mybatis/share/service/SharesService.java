package com.movision.mybatis.share.service;

import com.movision.mybatis.share.entity.SharesVo;
import com.movision.mybatis.share.mapper.SharesMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/7 10:50
 */
@Service
@Transactional
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

    /**
     * 查询帖子分享列表
     *
     * @param pager
     * @param postid
     * @return
     */
    public List<SharesVo> queryPostShareList(Paging<SharesVo> pager, Integer postid) {
        try {
            log.info("查询帖子分享列表");
            return sharesMapper.findAllQueryPostShareList(pager.getRowBounds(), postid);
        } catch (Exception e) {
            log.error("查询帖子分享列表异常");
            throw e;
        }
    }
}

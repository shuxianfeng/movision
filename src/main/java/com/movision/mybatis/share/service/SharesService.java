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
import java.util.Map;

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
            log.error("查询帖子分享次数异常", e);
            throw e;
        }
    }

    /**
     * 查询帖子分享列表
     *
     * @param
     * @param map
     * @return
     */
    public List<SharesVo> queryPostShareList(Paging<SharesVo> pager, Map map) {
        try {
            log.info("查询帖子分享列表");
            return sharesMapper.findAllQueryPostShareList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询帖子分享列表异常", e);
            throw e;
        }
    }

    /**
     * 根据用户id查询用户被分享的分享列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<SharesVo> querySharePostList(Map map, Paging<SharesVo> pager) {
        try {
            log.info("根据用户id查询用户被分享的分享列表");
            return sharesMapper.findAllqueryShareList(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("根据用户id查询用户被分享的分享列表", e);
            throw e;
        }
    }

    /**
     * 查询用户分享帖子的分享列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<SharesVo> queryUsersSharePostsList(String userid, Paging<SharesVo> pager) {
        try {
            log.info("查询用户分享帖子的分享列表");
            return sharesMapper.queryUsersSharePostsList(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询用户分享帖子的分享列表异常", e);
            throw e;
        }
    }
}

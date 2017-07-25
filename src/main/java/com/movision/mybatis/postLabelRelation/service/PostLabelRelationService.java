package com.movision.mybatis.postLabelRelation.service;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.postLabelRelation.mapper.PostLabelRelationMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/7/24 18:18
 */
@Service
public class PostLabelRelationService {
    private static Logger log = LoggerFactory.getLogger(PostLabelRelationService.class);
    @Autowired
    private PostLabelRelationMapper postLabelRelationMapper;


    public int labelPost(int labelid) {
        try {
            log.info("查询标签对应帖子个数");
            return postLabelRelationMapper.labelPost(labelid);
        } catch (Exception e) {
            log.error("查询标签对应帖子个数失败", e);
            throw e;
        }
    }

    public List<Integer> postList(int labelid) {
        try {
            log.info("查询帖子id");
            return postLabelRelationMapper.postList(labelid);
        } catch (Exception e) {
            log.error("查询帖子id失败", e);
            throw e;
        }
    }

    public List<PostVo> post(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.post(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }


    public List<PostVo> postHeatValue(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.postHeatValue(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }


    public List<PostVo> postIseecen(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.postIseecen(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }

    public int batchAdd(List<PostLabelRelation> postLabelRelationList) {
        try {
            log.info("批量增加帖子和标签的关系数据");
            return postLabelRelationMapper.batchInsert(postLabelRelationList);
        } catch (Exception e) {
            log.error("批量增加帖子和标签的关系数据失败", e);
            throw e;
        }
    }


}

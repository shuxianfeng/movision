package com.movision.mybatis.postLabelRelation.service;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.postLabelRelation.mapper.PostLabelRelationMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<PostVo> findAllPost(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.findAllPost(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }


    public List<PostVo> findAllPostHeatValue(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.findAllPostHeatValue(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }


    public List<PostVo> findAllPostIseecen(List postid, Paging<PostVo> paging) {
        try {
            log.info("查询帖子");
            return postLabelRelationMapper.findAllPostIseecen(postid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子失败", e);
            throw e;
        }
    }

    /**
     * 删除帖子和标签关系
     *
     * @param postid
     */
    public void deletePostLabelRelaton(Integer postid) {
        try {
            log.info("删除帖子和标签关系");
            postLabelRelationMapper.deletePostLabelRelaton(postid);
        } catch (Exception e) {
            log.error("删除帖子和标签关系异常", e);
            throw e;
        }
    }

    public int batchAdd(Map map) {
        try {
            log.info("批量增加帖子和标签的关系数据");
            return postLabelRelationMapper.batchInsert(map);
        } catch (Exception e) {
            log.error("批量增加帖子和标签的关系数据失败", e);
            throw e;
        }
    }

    /**
     * 查询帖子对应标签
     *
     * @param relation
     * @return
     */
    public List<PostLabel> queryPostLabelByPostid(PostLabelRelation relation) {
        try {
            log.info("根据帖子id查询对应标签");
            return postLabelRelationMapper.queryPostLabelByPostid(relation);
        } catch (Exception e) {
            log.error("查询帖子对应标签异常", e);
            throw e;
        }
    }

    public int followlabel(int labelid) {
        try {
            log.info("关注标签认识");
            return postLabelRelationMapper.followlabel(labelid);
        } catch (Exception e) {
            log.error("关注标签认识异常", e);
            throw e;
        }
    }

    public int isFollowLabel(Map map) {
        try {
            log.info("该用户有没有关注该标签");
            return postLabelRelationMapper.isFollowLabel(map);
        } catch (Exception e) {
            log.error("该用户有没有关注该标签异常", e);
            throw e;
        }
    }

    public void insertPostToLabel(PostLabelRelation relation) {
        try {
            log.info("新增帖子和标签关系");
            postLabelRelationMapper.insertPostLabelRelation(relation);
        } catch (Exception e) {
            log.error("新增帖子和标签关系异常", e);
            throw e;
        }
    }

}

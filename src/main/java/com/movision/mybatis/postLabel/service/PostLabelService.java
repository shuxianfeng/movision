package com.movision.mybatis.postLabel.service;

import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabel.mapper.PostLabelMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.peer.LabelPeer;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/19 15:50
 */
@Service
public class PostLabelService {

    private static Logger log = LoggerFactory.getLogger(PostLabelService.class);
    @Autowired
    private PostLabelMapper postLabelMapper;

    public List<PostLabel> queryLabelName() {
        try {
            log.info("查询所有标签");
            return postLabelMapper.queryLableName();
        } catch (Exception e) {
            log.error("查询所有标签失败", e);
            throw e;
        }
    }

    public Integer updateLabelHeatValue(Map map) {
        try {
            log.info("根据标签修改热度");
            return postLabelMapper.updateLabelHeatValue(map);
        } catch (Exception e) {
            log.error("根据标签修改热度失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelHeatValue() {
        try {
            log.info("根据热度排序");
            return postLabelMapper.queryLabelHeatValue();
        } catch (Exception e) {
            log.error("根据热度排序失败", e);
            throw e;
        }
    }

    public PostLabelTz queryName(int labelid) {
        try {
            log.info("查询名称");
            return postLabelMapper.queryName(labelid);
        } catch (Exception e) {
            log.error("查询名称失败", e);
            throw e;
        }
    }

    /**
     * 查询帖子标签列表
     *
     * @param label
     * @param pag
     * @return
     */
    public List<PostLabelDetails> findAllQueryPostLabelList(PostLabel label, Paging<PostLabelDetails> pag) {
        try {
            log.info("查询帖子标签列表");
            return postLabelMapper.findAllQueryPostLabelList(label, pag.getRowBounds());
        } catch (Exception e) {
            log.error("查询帖子标签列表异常", e);
            throw e;
        }
    }

    public int batchInsert(List<PostLabel> postLabelList) {
        try {
            log.info("新增帖子标签");
            return postLabelMapper.batchInsert(postLabelList);
        } catch (Exception e) {
            log.error("新增帖子标签失败", e);
            throw e;
        }
    }

    /**
     * 新增标签
     *
     * @param label
     */
    public void insertPostLabel(PostLabel label) {
        try {
            log.info("新增标签");
            postLabelMapper.insertSelective(label);
        } catch (Exception e) {
            log.error("新增标签异常", e);
            throw e;
        }
    }

    /**
     * 查询标签详情
     *
     * @param label
     * @return
     */
    public PostLabelDetails queryPostLabelById(PostLabel label) {
        try {
            log.info("查询标签详情");
            return postLabelMapper.queryPostLabelById(label);
        } catch (Exception e) {
            log.error("查询标签详情异常", e);
            throw e;
        }
    }

    /**
     * 修改帖子标签
     *
     * @param label
     */
    public void updatePostLabel(PostLabel label) {
        try {
            log.info("修改帖子标签");
            postLabelMapper.updateByPrimaryKeySelective(label);
        } catch (Exception e) {
            log.error("修改帖子标签异常", e);
            throw e;
        }
    }

    /**
     * 删除帖子标签
     *
     * @param label
     */
    public void deletePostLabel(PostLabel label) {
        try {
            log.info("删除帖子标签");
            postLabelMapper.deletePostLabel(label);
        } catch (Exception e) {
            log.error("删除帖子标签异常", e);
            throw e;
        }
    }

    /**
     * 查询标签是否推荐到首页
     *
     * @param label
     * @return
     */
    public Integer queryPostLabeIsRecommend(PostLabel label) {
        try {
            log.info("查询标签是否推荐到首页");
            return postLabelMapper.queryPostLabeIsRecommend(label);
        } catch (Exception e) {
            log.error("查询标签是否推荐到首页异常", e);
            throw e;
        }
    }


    /**
     * 标签推荐、取消推荐到发现页
     *
     * @param label
     */
    public void updatePostLabelIsRecommend(PostLabel label) {
        try {
            log.info("标签推荐、取消推荐到发现页");
            postLabelMapper.updatePostLabelIsRecommend(label);
        } catch (Exception e) {
            log.error("标签推荐、取消推荐到发现页异常", e);
            throw e;
        }
    }

    public List<Integer> queryLabelIdList(String[] strings) {
        try {
            log.info("根据标签名称集合查询标签id集合");
            return postLabelMapper.queryLabelIdList(strings);
        } catch (Exception e) {
            log.error("根据标签名称集合查询标签id集合失败", e);
            throw e;
        }
    }


    public List<PostLabelCount> queryCountLabelName(int labelid) {
        try {
            log.info("查询帖子标签列表");
            return postLabelMapper.queryCountLabelName(labelid);
        } catch (Exception e) {
            log.error("查询帖子标签列表异常", e);
            throw e;
        }
    }

    public CircleVo queryCircleByPostid(int circleid) {
        try {
            log.info("根据帖子id查询圈子标签");
            return postLabelMapper.queryCircleByPostid(circleid);
        } catch (Exception e) {
            log.error("根据帖子id查询圈子标签异常", e);
            throw e;
        }
    }


    public Integer postInCircle(int circleid) {
        try {
            log.info("今日在圈子发的帖子");
            return postLabelMapper.postInCircle(circleid);
        } catch (Exception e) {
            log.error("今日在圈子发的帖子异常", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelCircle(int circleid) {
        try {
            log.info("圈子里的标签");
            return postLabelMapper.queryLabelCircle(circleid);
        } catch (Exception e) {
            log.error("圈子里的标签异常", e);
            throw e;
        }
    }

    public List<PostVo> findAllHotPost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的最热帖子");
            return postLabelMapper.findAllHotPost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的最热帖子异常", e);
            throw e;
        }
    }

    public List<PostVo> findAllNewPost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的最新帖子");
            return postLabelMapper.findAllNewPost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的最新帖子失败", e);
            throw e;
        }
    }

    public List<PostVo> findAllIsencePost(int circleid, Paging<PostVo> paging) {
        try {
            log.info("圈子里的精华帖子");
            return postLabelMapper.findAllIsencePost(circleid, paging.getRowBounds());
        } catch (Exception e) {
            log.error("圈子里的精华帖子失败", e);
            throw e;
        }
    }

    public List<CircleCount> queryCirclePeople(int circleid) {
        try {
            log.info("圈子达人");
            return postLabelMapper.queryCirclePeople(circleid);
        } catch (Exception e) {
            log.error("圈子达人失败", e);
            throw e;
        }
    }

    public Integer countSameNameLabel(String name) {
        try {
            log.info("统计同名标签数量");
            return postLabelMapper.countSameNameLabel(name);
        } catch (Exception e) {
            log.error("统计同名标签数量失败", e);
            throw e;
        }
    }


    public List<PostLabel> isrecommendLabel() {
        try {
            log.info("有没有推荐标签");
            return postLabelMapper.isrecommendLabel();
        } catch (Exception e) {
            log.error("有没有推荐标签失败", e);
            throw e;
        }
    }


    public List<Integer> labelId(int userid) {
        try {
            log.info("用户关注标签");
            return postLabelMapper.labelId(userid);
        } catch (Exception e) {
            log.error("用户关注标签失败", e);
            throw e;
        }
    }


}

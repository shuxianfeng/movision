package com.movision.mybatis.post.service;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 14:27
 */
@Service
public class PostService {
    private static Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostMapper postMapper;

    public List<PostVo> queryTodayEssence() {
        return postMapper.queryTodayEssence();
    }

    public List<PostVo> queryDayAgoEssence(int dayago) {
        return postMapper.queryDayAgoEssence(dayago);
    }

    public List<Circle> queryMayLikeCircle(int userid) {
        return postMapper.queryMayLikeCircle(userid);
    }

    public List<Circle> queryRecommendCircle() {
        return postMapper.queryRecommendCircle();
    }

    public List<Post> queryHotActiveList() {
        return postMapper.queryHotActiveList();
    }

    public List<Post> queryCircleSubPost(int circleid) {
        return postMapper.queryCircleSubPost(circleid);
    }

    public int queryPostNumByCircleid(int circleid) {
        try {
            log.info("查询圈子中更新的帖子数");
            return postMapper.queryPostNumByCircleid(circleid);
        } catch (Exception e) {
            log.error("查询圈子中更新的帖子数失败");
            throw e;
        }
    }

    public PostVo queryPostDetail(Map<String, Object> parammap) {
        try {
            log.info("查询帖子详情");
            return postMapper.queryPostDetail(parammap);
        } catch (Exception e) {
            log.error("查询帖子详情失败");
            throw e;
        }
    }

    public String queryVideoUrl(int postid) {
        try {
            log.info("查询原生帖子详情");
            return postMapper.queryVideoUrl(postid);
        } catch (Exception e) {
            log.error("查询原生帖子详情失败");
            throw e;
        }
    }

    public List<PostVo> queryPastPostList(Map<String, Object> parammap) {
        return postMapper.queryPastPostList(parammap);
    }

    public List<PostVo> queryPostList(Paging<Post> pager, String circleid) {
        try {
            log.info("查询某个圈子发出的所有帖子列表");
            return postMapper.queryPostList(pager.getRowBounds(), Integer.parseInt(circleid));
        } catch (Exception e) {
            log.error("查询帖子列表失败");
            throw e;
        }
    }

    public List<PostVo> queryAllActive(Paging<Post> pager) {
        try {
            log.info("查询所有活动列表");
            return postMapper.queryAllActive(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有活动列表失败");
            throw e;
        }
    }

    public int queryUserPartSum(Map<String, Object> parammap) {
        try {
            log.info("查询该用户有没有参与过该活动");
            return postMapper.queryUserPartSum(parammap);
        } catch (Exception e) {
            log.error("查询该用户有没有参与过该活动失败");
            throw e;
        }
    }

    public int saveActiveRecord(Map<String, Object> parammap) {
        try {
            log.info("保存用户参与告知类活动的记录");
            return postMapper.saveActiveRecord(parammap);
        } catch (Exception e) {
            log.error("保存用户参与告知类活动的记录失败");
            throw e;
        }
    }

    public ActiveVo queryNoticeActive(String postid) {
        try {
            log.info("查询告知类活动详情");
            return postMapper.queryNoticeActive(Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询告知类活动详情失败");
            throw e;
        }

    }

    //查询某个帖子所属圈子
    public int queryPostByCircleid(String postid) {
        try {
            log.info("查询某个帖子所属圈子");
            return postMapper.queryPostByCircleid(Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询某个帖子所属圈子失败");
            throw e;
        }
    }

    //APP端发布普通帖
    public int releasePost(Post post) {
        try {
            log.info("APP端普通帖发布成功");
            return postMapper.releasePost(post);
        } catch (Exception e) {
            log.error("APP端普通帖发布失败");
            throw e;
        }
    }

    //跟新帖子点赞次数
    public int updatePostByZanSum(int id) {
        try {
            log.info("更新帖子点赞次数");
            return postMapper.updatePostByZanSum(id);
        } catch (Exception e) {
            log.error("帖子点赞次数更新异常");
            throw e;
        }
    }

    public int queryPostByZanSum(int id) {
        try {
            log.info("查询帖子点赞次数");
            return postMapper.queryPostByZanSum(id);
        } catch (Exception e) {
            log.info("查看帖子点赞次数异常");
            throw e;
        }
    }

    public int updatePostBycommentsum(int postid) {
        try {
            log.info("更新帖子的评论数量");
            return postMapper.updatePostBycommentsum(postid);
        } catch (Exception e) {
            log.error("帖子更新评论数量异常");
            throw e;
        }
    }
}

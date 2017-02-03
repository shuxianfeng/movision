package com.movision.mybatis.post.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    List<PostVo> queryTodayEssence();

    List<PostVo> queryDayAgoEssence(int dayago);

    List<Circle> queryMayLikeCircle(int userid);

    List<Circle> queryRecommendCircle();

    List<Post> queryHotActiveList();

    List<Post> queryCircleSubPost(int circleid);

    int queryPostNumByCircleid(int circleid);

    PostVo queryPostDetail(Map<String, Object> parammap);

    String queryVideoUrl(int postid);

    List<PostVo> queryPastPostList(Map<String, Object> parammap);

    List<PostVo> queryPostList(RowBounds rowBounds, int circleid);

    List<PostVo> queryAllActive(RowBounds rowBounds);

    int queryPostByCircleid(int id);

    int releasePost(Post post);

    int updatePostByZanSum(int id);

    int queryPostByZanSum(int id);

    int updatePostBycommentsum(int postid);
}
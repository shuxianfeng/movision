package com.movision.mybatis.post.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
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

    List<PostVo> personPost(RowBounds rowBounds, int userid);

    List<ActiveVo> personActive(RowBounds rowBounds, int userid);

    int queryPostNumByCircleid(int circleid);

    PostVo queryPostDetail(Map<String, Object> parammap);

    String queryVideoUrl(int postid);

    List<PostVo> queryPastPostList(Map<String, Object> parammap);

    List<PostVo> queryPostList(RowBounds rowBounds, int circleid);

    List<PostVo> queryAllActive(RowBounds rowBounds);

    int queryActivePartSum(int postid);

    int queryUserPartSum(Map<String, Object> parammap);

    int saveActiveRecord(Map<String, Object> parammap);

    ActiveVo queryNoticeActive(int postid);

    int queryPostByCircleid(int id);

    int releasePost(Post post);

    void insertPostShareGoods(List<PostShareGoods> postShareGoodsList);

    List<Goods> queryCollectGoodsList(RowBounds rowBounds, int userid);

    List<Goods> queryAllGoodsList(RowBounds rowBounds);

    int updatePostByZanSum(int id);

    int queryPostByZanSum(int id);

    int updatePostBycommentsum(int postid);

    List<PostList> findAllqueryPostByList(RowBounds rowBounds);

    int queryPostNum();

    int deletePost(Integer postid);

    PostList queryPostParticulars(Integer postid);

    int addPost(Map map);

    PostNum queryPostNumAndisessenceByCircleid(Integer circleid);

    int addPostChoiceness(Integer postid);

    /*List postSearch(Map map);*/
}
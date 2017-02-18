package com.movision.mybatis.post.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertActive(Post post);//新增活动
    int insertPerid(Period period);//新增活动周期
    int insertSelective(Post record);


    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    List<PostVo> queryTodayEssence();

    List<PostVo> queryDayAgoEssence(int dayago);

    List<Circle> queryMayLikeCircle(int userid);

    List<Circle> queryRecommendCircle();

    List<Post> queryHotActiveList();

    List<Post> queryCircleSubPost(Map<String, Object> map);

    List<PostVo> personPost(RowBounds rowBounds, int userid);

    List<ActiveVo> personActive(RowBounds rowBounds, int userid);

    int queryPostNumByCircleid(int circleid);

    PostVo queryPostDetail(Map<String, Object> parammap);

    String queryVideoUrl(int postid);

    List<PostVo> queryPastPostList(Map<String, Object> parammap);

    List<PostVo> queryPostList(RowBounds rowBounds, int circleid);

    List<PostVo> pastHotPostList(RowBounds rowBounds, int circleid);

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

    List<PostList> findAllIsessenceByList(RowBounds rowBounds);//查询精贴

    int queryPostNum();

    int deletePost(Integer postid);

    PostList queryPostParticulars(Integer postid);


    int addPost(Map map);

    PostNum queryPostNumAndisessenceByCircleid(Integer circleid);

    int addPostChoiceness(Post postid);

    List<Post> queryPostChoiceness();

    int deletePostChoiceness(Integer postid);

    List<PostList> findAllActiveByList(RowBounds rowBounds);

    List<PostActiveList> findAllActiveTOByList(RowBounds rowBounds);

        int findAllPerson(Integer postid);//查询报名人数


    Double findAllActivefee(Integer postid);

    PostCompile queryPostByIdEcho(Integer postid);

    Integer updateByPrimaryKeySelectiveById(Post post);
    /*List postSearch(Map map);*/
}
package com.movision.mybatis.post.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.postProcessRecord.entity.PostProcessRecord;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserLike;
import com.movision.mybatis.video.entity.Video;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertActive(Post post);//新增活动

    int insertActivet(PostTo post);//新增活动

    int insertPerid(Period period);//新增活动周期

    int insertSelective(Post record);

    int insertSelectivet(PostTo record);

    int insertGoods(Map typ);

    int insertPromotionGoods(Map map);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    List<PostVo> queryTodayEssence();

    List<PostVo> queryDayAgoEssence(int dayago);

    List<Circle> queryMayLikeCircle(int userid);

    List<Circle> queryRecommendCircle();

    List<Post> queryHotActiveList();

    List<Post> queryCircleSubPost(Map<String, Object> map);

    List<PostVo> findAllPersonPost(RowBounds rowBounds, int userid);

    List<ActiveVo> findAllPersonActive(RowBounds rowBounds, int userid);

    int queryPostNumByCircleid(int circleid);

    PostVo queryPostDetail(Map<String, Object> parammap);

    int queryRewardSum(String postid);

    List<UserLike> queryRewardPersonNickname(String postid);
    Video queryVideoUrl(int postid);

    List<PostVo> queryPastPostList(Map<String, Object> parammap);

    List<PostVo> findAllPostList(RowBounds rowBounds, int circleid);

    List<PostVo> findAllPastHotPostList(RowBounds rowBounds, int circleid);

    List<PostVo> findAllActive(RowBounds rowBounds);

    int queryActivePartSum(int postid);

    int queryUserPartSum(Map<String, Object> parammap);

    int saveActiveRecord(Map<String, Object> parammap);

    ActiveVo queryNoticeActive(Map<String, Object> parammap);

    List<ActiveVo> queryFourHotActive();

    List<Date> findAllDateSelect(RowBounds rowBounds);

    int queryPostByCircleid(int id);

    int releasePost(Post post);

    int updatePostIsdel(String vid);

    void insertPostShareGoods(List<PostShareGoods> postShareGoodsList);

    int delPost(Map<String, Object> parammap);

    List<Goods> queryCollectGoodsList(int userid);

    List<Goods> findAllGoodsList(RowBounds rowBounds);

    int queryIsZanPost(Map<String, Object> parammap);

    void insertZanRecord(Map<String, Object> parammap);

    int updatePostByZanSum(int id);

    int queryPostByZanSum(int id);

    void updatePostCollectCount(Map<String, Object> parammap);

    int updatePostBycommentsum(int postid);

    int updatePostBycommentsumT(int postid);

    List<PostList> findAllqueryPostByList(RowBounds rowBounds);

    List<PostList> findAllqueryPostByList2(Integer userid, RowBounds rowBounds);

    List<PostList> findAllqueryPostByManageByList(Map map, RowBounds rowBounds);

    List<PostList> findAllqueryPostByCircleId(Map map, RowBounds rowBounds);

    List<PostList> findAllQueryPostListByUserid(Map map, RowBounds rowBounds);

    List<PostList> findAllQueryCollectionListByUserid(String userid, RowBounds rowBounds);

    List<PostList> findAllIsessenceByList(Map map, RowBounds rowBounds);//查询精贴

    int deletePost(Integer postid);

    PostList queryPostParticulars(Integer postid);

    PostList queryActivityParticulars(Integer postid);

    int addPost(Map map);

    PostNum queryPostNumAndisessenceByCircleid(Integer circleid);

    int addPostChoiceness(PostTo postid);

    Integer queryPostByUser(String postid);

    int queryPostByIsessence(String id);

    int updatePostChoiceness(PostTo postTo);

    List<PostTo> queryPostChoicenesslist(Map essencedate);

    List<Post> queryPostIsessence();

    PostChoiceness queryPostChoiceness(Integer postid);

    int deletePostChoiceness(Integer postid);

    List<PostList> findAllActiveByList(RowBounds rowBounds);

    List<PostActiveList> findAllActiveTOByList(Map map, RowBounds rowBounds);

    int findAllPerson(Integer postid);//查询报名人数

    Double findAllActivefee(Integer postid);

    PostCompile queryPostByIdEcho(Integer postid);

    Integer updateByPrimaryKeySelectiveById(PostTo post);

    List<PostList> findAllpostSearch(Map spread, RowBounds rowBounds);

/*    List<PostList> findAllqueryPostByContributing(Map map,RowBounds rowBounds);*/

    List<PostList> findAllQueryCircleByPostList(Map map, RowBounds rowBounds);

    List<PostActiveList> findAllActivePostCondition(Map map, RowBounds rowBounds);

    List<Map> findAllMyCollectPost(RowBounds rowBounds, Map map);

    List<Post> queryMyPostList(Map map);

    Integer updateActiveById(PostActiveList postActiveList);//编辑活动帖子

    Integer updateActiveByIdP(Period period);//编辑活动帖子

    PostActiveList queryActiveById(Integer id);//根据id查询帖子

    List<PostList> findAllQueryCollectPostList(String userid, RowBounds rowBounds);

    Integer updateIshot(Integer id);//设为热门

    Integer activeIsHot(Integer id);//查询是不是热门

    Integer updateNoIshot(Integer id);//不是热门

    Integer queryUserByPostid(String postid);//根据帖子id查询用户id

    Integer addPostByisessencepool(Integer postid);//特邀嘉宾操作帖子，加入精选池

    List<PostList> findAllqueryPostByIsessencepoolList(Map map, RowBounds rowBounds);

    void updatePostShareNum(Map<String, Object> parammap);

    Integer queryPosterActivity(Integer postid);
}
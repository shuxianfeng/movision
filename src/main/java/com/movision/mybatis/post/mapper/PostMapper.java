package com.movision.mybatis.post.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.postHeatvalueEverydayRecord.entity.PostHeatvalueEverydayRecord;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postShareGoods.entity.PostShareGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
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

    List<ActiveVo> queryHotActiveList();

    int queryPostNumByUserid(int userid);

    int queryCollectByUserid(int userid);

    int queryZanSumByUserid(int userid);

    int queryEssencesumByUserid(int userid);

    List<Post> queryCircleSubPost(Map<String, Object> map);

    List<PostVo> findAllPersonPost(RowBounds rowBounds, int userid);

    List<ActiveVo> findAllPersonActive(RowBounds rowBounds, int userid);

    int queryPostNumByCircleid(int circleid);

    PostVo queryPostDetail(Map<String, Object> parammap);

    String queryCompressUrl(String coverimg);

    void updatePostCommentSum(Integer id);

    List<PostVo> queryPastPostList(Map<String, Object> parammap);

    List<PostVo> findAllPostList(RowBounds rowBounds, int circleid);

    List<PostVo> findAllPastHotPostList(RowBounds rowBounds, int circleid);

    List<PostVo> findAllActive(RowBounds rowBounds);

    int queryActivePartSum(int postid);

    List<PostVo> findAllCollectPostByUser(int userid, RowBounds rowBounds);

    int queryUserPartSum(Map<String, Object> parammap);

    int saveActiveRecord(Map<String, Object> parammap);

    ActiveVo queryNoticeActive(Map<String, Object> parammap);

    List<Date> findAllDateSelect(RowBounds rowBounds);

    int queryPostByCircleid(int id);

    int releasePost(Post post);

    int releaseModularPost(Post post);

    int updatePostIsdel(String vid);

    void insertPostShareGoods(List<PostShareGoods> postShareGoodsList);

    int delPost(Map<String, Object> parammap);

    List<Goods> queryCollectGoodsList(int userid);

    List<Goods> findAllGoodsList(RowBounds rowBounds);

    int queryIsZanPost(Map<String, Object> parammap);

    void insertZanRecord(Map<String, Object> parammap);

    int updatePostByZanSum(int id);

    void updatePostCollectCount(Map<String, Object> parammap);

    int updatePostBycommentsum(int postid);

    List<PostList> findAllqueryPostByList(RowBounds rowBounds);

    List<PostList> findAllqueryPostByManageByList(Map map, RowBounds rowBounds);

    List<PostList> findAllqueryPostByCircleId(Map map, RowBounds rowBounds);

    List<PostList> findAllQueryPostListByUserid(Map map, RowBounds rowBounds);

    List<PostList> findAllQueryCollectionListByUserid(String userid, RowBounds rowBounds);

    List<PostList> findAllIsessenceByList(Map map, RowBounds rowBounds);//查询精贴

    int deletePost(Integer postid);

    PostList queryPostParticulars(Integer postid);

    PostList queryActivityParticulars(Integer postid);

    int addPostChoiceness(PostTo postid);

    Integer queryPostByUser(String postid);

    Integer queryCircleByIDIsUpdate(PostTo post);

    int queryPostByIsessence(String id);

    int updatePostChoiceness(Map map);

    Integer queryPostHeate(Integer id);

    void updatePostByHeatValue(Map map);

    List<PostTo> queryPostChoicenesslist(Map essencedate);

    PostChoiceness queryPostChoiceness(Integer postid);

    int deletePostChoiceness(Integer postid);

    List<PostList> findAllActiveByList(RowBounds rowBounds);

    List<PostActiveList> findAllActiveTOByList(Map map, RowBounds rowBounds);

    int findAllPerson(Integer postid);//查询报名人数

    Double findAllActivefee(Integer postid);

    PostCompile queryPostByIdEcho(Integer postid);

    Integer updateByPrimaryKeySelectiveById(PostTo post);

    List<Post> getCostRecommendPost();

    List<Post> findAllCostRecommendPostList(RowBounds rowBounds);

    List<PostList> findAllpostSearch(Map spread, RowBounds rowBounds);

    List<PostList> findAllQueryCircleByPostList(Map map, RowBounds rowBounds);

    List<PostActiveList> findAllActivePostCondition(PostActiveList postActiveList, RowBounds rowBounds);

    List<Map> findAllMyCollectPost(RowBounds rowBounds, Map map);

    List<Post> queryMyPostList(Map map);

    PostActiveList queryActiveById(Integer id);//根据id查询帖子

    List<PostList> findAllQueryCollectPostList(String userid, RowBounds rowBounds);

    Integer updateNoIshot(Post id);//不是热门

    Integer queryUserByPostid(String postid);//根据帖子id查询用户id

    Integer addPostByisessencepool(Integer postid);//特邀嘉宾操作帖子，加入精选池

    List<PostList> findAllqueryPostByIsessencepoolList(Map map, RowBounds rowBounds);

    void updatePostShareNum(Map<String, Object> parammap);

    String selectAccid(Map map);

    Integer queryCrileid(int postid);

    List<Post> selectAllPost();

    String queryPostContentById(Map postid);

    List<PostVo> findAllPostHeatValue();//查询帖子的热度值

    List<PostVo> findAllPostListHeat();

    List<PostVo> queryPostCricle(int crileid);

    List<PostVo> queryoverPost(int crileid);

    List<Post> queryPostDetailById(int id);

    int queryPostIsessenceHeat(int postid);

    int updatePostHeatValue(Map map);//修改热度

    int queryPostHotHeat(int postid);

    int updateZanPostHeatValue(Map map);//修改热度


    int lessUserHeatValue(Map map);//用户热度
    int updateZeroHeatValue(int postid);

    int updateZeroUserHeatValue(int userid);
    int selectPostHeatValue(int postid);

    int selectUserHeatValue(int userid);
    //查询用户关注的圈子
    List<Integer> queryFollowCricle(int userid);

    List<Integer> queryFollowUser(int userid);


    List<PostVo> queryPostListByIds(List ids);

    List<PostVo> queryUserListByIds(List ids);

    List<PostVo> queryLabelListByIds(List ids);

    //根据圈子id查询帖子
    List<PostVo> findAllPostCrile(int circleid);

    List<PostVo> findAllNotCircle(List circleid);

    //根据用户查帖子
    List<PostVo> findUserPost(int postuserid);

    List<PostVo> findUserByLabelPost(List labelid);

    List<Integer> findPostByLabelId(int postid);

    String queryCityCode(String area);//查询code

    List<PostVo> queryCityPost(String area);

    List<PostVo> queryCityLabel(String area);

    String queryCityUserCode(int userid);

    List<PostVo> findAllCityPost(String citycode);//

    List<PostLabel> queryPostLabel(int postid);

    List<PostVo> findAllLabelAllPost(int labelid);

    List<PostVo> findAllPostByid(List postid, RowBounds rowBounds);

    List<PostVo> queryPost(int postid);

    List<PostVo> findAllUserPostList(int userid, RowBounds rowBounds);

    List<PostVo> findAllUserActive(int userid, RowBounds rowBounds);

    List<Post> querPostListByUser(int userid);

    int queryUserPostCount(int userid);
    int queryUserActiveCount(int userid);

    List<PostVo> findAllHotCommentPostInAll(RowBounds rowBounds);
    List<PostVo> findAllHotCommentPostInCurrentMonth(RowBounds rowBounds);

    List<PostVo> findAllMostZanPostInAll(RowBounds rowBounds);
    List<PostVo> findAllMostZanPostInCurrentMonth(RowBounds rowBounds);

    List<PostVo> findAllMostCollectInAll(RowBounds rowBounds);
    List<PostVo> findAllMostCollectInCurrentMonth(RowBounds rowBounds);

    List<PostVo> queryPostInAll(Map map);

    List<Post> findAllQueryActivitycontributeListById(Integer id, RowBounds rowBounds);


    List<Integer> queryActiveByOrderid();

    List<PostVo> findAllActivePost(int postid, RowBounds rowBounds);

    List<PostVo> findAllActivePostIntime(int postid, RowBounds rowBounds);

    int postUserId(int postid);

    List<Post> queryPostListByName(Map map);

    Post selectTitleById(Integer id);

    List<PostVo> findAllActivePostD(int id, RowBounds rowBounds);

    int zanIsPost(Map map);

    String queryOriginalDrawingUrl(String compressimgurl);

    Date queryPostIdByDate(Integer id);

    List<PostVo> queryPostListByHeatValue();

    Post queryxiaojijiPostForTest(String title);

    List<PostHeatvalueEverydayRecord> queryPostHeatvalueEveryday(Integer postid);

    List<PostXml> queryPostByXmlExport(Post post);

    List<PostList> findAllqueryXmlAnalysisAndPost(RowBounds rowBounds, Post post);

    int queryPostCountView(int id);

    List queryPostToLabelById(int id);

    int updateCountView(int postid);

    int updateRobotCountView(Map map);

    List<Post> queryAllPageHelper();

    PostReturnAll postReAll(Map map);

    List<PostVo> querySelectedSortedPosts(Integer[] ids);

    void deletePostByIDAndCircleid(Post post);

    List<Integer> queryPostIdByTitle(Post post);

    List<PostVo> findAllActivePost_20171220(@Param(value ="postid")String postid, RowBounds rowBounds, @Param(value = "device")String device);

    List<PostVo> findAllActivePostIntime_20171220(@Param(value ="postid")String postid, RowBounds rowBounds,  @Param(value = "device")String device);

    List<PostVo> findAllActivePostTake_20171220(@Param(value ="postid")String postid, RowBounds rowBounds, @Param(value = "device")String device);
}
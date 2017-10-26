package com.movision.mybatis.postLabel.mapper;

import com.movision.mybatis.circle.entity.CircleCount;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabel.entity.*;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.user.entity.User;
import javafx.geometry.Pos;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PostLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabel record);

    int insertSelective(PostLabel record);

    PostLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabel record);

    int updateByPrimaryKey(PostLabel record);

    List<PostLabelVo> findAllMineFollowLabel(Map<String, Object> parammap, RowBounds rowBounds);



    List<PostLabel> queryLabelHeatValue();

    List<PostLabelDetails> findAllQueryPostLabelList(PostLabelVo label, RowBounds rowBounds);

    PostLabelTz queryName(int labelid);

    List<PostLabelCount> queryCountLabelName(int labelid);

    int batchInsert(List<PostLabel> postLabelList);

    PostLabelDetails queryPostLabelById(PostLabel label);

    void deletePostLabel(PostLabel label);

    List<Integer> queryLabelIdList(String[] nameStr);

    List<PostLabel> queryLabelList(String[] nameStr);

    CircleVo queryCircleByPostid(int circleid);

    List<User> queryCircleManager(int circleid);


    List<PostLabel> queryLabelCircle(int circleid);

    List<PostVo> findAllHotPost(int circleid, RowBounds rowBounds);

    List<PostVo> findAllNewPost(int circleid, RowBounds rowBounds);

    List<PostVo> findAllIsencePost(int circleid, RowBounds rowBounds);

    List<CircleCount> queryCirclePeople(int circleid);

    List<PostLabel> isrecommendLabel();

    Integer queryPostLabeIsRecommend(PostLabel label);

    void updatePostLabelIsRecommend(PostLabel label);


    Integer countSameNormalNameLabel(String name);

    List<Integer> labelId(int userid);

    List<PostLabel> queryHotValueLabelList(Integer displayCount);

    List<PostLabel> queryPostLabelByName(PostLabel postLabel);

    int updatePostHeatValue(int postid);

    int queryPostHeatValue(int postid);

    int heatvale(int postid);

    int queryUserid(int postid);

    int queryUserHeatValue(int userids);

    int updateUserHeatValue(int userids);

    int userHeatVale(int userids);

    List<GeographicLabel> getfootmap(int userid);

    int queryCircleid(int postid);

    List<PostVo> queryCircleByPost(int circleid);

    int followCircle(int circleid);

    List<PostLabel> queryCityListByCityname(String name);

    List<PostLabel> queryGeogLabelByName(String name);

    int isFollowCircleid(Map map);

    List<PostVo> findAllLabelHotPost(Map map, RowBounds rowBounds);

    List<PostVo> findAllLabelNewPost(Map map, RowBounds rowBounds);

    List<PostVo> findAllLabelIsessenPost(Map map, RowBounds rowBounds);

    List<PostLabel> queryLabelByname(String name);

    List<PostLabel> findAllLabelByName(Map<String, Object> map, RowBounds rowBounds);

    PostLabel selectGeogLabelByCitycode(String citycode);

    Integer queryPostLabelByNameCompletely(String name);
}
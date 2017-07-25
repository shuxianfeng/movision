package com.movision.mybatis.postLabelRelation.mapper;

import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface PostLabelRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabelRelation record);

    int insertSelective(PostLabelRelation record);

    PostLabelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabelRelation record);

    int updateByPrimaryKey(PostLabelRelation record);

    int labelPost(int labelid);

    List<Integer> postList(int labelid);

    List<PostVo> post(List postid, RowBounds rowBounds);

    List<PostVo> postHeatValue(List postid, RowBounds rowBounds);

    List<PostVo> postIseecen(List postid, RowBounds rowBounds);

    int batchInsert(List<PostLabelRelation> postLabelRelationList);

}
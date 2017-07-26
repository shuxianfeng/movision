package com.movision.mybatis.postLabel.mapper;

import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.entity.PostLabelCount;
import com.movision.mybatis.postLabel.entity.PostLabelTz;
import com.movision.mybatis.postLabel.entity.PostLabelDetails;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabel record);

    int insertSelective(PostLabel record);

    PostLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabel record);

    int updateByPrimaryKey(PostLabel record);

    List<PostLabel> queryLableName();

    int updateLabelHeatValue(Map map);

    List<PostLabel> queryLabelHeatValue();

    List<PostLabelDetails> findAllQueryPostLabelList(PostLabel label, RowBounds rowBounds);

    PostLabelTz queryName(int labelid);

    List<PostLabelCount> queryCountLabelName(int labelid);

    int batchInsert(List<PostLabel> postLabelList);

    PostLabelDetails queryPostLabelById(PostLabel label);

    void deletePostLabel(PostLabel label);

    List<Integer> queryLabelIdList(String[] nameStr);

    CircleVo queryCircleByPostid(int postid);

    int postInCircle(int circleid);

    List<PostLabel> queryLabelCircle(int circleid);
}
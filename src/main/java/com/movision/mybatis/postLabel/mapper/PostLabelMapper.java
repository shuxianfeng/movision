package com.movision.mybatis.postLabel.mapper;

import com.movision.mybatis.postLabel.entity.PostLabel;

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
}
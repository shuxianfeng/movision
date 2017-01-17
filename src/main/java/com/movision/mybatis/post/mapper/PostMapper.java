package com.movision.mybatis.post.mapper;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;

import java.util.List;

public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKey(Post record);

    List<PostVo> queryIndexData();
}
package com.movision.mybatis.postDes.mapper;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postDes.entity.PostDes;

import java.util.List;

public interface PostDesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostDes record);

    int insertSelective(PostDes record);

    PostDes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostDes record);

    int updateByPrimaryKeyWithBLOBs(PostDes record);

    int updateByPrimaryKey(PostDes record);

    void batchAdd(List<Post> postList);

    int addFromPost(Post post);
}
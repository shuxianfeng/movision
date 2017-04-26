package com.movision.mybatis.postSensitiveWords.mapper;

import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostSensitiveWordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostSensitiveWords record);

    int insertSelective(PostSensitiveWords record);

    PostSensitiveWords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostSensitiveWords record);

    int updateByPrimaryKey(PostSensitiveWords record);

    List<PostSensitiveWords> findAllPostSensitiveWords(RowBounds rowBounds);

    List<PostSensitiveWords> findAllPostCodition(Map map, RowBounds rowBounds);
}
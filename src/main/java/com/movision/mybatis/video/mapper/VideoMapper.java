package com.movision.mybatis.video.mapper;

import com.movision.mybatis.video.entity.Video;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    int updateVoid(Video video);

    int queryVideoByID(Integer pid);
}
package com.movision.mybatis.followCircle.mapper;

import com.movision.mybatis.followCircle.entity.FollowCircle;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface FollowCircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowCircle record);

    int insertSelective(FollowCircle record);

    FollowCircle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowCircle record);

    int updateByPrimaryKey(FollowCircle record);

    int queryCountByFollow(Map<String, Object> parammap);

    void followCircle(Map<String, Object> parammap);

    void cancelFollowCircle(Map<String, Object> parammap);
}
package com.movision.mybatis.followUser.mapper;

import com.movision.mybatis.followUser.entity.FollowUser;
import com.movision.mybatis.followUser.entity.FollowUserVo;

import java.util.List;
import java.util.Map;

public interface FollowUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowUser record);

    int insertSelective(FollowUser record);

    FollowUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowUser record);

    int updateFollowuserVo(FollowUserVo followUserVo);

    int updateByPrimaryKey(FollowUser record);

    int yesOrNo(Map map);

    int cancleFollowUser(Map map);

    List<FollowUserVo> selectFollowUserVoList(Integer userid);

    int insertUserFans(int interestedusers);
}
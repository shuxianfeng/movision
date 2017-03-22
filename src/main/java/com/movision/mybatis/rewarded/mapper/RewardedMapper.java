package com.movision.mybatis.rewarded.mapper;

import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RewardedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Rewarded record);

    int insertSelective(Rewarded record);

    Rewarded selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rewarded record);

    int updateByPrimaryKey(Rewarded record);

    List<RewardedVo> findAllqueryPostAward(int postid, RowBounds rowBounds);

    int insertRewardRecord(Map map);

}
package com.movision.mybatis.followLabel.mapper;

import com.movision.mybatis.followLabel.entity.FollowLabel;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface FollowLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowLabel record);

    int insertSelective(FollowLabel record);

    FollowLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowLabel record);

    int updateByPrimaryKey(FollowLabel record);

    int yesOrNo(Map map);

    int cancleLabel(Map map);

    int updatePostLabel(int labelid);

    int updatePostLabelLess(int labelid);
    int updateLabelHeatValue(Map map);
}
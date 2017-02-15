package com.movision.mybatis.activePart.mapper;

import com.movision.mybatis.activePart.entity.ActivePart;
import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.post.entity.PostActiveList;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ActivePartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivePart record);

    int insertSelective(ActivePart record);

    ActivePart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivePart record);

    int updateByPrimaryKey(ActivePart record);

    List<ActivePartList> findAllCllActive(RowBounds rowBounds);//查询报名列表
}
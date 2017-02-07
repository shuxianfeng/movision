package com.movision.mybatis.share.mapper;

import com.movision.mybatis.share.entity.Shares;

public interface SharesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shares record);

    int insertSelective(Shares record);

    Shares selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shares record);

    int updateByPrimaryKey(Shares record);

    Integer querysum(Integer postid);
}
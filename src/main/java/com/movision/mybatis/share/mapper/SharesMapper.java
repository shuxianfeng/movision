package com.movision.mybatis.share.mapper;

import com.movision.mybatis.share.entity.Shares;
import com.movision.mybatis.share.entity.SharesVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SharesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shares record);

    int insertSelective(Shares record);

    Shares selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shares record);

    int updateByPrimaryKey(Shares record);

    Integer querysum(Integer postid);

    List<SharesVo> findAllQueryPostShareList(RowBounds rowBounds, Integer postid);
}
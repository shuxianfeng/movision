package com.movision.mybatis.share.mapper;

import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.share.entity.Shares;
import com.movision.mybatis.share.entity.SharesVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SharesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shares record);

    int insertSelective(Shares record);

    Shares selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shares record);

    int updateByPrimaryKey(Shares record);

    Integer querysum(Integer postid);

    List<SharesVo> findAllQueryPostShareList(RowBounds rowBounds, Map map);

    List<SharesVo> findAllqueryShareList(Map map, RowBounds rowBounds);
}
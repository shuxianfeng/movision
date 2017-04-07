package com.movision.mybatis.PostZanRecord.mapper;

import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecordVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PostZanRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostZanRecord record);

    int insertSelective(PostZanRecord record);

    PostZanRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostZanRecord record);

    int updateByPrimaryKey(PostZanRecord record);

    PostZanRecordVo queryByUserid(String userid);

    List<Map> findAllZanList(String userid, RowBounds rowBounds);
}
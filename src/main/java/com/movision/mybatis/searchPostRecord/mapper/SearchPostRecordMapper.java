package com.movision.mybatis.searchPostRecord.mapper;

import com.movision.mybatis.searchPostRecord.entity.SearchPostRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SearchPostRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchPostRecord record);

    int insertSelective(SearchPostRecord record);

    SearchPostRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchPostRecord record);

    int updateByPrimaryKey(SearchPostRecord record);

    List<Map<String, Object>> selectPostSearchHotWord();

    List<Map<String, Object>> selectHistoryRecord(@Param("userid") Integer userid);

    int UpdateSearchIsdel(Integer userid);
}
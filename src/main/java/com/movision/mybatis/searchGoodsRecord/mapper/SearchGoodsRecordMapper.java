package com.movision.mybatis.searchGoodsRecord.mapper;

import com.movision.mybatis.searchGoodsRecord.entity.SearchGoodsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SearchGoodsRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchGoodsRecord record);

    int insertSelective(SearchGoodsRecord record);

    SearchGoodsRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchGoodsRecord record);

    int updateByPrimaryKey(SearchGoodsRecord record);

    List<Map<String, Object>> selectGoodsSearchHotWord();

    List<Map<String, Object>> selectGoodsHistoryRecord(@Param("userid") Integer userid);
}
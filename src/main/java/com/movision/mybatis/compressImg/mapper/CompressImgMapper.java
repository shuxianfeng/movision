package com.movision.mybatis.compressImg.mapper;

import com.movision.mybatis.compressImg.entity.CompressImg;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CompressImgMapper {
    int insert(CompressImg record);

    int insertSelective(CompressImg record);

    int queryIsHaveCompress(String imgurl);

    CompressImg getProtoImg(String imgurl);

    int queryCount(CompressImg compressImg);

    String queryUrlIsCompress(Map map);
}
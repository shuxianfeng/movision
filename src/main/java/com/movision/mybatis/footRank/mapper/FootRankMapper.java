package com.movision.mybatis.footRank.mapper;

import com.movision.mybatis.footRank.entity.FootRank;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootRankMapper {
    int insert(FootRank record);

    int insertSelective(FootRank record);

    List<FootRank> queryFootMapRank(int userid);
}
package com.zhuhuibao.mybatis.news.mapper;

import com.zhuhuibao.mybatis.news.entity.NewsRecommendPlace;

import java.util.List;

/**
 * 资讯位置业务相关数据层
 *
 * @author liyang
 * @date 2016年10月17日
 */
public interface NewsRecommendPlaceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NewsRecommendPlace record);

    int insertSelective(NewsRecommendPlace record);

    NewsRecommendPlace selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsRecommendPlace record);

    int updateByPrimaryKey(NewsRecommendPlace record);

    List<NewsRecommendPlace> getPlaceByNewsId(Long newsId);
}

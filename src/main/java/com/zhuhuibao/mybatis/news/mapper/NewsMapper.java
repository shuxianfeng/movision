package com.zhuhuibao.mybatis.news.mapper;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.news.form.NewsForm;
import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.news.entity.News;

/**
 * 资讯业务相关数据层
 *
 * @author liyang
 * @date 2016年10月17日
 */
public interface NewsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(News record);

    int insertSelective(News record);

    NewsForm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);

    /**
     * OMS分页获取资讯分页信息
     *
     * @param queryMap
     * @param rowBounds
     * @return
     */
    List<NewsForm> selNewsPager(Map queryMap, RowBounds rowBounds);

    /**
     * 批量发布资讯信息
     *
     * @param ids
     */
    void batchPubNews(List<Integer> ids);

    /**
     * 触屏端所有资讯列表
     *
     * @param rowBounds
     */
    List<NewsForm>  selAllMobileNews(RowBounds rowBounds);

    /**
     * 触屏端按热点排序资讯列表
     *
     * @param rowBounds
     */
    List<NewsForm>  selHotMobileNews(RowBounds rowBounds);

    /**
     * 触屏端按分类查询对应资讯列表信息
     *
     * @param type
     * @param subtype
     * @param rowBounds
     */
    List<NewsForm>  selMobileNewsByType(String type, String subtype, RowBounds rowBounds);
}

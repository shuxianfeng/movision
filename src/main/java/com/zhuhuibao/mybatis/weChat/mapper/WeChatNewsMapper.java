package com.zhuhuibao.mybatis.weChat.mapper;

import com.zhuhuibao.mybatis.weChat.entity.WeChatNews;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 微信h5资讯业务相关数据层
 *
 * @author liyang
 * @date 2016年11月22日
 */
public interface WeChatNewsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(WeChatNews record);

    int insertSelective(WeChatNews record);

    WeChatNews selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeChatNews record);

    int updateByPrimaryKey(WeChatNews record);

    List<WeChatNews> findAllWeChatNewsList(Map<String, Object> queryMap, RowBounds rowBounds);
}

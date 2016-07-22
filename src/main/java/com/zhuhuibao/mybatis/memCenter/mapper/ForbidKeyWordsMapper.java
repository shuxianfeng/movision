package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.ForbidKeyWords;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/6/23 0023.
 */
public interface ForbidKeyWordsMapper {

    List<Map<String,String>> queryKeyWordsList(Map<String,Object> map);

    int addForbidKeyWords(ForbidKeyWords forbidKeyWords);

    int updForbidKeyWords(ForbidKeyWords forbidKeyWords);
}

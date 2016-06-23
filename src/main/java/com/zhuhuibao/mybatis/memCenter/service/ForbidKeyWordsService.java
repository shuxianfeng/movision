package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.ForbidKeyWords;
import com.zhuhuibao.mybatis.memCenter.mapper.ForbidKeyWordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/18 0018.
 */
@Service
@Transactional
public class ForbidKeyWordsService {

    private static final Logger log = LoggerFactory.getLogger(ForbidKeyWordsService.class);

    @Autowired
    ForbidKeyWordsMapper forbidKeyWordsMapper;

    public List<Map<String,String>> queryKeyWordsList(Map<String,Object> map){
        try {
            return forbidKeyWordsMapper.queryKeyWordsList(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public int addForbidKeyWords(ForbidKeyWords forbidKeyWords){
        try {
            return forbidKeyWordsMapper.addForbidKeyWords(forbidKeyWords);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public int deletleForbidKeyWords(ForbidKeyWords forbidKeyWords){
        try {
            return forbidKeyWordsMapper.updForbidKeyWords(forbidKeyWords);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

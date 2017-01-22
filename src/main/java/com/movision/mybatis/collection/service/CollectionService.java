package com.movision.mybatis.collection.service;

import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.mapper.CollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/1/22 15:59
 */
@Service
public class CollectionService {

    private static Logger log = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private CollectionMapper collectionMapper;

    public int collectionPost(Collection collection) {
        try {

            log.error("用户访问帖子内容时收藏帖子");
            return collectionMapper.collectionPost(collection);

        } catch (Exception e) {

            log.error("用户访问帖子内容时收藏帖子失败");
            throw e;

        }

    }
}

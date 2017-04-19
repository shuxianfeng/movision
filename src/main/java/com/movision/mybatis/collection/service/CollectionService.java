package com.movision.mybatis.collection.service;

import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.mapper.CollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/22 15:59
 */
@Service
@Transactional
public class CollectionService {

    private static Logger log = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private CollectionMapper collectionMapper;

    public int checkIsHave(Collection collection) {
        try {
            log.info("检查该用户是否收藏过该帖子");
            return collectionMapper.checkIsHave(collection);
        } catch (Exception e) {
            log.error("检查该用户是否收藏过该帖子失败");
            throw e;
        }
    }

    public int checkIsHaveGoods(Collection collection) {
        try {
            log.info("检查该用户是否收藏过该商品");
            return collectionMapper.checkIsHaveGoods(collection);
        } catch (Exception e) {
            log.error("检查该用户是否收藏过该商品失败");
            throw e;
        }
    }

    public int collectionPost(Collection collection) {
        try {

            log.error("用户访问帖子内容时收藏帖子");
            return collectionMapper.collectionPost(collection);

        } catch (Exception e) {

            log.error("用户访问帖子内容时收藏帖子失败");
            throw e;
        }
    }

    public void cancelCollectionPost(Map<String, Object> parammap) {
        try {
            log.info("取消收藏帖子活动");
            collectionMapper.cancelCollectionPost(parammap);
        } catch (Exception e) {
            log.error("取消收藏帖子活动失败");
            throw e;
        }
    }

    public void cancelCollectionGoods(Map<String, Object> parammap) {
        try {
            log.info("取消收藏商品");
            collectionMapper.cancelCollectionGoods(parammap);
        } catch (Exception e) {
            log.error("取消收藏商品失败");
            throw e;
        }
    }

    public void addCollectionSum(int postid) {
        try {
            log.info("被收藏的帖子收藏数+1");
            collectionMapper.addCollectionSum(postid);
        } catch (Exception e) {
            log.error("被收藏的帖子收藏数+1失败");
            throw e;
        }
    }

    public int collectionGoods(Collection record) {
        try {
            log.info("用户收藏商品");
            return collectionMapper.insertSelective(record);
        } catch (Exception e) {
            log.error("用户收藏商品失败");
            throw e;
        }
    }
}

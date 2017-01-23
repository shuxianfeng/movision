package com.movision.facade.collection;

import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/1/22 15:13
 */
@Service
public class CollectionFacade {

    @Autowired
    private CollectionService collectionService;

    //在帖子中点击收藏帖子调用该方法
    public int collectionPost(String postid, String userid, String type) {

        Collection collection = new Collection();
        collection.setUserid(Integer.parseInt(userid));
        collection.setPostid(Integer.parseInt(postid));
        collection.setType(Integer.parseInt(type));

        return collectionService.collectionPost(collection);
    }

}

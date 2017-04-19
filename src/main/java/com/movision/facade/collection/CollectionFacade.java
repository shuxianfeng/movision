package com.movision.facade.collection;

import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.service.CollectionService;
import com.movision.mybatis.post.service.PostService;
import com.movision.utils.IdGenerator;
import org.apache.commons.collections.map.AbstractHashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/22 15:13
 */
@Service
public class CollectionFacade {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private PostService postService;

    //在帖子中点击收藏帖子调用该方法
    public int collectionPost(String postid, String userid, String type) {

        Collection collection = new Collection();
        collection.setUserid(Integer.parseInt(userid));
        collection.setPostid(Integer.parseInt(postid));
        collection.setType(Integer.parseInt(type));
        //先检查该用户有没有收藏过该帖子
        int count = collectionService.checkIsHave(collection);

        if (count == 0) {
            //该帖子的被收藏次数+1
            collectionService.addCollectionSum(Integer.parseInt(postid));
            return collectionService.collectionPost(collection);
        } else {
            return -1;
        }
    }

    //取消收藏帖子活动/商品调用该方法
    public void cancelCollection(String postid, String goodsid, String userid, String type) {
        if (type.equals("0")) {
            //取消收藏帖子活动
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            parammap.put("postid", Integer.parseInt(postid));
            collectionService.cancelCollectionPost(parammap);
            postService.updatePostCollectCount(parammap);
        } else if (type.equals("1")) {
            //取消收藏商品
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("userid", Integer.parseInt(userid));
            parammap.put("goodsid", Integer.parseInt(goodsid));
            collectionService.cancelCollectionGoods(parammap);//因为商品不计总收藏次数字段，只记用户收藏记录
        }
    }

    //在商品详情中点击收藏商品调用该方法
    public int collectionGoods(String goodsid, String userid, String type) {
        Collection collection = new Collection();
        collection.setUserid(Integer.parseInt(userid));
        collection.setGoodsid(Integer.parseInt(goodsid));
        collection.setType(Integer.parseInt(type));
        //先检查该用户有没有收藏过该商品
        int count = collectionService.checkIsHaveGoods(collection);

        if (count == 0) {
            //加入一条收藏记录(商品表不记收藏总数，直接插入流水即可)
            return collectionService.collectionGoods(collection);
        } else {
            return -1;
        }
    }

}

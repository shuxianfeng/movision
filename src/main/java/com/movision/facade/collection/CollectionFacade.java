package com.movision.facade.collection;

import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.service.CollectionService;
import com.movision.utils.IdGenerator;
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

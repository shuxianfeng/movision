package com.movision.facade.collection;

import com.movision.common.constant.PointConstant;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.collection.entity.Collection;
import com.movision.mybatis.collection.service.CollectionService;
import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.mybatis.newInformation.service.NewInformationService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.utils.IdGenerator;
import org.apache.commons.collections.map.AbstractHashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private UserOperationRecordService userOperationRecordService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private NewInformationService newInformationService;

    //在帖子中点击收藏帖子调用该方法
    public int collectionPost(String postid, String userid, String type) {

        Collection collection = new Collection();
        collection.setUserid(Integer.parseInt(userid));
        collection.setPostid(Integer.parseInt(postid));
        collection.setType(Integer.parseInt(type));
        //先检查该用户有没有收藏过该帖子
        int count = collectionService.checkIsHave(collection);

        if (count == 0) {

            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------start
            //判断该用户有没有首次关注过圈子或有没有点赞过帖子评论等或有没有收藏过商品帖子活动
            addPointForMyPointTask(userid);
            //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end

            //该帖子的被收藏次数+1
            collectionService.addCollectionSum(Integer.parseInt(postid));


            //************************查询被收藏人的帖子是否被设为最新消息通知用户
            Integer isread = newInformationService.queryUserByNewInformation(Integer.parseInt(postid));
            NewInformation news = new NewInformation();
            //更新被收藏人的帖子最新消息
            if (isread != null) {
                news.setIsread(0);
                news.setIntime(new Date());
                news.setUserid(isread);
                newInformationService.updateUserByNewInformation(news);
            } else {
                //获取被收藏人
                Integer uid = collectionService.queryPostCollectByUser(Integer.parseInt(postid));
                //新增被收藏人的帖子最新消息
                news.setIsread(0);
                news.setIntime(new Date());
                news.setUserid(uid);
                newInformationService.insertUserByNewInformation(news);
            }
            //******************************************************************

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
            addPointForMyPointTask(userid);


            //加入一条收藏记录(商品表不记收藏总数，直接插入流水即可)
            return collectionService.collectionGoods(collection);
        } else {
            return -1;
        }
    }

    /**
     * “我的”模块个人积分任务 增加积分的公共代码
     *
     * @param userid
     */
    private void addPointForMyPointTask(String userid) {
        //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------start
        //判断该用户有没有首次关注过圈子或有没有点赞过帖子评论等或有没有收藏过商品帖子活动
        UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
        if (null == entiy || entiy.getIscollect() == 0) {
            //如果未收藏过帖子或商品的话,首次收藏赠送积分
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.first_collect.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
            UserOperationRecord userOperationRecord = new UserOperationRecord();
            userOperationRecord.setUserid(Integer.parseInt(userid));
            userOperationRecord.setIscollect(1);
            if (null == entiy) {
                //不存在新增
                userOperationRecordService.insertUserOperationRecord(userOperationRecord);
            } else if (entiy.getIscollect() == 0) {
                //存在更新
                userOperationRecordService.updateUserOperationRecord(userOperationRecord);
            }
        }
        //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end
    }

}

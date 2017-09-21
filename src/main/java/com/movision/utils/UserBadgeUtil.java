package com.movision.utils;

import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.entity.UserBadge;
import com.movision.mybatis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/9/21 9:58
 * 公共工具类，用于单独封装判断用户的徽章等级信息
 */
@Service
public class UserBadgeUtil {
    private static Logger logger = LoggerFactory.getLogger(UserBadgeUtil.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /**
     * 动态方法：根据 用户id和isdv字段 获取 --用户的整体徽章获取信息
     * @param userid
     * @param isdv
     * @return
     */
    public UserBadge judgeBadge(String userid, int isdv){

        logger.info("判断用户徽章等级信息");
        UserBadge userBadge = new UserBadge();

        //查询用户的所有徽章详情
        //a.发帖数(1/10/50)
        int postsum = postService.queryPostNumByUserid(Integer.parseInt(userid));
        //b.用户总评论数(10/50/100)
//        int commentsum = postService.queryCommentByUserid(Integer.parseInt(userid));
        //b.用户总被收藏数(10/50/100)//---------------运营智障强烈要求修改为被收藏数2017.09.08
        int collectsum = postService.queryCollectByUserid(Integer.parseInt(userid));
        //c.被点赞总数(50/100/200)
        int zansum = postService.queryZanSumByUserid(Integer.parseInt(userid));
        //d.邀请总人数(1/10/50)
        int invitesum = userService.queryInviteNum(Integer.parseInt(userid));
        //e.个人资料完善情况(0/1)
        int flag = userService.queryFinishUserInfo(Integer.parseInt(userid));
        //f.精选数(1/5/10)
        int essencesum = postService.queryEssencesumByUserid(Integer.parseInt(userid));
        //g.达人认证徽章(0/1)
        //入参isdv
        //h.足迹徽章(1个足迹/10个足迹/20个足迹)
        int footprint = userService.getfootmap(Integer.parseInt(userid));
        //i.实名认证徽章(0/1)
        int rnauth = -1;//敬请期待
        //j.消费徽章(0/1)
        int consume = -1;//敬请期待

        //发帖数
        if (postsum == 0) {
            userBadge.setPostsum(0);
        }else if (postsum >= 1 && postsum < 10){
            userBadge.setPostsum(1);
        }else if (postsum >= 10 && postsum < 50){
            userBadge.setPostsum(2);
        }else if (postsum >= 50){
            userBadge.setPostsum(3);
        }

        //被收藏总数
        if (collectsum < 10) {
            userBadge.setCommentsum(0);
        }else if (collectsum >= 10 && collectsum < 50){
            userBadge.setCommentsum(1);
        }else if (collectsum >= 50 && collectsum < 100){
            userBadge.setCommentsum(2);
        }else if (collectsum >= 100){
            userBadge.setCommentsum(3);
        }

        //被赞总数
        if (zansum < 50) {
            userBadge.setZansum(0);
        }else if (zansum >= 50 && zansum < 100){
            userBadge.setZansum(1);
        }else if (zansum >= 100 && zansum < 200){
            userBadge.setZansum(2);
        }else if (zansum >= 200){
            userBadge.setZansum(3);
        }

        //邀请好友总数
        if (invitesum < 1) {
            userBadge.setInvitesum(0);
        }else if (invitesum >= 1 && invitesum < 10){
            userBadge.setInvitesum(1);
        }else if (invitesum >= 10 && invitesum < 50){
            userBadge.setInvitesum(2);
        }else if (invitesum >= 50){
            userBadge.setInvitesum(3);
        }

        //完善个人资料
        if (flag == 0){
            userBadge.setFinishuserinfo(0);
        }else if (flag == 1){
            userBadge.setFinishuserinfo(1);
        }

        //被精选数
        if (essencesum < 1) {
            userBadge.setEssencesum(0);
        }else if (essencesum >= 1 && essencesum < 5){
            userBadge.setEssencesum(1);
        }else if (essencesum >= 5 && essencesum < 10){
            userBadge.setEssencesum(2);
        }else if (essencesum >= 10){
            userBadge.setEssencesum(3);
        }

        //是否达人
        if (isdv == 0){
            userBadge.setIsdv(0);
        }else if (isdv == 1){
            userBadge.setIsdv(1);
        }

        //足迹总数
        if (footprint < 1){
            userBadge.setFootprint(0);
        }else if (footprint >= 1 && footprint < 10){
            userBadge.setFootprint(1);
        }else if (footprint >= 10 && footprint < 20){
            userBadge.setFootprint(2);
        }else if (footprint >= 20){
            userBadge.setFootprint(3);
        }

        //实名认证
        userBadge.setRnauth(rnauth);
        //消费徽章
        userBadge.setConsume(consume);

        return userBadge;
    }

    /**
     * 静态工具方法：根据用户查询到的 徽章信息 计算得到 用户得到的徽章总数
     * @param userBadge
     * @return
     */
    public static int getBadgeNum(UserBadge userBadge){

        int badgenum = 0;//获取徽章总数
        if (userBadge.getIsdv() > 0){
            badgenum ++;
        }
        if (userBadge.getFinishuserinfo() > 0){
            badgenum ++;
        }
        if (userBadge.getFootprint() > 0){
            badgenum ++;
        }
        if (userBadge.getInvitesum() > 0){
            badgenum ++;
        }
        if (userBadge.getPostsum() > 0){
            badgenum ++;
        }
        if (userBadge.getZansum() > 0){
            badgenum ++;
        }
        if (userBadge.getCommentsum() > 0){
            badgenum ++;
        }
        if (userBadge.getEssencesum() > 0){
            badgenum ++;
        }
        return badgenum;
    }
}

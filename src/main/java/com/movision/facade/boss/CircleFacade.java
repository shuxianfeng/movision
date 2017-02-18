package com.movision.facade.boss;

import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.entity.PostNum;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/8 17:56
 */
@Service
public class CircleFacade {
    @Autowired
    CircleService circleService;
    @Autowired
    PostService postService;
     @Autowired
    UserService userService;

    @Autowired
    SharesService sharesService;

    @Autowired
    RewardedService rewardedService;

    @Autowired
    AccusationService accusationService;

    /**
     * 圈子首页列表查询
     *
     * @return
     */
    public List<CircleIndexList> queryCircleByList() {
        List<Integer> circlenum = circleService.queryListByCircleCategory();//查询圈子所有分类
        List<CircleIndexList> list = new ArrayList<>();
        for (int i = 0; i < circlenum.size(); i++) {
            CircleIndexList cir = new CircleIndexList();
            //查询，圈子分类，圈主，管理员列表，关注人数，今日关注人数，帖子数量，今日新增帖子，精贴数量，支持人数，创建日期
            List<User> users = circleService.queryCircleUserList(i);//查询管理员列表
            List<User> circlemaster = users;//圈主
            CircleFollowNum followNum = circleService.queryFollowAndNewNum(i);//返回关注数,今日新增关注人数
            CirclePostNum postnum = circleService.queryCirclePostNum(i);//返回帖子数量，今日新增帖子，精贴数
            CircleVo supportnum = circleService.queryCircleSupportnum(i);//返回圈子分类创建时间，支持人数
            cir.setCirclemaster(circlemaster);//圈主列表和管理员相同
            cir.setCategory(i);//圈子分类
            cir.setCirclemanagerlist(users);//管理员列表
            if (postnum != null) {
                cir.setPostnum(postnum.getPostnum());//帖子数量
                cir.setPostnewnum(postnum.getNewpostnum());//今日新增帖子数量
                cir.setIsessencenum(postnum.getIsessencenum());//精贴数量
            }
            if (followNum != null) {
                cir.setFollownum(followNum.getNum());//关注数
                cir.setFollownewnum(followNum.getNewnum());//今日新增关注数
            }
            cir.setIntime(supportnum.getIntime());//圈子创建时间
            cir.setSupportnum(supportnum.getSupportnum());//圈子支持人数
            /////////////////////分类列表////////////////////////
            List<CircleVo> listt = circleService.queryCircleByList(i);//获取圈子分类列表的圈子列表
            List<CircleVo> circleVoslist = new ArrayList<>();
            for (int e = 0; e < listt.size(); e++) {
                CircleVo vo = new CircleVo();
                Integer circleid = listt.get(e).getId();
                String circlemasterlist = circleService.queryCircleBycirclemaster(listt.get(e).getPhone());//查询圈主
                List<User> userslist = userService.queryCircleManagerList(circleid);//查询出圈子管理员列表
                for (int j = 0; j < userslist.size(); j++) {
                    if (userslist.get(e).getNickname() == null) {
                        userslist.get(e).setNickname("用户" + userslist.get(e).getPhone().substring(7));
                    }
                }
                CircleFollowNum followNumt = circleService.queryFollowAndNewNumt(circleid);//返回关注数,今日新增关注人数
                CirclePostNum postnumt = circleService.queryCirclePostNumt(circleid);//返回帖子数量，今日新增帖子，精贴数
                if (postnumt != null) {
                    vo.setPostnum(postnumt.getPostnum());//帖子数量
                    vo.setPostnewnum(postnumt.getNewpostnum());//今日新增帖子数量
                    vo.setIsessencenum(postnumt.getIsessencenum());//精贴数量
                }
                if (followNumt != null) {
                    vo.setFollownum(followNumt.getNum());//关注数
                    vo.setFollownewnum(followNumt.getNewnum());//今日新增关注数
                }
                vo.setId(listt.get(e).getId());//圈子id
                vo.setName(listt.get(e).getName());//圈子名称
                vo.setCategory(listt.get(e).getCategory());//圈子分类
                vo.setCategoryname(circlemasterlist);//圈主
                vo.setCirclemanagerlist(users);//圈子管理员列表
                vo.setSupportnum(listt.get(e).getSupportnum());//支持数
                vo.setStatus(listt.get(e).getStatus());//圈子状态：0 待审核 1 审核通过 2 审核不通过
                vo.setIsrecommend(listt.get(e).getIsrecommend());//推荐
                vo.setIsdiscover(listt.get(e).getIsdiscover());//首页
                vo.setCreatetime(listt.get(e).getCreatetime());//圈子创建时间
                circleVoslist.add(vo);
            }
            cir.setClassify(circleVoslist);
            list.add(cir);
        }
        return list;
    }


    /**
     * 后台管理--查询精贴列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostIsessenceByList(Paging<PostList> pager){
        List<PostList> list=postService.queryPostIsessenceByList(pager);
        List<PostList> rewardeds = new ArrayList<>();
        for (int i =0;i<list.size();i++){
            PostList postList = new PostList();
            Integer id= list.get(i).getId();
            Integer circleid=list.get(i).getCircleid();
            String nickname=userService.queryUserByNickname(circleid);
            Integer share = sharesService.querysum(id);//获取分享数
            Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
            Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举 报次数
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setCollectsum(list.get(i).getCollectsum());
            postList.setShare(share);
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setZansum(list.get(i).getZansum());
            postList.setRewarded(rewarded);
            postList.setAccusation(accusation);
            postList.setIsessence(list.get(i).getIsessence());
            postList.setEssencedate(list.get(i).getEssencedate());
            rewardeds.add(postList);
        }
        return rewardeds;
    }


    public Map<String, Object> addDiscoverList() {
        List<Circle> list = circleService.addDiscoverList();
        return null;
    }


}

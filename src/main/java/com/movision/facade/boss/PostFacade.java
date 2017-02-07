package com.movision.facade.boss;

import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/7 9:16
 */
@Service
public class PostFacade {
    @Autowired
    PostService postService;

    @Autowired
    CircleService circleService;

    @Autowired
    UserService userService;

    @Autowired
    SharesService sharesService;

    @Autowired
    RewardedService rewardedService;

    @Autowired
    AccusationService accusationService;

    @Autowired
    CommentService commentService;


    /**
     * 后台管理-查询帖子列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> queryPostByList(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<Post> list = postService.queryPostByList(pager);
        List<Object> rewardeds = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsdel() != 0) {//用于判断已删除的帖子
                continue;
            } else {
                PostList postList = new PostList();
                Integer circleid = list.get(i).getCircleid();//获取到圈子id
                String phone = circleService.queryCircleByPhone(circleid);//获取圈子中的用户手机号
                String nickname = userService.queryUserByNickname(phone);//获取发帖人
                Integer share = sharesService.querysum(list.get(i).getId());//获取分享数
                Integer rewarded = rewardedService.queryRewardedBySum(list.get(i).getId());//获取打赏积分
                Integer accusation = accusationService.queryAccusationBySum(list.get(i).getId());//查询帖子举报次数
                postList.setTitle(list.get(i).getTitle());
                postList.setNickname(nickname);
                postList.setCollectsum(list.get(i).getCollectsum());
                postList.setShare(share);
                postList.setCommentsum(list.get(i).getCommentsum());
                postList.setZansum(list.get(i).getZansum());
                postList.setRewarded(rewarded);
                postList.setAccusation(accusation);
                postList.setIsessence(list.get(i).getIsessence());
                rewardeds.add(postList);
            }
        }
        return rewardeds;
    }

    /**
     * 后台管理-帖子列表-发帖人信息
     *
     * @param postid
     * @return
     */
    public User queryPostByPosted(String postid) {
        Integer circleid = postService.queryPostByCircleid(postid);
        String phone = circleService.queryCircleByPhone(circleid);//获取圈子中的用户手机号
        User user = userService.queryUser(phone);
        return user;
    }

    /**
     * 后台管理-帖子列表-删除帖子
     *
     * @param postid
     * @return
     */
    public int deletePost(String postid) {
        return postService.deletePost(Integer.parseInt(postid));
    }

    /**
     * 后台管理-帖子列表-查看评论
     *
     * @param pageNo
     * @param pageSize
     * @param postid
     * @return
     */
    public List<CommentVo> queryPostAppraise(String postid, String pageNo, String pageSize) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<CommentVo> list = commentService.queryComments(postid, pager);
        return list;
    }

/*    *//**
     * 帖子按条件查询
     * @param title
     * @param circleid
     * @param name
     * @param date
     * @return
     *//*
    public List<Object> postSearch(String title, String circleid, String name, Date date,String pageNo,String pageSize){
        if (title!=null&&circleid!=null&&name!=null&&date!=null){//当没有添加条件的情况下执行全部搜索
            Map<String ,Object> map=new HashedMap();
            map.put("title",title);
            map.put("circleid",circleid);
            map.put("name",name);
            map.put("date",date);
            return postService.postSearch(map);
        }else{
            return queryPostByList(pageNo,pageSize);
        }

    }*/

}

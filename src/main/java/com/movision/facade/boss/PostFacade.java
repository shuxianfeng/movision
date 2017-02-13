package com.movision.facade.boss;

import com.movision.common.Response;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.period.service.PeriodService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.entity.SharesVo;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    PeriodService periodService;


    /**
     * 后台管理-查询帖子列表
     * @param pager
     * @return
     */
    public List<PostList> queryPostByList(Paging<PostList> pager) {
        List<PostList> list = postService.queryPostByList(pager);
        List<PostList> rewardeds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
                PostList postList = new PostList();
            Integer id = list.get(i).getId();
                Integer circleid = list.get(i).getCircleid();//获取到圈子id
            String nickname = userService.queryUserByNickname(circleid);//获取发帖人
            Integer share = sharesService.querysum(id);//获取分享数
            Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
            Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举报次数
            Circle circlename = circleService.queryCircleByName(circleid);//获取圈子名称
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
            postList.setCirclename(circlename.getName());//帖子所属圈子
            postList.setOrderid(list.get(i).getOrderid());//获取排序
            postList.setEssencedate(list.get(i).getEssencedate());//获取精选日期
                rewardeds.add(postList);
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
    public Map<String, Integer> deletePost(String postid) {
        int resault = postService.deletePost(Integer.parseInt(postid));
        Map<String, Integer> map = new HashedMap();
        map.put("resault", resault);
        return map;
    }


    /**
     * 后台管理-帖子列表-查看评论
     * @param postid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostAppraise(String postid, Paging<CommentVo> pager) {
        List<CommentVo> list = commentService.queryCommentsByLsit(postid, pager);
        return list;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-帖子详情
     *
     * @param commentid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostByCommentParticulars(String commentid, Paging<CommentVo> pager) {
        List<CommentVo> list = commentService.queryPostByCommentParticulars(Integer.parseInt(commentid), pager);
        return list;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-添加评论
     *
     * @param postid
     * @param userid
     * @param content
     * @return
     */
    public int addPostAppraise(String postid, String userid, String content) {
        CommentVo comm = new CommentVo();
        comm.setPostid(Integer.parseInt(postid));
        comm.setUserid(Integer.parseInt(userid));
        comm.setIntime(new Date());
        comm.setContent(content);
        int c = commentService.insertComment(comm);
        if (c == 1) {
            postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子的评论数
        }
        return 1;
    }

    /**
     * 后台管理-帖子列表-删除帖子评论
     *
     * @param id
     * @return
     */
    public int deletePostAppraise(String id) {
        return commentService.deletePostAppraise(Integer.parseInt(id));
    }

    /**
     * 后台管理-帖子列表-帖子打赏列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<RewardedVo> queryPostAward(String postid, Paging<RewardedVo> pager) {
        return rewardedService.queryPostAward(Integer.parseInt(postid), pager);//分页返回帖子打赏列表
    }

    /**
     * 后台管理-帖子列表-帖子预览
     *
     * @param postid
     * @return
     */
    public PostList queryPostParticulars(String postid) {
        PostList postList = postService.queryPostParticulars(Integer.parseInt(postid));
        Integer circleid = postList.getCircleid();//获取到圈子id
        Integer id = postList.getId();//获取帖子id
        Integer share = sharesService.querysum(id);//获取分享数
        Integer rewarded = rewardedService.queryRewardedBySum(id);//获取打赏积分
        Integer accusation = accusationService.queryAccusationBySum(id);//查询帖子举报次数
        String nickname = userService.queryUserByNickname(circleid);//获取发帖人
        Circle circle = circleService.queryCircleByName(circleid);//获取圈子名称
        postList.setNickname(nickname);
        postList.setShare(share);//分享次数
        postList.setRewarded(rewarded);//打赏积分
        postList.setAccusation(accusation);//举报次数
        postList.setCirclename(circle.getName());//帖子所属圈子
        postList.setCategory(circle.getCategory());//
        return postList;
    }


    /**
     * 添加帖子
     *
     * @param title
     * @param subtitle
     * @param type
     * @param circleid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param time
     * @return
     */
    public Map<String, Integer> addPost(String title, String subtitle, String type, String circleid, String vid,
                                        String coverimg, String postcontent, String isessence, String isessencepool, String time) {
        Post post = new Post();
        Map<String, Integer> map = new HashedMap();
        post.setTitle(title);//帖子标题
        post.setSubtitle(subtitle);//帖子副标题
        post.setType(Integer.parseInt(type));//帖子类型
        post.setCircleid(Integer.parseInt(circleid));//圈子id
        String vod = vid;
        post.setCoverimg(coverimg);//帖子封面
        post.setPostcontent(postcontent);//帖子内容
        if (isessence != null) {
            post.setIsessence(Integer.parseInt(isessence));//是否为首页精选
        }
        if (isessencepool != null) {
            post.setIsessencepool(Integer.parseInt(isessencepool));//是否为圈子精选
        }
        post.setIntime(new Date());
        Date isessencetime = null;//加精时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        if (time != null) {
            try {
                isessencetime = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        post.setEssencedate(isessencetime);
        int result = postService.addPost(post);//添加帖子
        map.put("result", result);
        return map;

    }

    /**
     * 帖子加精
     *
     * @param postid
     * @return
     */
    public Map<String, Integer> addPostChoiceness(String postid, String essencedate, String orderid) {
        Map<String, Integer> map = new HashedMap();
        Post p = new Post();
        Integer typ = postService.queryPostChoiceness(Integer.parseInt(postid));
        if (typ == 0) {
            p.setId(Integer.parseInt(postid));
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            if (essencedate != null) {
                try {
                    date = format.parse(essencedate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            p.setEssencedate(date);//精选日期
            p.setOrderid(Integer.parseInt(orderid));
            Integer result = postService.addPostChoiceness(p);
            map.put("result", result);
            return map;
        } else {
            postService.deletePostChoiceness(Integer.parseInt(postid));
            map.put("result", 2);
            return map;
        }
    }

    /**
     * 查询帖子分享列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<SharesVo> queryPostShareList(String postid, Paging<SharesVo> pager) {
        return sharesService.queryPostShareList(pager, Integer.parseInt(postid));
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

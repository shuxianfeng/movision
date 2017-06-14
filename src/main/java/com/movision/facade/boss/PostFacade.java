package com.movision.facade.boss;

import com.movision.common.Response;
import com.movision.common.constant.JurisdictionConstants;
import com.movision.common.constant.PointConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.activePart.entity.ActivePartList;
import com.movision.mybatis.activePart.service.ActivePartService;
import com.movision.mybatis.activityContribute.entity.ActivityContribute;
import com.movision.mybatis.activityContribute.entity.ActivityContributeVo;
import com.movision.mybatis.activityContribute.service.ActivityContributeService;
import com.movision.mybatis.applyVipDetail.entity.ApplyVipDetail;
import com.movision.mybatis.applyVipDetail.service.ApplyVipDetailService;
import com.movision.mybatis.auditVipDetail.entity.AuditVipDetail;
import com.movision.mybatis.auditVipDetail.service.AuditVipDetailService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.period.service.PeriodService;
import com.movision.mybatis.post.entity.*;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postProcessRecord.entity.PostProcessRecord;
import com.movision.mybatis.postProcessRecord.service.PostProcessRecordService;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.share.entity.SharesVo;
import com.movision.mybatis.share.service.SharesService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserLike;
import com.movision.mybatis.user.service.UserService;
import com.movision.mybatis.video.entity.Video;
import com.movision.mybatis.video.service.VideoService;
import com.movision.utils.JsoupCompressImg;
import com.movision.utils.ListUtil;
import com.movision.utils.VideoUploadUtil;
import com.movision.utils.file.FileUtil;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private VideoUploadUtil videoUploadUtil;
    @Autowired
    private PostService postService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SharesService sharesService;

    @Autowired
    private RewardedService rewardedService;

    @Autowired
    private ActivePartService activePartService;

    @Autowired
    private BossUserService bossUserService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private commonalityFacade commonalityFacade;

    @Autowired
    private JsoupCompressImg jsoupCompressImg;

    @Autowired
    private ApplyVipDetailService applyVipDetailService;

    @Autowired
    private AuditVipDetailService auditVipDetailService;

    @Autowired
    private PostProcessRecordService postProcessRecordService;

    @Autowired
    private ActivityContributeService activityContributeService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private CompressImgService compressImgService;

    private static Logger log = LoggerFactory.getLogger(PostFacade.class);


    /**
     * 后台管理-查询帖子列表
     *
     * @param pager
     * @return
     */
    public List<PostList> queryPostByList(String loginid, Paging<PostList> pager) {
        Integer userid = Integer.parseInt(loginid);
        Map res = commonalityFacade.verifyUserByQueryMethod(userid, JurisdictionConstants.JURISDICTION_TYPE.select.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), null);
        List<PostList> list = new ArrayList<>();
        if (res.get("resault").equals(1)) {//查询当前登录用户的帖子列表
                Map map = new HashMap();
            map.put("loginid", loginid);
                list = postService.queryPostByManageByList(map, pager);
            return list;
        } else if (res.get("resault").equals(2) || res.get("resault").equals(0)) {//权限最大查询所有帖子列表
            list = postService.queryPostByList(pager);
            return list;
        } else {
            return list;
        }
    }


    /**
     * 根据用户id查询帖子列表
     *
     * @param userid
     * @param type
     * @param pager
     * @return
     */
    public List<PostList> queryPostListByUserid(String userid, String type, Paging<PostList> pager) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("type", type);
        return postService.queryPostListByUserid(map, pager);
    }

    /**
     * 根据用户id查询用户被收藏的帖子列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<PostList> queryCollectionListByUserid(String userid, Paging<PostList> pager) {
        return postService.queryCollectionListByUserid(userid, pager);
    }

    /**
     * 根据用户id查询用户被分享的帖子列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<SharesVo> querySharePostList(String userid, Paging<SharesVo> pager) {
        Map map = new HashedMap();
        map.put("userid", userid);
        map.put("type", 1);
        return sharesService.querySharePostList(map, pager);
    }

    /**
     * 查询用户分享帖子的分享列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<SharesVo> queryUsersSharePostsList(String userid, Paging<SharesVo> pager) {
        try {
            logger.info("查询用户分享帖子的分享列表");
            return sharesService.queryUsersSharePostsList(userid, pager);
        } catch (Exception e) {
            logger.error("查询用户分享帖子的分享列表异常");
            throw e;
        }
    }

    /**
     * 后台管理-查询活动列表（草稿箱）
     *
     * @param pager
     * @return
     */
    public List<PostList> queryPostActiveByList(Paging<PostList> pager) {
        List<PostList> list = postService.queryPostActiveByList(pager);
        List<PostList> rewardeds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PostList postList = new PostList();
            Integer circleid = list.get(i).getCircleid();//获取到圈子id
            String nickname = userService.queryNicknameByUserid(circleid);//获取发帖人
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());
            postList.setNickname(nickname);
            postList.setIntime(list.get(i).getIntime());
            rewardeds.add(postList);
        }
        return rewardeds;
    }

    /**
     * 后台管理-查询活动列表
     *
     * @param loginid
     * @param pager
     * @return
     */
    public List<PostActiveList> queryPostActiveToByList(String loginid, Paging<PostActiveList> pager) {

        Integer userid = Integer.parseInt(loginid);

        Map res = commonalityFacade.verifyUserByQueryMethod(userid, JurisdictionConstants.JURISDICTION_TYPE.select.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), null);
        List<PostActiveList> list = new ArrayList<>();
        BossUser logintype = bossUserService.queryUserByAdministrator(userid);//根据登录用户id查询当前用户有哪些权限
        if (res.get("resault").equals(1)) {//查询当前登录用户的帖子列表
            //查询用户管理的圈子id
            if (logintype.getCirclemanagement() == 1) {//圈子管理员
                Map map = new HashMap();
                map.put("loginid", userid);
                list = postService.queryPostActiveToByList(map, pager);
            } else if (logintype.getIscircle() == 1) {//圈主
                Map map = new HashMap();
                map.put("loginid", userid);
                list = postService.queryPostActiveToByList(map, pager);
            }
        } else if (res.get("resault").equals(2) || res.get("resault").equals(0)) {//权限最大查询所有帖子列表
            Map map = new HashMap();
            list = postService.queryPostActiveToByList(map, pager);
        }
        List<PostActiveList> rewardeds = new ArrayList<>();
        Date date = new Date();
        long str = date.getTime();
        for (int i = 0; i < list.size(); i++) {
            PostActiveList postList = new PostActiveList();
            Integer postid = list.get(i).getId();//获取到帖子id
            Integer persum = postService.queryPostPerson(postid);
            String nickname = userService.queryUserByNicknameBy(postid);
            Period periods = periodService.queryPostPeriod(postid);
            Double activefee = list.get(i).getActivefee();
            Double sumfree = 0.0;
            if (activefee != null) {
                sumfree = persum * activefee;
            }

            Date begintime = periods.getBegintime();
            Date endtime = periods.getEndtime();
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());//主题
            postList.setNickname(nickname);//昵称
            postList.setActivetype(list.get(i).getActivetype());//活动类型
            postList.setActivefee(activefee);//活动费用
            postList.setEssencedate(list.get(i).getEssencedate());//精选日期
            postList.setOrderid(list.get(i).getOrderid());//精选排序
            postList.setBegintime(begintime);//开始时间
            postList.setEndtime(endtime);//结束时间
            postList.setPersum(persum);//报名人数
            postList.setSumfree(sumfree);//总费用
            String activeStatue = "";
            if (begintime != null && endtime != null) {
                long begin = begintime.getTime();
                long end = endtime.getTime();
                if (str > begin && str < end) {
                    activeStatue = "报名中";
                } else if (str < begin) {
                    activeStatue = "未开始";
                } else if (str > end) {
                    activeStatue = "已结束";
                }
            }
            postList.setActivestatue(activeStatue);//活动状态
            postList.setIsessence(list.get(i).getIsessence());
            postList.setZansum(list.get(i).getZansum());
            postList.setCollectsum(list.get(i).getCollectsum());
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setForwardsum(list.get(i).getForwardsum());
            postList.setIntime(list.get(i).getIntime());
            postList.setIshot(list.get(i).getIshot());
            postList.setHotimgurl(list.get(i).getHotimgurl());
            rewardeds.add(postList);
        }
        return rewardeds;
    }

    /**
     * 后台管理-查询报名列表记录
     *
     * @param pager
     * @return
     */
    public List<ActivePartList> queyPostCallActive(Paging<ActivePartList> pager) {
        List<ActivePartList> list = activePartService.queryPostCallActive(pager);
        List<ActivePartList> rewardeds = new ArrayList<>();
        Date date = new Date();
        long str = date.getTime();
        for (int i = 0; i < list.size(); i++) {
            ActivePartList postActiveList = new ActivePartList();
            Integer postid = list.get(i).getPostid();//获取到帖子id
            Double activefee = postService.queryPostActiveFee(postid);
            Period periods = periodService.queryPostPeriod(postid);
            postActiveList.setActivefee(activefee);//活动单价
            Integer id = list.get(i).getUserid();//用户id
            User user = userService.queryUserB(id);
            Date begintime = null;
            Date endtime = null;
            if (periods != null) {
                begintime = periods.getBegintime();
                endtime = periods.getEndtime();
            }
            postActiveList.setBegintime(begintime);
            postActiveList.setEndtime(endtime);
            long begin = begintime.getTime();
            long end = endtime.getTime();
            String activeStatue = "";
            if (str > begin && str < end) {
                activeStatue = "报名中";
            } else if (str < begin) {
                activeStatue = "未开始";
            } else if (str > end) {
                activeStatue = "已结束";
            }
            postActiveList.setActivestatue(activeStatue);//活动状态
            postActiveList.setIntime(list.get(i).getIntime());//报名时间
            postActiveList.setNickname(user.getNickname());//用户名
            postActiveList.setPhone(user.getPhone());//联系方式
            postActiveList.setPayStatue(list.get(i).getPayStatue());//支付方式
            postActiveList.setMoneypay(list.get(i).getMoneypay());//实付金额
            postActiveList.setMoneyying(list.get(i).getMoneyying());//应付金额
            rewardeds.add(postActiveList);
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
        String phone = circleService.queryPhoneInCircleByCircleid(circleid);//获取圈子中的用户手机号
        User user = userService.queryUser(phone);
        return user;
    }

    /**
     * 后台管理-帖子列表-删除帖子
     *
     * @param postid
     * @return
     */
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map deletePost(String postid, String loginid) {
        Map map = new HashedMap();
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(loginid), JurisdictionConstants.JURISDICTION_TYPE.delete.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), Integer.parseInt(postid));
        if (res.get("resault").equals(1)) {
            int resault = postService.deletePost(Integer.parseInt(postid));
            map.put("resault", resault);
            return map;
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
    }


    /**
     * 后台管理-帖子列表-查看评论
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostAppraise(String postid, String type, Paging<CommentVo> pager) {
        Map map = new HashedMap();
        List<CommentVo> commentVo = new ArrayList<>();
        map.put("postid", postid);
        map.put("type", type);
        List<CommentVo> co = commentService.queryCommentsByLsit(pager, map);
        return co;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-帖子详情
     *
     * @param commentid
     * @param pager
     * @return
     */
    public List<CommentVo> queryPostByCommentParticulars(String commentid, String postid, Paging<CommentVo> pager) {
        Map<String, Integer> map = new HashedMap();
        List<CommentVo> resautl = new ArrayList<>();
        map.put("commentid", Integer.parseInt(commentid));
        map.put("postid", Integer.parseInt(postid));
        CommentVo com = commentService.queryCommentById(map);
        List<CommentVo> list = commentService.queryPostByCommentParticulars(map, pager);
        com.setSoncomment(list);
        resautl.add(com);
        return resautl;
    }

    /**
     * 后台管理-帖子列表-帖子评论列表-添加评论
     *
     * @param postid
     * @param loginid
     * @param content
     * @return
     */
    @Transactional
    public Map addPostAppraise(String postid, String content, String loginid) {
        Integer id = Integer.parseInt(loginid);
        CommentVo comm = new CommentVo();
        Map map = new HashedMap();
        //Integer speciall= bossUserService.queryUserIdBySpeciallyGuest(id);
        Map res = commonalityFacade.verifyUserJurisdiction(id, JurisdictionConstants.JURISDICTION_TYPE.add.getCode(), JurisdictionConstants.JURISDICTION_TYPE.comment.getCode(), null);
        Integer uid = Integer.parseInt(loginid);
        comm.setPostid(Integer.parseInt(postid));
        comm.setIntime(new Date());
        comm.setContent(content);
            Integer u = bossUserService.queryUserById(uid);//根据用户id查询前台对应用户id
            comm.setUserid(u);
            comm.setIntime(new Date());
        if (res.get("resault").equals(2)) {//特邀嘉宾发帖子评论
            comm.setIscontribute(1);//特邀嘉宾
            comm.setStatus(0);
            comm.setIsdel("1");
            int c = commentService.insertComment(comm);//添加评论
            if (c == 1) {
                //postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子的评论数
                map.put("resault", 1);
                return map;
            } else {
                map.put("resault", -1);
                return map;
            }
        } else if (res.get("resault").equals(1)) {//普通用户发帖子评论
            comm.setIscontribute(0);//不是特邀嘉宾
            comm.setStatus(null);
            comm.setIsdel("0");
            int c = commentService.insertComment(comm);//添加评论
            if (c == 1) {
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子的评论数
                map.put("resault", 1);
                return map;
            } else {
                map.put("resault", -1);
                return map;
            }
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
    }

    /**
     * 后台管理-帖子列表-删除帖子评论
     *
     * @param id
     * @return
     */
    public Map deletePostAppraise(String id, String loginid) {
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(loginid), JurisdictionConstants.JURISDICTION_TYPE.delete.getCode(), JurisdictionConstants.JURISDICTION_TYPE.comment.getCode(), Integer.parseInt(id));
        Map map = new HashMap();
        if (res.get("resault").equals(1)) {
            commentService.deletePostAppraise(Integer.parseInt(id));
            map.put("resault", 1);
            return map;
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
    }

    /**
     * 后台管理-帖子列表-评论列表-编辑评论
     *
     * @param commentid
     * @param content
     * @param loginid
     * @return
     */
    public Map updatePostComment(String commentid, String content, String loginid) {
        Map chuan = new HashedMap();
        Map map = new HashedMap();
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(loginid), JurisdictionConstants.JURISDICTION_TYPE.update.getCode(), JurisdictionConstants.JURISDICTION_TYPE.comment.getCode(), Integer.parseInt(commentid));
        if (res.get("resault").equals(1)) {
            chuan.put("commentid", commentid);
            chuan.put("content", content);
            //查询出前台对应的用户id
            Integer uid = bossUserService.queryUserById(Integer.parseInt(loginid));
            chuan.put("userid", uid);
            int i = commentService.updatePostComment(chuan);
            map.put("resault", i);
            return map;
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
    }

    /**
     * 回复帖子评论
     *
     * @param pid
     * @param content
     * @param userid
     * @return
     */
    public Map replyPostComment(String pid, String content, String postid, String userid) {
        Map map = new HashedMap();
        Comment comment = commentService.queryAuthenticationBypid(Integer.parseInt(pid));
        if (comment == null) {
            Map chuan = new HashedMap();
            chuan.put("pid", pid);
            chuan.put("content", content);
            chuan.put("userid", userid);
            chuan.put("postid", postid);
            chuan.put("intime", new Date());
            int i = commentService.replyPostComment(chuan);
            map.put("resault", i);
            return map;
        } else {
            map.put("resault", -1);
            map.put("msagger", "官方回复不能评论");
            return map;
        }
    }
//bossUserService.queryUserIdBySpeciallyGuest(Integer.parseInt(commentid));判断用户是否是特邀嘉宾

    /**
     * 特约嘉宾评论审核
     *
     * @param commentid
     * @param userid
     * @return
     */
    @Transactional
    public Map auditComment(String commentid, String userid, String type) {
        Map map = new HashedMap();
        //查询该用户是否是超管或普管
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(userid), JurisdictionConstants.JURISDICTION_TYPE.commentAudit.getCode(), JurisdictionConstants.JURISDICTION_TYPE.comment.getCode(), Integer.parseInt(commentid));
        if (res.get("resault").equals(1)) {
            Map map1 = new HashMap();
            map1.put("commentid", commentid);
            map1.put("type", type);
            Integer resault = commentService.updateCommentAudit(map1);
            postService.updatePostBycommentsumT(Integer.parseInt(commentid));//更新帖子的评论数
            map.put("resault", 1);
            return map;
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
            /*if (user.getIssuper() == 1 || user.getCommon() == 1) {//是管理员
                Map map1=new HashMap();
                map1.put("commentid",commentid);
                map1.put("type",type);
                Integer resault = commentService.updateCommentAudit(map1);
                postService.updatePostBycommentsumT(Integer.parseInt(commentid));//更新帖子的评论数
                map.put("massege", "审核成功");
                map.put("resault", resault);
                return map;
            } else {
                map.put("massege", "权限不足");
                map.put("resault", -1);
                return map;
            }*/
       /* } else {
            map.put("massege", "没有此用户");
            map.put("resault", -1);
            return map;
        }*/
    }



    /**
     * 后台管理-帖子列表-帖子打赏列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<RewardedVo> queryPostAward(String postid, String pai, Paging<RewardedVo> pager) {
        Map map = new HashedMap();
        map.put("postid", postid);
        map.put("pai", pai);
        return rewardedService.queryPostAward(map, pager);//分页返回帖子打赏列表
    }

    /**
     * 后台管理-帖子列表-帖子预览
     *
     * @param postid
     * @return
     */
    public PostList queryPostParticulars(String postid) {
        PostList postList = postService.queryPostParticulars(Integer.parseInt(postid));
        String str = null;
        if (postList != null) {
            Map map = new HashMap();
            map.put("url", postList.getCoverimg());
            //查找是否有缩略图，有显示缩略图，否则显示原图
            str = compressImgService.queryUrlIsCompress(map);
        }
        if (str != null) {
            postList.setCoverimg(str);
        }
        List<GoodsVo> goodses = null;
        if (postList != null) {
            goodses = goodsService.queryGoods(postList.getId());
        }
        if (goodses != null) {
            postList.setPromotionGoods(goodses);
        }
        return postList;
    }

    /**
     * 后台管理*-活动预览
     *
     * @param postid
     * @return
     */
    public PostList queryPostActiveQ(String postid) {
        PostList postList = postService.queryActivityParticulars(Integer.parseInt(postid));
        Integer id = postList.getId();
        Date date = new Date();
        long str = date.getTime();
        Integer share = sharesService.querysum(id);
        Period periods = periodService.queryPostPeriod(Integer.parseInt(postid));
        String nickname = userService.queryUserByNicknameBy(Integer.parseInt(postid));//获取发帖人
        postList.setNickname(nickname);
        postList.setShare(share);//分享次数
        postList.setTitle(postList.getTitle());//主标题
        postList.setSubtitle(postList.getSubtitle());//副标题
        postList.setEssencedate(postList.getEssencedate());//精选日期
        postList.setZansum(postList.getZansum());//赞
        postList.setCommentsum(postList.getCommentsum());//评论
        postList.setCollectsum(postList.getCollectsum());//收藏
        postList.setActivetype(postList.getActivetype());//活动类型
        Date begintime = periods.getBegintime();
        Date endtime = periods.getEndtime();
        long begin = begintime.getTime();
        long end = endtime.getTime();
        String activeStatue = "";
        if (str > begin && str < end) {
            activeStatue = "报名中";
        } else if (str < begin) {
            activeStatue = "未开始";
        } else if (str > end) {
            activeStatue = "已结束";
        }
        postList.setActivestatue(activeStatue);//活动状态
        postList.setActivefee(postList.getActivefee());//活动单价
        Integer persum = postService.queryPostPerson(Integer.parseInt(postid));//查询报名人数
        postList.setPersum(persum);//报名人数
        postList.setPostcontent(postList.getPostcontent());//活动内容
        postList.setIntime(postList.getIntime());//发布时间
        if (postList.getActivetype() == 1) {//含有商城促销类商品
            List<GoodsVo> li = goodsService.queryGoodsByPostid(postList.getId());//查询活动商城促销类商品列表
            if (li != null) {
                postList.setPromotionGoods(li);//封装活动促销类商品
            }
        }
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
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map addPost(HttpServletRequest request, String title, String subtitle, String type, String circleid,
                       String userid, String coverimg, String vid, String bannerimgurl,
                       String postcontent, String isessence, String ishot, String orderid, String time, String goodsid, String loginid) {
        PostTo post = new PostTo();
        Map map = new HashedMap();
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(loginid), JurisdictionConstants.JURISDICTION_TYPE.add.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), Integer.parseInt(circleid));
        if (res.get("resault").equals(1)) {
            if (postcontent.length() < 20000) {
                if (StringUtil.isNotEmpty(title)) {
                    post.setTitle(title);//帖子标题
                }
                if (StringUtil.isNotEmpty(subtitle)) {
                    post.setSubtitle(subtitle);//帖子副标题
                }
                if (StringUtil.isNotEmpty(type)) {
                    post.setType(type);//帖子类型
                }
                if (StringUtil.isNotEmpty(circleid)) {
                    post.setCircleid(circleid);//圈子id
                }
                if (StringUtil.isNotEmpty(bannerimgurl)) {
                    post.setCoverimg(bannerimgurl);//添加帖子封面
                }
                post.setIsactive("0");//设置状态为帖子
                if (StringUtil.isNotEmpty(postcontent)) {

                    //内容转换
                    Map con = jsoupCompressImg.compressImg(request, postcontent);
                    System.out.println(con);
                    if ((int) con.get("code") == 200) {
                        String str = con.get("content").toString();
                        str = str.replace("\\", "");
                        post.setPostcontent(str);//帖子内容
                    } else {
                        logger.error("帖子内容转换异常");
                        post.setPostcontent(postcontent);
                    }

                    /*post.setPostcontent(postcontent);*/
                }
                post.setIntime(new Date());//插入时间
                if (StringUtil.isNotEmpty(ishot)) {
                    post.setIshot(ishot);//是否为圈子精选
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                if (StringUtil.isNotEmpty(isessence)) {
                    if (isessence != "0") {//判断是否为加精
                        post.setIsessence(isessence);//是否为首页精选
                        if (StringUtil.isNotEmpty(orderid)) {
                            post.setOrderid(Integer.parseInt(orderid));
                        }
                        if (StringUtil.isNotEmpty(time)) {
                            try {
                                d = format.parse(time);
                                post.setEssencedate(d);
                            } catch (ParseException e) {
                                log.error("时间插入异常");
                            }
                        }

                    }
                }
                post.setCoverimg(coverimg);//插入图片地址
                post.setUserid(userid);
                post.setIsdel("0");
                int result = postService.addPost(post);//添加帖子
                //String fName = FileUtil.getPicName(vid);//获取视频文件名
                //查询圈子名称
                //  String circlename = circleService.queryCircleName(Integer.parseInt(circleid));
                // String vedioid = videoUploadUtil.videoUpload(vid, fName, "", bannerimgurl, circlename);
                if (result == 1) {
                    Integer in = 0;
                    Integer pid = post.getId();//获取到刚刚添加的帖子id
                    if (type.equals("1")) {
                        Video vide = new Video();
                        vide.setPostid(pid);
                        vide.setVideourl(vid);
                        vide.setBannerimgurl(bannerimgurl);
                        vide.setIntime(new Date());
                        in = videoService.insertVideoById(vide);//添加视频表
                    } else if (type.equals("2")) {//分享视频贴
                        Video vide = new Video();
                        vide.setPostid(pid);
                        vide.setVideourl(vid);
                        vide.setBannerimgurl(coverimg);//分享视频贴的视频封面是帖子封面
                        vide.setIntime(new Date());
                        in = videoService.insertVideoById(vide);//添加视频表
                    }
                    if (StringUtil.isNotEmpty(goodsid)) {//帖子添加商品
                        String[] lg = goodsid.split(",");//以逗号分隔
                        for (int i = 0; i < lg.length; i++) {
                            Map addgoods = new HashedMap();
                            addgoods.put("postid", pid);
                            addgoods.put("goodsid", lg[i]);
                            int goods = postService.insertGoods(addgoods);//添加帖子分享的商品
                            if (goods == 1 && in == 1) {//添加视频和添加帖子同时成功
                                map.put("result", goods);
                            }
                        }
                    }

                    PostProcessRecord pprd = new PostProcessRecord();
                    if (ishot != null) {
                        pprd.setIshot(Integer.parseInt(ishot));
                    }
                    pprd.setPostid(post.getId());
                    if (isessence != null) {
                        pprd.setIsesence(Integer.parseInt(isessence));
                    }
                    postProcessRecordService.insertProcessRecord(pprd);//插入精选、热门记录
                    if (StringUtil.isNotEmpty(ishot)) {
                        if (ishot.equals("1")) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.circle_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                    }
                    if (StringUtil.isNotEmpty(isessence)) {
                        if (isessence.equals("1")) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                    }
                    if (Integer.parseInt(loginid) != -1) {
                        pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                    }
                }
                map.put("resault", 1);
                // map.put("vedioid", vedioid);
                return map;
            } else {
                map.put("resault", -2);
                return map;
            }
        } else {
            map.put("resault", -1);
            map.put("massge", "权限不足");
            return map;
        }
    }

    /**
     * 改版发帖
     *
     * @param request
     * @param title
     * @param subtitle
     * @param circleid
     * @param userid
     * @param postcontent
     * @param isessence
     * @param ishot
     * @param orderid
     * @param time
     * @param goodsid
     * @param loginid
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map addPostTest(HttpServletRequest request, String title, String subtitle, String circleid,
                           String userid, String postcontent, String isessence, String ishot, String orderid, String time, String goodsid, String loginid) {
        PostTo post = new PostTo();
        Map map = new HashedMap();
        Map res = commonalityFacade.verifyUserJurisdiction(Integer.parseInt(loginid), JurisdictionConstants.JURISDICTION_TYPE.add.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), Integer.parseInt(circleid));
        if (res.get("resault").equals(1)) {
            if (postcontent.length() < 20000) {
                if (StringUtil.isNotEmpty(title)) {
                    post.setTitle(title);//帖子标题
                }
                if (StringUtil.isNotEmpty(subtitle)) {
                    post.setSubtitle(subtitle);//帖子副标题
                }
                post.setIsactive("0");//设置状态为帖子
                if (StringUtil.isNotEmpty(postcontent)) {

                    //内容转换
                    Map con = jsoupCompressImg.compressImg(request, postcontent);
                    System.out.println(con);
                    if ((int) con.get("code") == 200) {
                        String str = con.get("content").toString();
                        str = str.replace("\\", "");
                        post.setPostcontent(str);//帖子内容
                    } else {
                        logger.error("帖子内容转换异常");
                        post.setPostcontent(postcontent);
                    }
                }
                post.setIntime(new Date());//插入时间
                if (StringUtil.isNotEmpty(ishot)) {
                    post.setIshot(ishot);//是否为圈子精选
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date d = null;
                if (StringUtil.isNotEmpty(isessence)) {
                    if (isessence != "0") {//判断是否为加精
                        post.setIsessence(isessence);//是否为首页精选
                        if (StringUtil.isNotEmpty(orderid)) {
                            post.setOrderid(Integer.parseInt(orderid));
                        }
                        if (StringUtil.isNotEmpty(time)) {
                            try {
                                d = format.parse(time);
                                post.setEssencedate(d);
                            } catch (ParseException e) {
                                log.error("时间插入异常");
                            }
                        }

                    }
                }

                //-------解析JSON字符串，把视频路径，图片路径分别存放
                /*String vid=null;
                String vurl=null;
                // Map<String,List<Map<String,String>>> postmap=new HashMap<>();模型
                JSONObject J=JSONObject.fromObject(postcontent);//转换为json对象
                JSONArray jsonArray=JSONArray.fromObject(J.get("postcontent"));//取出json中list集合数组
                for (int i = 0; i<jsonArray.size(); i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);//遍历出每一个list元素
                    //String orderids=jsonObject.get("orderid").toString();
                    String types=jsonObject.getString("type").toString();//类型
                    ///String value=jsonObject.getString("value").toString();
                    String whs=jsonObject.getString("wh").toString();//宽高
                    //String dirs=jsonObject.getString("dir").toString();
                    if (StringUtil.isBlank(whs) && types.equals(2)){//没有宽高，是视频,保存视频记录
                        //把必要字段存放在变量中，当帖子发布完成后添加视频表中
                    }
                    if (StringUtil.isNotBlank(types)){
                        if (types.equals(2)){//代表是视频贴
                            post.setType("1");
                        }else {
                            post.setType("0");
                        }
                    }
                }*/
                post.setUserid(userid);
                post.setIsdel("0");
                postService.addPost(post);//添加帖子
                /*Integer pid = post.getId();//获取到刚刚添加的帖子id
                Video vide = new Video();
                vide.setPostid(pid);
                vide.setVideourl(vid);
                vide.setBannerimgurl("");
                vide.setIntime(new Date());
                videoService.insertVideoById(vide);//添加视频表*/
                //查询圈子名称
                Integer in = 0;
                if (StringUtil.isNotEmpty(goodsid)) {//帖子添加商品
                    Integer pid = post.getId();//获取到刚刚添加的帖子id
                    String[] lg = goodsid.split(",");//以逗号分隔
                    for (int i = 0; i < lg.length; i++) {
                        Map addgoods = new HashedMap();
                        addgoods.put("postid", pid);
                        addgoods.put("goodsid", lg[i]);
                        int goods = postService.insertGoods(addgoods);//添加帖子分享的商品
                        map.put("result", goods);
                    }
                }

                PostProcessRecord pprd = new PostProcessRecord();
                if (ishot != null) {
                    pprd.setIshot(Integer.parseInt(ishot));
                }
                pprd.setPostid(post.getId());
                if (isessence != null) {
                    pprd.setIsesence(Integer.parseInt(isessence));
                }
                postProcessRecordService.insertProcessRecord(pprd);//插入精选、热门记录
                if (StringUtil.isNotEmpty(ishot)) {
                    if (ishot.equals("1")) {
                        pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.circle_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                    }
                }
                if (StringUtil.isNotEmpty(isessence)) {
                    if (isessence.equals("1")) {
                        pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                    }
                }
                if (Integer.parseInt(loginid) != -1) {
                    pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.post.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
                }

                map.put("resault", 1);
                // map.put("vedioid", vedioid);
                return map;
            } else {
                map.put("resault", -2);
                return map;
            }
        } else {
            map.put("resault", -1);
            map.put("massge", "权限不足");
            return map;
        }
    }

    /**
     * 后台管理--增加活动
     *
     * @param title
     * @param subtitle
     * @param
     * @param
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param essencedate
     * @param begintime
     * @param endtime
     * @param userid
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map<String, Integer> addPostActive(String title, String subtitle, String activetype, String iscontribute, String activefee,
                                              String coverimg, String postcontent, String isessence, String orderid, String essencedate,
                                              String begintime, String endtime, String userid, String hotimgurl, String ishot, String goodsid) {
        PostTo post = new PostTo();
        if (postcontent.length() < 20000) {
            Map<String, Integer> map = new HashedMap();
            post.setTitle(title);//帖子标题
            post.setSubtitle(subtitle);//帖子副标题
            Integer typee = Integer.parseInt(activetype);
            post.setActivetype(activetype);
            post.setIscontribute(iscontribute);//是否投稿，必填
            if (typee == 0) {
                if (!StringUtils.isEmpty(activefee)) {
                    post.setActivefee(Double.parseDouble(activefee));//金额
                } else {
                    post.setActivefee(0.0);
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            post.setCoverimg(coverimg);
            if (StringUtil.isNotEmpty(postcontent)) {
                post.setPostcontent(postcontent);
            }
            if (!StringUtils.isEmpty(isessence)) {
                if (Integer.parseInt(isessence) != 0) {//判断是否为加精
                    post.setIsessence(isessence);//是否为首页精选
                    if (StringUtil.isNotEmpty(orderid)) {
                            post.setOrderid(Integer.parseInt(orderid));
                        } else {
                            post.setOrderid(0);
                        }
                    Date d = null;
                    if (StringUtil.isNotEmpty(essencedate)) {
                            try {
                                d = format.parse(essencedate);
                                post.setEssencedate(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                }
            }

            post.setIntime(new Date());
            if (StringUtil.isNotEmpty(orderid)) {
                post.setOrderid(Integer.parseInt(orderid));//排序精选
            }
            post.setUserid(userid);//发帖人
            if (!StringUtils.isEmpty(hotimgurl)) {
                post.setHotimgurl(hotimgurl);//活动首页方形图
            }
            if (!StringUtil.isEmail(ishot)) {
                post.setIshot(ishot);//是否设为热门
            }
            post.setIsactive("1");
            int result = postService.addPostActiveList(post);//新建活动
            Period period = new Period();
            Date begin = null;//开始时间
            if (StringUtil.isNotEmpty(begintime)) {
                try {
                    begin = format.parse(begintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date end = null;
            if (StringUtil.isNotEmpty(endtime)) {
                try {
                    end = format.parse(endtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            period.setBegintime(begin);
            period.setEndtime(end);
            Integer id = post.getId();
            period.setPostid(id);
            int r = postService.addPostPeriod(period);
            if (!StringUtils.isEmpty(goodsid)) {//添加商城促销商品
                String[] lg = goodsid.split(",");//以逗号分隔
                for (int i = 0; i < lg.length; i++) {
                    Map addgoods = new HashedMap();
                    addgoods.put("postid", post.getId());
                    addgoods.put("goodsid", lg[i]);
                    int goods = postService.insertPromotionGoods(addgoods);//添加活动商城促销类商品
                    if (goods == 1) {//添加视频和添加帖子同时成功
                        map.put("result", goods);
                    }
                }
            }
            PostProcessRecord pprd = new PostProcessRecord();
            pprd.setIshot(Integer.parseInt(ishot));
            pprd.setPostid(post.getId());
            pprd.setIsesence(Integer.parseInt(isessence));
            postProcessRecordService.insertProcessRecord(pprd);//插入精选、热门记录
            map.put("result", r);
            map.put("result", result);
            return map;
        } else {
            Map map = new HashMap();
            map.put("resault", -2);
            return map;
        }
    }

    /**
     * 帖子加精/取消加精
     *
     * @param postid
     * @return
     */
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map<String, Integer> addPostChoiceness(String postid, String subtitle, String essencedate, String orderid) {
        Map<String, Integer> map = new HashedMap();
        PostTo p = new PostTo();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date esdate = null;
        if (Integer.parseInt(orderid) > 0) {//加精动作
            p.setId(Integer.parseInt(postid));
            if (StringUtil.isNotEmpty(essencedate)) {
                try {
                    esdate = format.parse(essencedate);
                    p.setEssencedate(esdate);
                } catch (ParseException e) {
                    logger.error("时间转换异常", e);
                }
            }
            if (orderid != null) {
                p.setOrderid(Integer.parseInt(orderid));
            }
            p.setSubtitle(subtitle);
            Integer result = null;
            int isessence = postService.queryPostByIsessence(postid);//判断是否加精
            if (isessence == 1) {//已经加精,做修改操作
                p.setId(Integer.parseInt(postid));
                result = postService.updatePostChoiceness(p);
            } else {
                result = postService.addPostChoiceness(p);
            }
            Integer userid = postService.queryPostByUser(postid);
            map.put("result", result);
            if (!orderid.equals("0")) {//在加精时操作
                //查询帖子是否被设为加精活精选
                PostProcessRecord postProcessRecord = postProcessRecordService.queryPostByIsessenceOrIshot(Integer.parseInt(postid));
                if (postProcessRecord != null) {//已经加精过活精选
                    if (postProcessRecord.getIsesence() == 0) {//判断是否首页精选
                        postProcessRecord.setIsesence(1);
                        //修改
                        postProcessRecordService.updateProcessRecord(postProcessRecord);
                        //增加积分
                        pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), userid);//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                    }
                } else {
                    //积分操作
                    if (userid != null) {
                        pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), userid);//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                    }
                    //新增
                    PostProcessRecord pprd = new PostProcessRecord();
                    pprd.setPostid(Integer.parseInt(postid));
                    pprd.setIsesence(1);
                    pprd.setIshot(0);
                    postProcessRecordService.insertProcessRecord(pprd);
                }
            }
            return map;
        } else {//取消加精
            int i = postService.deletePostChoiceness(Integer.parseInt(postid));
            Integer userid = postService.queryPostByUser(postid);
            PostProcessRecord postProcessRecord = postProcessRecordService.queryPostByIsessenceOrIshot(Integer.parseInt(postid));
            if (postProcessRecord != null) {//已经加精过活精选
                if (postProcessRecord.getIsesence() == 0) {//判断是否首页精选
                    postProcessRecord.setIsesence(0);
                    //修改
                    postProcessRecordService.updateProcessRecord(postProcessRecord);
                }
            } else {
                //新增
                PostProcessRecord pprd = new PostProcessRecord();
                pprd.setPostid(Integer.parseInt(postid));
                pprd.setIsesence(0);
                pprd.setIshot(0);
                postProcessRecordService.insertProcessRecord(pprd);
            }
            if (i == 1) {
                Integer t = 2;
                map.put("result", t);
            }
            return map;
        }
    }

    Logger logger = LoggerFactory.getLogger(PostFacade.class);

    /**
     * 查询加精排序
     *
     * @return
     */
    public PostChoiceness queryPostChoiceness(String postid, String essencedate) {
        Map<String, List> map = new HashedMap();
        PostChoiceness postChoiceness = new PostChoiceness();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date esdate = null;
        if (!StringUtils.isEmpty(essencedate)) {
            try {
                esdate = format.parse(essencedate);
            } catch (ParseException e) {
                logger.error("时间格式转换异常");
            }
        }
        Map m = new HashedMap();
        m.put("esdate", esdate);
        List<PostTo> posts = postService.queryPostChoicenesslist(m);//返回加精日期内有几条加精
        if (postid != null) {
            postChoiceness = postService.queryPostChoiceness(Integer.parseInt(postid));//查询帖子加精回显数据
        }
        List<Integer> lou = new ArrayList();
        for (int e = 1; e < 6; e++) {//赋值一个的集合，用于返回排序
            lou.add(e);
        }
        for (int i = 0; i < posts.size(); i++) {
            for (int j = 0; j < lou.size(); j++) {
                if (posts.get(i).getOrderid() == lou.get(j)) {
                    if (postid != null && postChoiceness != null) {
                        if (lou.get(j) != postChoiceness.getOrderid()) {
                            lou.remove(j);
                        }
                    } else {
                            lou.remove(j);
                        }
                    }
                }
            }

        map.put("result", lou);
        if (postChoiceness != null) {
            postChoiceness.setOrderids(map);
        } else {
            PostChoiceness p = new PostChoiceness();
            p.setOrderids(map);
        }
        return postChoiceness;
    }


    /**
     * 查询帖子分享列表
     *
     * @param postid
     * @param pager
     * @return
     */
    public List<SharesVo> queryPostShareList(String postid, String type, Paging<SharesVo> pager) {
        Map map = new HashedMap();
        map.put("postid", postid);
        map.put("type", type);
        return sharesService.queryPostShareList(pager, map);
    }

    /**
     * 查询名称
     *
     * @param name
     * @return
     */
    public List<UserLike> likeQueryPostByNickname(String name, String loginid) {
        Map map = new HashMap();
        map.put("name", name);
        Map resault = commonalityFacade.verifyUserByQueryMethod(Integer.parseInt(loginid), null, JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), null);
        if (resault.get("resault").equals(2)) {
            return userService.likeQueryPostByNickname(map);
        } else {
            map.put("loginid", loginid);
            return userService.likeQueryPostByNickname(map);
        }
    }


    /**
     * 帖子编辑数据回显
     *
     * @param postid
     * @return
     */
    public PostCompile queryPostByIdEcho(String postid) {
        PostCompile postCompile = postService.queryPostByIdEcho(Integer.parseInt(postid));//帖子编辑数据回显
        //查找是否有缩略图，有显示缩略图，否则显示原图
        String str = null;
        if (postCompile != null) {
            Map map = new HashMap();
            map.put("url", postCompile.getCoverimg());
            str = compressImgService.queryUrlIsCompress(map);
        }
        if (str != null) {
            postCompile.setCoverimg(str);
        }
        List<GoodsVo> goodses = null;
        if (postCompile != null) {
            goodses = goodsService.queryGoods(postCompile.getId());//根据帖子id查询被分享的商品
        }
        if (goodses != null) {
            postCompile.setGoodses(goodses);
        }
        return postCompile;
    }

    /**
     * 编辑活动帖子
     *
     * @param
     * @param id
     * @param title
     * @param subtitle
     * @param activetype
     * @param activefee
     * @param userid
     * @param coverimg
     * @param begintime
     * @param endtime
     * @param isessence
     * @param orderid
     * @param postcontent
     * @param essencedate
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map<String, Integer> updateActivePostById(String id, String title, String subtitle, String userid, String coverimg, String postcontent, String isessence,
                                                     String orderid, String activefee, String activetype, String iscontribute, String begintime, String endtime, String hotimgurl, String ishot, String essencedate, String goodsid) {
        PostActiveList postActiveList = new PostActiveList();
        Map<String, Integer> map = new HashedMap();
        if (postcontent.length() < 20000) {
            try {
                postActiveList.setId(Integer.parseInt(id));//帖子id
                postActiveList.setTitle(title);//帖子标题
                postActiveList.setSubtitle(subtitle);//帖子副标题
                if (!StringUtil.isEmpty(activetype)) {
                    postActiveList.setActivetype(Integer.parseInt(activetype));
                }
                postActiveList.setCoverimg(coverimg);//帖子封面
                if (!StringUtil.isEmpty(activefee)) {
                    postActiveList.setActivefee(Double.parseDouble(activefee));//费用
                } else {
                    postActiveList.setActivefee(0.0);
                }
                if (!StringUtils.isEmpty(iscontribute)) {//是否投稿
                    postActiveList.setIscontribute(iscontribute);
                }
                if (!StringUtil.isEmpty(userid)) {
                    postActiveList.setUserid(Integer.parseInt(userid));
                }
                if (StringUtil.isNotEmpty(postcontent)) {
                    if (postcontent.length() > 2000)
                        postActiveList.setPostcontent(postcontent);//内容
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date estime = null;
                if (!StringUtil.isEmpty(isessence)) {
                    if (Integer.parseInt(isessence) == 0) {
                        postActiveList.setIsessence(Integer.parseInt(isessence));//是否为首页精选
                        postActiveList.setEssencedate(null);
                        postActiveList.setOrderid(null);
                    } else {
                        if (StringUtil.isNotEmpty(essencedate)) {
                            try {
                                estime = format.parse(essencedate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            postActiveList.setEssencedate(estime);
                        }
                        if (StringUtil.isNotEmpty(orderid)) {
                            postActiveList.setOrderid(Integer.parseInt(orderid));
                        }
                        postActiveList.setIsessence(Integer.parseInt(isessence));//是否为首页精选
                    }
                }
                if (!StringUtils.isEmpty(hotimgurl)) {
                    postActiveList.setHotimgurl(hotimgurl);//首页方形图
                }
                if (!StringUtil.isEmpty(ishot)) {
                    postActiveList.setIshot(ishot);
                }
                int result = postService.updateActivePostById(postActiveList);//编辑活动帖子
                Period period = new Period();
                period.setPostid(Integer.parseInt(id));
                Date bstime = null;
                if (!StringUtil.isEmpty(begintime)) {
                    try {
                        bstime = format.parse(begintime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                period.setBegintime(bstime);
                Date enstime = null;
                if (!StringUtil.isEmpty(endtime)) {
                    try {
                        enstime = format.parse(endtime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                period.setEndtime(enstime);
                if (enstime != null && bstime != null) {
                    int res = postService.updateActivePostPerById(period);
                }
                if (!StringUtils.isEmpty(goodsid)) {
                    String[] lg = goodsid.split(",");//以逗号分隔
                    int de = goodsService.deleteActivityByGoods(Integer.parseInt(id));//删除活动发表的商品

                    for (int i = 0; i < lg.length; i++) {
                        Map addgoods = new HashedMap();
                        addgoods.put("postid", id);
                        addgoods.put("goodsid", lg[i]);
                        int goods = postService.insertPromotionGoods(addgoods);//添加活动商城促销类商品
                        if (goods == 1) {//修改活动
                            map.put("result", goods);
                        }
                    }
                } else {
                    goodsService.deleteActivityByGoods(Integer.parseInt(id));//删除活动发表的商品
                }

                PostProcessRecord postProcessRecord = postProcessRecordService.queryPostByIsessenceOrIshot(Integer.parseInt(id));
                if (postProcessRecord != null) {//已经加精过活精选
                    //修改
                    PostProcessRecord pprd = new PostProcessRecord();
                    pprd.setIshot(Integer.parseInt(ishot));
                    pprd.setPostid(Integer.parseInt(id));
                    pprd.setIsesence(Integer.parseInt(isessence));
                    postProcessRecordService.updateProcessRecord(pprd);
                } else {
                    //新增
                    PostProcessRecord pprd = new PostProcessRecord();
                    pprd.setPostid(Integer.parseInt(id));
                    pprd.setIsesence(Integer.parseInt(isessence));
                    pprd.setIshot(Integer.parseInt(ishot));
                    postProcessRecordService.insertProcessRecord(pprd);
                }
                map.put("result", result);
            } catch (Exception e) {
                log.error("帖子编辑异常", e);
            }
        } else {
            map.put("resault", -2);
        }

        return map;
    }
    /**
     * 编辑帖子
     *
     * @param
     * @param title
     * @param subtitle
     * @param type
     * @param userid
     * @param circleid
     * @param vid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param orderid
     * @param time
     * @return
     */
    @Transactional
    @CacheEvict(value = "indexData", key = "'index_data'")
    public Map updatePostById(HttpServletRequest request, String id, String title, String subtitle, String type,
                              String userid, String circleid, String vid, String bannerimgurl,
                              String coverimg, String postcontent, String isessence, String ishot, String orderid, String time, String goodsid, String loginid) {
        PostTo post = new PostTo();
        Map map = new HashedMap();
        Integer lgid = Integer.parseInt(loginid);
        Integer pid = Integer.parseInt(id);
        Map res = commonalityFacade.verifyUserJurisdiction(lgid, JurisdictionConstants.JURISDICTION_TYPE.update.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), pid);
        if (res.get("resault").equals(1)) {
            if (postcontent.length() < 30000) {
                try {
                    post.setId(pid);//帖子id
                    post.setTitle(title);//帖子标题
                    post.setSubtitle(subtitle);//帖子副标题
                    if (!StringUtils.isEmpty(type)) {
                        post.setType(type);//帖子类型
                    }
                    if (!StringUtils.isEmpty(circleid)) {
                        post.setCircleid(circleid);//圈子id
                    }

                    Video vide = new Video();
                    Integer in = null;
                    //String fName = FileUtil.getPicName(vid);//获取视频文件名
                    //查询圈子名称
                    //String circlename = circleService.queryCircleName(Integer.parseInt(circleid));
                    //String videoid = videoUploadUtil.videoUpload(vid, fName, "", bannerimgurl, circlename);
                    if (type.equals("1")) {//帖子类型为原生视频贴时修改
                        if (!StringUtils.isEmpty(id)) {
                            vide.setPostid(pid);
                        }
                        if (!StringUtils.isEmpty(vid)) {
                            vide.setVideourl(vid);
                        }
                        if (!StringUtils.isEmpty(bannerimgurl)) {
                            vide.setBannerimgurl(bannerimgurl);
                        }
                        vide.setIntime(new Date());
                        int tt = videoService.queryVideoByID(pid);//查询帖子是否有发视频
                        if (tt > 0) {//已经有啦  修改
                            in = videoService.updateVideoById(vide);
                        } else {//没有添加
                            vide.setBannerimgurl(coverimg);
                            in = videoService.insertVideoById(vide);
                        }
                    } else if (type.equals("2")) {//帖子为分享视频贴时修改
                        if (!StringUtils.isEmpty(id)) {
                            vide.setPostid(pid);
                        }
                        if (!StringUtils.isEmpty(vid)) {
                            vide.setVideourl(vid);
                        }
                        if (!StringUtils.isEmpty(bannerimgurl)) {
                            vide.setBannerimgurl(coverimg);//分享视频贴的封面是帖子的封面
                        }
                        vide.setIntime(new Date());
                        int tt = videoService.queryVideoByID(pid);//查询帖子是否有发视频
                        if (tt > 0) {//已经有啦  修改
                            in = videoService.updateVideoById(vide);
                        } else {//没有添加
                            vide.setBannerimgurl(coverimg);
                            in = videoService.insertVideoById(vide);
                        }
                    }
                    if (!StringUtils.isEmpty(coverimg)) {
                        post.setCoverimg(coverimg);//编辑帖子封面
                    }
                    post.setIsactive("0");//设置状态为帖子
                    if (StringUtil.isNotEmpty(postcontent)) {


                        //内容转换
                        Map con = jsoupCompressImg.compressImg(request, postcontent);
                        if ((int) con.get("code") == 200) {
                            System.out.println(con);
                            String str = con.get("content").toString();
                            str = str.replace("\\", "");
                            post.setPostcontent(str);//帖子内容
                        } else {
                            logger.error("帖子内容转换异常");
                            post.setPostcontent(postcontent);
                        }
                        /*post.setPostcontent(postcontent);*/

                    }
                    post.setIntime(new Date());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date estime = null;

                    if (!StringUtils.isEmpty(isessence)) {
                        if (Integer.parseInt(isessence) == 0) {
                            post.setIsessence(isessence);//是否为首页精选
                            post.setEssencedate(null);
                            post.setOrderid(null);
                        } else {
                            if (StringUtil.isNotEmpty(time)) {
                                try {
                                    estime = format.parse(time);
                                    post.setEssencedate(estime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (StringUtil.isNotEmpty(orderid)) {
                                post.setOrderid(Integer.parseInt(orderid));
                            }
                            post.setIsessence(isessence);//是否为首页精选
                        }
                    }
                    if (!StringUtils.isEmpty(ishot)) {
                        post.setIshot(ishot);//是否为圈子精选
                    }
                    post.setUserid(userid);
                    int result = postService.updatePostById(post);//编辑帖子
                    map.put("result", result);
                    // map.put("videoid", videoid);
                    if (goodsid != null && goodsid != "") {//添加商品
                        String[] lg = goodsid.split(",");//以逗号分隔
                        int de = goodsService.deletePostyByGoods(Integer.parseInt(id));//删除帖子分享的商品
                        for (int i = 0; i < lg.length; i++) {
                            Map addgoods = new HashedMap();
                            addgoods.put("postid", id);
                            addgoods.put("goodsid", lg[i]);
                            int goods = postService.insertGoods(addgoods);//添加帖子分享的商品
                            if (goods == 1 && in == 1) {//修改帖子和修改视频文件成功
                                map.put("result", goods);
                            }
                        }
                    } else {
                        goodsService.deletePostyByGoods(Integer.parseInt(id));//删除帖子分享的商品
                    }
                    PostProcessRecord postProcessRecord = postProcessRecordService.queryPostByIsessenceOrIshot(Integer.parseInt(id));
                    if (postProcessRecord != null) {//已经加精过活精选
                        //积分操作
                        if (postProcessRecord.getIshot() == 0 && Integer.parseInt(ishot) == 1) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.circle_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                        if (postProcessRecord.getIsesence() == 0 && Integer.parseInt(isessence) == 1) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                        //修改
                        PostProcessRecord pprd = new PostProcessRecord();
                        if (StringUtil.isNotEmpty(ishot)) {
                            pprd.setIshot(Integer.parseInt(ishot));
                        }
                        pprd.setPostid(post.getId());
                        if (StringUtil.isNotEmpty(isessence)) {
                            pprd.setIsesence(Integer.parseInt(isessence));
                        }
                        postProcessRecordService.updateProcessRecord(pprd);
                    } else {
                        //积分操作
                        if (postProcessRecord.getIshot() == 1) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.circle_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                        if (postProcessRecord.getIsesence() == 1) {
                            pointRecordFacade.addPointForCircleAndIndexSelected(PointConstant.POINT_TYPE.index_selected.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
                        }
                        //新增
                        PostProcessRecord pprd = new PostProcessRecord();
                        pprd.setPostid(Integer.parseInt(id));
                        if (StringUtil.isNotEmpty(isessence)) {
                            pprd.setIsesence(Integer.parseInt(isessence));
                        }
                        if (StringUtil.isNotEmpty(ishot)) {
                            pprd.setIshot(Integer.parseInt(ishot));
                        }
                        postProcessRecordService.insertProcessRecord(pprd);
                    }
                } catch (Exception e) {
                    log.error("帖子编辑异常", e);
                }
            } else {
                map.put("resault", -2);
            }
            return map;
        } else {
            map.put("resault", -1);
            map.put("message", "权限不足");
            return map;
        }
    }


    /**
     * 帖子按条件查询
     *
     * @param title
     * @param circleid
     * @param postcontent
     * @param endtime
     * @param begintime
     * @param essencedate
     * @param pager
     * @return
     */
    public List<PostList> postSearch(String title, String circleid,
                                     String userid, String postcontent, String endtime,
                                     String begintime, String pai, String essencedate, String uid, String price, String loginid, Paging<PostList> pager) {
        Map map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date end = null;
        Date beg = null;
        Date ess = null;
        //结束时间,开始时间
        if (!StringUtils.isEmpty(endtime) && !StringUtils.isEmpty(begintime)) {
            try {
                end = format.parse(endtime);
                beg = format.parse(begintime);
                map.put("endtime", end);
                map.put("begintime", beg);
            } catch (ParseException e) {
                log.error("时间格式转换异常");
            }
        }
        map.put("endtime", end);
        map.put("begintime", beg);
        if (StringUtil.isNotEmpty(uid)) {
            map.put("uid", uid);//从用户列表跳转过来传参
        }
        if (StringUtil.isNotEmpty(price) && StringUtil.isNotEmpty(uid)) {
            map.put("price", price);//从用户列表跳转过来传参
        }
        //精选时间
        if (!StringUtils.isEmpty(essencedate)) {
            try {
                ess = format.parse(essencedate);
                map.put("essencedate", ess);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("essencedate", ess);
        if (!StringUtils.isEmpty(title)) {
            map.put("title", title);//帖子标题
        }
        if (!StringUtils.isEmpty(circleid)) {
            map.put("circleid", circleid);//圈子id
        }
        if (!StringUtils.isEmpty(userid)) {
            map.put("userid", userid);//发帖人id
        }
        if (!StringUtils.isEmpty(postcontent)) {
            map.put("postcontent", postcontent);//帖子内容
        }
        if (!StringUtils.isEmpty(pai)) {
            map.put("pai", pai);
        }
        Integer louser = Integer.parseInt(loginid);
        Map res = commonalityFacade.verifyUserByQueryMethod(louser, JurisdictionConstants.JURISDICTION_TYPE.select.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), null);
        List<PostList> list = new ArrayList<>();
        if (res.get("resault").equals(2) || res.get("resault").equals(0)) {
            list = postService.postSearch(map, pager);
            return list;
        } else if (res.get("resault").equals(1)) {
            map.put("loginid", loginid);
                list = postService.queryPostByManageByList(map, pager);
            return list;
        }
        return list;
    }

    /**
     * 评论列表根据点赞人气排序
     *
     * @param postid
     * @return
     */
    public List<CommentVo> commentZanSork(String postid, Paging<CommentVo> pager) {
        return commentService.commentZanSork(Integer.parseInt(postid), pager);
    }

    /**
     * 搜索含有敏感字的评论
     *
     * @param content
     * @param words
     * @param
     * @return
     */
    public List<CommentVo> queryCommentSensitiveWords(String content, String words, String begintime, String endtime, Paging<CommentVo> pager) {
        Date beg = null;
        Date end = null;
        if (!StringUtils.isEmpty(begintime) && !StringUtils.isEmpty(endtime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            } catch (ParseException e) {
                logger.error("时间转换异常", e);
            }
        }
        Map map = new HashedMap();
        map.put("content", content);
        map.put("words", words);
        map.put("begintime", beg);
        map.put("endtime", end);
        List<CommentVo> list = commentService.queryCommentSensitiveWords(map, pager);
        return list;
    }

    /**
     * 活动条件查询
     *
     * @param title
     * @param name
     * @param content
     * @param begintime
     * @param endtime
     * @param statue
     * @param loginid
     * @param pager
     * @return
     */
    public List<PostActiveList> queryAllActivePostCondition(String title, String name, String content, String begintime, String endtime, String statue, String pai, String loginid, Paging<PostActiveList> pager) {
        Map<String, Object> map = new HashedMap();
        if (StringUtil.isNotEmpty(title)) {
            map.put("title", title);
        }
        if (StringUtil.isNotEmpty(name)) {
            map.put("userid", name);
        }
        if (StringUtil.isNotEmpty(content)) {
            map.put("content", content);
        }


        Date isessencetime = null;//开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtil.isNotEmpty(begintime)) {
            try {
                isessencetime = format.parse(begintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("begintime", isessencetime);
        Date max = null;//最大时间
        if (StringUtil.isNotEmpty(endtime)) {
            try {
                max = format.parse(endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("endtime", max);
        if (!StringUtils.isEmpty(statue)) {
            map.put("statue", statue);
        }
        if (!StringUtils.isEmpty(pai)) {
            map.put("pai", pai);
        }
        Integer userid = Integer.parseInt(loginid);
        Map res = commonalityFacade.verifyUserByQueryMethod(userid, JurisdictionConstants.JURISDICTION_TYPE.select.getCode(), JurisdictionConstants.JURISDICTION_TYPE.post.getCode(), null);
        List<PostActiveList> list = new ArrayList<>();
        BossUser logintype = bossUserService.queryUserByAdministrator(userid);//根据登录用户id查询当前用户有哪些权限
        if (res.get("resault").equals(1)) {//查询当前登录用户的帖子列表
            //查询用户管理的圈子id
            if (logintype.getCirclemanagement() == 1) {//圈子管理员
                map.put("loginid", userid);
                list = postService.queryAllActivePostCondition(map, pager);
            } else if (logintype.getIscircle() == 1) {//圈主
                map.put("loginid", userid);
                list = postService.queryAllActivePostCondition(map, pager);
            }
        } else if (res.get("resault").equals(2) || res.get("resault").equals(0)) {//权限最大查询所有帖子列表
            list = postService.queryAllActivePostCondition(map, pager);
        }
        List<PostActiveList> rewardeds = new ArrayList<>();
        Date date = new Date();
        long str = date.getTime();
        for (int i = 0; i < list.size(); i++) {
            PostActiveList postList = new PostActiveList();
            Integer postid = list.get(i).getId();//获取到帖子id
            Integer persum = postService.queryPostPerson(postid);
            String nickname = userService.queryUserByNicknameBy(postid);
            Period periods = periodService.queryPostPeriod(postid);
            Double activefee = list.get(i).getActivefee();
            Double sumfree = 0.0;
            if (activefee != null) {
                sumfree = persum * activefee;
            }
            Date begintimes = periods.getBegintime();
            Date endtimes = periods.getEndtime();
            postList.setId(list.get(i).getId());
            postList.setTitle(list.get(i).getTitle());//主题
            postList.setNickname(nickname);//昵称
            postList.setActivetype(list.get(i).getActivetype());//活动类型
            postList.setActivefee(activefee);//活动费用
            postList.setEssencedate(list.get(i).getEssencedate());//精选日期
            postList.setOrderid(list.get(i).getOrderid());//精选排序
            postList.setBegintime(begintimes);//开始时间
            postList.setEndtime(endtimes);//结束时间
            postList.setPersum(persum);//报名人数
            postList.setSumfree(sumfree);//总费用
            String activeStatue = "";
            if (begintime != null && endtime != null) {
                long begin = begintimes.getTime();
                long end = endtimes.getTime();
                if (str > begin && str < end) {
                    activeStatue = "报名中";
                } else if (str < begin) {
                    activeStatue = "未开始";
                } else if (str > end) {
                    activeStatue = "已结束";
                }
            }
            postList.setActivestatue(activeStatue);//活动状态
            postList.setZansum(list.get(i).getZansum());
            postList.setCollectsum(list.get(i).getCollectsum());
            postList.setCommentsum(list.get(i).getCommentsum());
            postList.setForwardsum(list.get(i).getForwardsum());
            postList.setIntime(list.get(i).getIntime());
            rewardeds.add(postList);
        }

        return rewardeds;
    }


    /**
     * 查询商品列表
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryPostByGoodsList(Paging<GoodsVo> pager) {
        List<GoodsVo> goodsVos = goodsService.queryPostByGoodsList(pager);
        return goodsVos;
    }

    /**
     * 用于条件查询商品列表（帖子使用）
     *
     * @param name
     * @param brandid
     * @param protype
     * @param pager
     * @return
     */
    public List<GoodsVo> findAllQueryLikeGoods(String name, String goodsid, String brandid, String protype, Paging<GoodsVo> pager) {
        Map map = new HashedMap();
        if (!StringUtils.isEmpty(name)) {
            map.put("name", name);
        }
        if (!StringUtils.isEmpty(brandid)) {
            map.put("brandid", brandid);
        }
        if (!StringUtils.isEmpty(protype)) {
            map.put("protype", protype);
        }
        if (!StringUtils.isEmpty(goodsid)) {
            map.put("goodsid", goodsid);
        }
        return goodsService.findAllQueryLikeGoods(map, pager);
    }

    public List<Map> findAllMyCollectPostList(Paging<Map> paging, int userid) throws ParseException {
        Map map = new HashedMap();
        map.put("userid", userid);
        List<Map> list = postService.findAllMyCollectPost(paging, map);

        /**
         * 添加活动距离结束的剩余天数——enddays
         * 已投稿总数——partsum
         */
        for (int i = 0; i < list.size(); i++) {
            Object begintimeObj = list.get(i).get("begintime");
            Object endtimeObj = list.get(i).get("endtime");
            if (null != begintimeObj && null != endtimeObj) {

                getEnddays(list, i, (Date) begintimeObj, (Date) endtimeObj);
            } else {
                list.get(i).put("enddays", -2); //表示无活动
            }

            //计算已投稿总数
            int isactive = Integer.valueOf(String.valueOf(list.get(i).get("isactive")));
            int partsum = -1;   //默认-1，表示不是活动
            if (isactive == 1) {  //该帖子属于活动
                int postid = Integer.valueOf(String.valueOf(list.get(i).get("id")));//获取活动id
                partsum = postService.queryActivePartSum(postid);
            }
            list.get(i).put("partsum", partsum);

            //essencedate 日期2017-04-28转化为毫秒值
            if (list.get(i).get("essencedate") != null) {
                Date date = (Date) list.get(i).get("essencedate");
                list.get(i).put("essencedate", date.getTime());
            }
        }

        return list;
    }

    /**
     * 遍历所有的活动开始时间和结束时间，计算活动距离结束的剩余天数
     *
     * @param list
     * @param i
     * @param begintimeObj
     * @param endtimeObj
     * @throws ParseException
     */
    private void getEnddays(List<Map> list, int i, Date begintimeObj, Date endtimeObj) throws ParseException {
        Date begin = begintimeObj;//活动开始时间
        Date end = endtimeObj;//活动结束时间
        Date now = new Date();//活动当前时间
        if (now.before(begin)) {
            list.get(i).put("enddays", -1);//活动还未开始
        } else if (end.before(now)) {
            list.get(i).put("enddays", 0);//活动已结束
        } else if (begin.before(now) && now.before(end)) {
            try {
                log.error("计算活动剩余结束天数");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date a = sdf.parse(sdf.format(now));
                Date b = sdf.parse(sdf.format(end));
                Calendar cal = Calendar.getInstance();
                cal.setTime(a);
                long time1 = cal.getTimeInMillis();
                cal.setTime(b);
                long time2 = cal.getTimeInMillis();
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                list.get(i).put("enddays", Integer.parseInt(String.valueOf(between_days)));
            } catch (Exception e) {
                log.error("计算活动剩余结束天数失败", e);
                throw e;
            }
        }
    }

    /**
     * 获取我的申请vip的准备条件的统计
     *
     * @return
     */
    public Map getMyVipApplyStatistics() {
        Map result = new HashedMap();

        queryPostDataAndCount(result);

        queryVipApplyStatus(result);

        return result;
    }

    /**
     * 查询帖子的：发帖，首页精选，被赞， 被分享  等情况
     *
     * @param result
     */
    private void queryPostDataAndCount(Map result) {
        Map map = new HashedMap();
        map.put("userid", ShiroUtil.getAppUserID());

        List<Post> postList = postService.queryMyPostList(map);
        if (ListUtil.isNotEmpty(postList)) {
            //1 发帖次数大于50
            result.put("publish_count_flag", postList.size() >= 50);

            //2 首页精选次数达到10次, 3 被赞次数达到500次， 4 分享总数达到500次
            int selectedCount = 0, supportCount = 0, shareCount = 0;
            for (Post post : postList) {

                if (null != post.getIsessence() && post.getIsessence() == 1) {
                    selectedCount++;
                }
                supportCount += null == post.getZansum() ? 0 : post.getZansum();
                shareCount += null == post.getForwardsum() ? 0 : post.getForwardsum();
            }
            result.put("selected_count_flag", selectedCount >= 10);
            result.put("support_count_flag", supportCount >= 500);
            result.put("share_count_flag", shareCount >= 500);
            //返回次数
            result.put("publish_count", postList.size());
            result.put("selected_count", selectedCount);
            result.put("support_count", supportCount);
            result.put("share_count", shareCount);

        } else {
            result.put("publist_count_flag", false);
            result.put("selected_count_flag", false);
            result.put("support_count_flag", false);
            result.put("share_count_flag", false);

            result.put("publish_count", 0);
            result.put("selected_count", 0);
            result.put("support_count", 0);
            result.put("share_count", 0);
        }
    }

    /**
     * 查询申请VIP的状态
     *
     * @param result
     */
    private void queryVipApplyStatus(Map result) {
        ApplyVipDetail latestApplyVipDetail = applyVipDetailService.selectLatestVipApplyRecord(ShiroUtil.getAppUserID());
        if (null == latestApplyVipDetail) {
            result.put("status_code", "3");
            result.put("status_msg", "未申请");
        } else {
            AuditVipDetail auditVipDetail = auditVipDetailService.selectByApplyId(latestApplyVipDetail.getId());
            if (null == auditVipDetail) {
                result.put("status_code", "2");
                result.put("status_msg", "待审核");
            } else {
                Integer status = auditVipDetail.getStatus();
                if (status == 0) {
                    result.put("status_code", "0");
                    result.put("status_msg", "审核通过");
                } else {
                    result.put("status_code", "1");
                    result.put("status_msg", "审核不通过");
                }
            }
        }
    }

    /**
     * 根据id查询出活动
     *
     * @param id
     * @return
     */
    public PostActiveList queryActiveById(Integer id) {
        PostActiveList postActiveList = postService.queryActiveById(id);
        List<GoodsVo> goodses = null;
        if (postActiveList != null) {
            goodses = goodsService.queryGoods(postActiveList.getId());
        }
        postActiveList.setGoodss(goodses);
        return postActiveList;
    }

    /**
     * 查询用户收藏的帖子列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<PostList> queryCollectPostList(String userid, Paging<PostList> pager) {
        return postService.queryCollectPostList(userid, pager);
    }

    /**
     * 遍历帖子中每个img时先判断是否已经压缩过（防止修改帖子内容时重复压缩）
     */
    public int queryIsHaveCompress(String imgurl) {
        return postService.queryIsHaveCompress(imgurl);
    }

    /**
     * 查询帖子中的这张图片在压缩映射关系表中是否存在
     */
    public int queryCount(CompressImg compressImg) {
        return postService.queryCount(compressImg);
    }

    /**
     * 帖子中高清图片压缩处理后保存原图和压缩图url对应关系
     */
    public void addCompressImg(CompressImg compressImg) {
        postService.addCompressImg(compressImg);
    }

    /**
     * 是否设为热门
     *
     * @param id
     * @return
     */
    public Integer updateIshot(Integer id) {
        int ishot = postService.activeIsHot(id);
        int result = 0;
        if (ishot == 0) {
            result = postService.updateIshot(id);
        } else if (ishot == 1) {
            result = postService.updateNoIshot(id);
        }
        return result;
    }

    /**
     * 特邀嘉宾操作帖子，加入精选池
     *
     * @param postid
     * @param userid
     * @return
     */
    public Map addPostByisessencepool(String postid, String userid) {
        Map map = new HashedMap();
        //查询登录用户是否是特邀嘉宾
        Integer guest = bossUserService.queryUserIdBySpeciallyGuest(Integer.parseInt(userid));//判断用户是否是特邀嘉宾
        if (guest != null) {
            if (guest.equals(1)) {//是
                Integer resault = postService.addPostByisessencepool(Integer.parseInt(postid));
                map.put("resault", resault);
                return map;
            } else {
                map.put("resault", -1);
                return map;
            }
        } else {
            map.put("resault", -1);
            return map;
        }
    }

    /**
     * 查询精选池帖子列表
     *
     * @param loginid
     * @param pager
     * @return
     */
    public List queryPostByIsessencepoolList(String title, String circleid, String userid, String postcontent, String endtime,
                                             String begintime, String essencedate, String loginid, String pai, Paging<PostList> pager) {
        Integer uid = Integer.parseInt(loginid);
        Map res = commonalityFacade.verifyUserByQueryMethod(uid, JurisdictionConstants.JURISDICTION_TYPE.select.getCode(), JurisdictionConstants.JURISDICTION_TYPE.choicenesspool.getCode(), null);
        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date end = null;
        Date beg = null;
        Date ess = null;
        if (res.get("resault").equals(2)) {
            //结束时间,开始时间
            if (!StringUtils.isEmpty(endtime) && !StringUtils.isEmpty(begintime)) {
                try {
                    end = format.parse(endtime);
                    beg = format.parse(begintime);
                    map.put("endtime", end);
                    map.put("begintime", beg);
                } catch (ParseException e) {
                    log.error("时间格式转换异常");
                }
            }
            if (StringUtil.isNotEmpty(essencedate)) {
                try {
                    ess = format.parse(essencedate);
                    map.put("essencedate", ess);
                } catch (ParseException e) {
                    log.error("时间格式转换异常");
                }
            }
            if (StringUtil.isNotEmpty(title)) {
                map.put("title", title);
            }
            if (StringUtil.isNotEmpty(circleid)) {
                map.put("circleid", circleid);
            }
            if (StringUtil.isNotEmpty(userid)) {
                map.put("userid", userid);
            }
            if (StringUtil.isNotEmpty(postcontent)) {
                map.put("postcontent", postcontent);
            }
            if (StringUtil.isNotEmpty(pai)) {
                map.put("pai", pai);
            }
            return postService.queryPostByIsessencepoolList(map, pager);
        } else {
            return null;
        }
    }

    /**
     * 查询活动投稿列表
     *
     * @param nickname
     * @param email
     * @param type
     * @param postname
     * @param begintime
     * @param endtime
     * @param pai
     * @param pager
     * @return
     */
    public List<ActivityContributeVo> findAllQueryActivityContribute(String nickname, String email, String type, String level, String postname, String title,
                                                                     String begintime, String endtime, String pai, Paging<ActivityContributeVo> pager) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Map map = new HashMap();
        Date beg = null;
        Date end = null;
        try {
            if (StringUtil.isNotEmpty(begintime) && StringUtil.isNotEmpty(endtime)) {
                beg = format.parse(begintime);
                end = format.parse(endtime);
            }
            map.put("begintime", beg);
            map.put("endtime", end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (StringUtil.isNotEmpty(nickname)) {
            map.put("nickname", nickname);
        }
        if (StringUtil.isNotEmpty(email)) {
            map.put("email", email);
        }
        if (StringUtil.isNotEmpty(type)) {
            map.put("type", type);
        }
        if (StringUtil.isNotEmpty(level)) {
            map.put("level", level);
        }
        if (StringUtil.isNotEmpty(postname)) {
            map.put("postname", postname);
        }
        if (StringUtil.isNotEmpty(pai)) {
            map.put("pai", pai);
        }
        if (StringUtil.isNotEmpty(title)) {
            map.put("title", title);
        }
        return activityContributeService.findAllQueryActivityContribute(map, pager);
    }

    /**
     * 查询活动投稿详情
     *
     * @param id
     * @return
     */
    public ActivityContribute queryContributeExplain(String id) {
        return activityContributeService.queryContributeExplain(id);
    }

}

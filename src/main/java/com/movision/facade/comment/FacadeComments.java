package com.movision.facade.comment;

import com.google.gson.Gson;
import com.movision.common.constant.PointConstant;
import com.movision.facade.im.ImFacade;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.newInformation.entity.NewInformation;
import com.movision.mybatis.newInformation.service.NewInformationService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/1/22 14:34
 */
@Service
public class FacadeComments {

    @Autowired
    public CommentService commentService;
    @Autowired
    public FacadePost facadePost;
    @Autowired
    public PostService postService;
    @Autowired
    private UserOperationRecordService userOperationRecordService;
    @Autowired
    private PointRecordFacade pointRecordFacade;
    @Autowired
    private NewInformationService newInformationService;
    @Autowired
    private ImFacade imFacade;
    @Autowired
    private FacadeHeatValue facadeHeatValue;
    /**
     * 帖子评论列表（二级）
     *
     * @param postid
     * @return
     */
    public List<CommentVo> queryCommentsByLsit(Paging<CommentVo> pager, String postid, String type, String userid) {
        Map map = new HashedMap();
        map.put("postid", postid);
        map.put("type", type);
        List<CommentVo> volist = commentService.queryCommentsByLsit(pager, map);
        for (int i = 0; i < volist.size(); i++) {//遍历所有帖子
            //查询该用户有没有点赞该评论
            if (!StringUtil.isEmpty(userid)) {
                Map<String, Object> parammap = new HashMap<>();
                parammap.put("userid", Integer.parseInt(userid));
                parammap.put("commentid", volist.get(i).getId());
                int sum = commentService.queryIsZan(parammap);
                volist.get(i).setIsZan(sum);
            } else {
                volist.get(i).setIsZan(0);
            }

            //遍历所有评论获取父评论
            if (null != volist.get(i).getPid()) {//当评论的pid不为空的话说明是对别的评论的评论，查出父评论
                //通过pid查询评论实体
                CommentVo commentVo = commentService.queryCommentByPid(volist.get(i).getPid());
                volist.get(i).addVo(commentVo);
            }
        }
        return volist;
    }

    public int updateCommentZanSum(String commentid, String userid) {

        //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------start
        //判断该用户有没有首次关注过圈子或有没有点赞过帖子评论等或有没有收藏过商品帖子活动
        UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
        if (null == entiy || entiy.getIszan() == 0) {
            //如果未收藏过帖子或商品的话,首次收藏赠送积分
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.first_support.getCode(), Integer.parseInt(userid));//根据不同积分类型赠送积分的公共方法（包括总分和流水）
            UserOperationRecord userOperationRecord = new UserOperationRecord();
            userOperationRecord.setUserid(Integer.parseInt(userid));
            userOperationRecord.setIszan(1);
            if (null == entiy) {
                //不存在新增
                userOperationRecordService.insertUserOperationRecord(userOperationRecord);
            } else if (entiy.getIszan() == 0) {
                //存在更新
                userOperationRecordService.updateUserOperationRecord(userOperationRecord);
            }
        }
        //增加评论热度
        facadeHeatValue.addCommentHeatValue(1, Integer.parseInt(commentid));
        //-------------------“我的”模块个人积分任务 增加积分的公共代码----------------------end

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("commentid", Integer.parseInt(commentid));
        parammap.put("intime", new Date());
        commentService.insertCommentZanRecord(parammap);
        commentService.updateCommentZanSum(Integer.parseInt(commentid));//更新评论点赞次数
        int sum = commentService.queryCommentZanSum(Integer.parseInt(commentid));//查询点赞次数

        //************************查询被点赞人的帖子评论是否被设为最新消息通知用户
        Integer isread = newInformationService.queryUserByNewInformationByCommentid(Integer.parseInt(commentid));
        NewInformation news = new NewInformation();
        //更新被点赞人的帖子评论最新消息
        if (isread != null) {
            news.setIsread(0);
            news.setIntime(new Date());
            news.setUserid(isread);
            newInformationService.updateUserByNewInformation(news);
        } else {
            //查询被点赞的评论用户
            Integer uid = commentService.queryUseridByComment(Integer.parseInt(commentid));
            //新增被点在人的帖子评论最新消息
            news.setIsread(0);
            news.setIntime(new Date());
            news.setUserid(uid);
            newInformationService.insertUserByNewInformation(news);
        }
        //******************************************************************
        return sum;
    }

    public int insertComment(String userid, String content, String fuid, String postid) {
        if (content.length() > 500) {
            return 2;
        } else {
            int type = 0;
            if (fuid == null) {//父id为空 表示此评论是父评论
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                vo.setIsdel("0");
                vo.setStatus(0);
                vo.setIscontribute(0);
                type = commentService.insertComment(vo);//添加评论
                //更新用户最后操作时间和帖子评论总次数
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子表的评论次数字段
                //增加帖子热度
                facadeHeatValue.addHeatValue(Integer.parseInt(postid), 4, userid);
                //增加用户热度
                //************************查询被评论的帖子是否被设为最新消息通知用户
                Integer isread = newInformationService.queryUserByNewInformation(Integer.parseInt(postid));
                NewInformation news = new NewInformation();
                //更新被点赞人的帖子最新消息
                if (isread != null) {
                    news.setIsread(0);
                    news.setIntime(new Date());
                    news.setUserid(isread);
                    newInformationService.updateUserByNewInformation(news);
                } else {
                    //查询被点赞的帖子发帖人
                    Integer uid = postService.queryPosterActivity(Integer.parseInt(postid));
                    //新增被点在人的帖子最新消息
                    news.setIsread(0);
                    news.setIntime(new Date());
                    news.setUserid(uid);
                    newInformationService.insertUserByNewInformation(news);
                }
                //******************************************************************
            } else {//表示是其他评论的子评论，不算评论次数
                CommentVo vo = new CommentVo();
                vo.setContent(content);
                vo.setPostid(Integer.parseInt(postid));
                vo.setUserid(Integer.parseInt(userid));
                vo.setIntime(new Date());
                vo.setZansum(0);
                vo.setIsdel("0");
                vo.setStatus(0);
                vo.setIscontribute(0);
                vo.setPid(Integer.parseInt(fuid));
                type = commentService.insertComment(vo);//添加评论
                //增加评论热度
                facadeHeatValue.addCommentHeatValue(1, Integer.parseInt(fuid));
                //************************查询被评论的帖子是否被设为最新消息通知用户
                Integer isread = newInformationService.queryUserByNewInformation(Integer.parseInt(postid));
                NewInformation news = new NewInformation();
                //更新被评论的帖子最新消息
                if (isread != null) {
                    news.setIsread(0);
                    news.setIntime(new Date());
                    news.setUserid(isread);
                    newInformationService.updateUserByNewInformation(news);
                } else {
                    //新增被评论的最新消息
                    news.setIsread(0);
                    news.setIntime(new Date());
                    news.setUserid(Integer.parseInt(userid));
                    newInformationService.insertUserByNewInformation(news);
                }
                //******************************************************************
            }

            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.comment.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
            try {
                String fromaccid = userOperationRecordService.selectAccid(userid);
                String to = postService.selectToAccid(Integer.parseInt(postid));
                String nickname = userOperationRecordService.selectNickname(userid);
                String pinnickname = nickname + "评论了你";
                Map map = new HashMap();
                map.put("body", pinnickname);
                Gson gson = new Gson();
                String json = gson.toJson(map);
                String pushcontent = nickname + "评论了你";
                imFacade.sendMsgInform(json, fromaccid, to, pushcontent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return type;
        }
    }

    /**
     * 删除评论
     *
     * @param
     * @return
     */
    public int deleteComment(int id, int userid) {
        //查询评论是不是自己发的
        Map map = new HashMap();
        map.put("id", id);
        map.put("userid", userid);
        int count = commentService.commentCount(map);
        if (count == 0) {//不是自己发的
            return 1;
        } else {
            int result = commentService.deleteComment(id);
            return 0;
        }
    }


    /**
     * 所有评论(现在最新)
     *
     * @param postid
     * @param paging
     * @return
     */

    public List queryLNewComment(int postid, Paging<CommentVo> paging) {
        List<CommentVo> commentVos = commentService.queryOneComment(postid, paging);//所有父评论
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                commentVos.get(i).setCommentVos(NewCommentVoList(commentVos.get(i).getId()));
            }
        }
        return commentVos;
    }

    int t = 0;

    public List<CommentVo> NewCommentVoList(int pid) {
        List<CommentVo> commentVoList = commentService.queryTwoComment(pid);//父评论的子评论
        //查询子拼轮的父评论user
        User puser = commentService.queryUserInfor(pid);
        List<CommentVo> list = null;
        if (commentVoList != null && t < 2) {
            for (int i = 0; i < commentVoList.size(); i++) {
                //查询子评论下面有多少子评论
                List<CommentVo> CommentVozjh = NewCommentVoList(commentVoList.get(i).getId());
                for (int j = 0; j < CommentVozjh.size(); j++) {
                    list = new ArrayList<>();
                    list.add(CommentVozjh.get(i));
                }
                commentVoList.get(i).setCommentVos(list);
                commentVoList.get(i).setPuser(puser);
            }
            t++;
        }
        return commentVoList;
    }




    /**
     * 所有评论
     *
     * @param postid
     * @param paging
     * @return
     */
    public List queryNewComment(int postid, Paging<CommentVo> paging) {
        List<CommentVo> commentVos = commentService.queryOneComment(postid, paging);
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                commentVos.get(i).setCommentVos(GetCommentVoList(commentVos.get(i).getId()));
            }
        }
        return commentVos;
    }

    public List<CommentVo> GetCommentVoList(int pid) {
        List<CommentVo> commentVoList = commentService.queryTwoComment(pid);
        //查询子拼轮的父评论user
        User puser = commentService.queryUserInfor(pid);
        if (commentVoList != null) {
            for (int i = 0; i < commentVoList.size(); i++) {
                //查询父评论下面有多少子评论
                List<CommentVo> CommentVozjh = GetCommentVoList((int) commentVoList.get(i).getId());
                commentVoList.get(i).setCommentVos(CommentVozjh);
                commentVoList.get(i).setPuser(puser);
            }
        }
        return commentVoList;
    }


    public List queryPostNewComment(int postid) {
        List<CommentVo> commentVos = commentService.queryThreeComment(postid);
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                commentVos.get(i).setCommentVos(pCommentVoList(commentVos.get(i).getId()));
                int repliesNumber = commentService.repliesNumber(commentVos.get(i).getId());
                //根据id查询里面回复条数
                commentVos.get(i).setRepliesnumber(repliesNumber);
            }
        }
        return commentVos;
    }

    public List<CommentVo> pCommentVoList(int pid) {
        List<CommentVo> commentVoList = commentService.queryTwoComment(pid);
        if (commentVoList != null) {
            for (int i = 0; i < commentVoList.size(); i++) {
                //查询父评论下面有多少子评论
                List<CommentVo> CommentVozjh = pCommentVoList(commentVoList.get(i).getId());
                commentVoList.get(i).setCommentVos(CommentVozjh);
                int repliesNumber = commentService.repliesNumber(commentVoList.get(i).getId());
                commentVoList.get(i).setRepliesnumber(repliesNumber);
            }
        }
        return commentVoList;

    }
}


package com.movision.facade.comment;

import com.google.gson.Gson;
import com.movision.common.constant.PointConstant;
import com.movision.facade.im.ImFacade;
import com.movision.facade.index.FacadeHeatValue;
import com.movision.facade.index.FacadePost;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postCommentAccusation.entity.PostCommentAccusation;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.userOperationRecord.entity.UserOperationRecord;
import com.movision.mybatis.userOperationRecord.service.UserOperationRecordService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhurui
 * @Date 2017/1/22 14:34
 */
@Service
public class FacadeComments {

    private static Logger log = LoggerFactory.getLogger(FacadeComments.class);

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

    /**
     * 评论的点赞操作处理
     *
     * @param commentid
     * @param userid
     * @return
     */
    public int doZanWithComment(String commentid, String userid) {
        //查看用户点赞操作行为，并记录积分流水
        UserOperationRecord entiy = userOperationRecordService.queryUserOperationRecordByUser(Integer.parseInt(userid));
        facadePost.handleZanStatusAndZanPoint(userid, entiy);

        //增加评论热度
        facadeHeatValue.addCommentHeatValue(1, Integer.parseInt(commentid));

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("commentid", Integer.parseInt(commentid));
        parammap.put("intime", new Date());
        commentService.insertCommentZanRecord(parammap);    //插入评论点赞记录
        commentService.updateCommentZanSum(Integer.parseInt(commentid));    //更新帖子评论点赞次数
        int sum = commentService.queryCommentZanSum(Integer.parseInt(commentid));   //查询帖子评论点赞次数

        return sum;
    }

    /**
     * 评论帖子/评论父评论 业务流程
     *
     * @param userid
     * @param content
     * @param fuid
     * @param postid
     * @return
     */
    public int insertComment(String userid, String content, String fuid, String postid) {
        if (content.length() > 500) {
            return 2;
        } else {
            int type = 0;
            if (fuid == null) {
                //父id为空 表示此评论是父评论
                CommentVo vo = wrapParentCommentVo(userid, content, postid);
                type = commentService.insertComment(vo);//添加评论
                //更新用户最后操作时间和帖子评论总次数
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子表的评论次数字段
                //增加帖子热度
                facadeHeatValue.addHeatValue(Integer.parseInt(postid), 4, userid);

            } else {
                //表示是其他评论的子评论，不算评论次数
                CommentVo vo = wrapChildCommentVo(userid, content, fuid, postid);
                type = commentService.insertComment(vo);//添加评论
                postService.updatePostBycommentsum(Integer.parseInt(postid));//更新帖子表的评论次数字段
                //增加评论热度
                facadeHeatValue.addCommentHeatValue(1, Integer.parseInt(fuid));
            }

            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.comment.getCode(), Integer.parseInt(userid));//完成积分任务根据不同积分类型赠送积分的公共方法（包括总分和流水）
            sendCommentPush(userid, postid);
            return type;
        }
    }

    /**
     * 封装父评论对象
     *
     * @param userid
     * @param content
     * @param postid
     * @return
     */
    private CommentVo wrapParentCommentVo(String userid, String content, String postid) {
        CommentVo vo = new CommentVo();
        vo.setContent(content);
        vo.setPostid(Integer.parseInt(postid));
        vo.setUserid(Integer.parseInt(userid));
        vo.setIntime(new Date());
        vo.setZansum(0);
        vo.setIsdel("0");
        vo.setStatus(1);
        vo.setIscontribute(0);
        return vo;
    }

    /**
     * 封装子评论对象
     *
     * @param userid
     * @param content
     * @param fuid
     * @param postid
     * @return
     */
    private CommentVo wrapChildCommentVo(String userid, String content, String fuid, String postid) {
        CommentVo vo = wrapParentCommentVo(userid, content, postid);
        vo.setPid(Integer.parseInt(fuid));
        return vo;
    }

    /**
     * 推送评论通知
     *
     * @param userid
     * @param postid
     */
    private void sendCommentPush(String userid, String postid) {
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
            log.error("推送评论通知失败", e);
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
            /**if (result == 1) {
                //帖子评论次数-1
                commentService.lessPostComment(id);
             */
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
    List<CommentVo> list = new ArrayList<>();
    public List queryLNewComment(int postid, Paging<CommentVo> paging) {
        List<CommentVo> commentVos = commentService.queryOneComment(postid, paging);//所有父评论
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                commentVos.get(i).setCommentVos(NewCommentVoList(commentVos.get(i).getId()));
                commentVos.get(i).setCommentVos(list);
                list = new ArrayList();
            }
        }
        return commentVos;
    }

    public List asss(int postid, Paging<CommentVo> paging, String userid) {
        List<CommentVo> commentVoList = queryLNewComment(postid, paging);
        List<CommentVo> listWithoutDup = null;
        for (int i = 0; i < commentVoList.size(); i++) {
            //查询该用户有没有点赞该评论
            if (!StringUtil.isEmpty(userid)) {
                Map<String, Object> parammap = new HashMap<>();
                parammap.put("userid", Integer.parseInt(userid));
                parammap.put("commentid", commentVoList.get(i).getId());
                int sum = commentService.queryIsZan(parammap);
                commentVoList.get(i).setIsZan(sum);
            } else {
                commentVoList.get(i).setIsZan(0);
            }
            listWithoutDup = new ArrayList<CommentVo>(new HashSet<CommentVo>(commentVoList.get(i).getCommentVos()));
            for (int j = 0; j < listWithoutDup.size(); j++) {
                if (listWithoutDup.get(j).getPid().equals(commentVoList.get(i).getId())) {
                    listWithoutDup.get(j).setIspid(0);//第一条
                } else if (listWithoutDup.get(j).getPid() != commentVoList.get(i).getId()) {
                    User puser = commentService.queryUserInfor(listWithoutDup.get(j).getPid());
                    listWithoutDup.get(j).setPuser(puser);
                    listWithoutDup.get(j).setIspid(1);//子评论子
                }
                if (!StringUtil.isEmpty(userid)) {
                    Map<String, Object> parammap = new HashMap<>();
                    parammap.put("userid", Integer.parseInt(userid));
                    parammap.put("commentid", listWithoutDup.get(j).getId());
                    int sum = commentService.queryIsZan(parammap);
                    listWithoutDup.get(j).setIsZan(sum);
                } else {
                    listWithoutDup.get(j).setIsZan(0);
                }
            }
            commentVoList.get(i).setCommentVos(listWithoutDup);
        }
        return commentVoList;
    }


    /**
     * 帖子详情评论
     */
    List<CommentVo> lists = new ArrayList<>();
    public List queryPostsNewComment(Map<String, Object> parammap) {
        List<CommentVo> commentVos = commentService.queryThreeComment(parammap);//所有父评论
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                commentVos.get(i).setCommentVos(postDetailC(commentVos.get(i).getId()));
                commentVos.get(i).setCommentVos(lists);
                lists = new ArrayList();
            }
        }
        return commentVos;
    }

    /**
     * 帖子详情评论
     */
    public List postDetailComment(int postid, String userid) {
        Map map = new HashMap();
        map.put("postid", postid);
        map.put("userid", userid);
        List<CommentVo> commentVoList = queryPostsNewComment(map);
        List<CommentVo> listWithoutDup = null;
        for (int i = 0; i < commentVoList.size(); i++) {
            //查询该用户有没有点赞该评论
            /**if (!StringUtil.isEmpty(userid)) {
             Map<String, Object> parammap = new HashMap<>();
             parammap.put("userid", Integer.parseInt(userid));
             parammap.put("commentid", commentVoList.get(i).getId());
             int sum = commentService.queryIsZan(parammap);
             commentVoList.get(i).setIsZan(sum);
             } else {
             commentVoList.get(i).setIsZan(0);
             }*/
            listWithoutDup = new ArrayList<CommentVo>(new HashSet<CommentVo>(commentVoList.get(i).getCommentVos()));
            for (int j = 0; j < listWithoutDup.size(); j++) {
                if (listWithoutDup.get(j).getPid().equals(commentVoList.get(i).getId())) {
                    listWithoutDup.get(j).setIspid(0);//第一条
                } else if (listWithoutDup.get(j).getPid() != commentVoList.get(i).getId()) {
                    User puser = commentService.queryUserInfor(listWithoutDup.get(j).getPid());
                    listWithoutDup.get(j).setPuser(puser);
                    listWithoutDup.get(j).setIspid(1);//子评论子
                }
                /**   if (!StringUtil.isEmpty(userid)) {
                 Map<String, Object> parammap = new HashMap<>();
                 parammap.put("userid", Integer.parseInt(userid));
                 parammap.put("commentid", listWithoutDup.get(j).getId());
                 int sum = commentService.queryIsZan(parammap);
                 listWithoutDup.get(j).setIsZan(sum);
                 } else {
                 listWithoutDup.get(j).setIsZan(0);
                 }*/
            }
            commentVoList.get(i).setCommentVos(listWithoutDup);
        }
        return commentVoList;
    }


    /**
     * 帖子详情
     *
     * @param pid
     * @return
     */
    public List<CommentVo> postDetailC(int pid) {
        List<CommentVo> commentVoList = commentService.queryTwoComment(pid);//父评论的子评论2
        if (commentVoList.size() != 0) {
            for (int i = 0; i < commentVoList.size(); i++) {
                lists.addAll(commentVoList);
                //查询子评论下面有多少子评论
                postDetailC(commentVoList.get(i).getId());
            }
        }

        return commentVoList;
    }


    /**
     * 全部评论
     *
     * @param pid
     * @return
     */
    public List<CommentVo> NewCommentVoList(int pid) {
        List<CommentVo> commentVoList = commentService.queryTwoComment(pid);//父评论的子评论2
        if (commentVoList.size() != 0) {
            for (int i = 0; i < commentVoList.size(); i++) {
                list.addAll(commentVoList);
                //查询子评论下面有多少子评论
                NewCommentVoList(commentVoList.get(i).getId());
            }
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
                List<CommentVo> CommentVozjh = GetCommentVoList(commentVoList.get(i).getId());
                commentVoList.get(i).setCommentVos(CommentVozjh);
                commentVoList.get(i).setPuser(puser);
            }
        }
        return commentVoList;
    }

    public List queryPostNewComment(Map<String, Object> parammap) {
        List<CommentVo> commentVos = commentService.queryThreeComment(parammap);
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

    public int updateCommentByinform(String userid, String comment, String commentid) {
        PostCommentAccusation accusation = new PostCommentAccusation();
        if (StringUtil.isNotEmpty(userid)) {
            accusation.setUserid(Integer.parseInt(userid));
        }
        if (StringUtil.isNotEmpty(comment)) {
            accusation.setComment(comment);
        }
        if (StringUtil.isNotEmpty(commentid)) {
            accusation.setCommentid(Integer.parseInt(commentid));
        }
        accusation.setIntime(new Date());
        accusation.setType(1);
        return commentService.updateCommentByinform(accusation);
    }
}


package com.movision.facade.msgCenter;

import com.movision.common.constant.MsgCenterConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.pojo.InstantInfo;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.paging.PageFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;
import com.movision.mybatis.PostZanRecord.entity.ZanRecordVo;
import com.movision.mybatis.PostZanRecord.service.PostZanRecordService;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.entity.ReplyComment;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.followUser.entity.FollowUserVo;
import com.movision.mybatis.followUser.service.FollowUserService;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogueVo;
import com.movision.mybatis.imFirstDialogue.service.ImFirstDialogueService;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.mybatis.imSystemInform.service.ImSystemInformService;
import com.movision.mybatis.imSystemInformRead.entity.ImSystemInformRead;
import com.movision.mybatis.imSystemInformRead.service.ImSystemInformReadService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.postCommentZanRecord.service.PostCommentZanRecordService;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.model.ServicePaging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 消息中心
 *
 * @Author zhuangyuhao
 * @Date 2017/4/5 13:48
 */
@Service
public class MsgCenterFacade {

    private static Logger log = LoggerFactory.getLogger(MsgCenterFacade.class);

    @Autowired
    private PageFacade pageFacade;

    @Autowired
    private ImSystemInformService imSystemInformService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RewardedService rewardedService;

    @Autowired
    private PostCommentZanRecordService postCommentZanRecordService;

    @Autowired
    private PostZanRecordService postZanRecordService;

    @Autowired
    private ImFirstDialogueService imFirstDialogueService;

    @Autowired
    private ImSystemInformReadService imSystemInformReadService;

    @Autowired
    private FollowUserService followUserService;

    /**
     * 获取消息中心的列表
     *
     * @return
     */
    public Map getMsgCenterList(Integer userid) {
        //都是查找最新的消息
        Map reMap = new HashedMap();
        CommentVo comment = null;
        PostCommentZanRecordVo postCommentZanRecord = null;
        //1 赞消息 。包含：帖子，活动，评论，快问（后期）
        List<PostCommentZanRecordVo> postCommentZanRecordVos = zan(userid);
        if (postCommentZanRecordVos.size() > 0) {
            postCommentZanRecord = postCommentZanRecordVos.get(0);
        }
        /**for (int i = 0; i < postCommentZanRecordVos.size(); i++) {
            postCommentZanRecord = postCommentZanRecordVos.get(i);
            break;
         }*/
        //2 打赏消息
        RewardedVo rewarded = rewardedService.queryRewardByUserid(userid);
        //3 评论消息
        List<CommentVo> commentVos = comm(userid);
        if (commentVos.size() > 0) {
            comment = commentVos.get(0);
        }
        /**for (int i = 0; i < commentVos.size(); i++) {
            comment = commentVos.get(i);
            break;
         }*/
        //4 系统通知
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("informTime", ShiroUtil.getAppUser().getRegisterTime());
        ImSystemInformVo imSystemInform = imSystemInformService.queryByUserid(map);//查询最新一条
        //查询是否有未读系统通知
        Integer system = imSystemInformService.querySystemPushByUserid(map);
        if (imSystemInform != null) {
            if (system > 0) {
                imSystemInform.setIsread(0);
            } else {
                imSystemInform.setIsread(1);
            }
        }
        //5 打招呼消息
        ImFirstDialogueVo imFirstDialogue = imFirstDialogueService.queryFirst(userid);
        //查询用户打招呼信息是否还有未读
        Integer isr = imFirstDialogueService.queryIsreadByUserid(userid);
        if (imFirstDialogue != null) {
            if (isr > 0) {//还有未读
                imFirstDialogue.setIsread(0);
            } else {
                imFirstDialogue.setIsread(1);
            }
        }
        //6 客服消息
        reMap.put("imSystemInform", imSystemInform);
        reMap.put("rewarded", rewarded);
        reMap.put("comment", comment);
        reMap.put("postCommentZanRecord", postCommentZanRecord);
        reMap.put("imFirstDialogue", imFirstDialogue);
        return reMap;
    }

    /**
     * 获取消息中心-动态消息
     *
     * @param paging
     * @return
     */
    public List getInstantInfo(ServicePaging<InstantInfo> paging) {
        List<InstantInfo> list = new ArrayList<>();

        //一 评论： 1 评论帖子，
        handleCommentlist(list);
        //二 2 评论回复
        handleReplyCommentList(list);
        //二 赞
        handleZanlist(list);
        //三 关注 1 关注人 2 关注帖子 3 关注标签
        getFollowList(list, ShiroUtil.getAppUserID());


        //排序
        Collections.sort(list, InstantInfo.intimeComparator);
        //计算Paging中的分页参数
        paging.setTotal(list.size());

        //代码层分页操作
        List<InstantInfo> resultList = pageFacade.getPageList(list, paging.getCurPage(), paging.getPageSize());

        int size = resultList == null ? 0 : resultList.size();
        log.debug("【row中list的数量】：" + size);
        log.debug("【row中的list】：" + resultList.toString());
        //操作已读未读处理
        setDataIsRead(resultList);

        return resultList;

    }

    /**
     * 更新个人消息中未读
     *
     * @param userid
     * @param type
     */
    public Map updateReadByMyMessageCenter(String userid, String type) {
        Integer id = null;
        Map m = new HashMap();
        try {
            if (StringUtil.isNotEmpty(userid)) {
                id = Integer.parseInt(userid);
            }
            if (StringUtil.isNotEmpty(type)) {
                //type= 1：动态 2：通知
                if (type.equals("1")) {
                    //动态分为：评论、回复评论、谁关注我、点赞帖子、点赞评论
                    updateComment(id);//更新评论已读
                    updateZan(id);//更新赞
                    updateAttention(id);//更新关注
                } else if (type.equals("2")) {
                    updateInform(id);
                }
            }
            m.put("resault", 1);
            return m;
        } catch (NumberFormatException e) {
            m.put("resault", 2);
            return m;
        }

    }

    public void updateComment(Integer id) {
        //更新评论
        commentService.updateCommentIsRead(id);
    }

    public void updateZan(Integer id) {
        //更新点赞
        postZanRecordService.updateZanRead(id);
    }

    public void updateAttention(Integer userid) {
        //更新关注我
        followUserService.updateAttentionIsRead(userid);
    }

    public void updateInform(Integer userid) {
        ImSystemInform inform = new ImSystemInform();
        inform.setInformTime(ShiroUtil.getAppUser().getRegisterTime());//注册时间
        inform.setUserid(userid);
        //新增系统通知
        wholeSignRead(userid);
        //更新未读通知
        imSystemInformReadService.updateInform(inform);
    }

    private void handleReplyCommentList(List<InstantInfo> list) {
        List<ReplyComment> replyCommentList = commentService.selectReplyCommentList(ShiroUtil.getAppUserID());
        int len = replyCommentList.size();
        if (len > 10) {
            for (int i = 0; i < 10; i++) {
                getInstantInfoFromReplyCommentlist(list, replyCommentList, i);
            }
        } else {
            for (int i = 0; i < len; i++) {
                getInstantInfoFromReplyCommentlist(list, replyCommentList, i);
            }
        }
    }

    /**
     * 操作已读未读处理
     *
     * @param resultList
     */
    private void setDataIsRead(List<InstantInfo> resultList) {
        for (InstantInfo info : resultList) {
            int type = info.getType();
            if (MsgCenterConstant.INSTANT_INFO_TYPE.comment.getCode() == type) {
                CommentVo commentVo = (CommentVo) info.getObject();
                commentVo.setIsread(1); //已读
                commentService.updateCommentVo(commentVo);

            } else if (MsgCenterConstant.INSTANT_INFO_TYPE.zan.getCode() == type) {
                ZanRecordVo postCommentZanRecordVo = (ZanRecordVo) info.getObject();
                postCommentZanRecordVo.setIsread(1);    //已读
                postCommentZanRecordService.updatePostCommentZanRecordVo(postCommentZanRecordVo);

            } else if (MsgCenterConstant.INSTANT_INFO_TYPE.follow.getCode() == type) {
                FollowUserVo followUserVo = (FollowUserVo) info.getObject();
                followUserVo.setIsread(1);  //已读
                followUserService.updateFollowuserVo(followUserVo);

            } else {
                log.warn("非正常的动态消息类型");
            }
        }
    }

    private void handleCommentlist(List<InstantInfo> list) {
        List<CommentVo> commentList = getCommentList(ShiroUtil.getAppUserID());
        int len = commentList.size();
        if (len > 10) {
            for (int i = 0; i < 10; i++) {
                getInstantInfoFromCommentlist(list, commentList, i);
            }
        } else {
            for (int i = 0; i < len; i++) {
                getInstantInfoFromCommentlist(list, commentList, i);
            }
        }
    }

    private void handleZanlist(List<InstantInfo> list) {
        List<ZanRecordVo> zanlist = findZan(ShiroUtil.getAppUserID());

        int zanLength = zanlist.size();
        if (zanLength > 10) {
            for (int i = 0; i < 10; i++) {
                getInstantInfoFromZanlist(list, zanlist, i);
            }
        } else {
            for (int i = 0; i < zanLength; i++) {
                getInstantInfoFromZanlist(list, zanlist, i);
            }
        }
    }

    /**
     * 从评论列表中获取动态消息
     * @param list
     * @param commentVoList
     * @param i
     */
    private void getInstantInfoFromCommentlist(List<InstantInfo> list, List<CommentVo> commentVoList, int i) {
        InstantInfo instantInfo = new InstantInfo();
        instantInfo.setObject(commentVoList.get(i));
        instantInfo.setIntime(commentVoList.get(i).getIntime());
        instantInfo.setType(MsgCenterConstant.INSTANT_INFO_TYPE.comment.getCode());
        list.add(instantInfo);
    }

    private void getInstantInfoFromReplyCommentlist(List<InstantInfo> list, List<ReplyComment> replyCommentList, int i) {
        InstantInfo instantInfo = new InstantInfo();
        instantInfo.setObject(replyCommentList.get(i));
        instantInfo.setIntime(replyCommentList.get(i).getIntime());
        instantInfo.setType(MsgCenterConstant.INSTANT_INFO_TYPE.replyComment.getCode());
        list.add(instantInfo);
    }


    /**
     * 从点赞列表中获取动态信息
     *
     * @param list
     * @param zanlist
     * @param i
     */
    private void getInstantInfoFromZanlist(List<InstantInfo> list, List<ZanRecordVo> zanlist, int i) {
        InstantInfo instantInfo = new InstantInfo();
        instantInfo.setObject(zanlist.get(i));
        instantInfo.setIntime(zanlist.get(i).getIntime());
        instantInfo.setType(MsgCenterConstant.INSTANT_INFO_TYPE.zan.getCode());
        list.add(instantInfo);
    }

    /**
     * 评论
     *
     * @param userid
     * @return
     */
    public List comm(int userid) {
        List<CommentVo> commentVos = commentService.queryCommentByUserid(userid);
        if (commentVos != null) {
            for (int i = 0; i < commentVos.size(); i++) {
                int usersid = commentVos.get(i).getUserid();
                if (usersid != userid) {
                    User ruser = postCommentZanRecordService.queryusers(usersid);
                    commentVos.get(i).setUser(ruser);
                } else {
                    commentVos.remove(commentVos.get(i));
                    i--;
                }
            }

        }
        return commentVos;
    }

    public void getFollowList(List<InstantInfo> infoList, int userid) {
        List<FollowUserVo> list = followUserService.selectFollowUserVoList(userid);
        for (int i = 0; i < list.size(); i++) {
            InstantInfo instantInfo = new InstantInfo();
            instantInfo.setIntime(list.get(i).getIntime());
            instantInfo.setObject(list.get(i));
            instantInfo.setType(MsgCenterConstant.INSTANT_INFO_TYPE.follow.getCode());
            infoList.add(instantInfo);
        }
    }

    /**
     * 赞
     *
     * @param userid
     * @return
     */
    public List zan(int userid) {
        List<PostCommentZanRecordVo> postCommentZanRecordVos = postCommentZanRecordService.queryByUserid(userid);
        if (postCommentZanRecordVos != null) {
            for (int i = 0; i < postCommentZanRecordVos.size(); i++) {
                int use = postCommentZanRecordVos.get(i).getUserid();
                if (use != userid) {
                    User zusew = postCommentZanRecordService.queryusers(use);
                    postCommentZanRecordVos.get(i).setUser(zusew);
                } else {
                    postCommentZanRecordVos.remove(postCommentZanRecordVos.get(i));
                    i--;
                }
            }
        }
        return postCommentZanRecordVos;
    }


    /**
     * 获取系统通知列表
     *
     * @return
     */
    public List<ImSystemInformVo> getMsgInformationList(Integer userid, Date informTime, Paging<ImSystemInformVo> paging) {
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("informTime", informTime);
        List<ImSystemInformVo> list = imSystemInformService.findAllIm(map, paging);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCoverimg() != null) {//代表是运营通知
                list.get(i).setIsoperation(1);
            } else {//代表是系统通知
                list.get(i).setIsoperation(0);
            }
        }
        return list;
    }

    /**
     * 查询通知详情接口
     *
     * @param id
     * @return
     */
    public ImSystemInformVo queryMyMsgInforDetails(String id) {
        ImSystemInform im = new ImSystemInform();
        im.setId(Integer.parseInt(id));
        //获取当前登录用户
        Integer userid = ShiroUtil.getAppUser().getId();
        return imSystemInformService.queryMyMsgInforDetails(im);
    }

    /**
     * 获取评论列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<CommentVo> getMsgCommentList(Integer userid, Paging<CommentVo> pager) {
        List<CommentVo> comments = commentService.findAllQueryComment(userid, pager);
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                Integer pid = comments.get(i).getPid();
                Integer usersid = comments.get(i).getUserid();

                if (!usersid.equals(userid)) {
                    User user = postCommentZanRecordService.queryusers(usersid);
                    if (pid == null) {
                        Integer postid = comments.get(i).getPostid();
                        List<Post> post = postZanRecordService.queryPost(postid);
                        for (int j = 0; j < post.size(); j++) {
                            String str = post.get(j).getPostcontent();
                            String a = MsgCenterFacade.removeHtmlTag(str);
                            String b = a.replaceAll("  ", "");
                            if (StringUtil.isBlank(b)) {
                                String nickname = postCommentZanRecordService.queryPostNickname(postid);
                                String text = nickname + "的帖子";
                                comments.get(i).setPhoto(text);
                                post.get(j).setPostcontent("");
                            } else {
                                post.get(j).setPostcontent(b);
                            }
                        }
                        comments.get(i).setPost(post);
                        comments.get(i).setUser(user);
                    } else if (pid != null) {
                        List<CommentVo> commentVos = commentService.queryPidComment(pid);
                        comments.get(i).setCommentVos(commentVos);
                        comments.get(i).setUser(user);
                    }
                } else {
                    comments.remove(comments.get(i));
                    i--;
                }
            }
        }
        return comments;
    }


    /**
     * 获取评论列表
     *
     * @param userid
     * @param
     * @return
     */
    public List<CommentVo> getCommentList(Integer userid) {
        List<CommentVo> comments = commentService.findQueryComment(userid);
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                Integer pid = comments.get(i).getPid();
                Integer usersid = comments.get(i).getUserid();

                if (!usersid.equals(userid)) {
                    User user = postCommentZanRecordService.queryusers(usersid);
                    if (pid == null) {
                        Integer postid = comments.get(i).getPostid();
                        List<Post> post = postZanRecordService.queryPost(postid);
                        for (int j = 0; j < post.size(); j++) {
                            String str = post.get(j).getPostcontent();
                            String a = MsgCenterFacade.removeHtmlTag(str);
                            String b = a.replaceAll("  ", "");
                            if (StringUtil.isBlank(b)) {
                                String nickname = postCommentZanRecordService.queryPostNickname(postid);
                                String text = nickname + "的帖子";
                                comments.get(i).setPhoto(text);
                                post.get(j).setPostcontent("");
                            } else {
                                post.get(j).setPostcontent(b);
                            }
                        }
                        comments.get(i).setPost(post);
                        comments.get(i).setUser(user);
                    } else if (pid != null) {
                        List<CommentVo> commentVos = commentService.queryPidComment(pid);
                        comments.get(i).setCommentVos(commentVos);
                        comments.get(i).setUser(user);
                    }
                } else {
                    comments.remove(comments.get(i));
                    i--;
                }
            }
        }
        return comments;
    }

    /**
     * 正則表達式
     * @param inputString
     * @return
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_special;
        java.util.regex.Matcher m_special;
        try {
//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
// 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
// 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 获取打赏列表
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<RewardedVo> getMsgRewardedList(Integer userid, Paging<RewardedVo> pager) {
        List<RewardedVo> rewardedVos = rewardedService.findAllRewarded(userid, pager);
        if (rewardedVos != null) {
            for (int i = 0; i < rewardedVos.size(); i++) {
                Integer postid = rewardedVos.get(i).getPostid();
                List<Post> post = postZanRecordService.queryPost(postid);
                for (int j = 0; j < post.size(); j++) {
                    String str = post.get(j).getPostcontent();
                    String a = MsgCenterFacade.removeHtmlTag(str);
                    String b = a.replaceAll("  ", "");
                    if (StringUtil.isBlank(b)) {
                        String nickname = postCommentZanRecordService.queryPostNickname(postid);
                        String text = nickname + "的帖子";
                        rewardedVos.get(i).setContent(text);
                        post.get(j).setPostcontent("");
                    } else {
                        post.get(j).setPostcontent(b);
                    }
                    post.get(j).setPostcontent(b);
                }
                rewardedVos.get(i).setPosts(post);
            }
        }

        return rewardedVos;
    }


    /**
     * 赞列表
     * @param userid
     * @param pager
     * @return
     */
    public List<ZanRecordVo> findAllZan(Integer userid, Paging<ZanRecordVo> pager) {
        List<ZanRecordVo> zanRecordVos = postCommentZanRecordService.findAllZan(userid, pager);
        if (zanRecordVos != null) {
            for (int i = 0; i < zanRecordVos.size(); i++) {
                Integer commentid = zanRecordVos.get(i).getCommentid();
                Integer postid = zanRecordVos.get(i).getPostid();
                Integer usersid = zanRecordVos.get(i).getUserid();
                if (!usersid.equals(userid)) {
                    User user = postCommentZanRecordService.queryusers(usersid);
                    if (commentid != null) {
                        List<CommentVo> commentVo = postCommentZanRecordService.queryComment(commentid);
                        zanRecordVos.get(i).setComment(commentVo);
                        zanRecordVos.get(i).setCtype(1);
                        zanRecordVos.get(i).setUser(user);
                    }
                    if (postid != null) {
                        List<Post> post = postZanRecordService.queryPost(postid);
                        zanRecordVos.get(i).setCtype(2);
                        for (int j = 0; j < post.size(); j++) {
                            String str = post.get(j).getPostcontent();
                            String a = MsgCenterFacade.removeHtmlTag(str);
                            String b = a.replaceAll("  ", "");
                            if (StringUtil.isBlank(b)) {
                                String nickname = postCommentZanRecordService.queryPostNickname(postid);
                                String text = nickname + "的帖子";
                                zanRecordVos.get(i).setContent(text);
                                post.get(j).setPostcontent("");
                            } else {
                                post.get(j).setPostcontent(b);
                            }
                        }
                        zanRecordVos.get(i).setUser(user);
                        zanRecordVos.get(i).setPosts(post);
                    }
                } else {
                    zanRecordVos.remove(zanRecordVos.get(i));
                    i--;
                }
            }
        }
        return zanRecordVos;
    }

    /**
     * 赞列表
     *
     * @param userid
     * @param
     * @return
     */
    public List<ZanRecordVo> findZan(Integer userid) {
        List<ZanRecordVo> zanRecordVos = postCommentZanRecordService.findZan(userid);
        if (zanRecordVos != null) {
            for (int i = 0; i < zanRecordVos.size(); i++) {
                Integer commentid = zanRecordVos.get(i).getCommentid();
                Integer postid = zanRecordVos.get(i).getPostid();
                Integer usersid = zanRecordVos.get(i).getUserid();
                if (!usersid.equals(userid)) {
                    User user = postCommentZanRecordService.queryusers(usersid);
                    if (commentid != null) {
                        List<CommentVo> commentVo = postCommentZanRecordService.queryComment(commentid);
                        zanRecordVos.get(i).setComment(commentVo);
                        zanRecordVos.get(i).setCtype(1);
                        zanRecordVos.get(i).setUser(user);
                    }
                    if (postid != null) {
                        List<Post> post = postZanRecordService.queryPost(postid);
                        zanRecordVos.get(i).setCtype(2);
                        for (int j = 0; j < post.size(); j++) {
                            String str = post.get(j).getPostcontent();
                            String a = MsgCenterFacade.removeHtmlTag(str);
                            String b = a.replaceAll("  ", "");
                            if (StringUtil.isBlank(b)) {
                                String nickname = postCommentZanRecordService.queryPostNickname(postid);
                                String text = nickname + "的帖子";
                                zanRecordVos.get(i).setContent(text);
                                post.get(j).setPostcontent("");
                            } else {
                                post.get(j).setPostcontent(b);
                            }
                        }
                        zanRecordVos.get(i).setUser(user);
                        zanRecordVos.get(i).setPosts(post);
                    }
                } else {
                    zanRecordVos.remove(zanRecordVos.get(i));
                    i--;
                }
            }
        }
        return zanRecordVos;
    }
    public List<ImFirstDialogueVo> findAllDialogue(Integer userid, Paging<ImFirstDialogueVo> pager) {
        List<ImFirstDialogueVo> list = imFirstDialogueService.findAllDialogue(userid, pager);
        return list;
    }

    public void queryIsread(Integer userid, Integer id) {
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("id", id);
        imFirstDialogueService.queryIsread(map);
    }

    /**
     * 更新类型 1：赞 2：打赏 3：评论 4：系统 5：打招呼
     *
     * @param type
     * @return
     */
    public Integer updateisread(String type, Integer userid, String informidentity) {
        Integer resault = null;
        System.out.println("用户----------" + userid + "唯一标识-------------" + informidentity);
        if (type.equals("1") || type.equals(1)) {
            resault = postZanRecordService.updateZanRead(userid);//更新赞已读
        } else if (type.equals("2") || type.equals(2)) {
            resault = rewardedService.updateRewardRead(userid);//更新打赏已读
        } else if (type.equals("3") || type.equals(3)) {
            resault = commentService.updateCommentRead(userid);//更新评论已读
        } else if (type.equals("4") || type.equals(4)) {
            Map map = new HashMap();
            map.put("userid", userid);
            map.put("informidentity", informidentity);
            map.put("intime", new Date());
            //查询该用户是否查看过此条系统推送
            Integer check = imSystemInformReadService.queryUserCheckPush(map);
            if (check != 1) {
                map.put("isread", 1);
                resault = imSystemInformReadService.insertSystemRead(map);//新增系统消息已读记录
            } else {
                ImSystemInformRead imSd = imSystemInformReadService.queryInfromRead(map);//查询此条已读状态
                if (imSd.getIsread() == 0) {
                    map.put("isread", 1);
                    resault = imSystemInformReadService.updateSystemRead(map);//更新系统消息已读
                }
            }
        } else if (type.equals("5") || type.equals(5)) {
            resault = imFirstDialogueService.updateCallRead(userid);//更新打招呼已读
        }
        return resault;
    }

    /**
     * 系统消息全部置为已读
     *
     * @param userid
     * @return
     */
    public Integer wholeSignRead(Integer userid) {
        List<String> informidentity = imSystemInformService.queryUnreadSystemMessage(userid);
        Map map = new HashMap();
        int i;
        for (i = 0; i < informidentity.size(); i++) {
            map.put("userid", userid);
            map.put("intime", new Date());
            map.put("informidentity", informidentity.get(i));
            map.put("isread", 1);
            imSystemInformReadService.wholeSignRead(map);
        }
        return i;
    }


    /**
     * 查询用户所有未读消息
     *
     * @param userid
     * @return
     */
    public Map queryUserAllUnreadMessage(String userid) {
        Map map = new HashMap();
        int count = 0;
        int zanNumber = 0;//赞
        int commentIsRead = 0;//评论
        int imsysIsRead = 0;//系统
        int follow = 0;//关注
        map.put("count", count);
        if (userid == null) {
            //用户未登录状态下全部返回0
            return map;
        } else {
            //用户登录状态下
            //点赞未读数
            zanNumber = postZanRecordService.queryZanNumber(Integer.parseInt(userid));
            //评论未读数
            commentIsRead = commentService.queryCommentIsRead(Integer.parseInt(userid));
            //系统通知未读数
            map.put("userid", userid);
            map.put("informTime", ShiroUtil.getAppUser().getRegisterTime());
            imsysIsRead = imSystemInformService.querySystemPushByUserid(map);
            //聊天未读数
            //关注未读数
            follow = followUserService.queryUserIsRead(Integer.parseInt(userid));
            //计算总数
            count = zanNumber + commentIsRead + imsysIsRead + follow;
            map.put("count", count);
        }

        return map;
    }


    public Map isRead(int type, String userid) {
        Map map = new HashMap();
        int flag = 0;
        int zanNumber = 0;//赞
        int commentIsRead = 0;//评论
        int imsysIsRead = 0;//系统
        int follow = 0;//关注
        zanNumber = postZanRecordService.queryZanNumber(Integer.parseInt(userid));
        //评论未读数
        commentIsRead = commentService.queryCommentIsRead(Integer.parseInt(userid));
        //系统通知未读数
        map.put("userid", userid);
        map.put("informTime", ShiroUtil.getAppUser().getRegisterTime());
        imsysIsRead = imSystemInformService.querySystemPushByUserid(map);
        //聊天未读数
        //关注未读数
        follow = followUserService.queryUserIsRead(Integer.parseInt(userid));
        if (type == 1) {//动态红点
            if (zanNumber > 0 || commentIsRead > 0 || follow > 0) {
                flag = 1;
                map.put("flag", flag);
            } else {
                map.put("flag", flag);
            }
        } else if (type == 2) {//系统通知红点
            if (imsysIsRead > 0) {
                flag = 1;
                map.put("flag", flag);
            } else {
                map.put("flag", flag);
            }
        }
        return map;
    }

}

package com.movision.facade.msgCenter;

import com.movision.common.util.ShiroUtil;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecordVo;
import com.movision.mybatis.PostZanRecord.entity.ZanRecordVo;
import com.movision.mybatis.PostZanRecord.service.PostZanRecordService;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.comment.service.CommentService;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogue;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogueVo;
import com.movision.mybatis.imFirstDialogue.service.ImFirstDialogueService;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.mybatis.imSystemInform.service.ImSystemInformService;
import com.movision.mybatis.imSystemInformRead.entity.ImSystemInformRead;
import com.movision.mybatis.imSystemInformRead.service.ImSystemInformReadService;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.postCommentZanRecord.service.PostCommentZanRecordService;
import com.movision.mybatis.rewarded.entity.Rewarded;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
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
    private UserService userService;
    /**
     * 获取消息中心的列表
     *
     * @return
     */
    public Map getMsgCenterList(Integer userid) {
        //都是查找最新的消息
        Map reMap = new HashedMap();
        //1 赞消息 。包含：帖子，活动，评论，快问（后期）
        PostCommentZanRecordVo postCommentZanRecord = postCommentZanRecordService.queryByUserid(userid);
        if (postCommentZanRecord != null) {
            int use = postCommentZanRecord.getUserid();
            if (use != userid) {
                User zusew = postCommentZanRecordService.queryusers(use);
                postCommentZanRecord.setUser(zusew);
            }
        }
        //2 打赏消息
        RewardedVo rewarded = rewardedService.queryRewardByUserid(userid);
        //3 评论消息
        CommentVo comment = commentService.queryCommentByUserid(userid);
        if (comment != null) {
            int usersid = comment.getUserid();
            if (usersid != userid) {
                User ruser = postCommentZanRecordService.queryusers(usersid);
                comment.setUser(ruser);
            }
        }
        //4 系统通知
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("informTime", ShiroUtil.getAppUser().getRegisterTime());
        ImSystemInformVo imSystemInform = imSystemInformService.queryByUserid(map);//查询最新一条
        //查询是否有未读系统通知
        Integer system = imSystemInformService.querySystemPushByUserid(userid);
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
     * 获取系统通知列表
     *
     * @return
     */
    public List<ImSystemInformVo> getMsgInformationList(Integer userid, Date informTime, Paging<ImSystemInformVo> paging) {
        Map map = new HashMap();
        map.put("userid", userid);
        map.put("informTime", informTime);
        List<ImSystemInformVo> list = imSystemInformService.findAllIm(map, paging);
        return list;
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

                if (usersid != userid) {
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
                    comments.remove(i);
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
                if (usersid != userid) {
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
}

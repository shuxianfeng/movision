package com.movision.facade.msgCenter;

import com.mongodb.DBObject;
import com.movision.common.constant.MsgCenterConstant;
import com.movision.common.pojo.InstantInfo;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.paging.PageFacade;
import com.movision.fsearch.utils.StringUtil;
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
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.postCommentZanRecord.service.PostCommentZanRecordService;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.rewarded.service.RewardedService;
import com.movision.mybatis.systemInformReadRecord.entity.SystemInformReadRecord;
import com.movision.mybatis.systemInformReadRecord.service.SystemInformReadRecordService;
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
    private SystemInformReadRecordService systemInformReadRecordService;

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
     * 获取消息中心-动态消息, 动态消息全部置为已读
     *
     *
     * @param paging
     * @return
     */
    public List getInstantInfo(String userid, ServicePaging<InstantInfo> paging) {
        List<InstantInfo> list = new ArrayList<>();
        //代码层分页操作
        List<InstantInfo> resultList = null;
        if (StringUtil.isNotEmpty(userid)) {
            int curid = Integer.parseInt(userid);
            //一 评论： 1 评论帖子，
            handleCommentlist(list);
            //2 评论回复
            handleReplyCommentList(list);
            //二 赞 （评论+帖子）
            handleZanlist(list);
            //三 关注 （ 关注人）
            getFollowList(list, curid);
            //按照时间倒序排序
            Collections.sort(list, InstantInfo.intimeComparator);
            //计算Paging中的分页参数
            paging.setTotal(list.size());
            //代码层分页操作，每次取10条
            resultList = pageFacade.getPageList(list, paging.getCurPage(), paging.getPageSize());

            int size = resultList == null ? 0 : resultList.size();
            log.debug("【row中list的数量】：" + size);
//            log.debug("【row中的list】：" + resultList.toString());
            //操作已读未读处理
            updateComment(curid);    //更新评论已读
            updateZan(curid);  //更新赞
            updateAttention(curid);    //更新关注
        }
        return resultList;

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

    private void handleCommentlist(List<InstantInfo> list) {

        List<CommentVo> commentList = commentService.selectPostComment(ShiroUtil.getAppUserID());
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
        List<ImSystemInformVo> list = null;
        list = imSystemInformService.findAllIm(informTime, paging);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCoverimg() != null) {
                //代表是运营通知
                list.get(i).setIsoperation(1);
            } else {
                //代表是系统通知
                list.get(i).setIsoperation(0);
            }
        }
        return list;
    }


    /**
     * 获取系统通知列表
     *
     * @return
     */
    public List<ImSystemInformVo> getMsgInformationListNew(String userid, Paging<ImSystemInformVo> paging) {
        List<ImSystemInformVo> list = null;
        if (StringUtil.isNotEmpty(userid)) {
            //获取该用户的注册时间
            Date informTime = imSystemInformService.queryDate(Integer.parseInt(userid));
            //分页获取系统通知和运营通知
            list = imSystemInformService.findAllIm(informTime, paging);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCoverimg() != null) {
                    //代表是运营通知
                    list.get(i).setIsoperation(1);
                } else {
                    //代表是系统通知
                    list.get(i).setIsoperation(0);
                }
            }
            //获取所有的系统消息
            List<ImSystemInformVo> systemInformList = getImSystemInformVos(informTime);
            int curId = Integer.parseInt(userid);
            //把所有此人的系统通知置为已读
            setSystemInfoIsRead(systemInformList, curId);
        }
        return list;
    }

    /**
     * 获取所有的系统消息
     *
     * @param informTime
     * @return
     */
    private List<ImSystemInformVo> getImSystemInformVos(Date informTime) {
        Paging<ImSystemInformVo> allPaging = new Paging<ImSystemInformVo>(1, 1000);
        List<ImSystemInformVo> alllist = imSystemInformService.findAllIm(informTime, allPaging);
        return alllist;
    }

    /**
     * 把所有此人的系统通知置为已读
     * (实际上是把系统通知与该人的关系插入到mongo中的systemInformReadRecord)
     *
     * @param systemInformList  系统通知
     * @param curId
     */
    private void setSystemInfoIsRead(List<ImSystemInformVo> systemInformList, int curId) {
        //查询个人已读系统消息记
        List<DBObject> mongoList = systemInformReadRecordService.selectPersonSystemInfoRecord(curId);
        List<ImSystemInformVo> sameList = new ArrayList<>();
        for (int i = 0; i < systemInformList.size(); i++) {
            for (int j = 0; j < mongoList.size(); j++) {
                if (systemInformList.get(i).getInformidentity().equals(mongoList.get(j).get("informIdentity"))) {
                    sameList.add(systemInformList.get(i));
                }
            }
        }
        //过滤掉之前已读的系统通知，得到剩下未读的
        systemInformList.removeAll(sameList);
        //插入mongo, 即置为已读
        for (ImSystemInformVo vo : systemInformList) {
            SystemInformReadRecord record = new SystemInformReadRecord();
            record.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            record.setInformIdentity(vo.getInformidentity());
            record.setIntime(new Date());
            record.setUserid(ShiroUtil.getAppUserID());
            systemInformReadRecordService.insert(record);
        }
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
     * 获取被点赞的消息
     * 1 评论被点赞
     * 2 帖子被点赞
     * 注意： 自己赞自己的点赞信息需要过滤
     *
     * @param userid
     * @param
     * @return
     */
    public List<ZanRecordVo> findZan(Integer userid) {
        //获取被点赞的消息
        List<ZanRecordVo> zanRecordVos = postCommentZanRecordService.findZan(userid);

        if (zanRecordVos != null) {
            //对点赞信息进行过滤和封装
            for (int i = 0; i < zanRecordVos.size(); i++) {
                Integer commentid = zanRecordVos.get(i).getCommentid(); //被赞的评论id
                Integer postid = zanRecordVos.get(i).getPostid();       //被赞的帖子id
                Integer doZanUserid = zanRecordVos.get(i).getUserid();  //点赞人id
                //点赞人不能是自己（过滤掉了自己赞自己）
//                if (!doZanUserid.equals(userid)) {
                //获取点赞人的个人信息
                User user = postCommentZanRecordService.queryusers(doZanUserid);
                zanRecordVos.get(i).setUser(user);
                //封装被点赞的评论对象
                wrapBeZanCommentInfo(zanRecordVos, i, commentid);
                //封装被点赞的帖子对象
                wrapBeZanPostInfo(zanRecordVos, i, postid);
//                } else {
//                    //（过滤掉了自己赞自己）
//                    zanRecordVos.remove(zanRecordVos.get(i));
//                    i--;
//                }
            }
        }
        return zanRecordVos;
    }

    /**
     * 封装被点赞的评论对象
     *
     * @param zanRecordVos
     * @param i
     * @param commentid
     */
    private void wrapBeZanCommentInfo(List<ZanRecordVo> zanRecordVos, int i, Integer commentid) {
        if (commentid != null) {
            List<CommentVo> commentVo = postCommentZanRecordService.queryComment(commentid);
            zanRecordVos.get(i).setComment(commentVo);
            zanRecordVos.get(i).setCtype(1);
            zanRecordVos.get(i).setCommentid(commentid);
        }
    }

    /**
     * 封装被点赞的帖子对象
     *
     * @param zanRecordVos
     * @param i
     * @param postid
     */
    private void wrapBeZanPostInfo(List<ZanRecordVo> zanRecordVos, int i, Integer postid) {
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

            zanRecordVos.get(i).setPosts(post);
            zanRecordVos.get(i).setPostid(postid);
        }
    }

    /**
     * 查询用户所有未读消息
     *
     * @param userid
     * @return
     */
    public Map queryUserAllUnreadMessage(String userid) {
        Map map = new HashMap();
        int totalCount = 0;
        int zanNumber;//赞
        int commentIsReadCount;//评论
        int sysInfoNotReadCount = 0;//系统
        int follow;//关注
        map.put("count", totalCount);
        //未登录校验
        if (ShiroUtil.getAppUserID() == 0) {
            return map;
        }
        if (userid == null) {
            //用户未登录状态下全部返回0
            return map;
        } else {
            //用户登录状态下
            //点赞未读数
            zanNumber = postZanRecordService.queryZanNumber(Integer.parseInt(userid));
            //评论未读数
            commentIsReadCount = commentService.queryCommentIsRead(Integer.parseInt(userid));
            //系统通知未读数
            sysInfoNotReadCount = getImsysIsReadCount(userid, sysInfoNotReadCount);
            //聊天未读数
            // TODO: 2017/9/5   待集成IM聊天
            //关注未读数
            follow = followUserService.queryUserIsRead(Integer.parseInt(userid));
            //计算总数
            totalCount = zanNumber + commentIsReadCount + sysInfoNotReadCount + follow;
            log.debug("zanNumber=" + zanNumber);
            log.debug("commentIsReadCount=" + commentIsReadCount);
            log.debug("imsysIsReadCount=" + sysInfoNotReadCount);
            log.debug("follow=" + follow);
            log.debug("totalCount=" + totalCount);
            map.put("count", totalCount);
        }

        return map;
    }

    /**
     * 统计该用户系统消息未读的数量
     *
     * @param userid
     * @param imsysIsReadCount
     * @return
     */
    private int getImsysIsReadCount(String userid, int imsysIsReadCount) {
        Paging<ImSystemInformVo> paging = new Paging<ImSystemInformVo>(1, 1000);    //所以取第一页的1000条，目的是获取所有的系统通知
        if (StringUtil.isNotEmpty(userid)) {
            //获取该用户的注册时间
            Date informTime = imSystemInformService.queryDate(Integer.parseInt(userid));
            //获取系统通知和运营通知
            List<ImSystemInformVo> list = imSystemInformService.findAllIm(informTime, paging);
            int curId = Integer.parseInt(userid);
            //从mongoDB中查询个人已读系统消息记
            List<DBObject> mongoList = systemInformReadRecordService.selectPersonSystemInfoRecord(curId);
            log.debug("之前已读的系统消息数量：" + mongoList.size());
            List<ImSystemInformVo> sameList = new ArrayList<>();
            //统计之前已读的系统消息
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < mongoList.size(); j++) {
                    if (list.get(i).getInformidentity().equals(mongoList.get(j).get("informIdentity"))) {
                        sameList.add(list.get(i));
                    }
                }
            }
            log.debug("相同的系统消息数量：" + sameList.size());
            //过滤掉之前已读的系统通知，得到剩下未读的
            list.removeAll(sameList);
            log.debug("该用户未读的系统消息（系统通知+运营通知）=" + list.toString());
            imsysIsReadCount = list.size();
            log.debug("该用户未读的系统消息数量（系统通知+运营通知）=" + imsysIsReadCount);
        }
        return imsysIsReadCount;
    }


    /**
     * 判断消息中心-动态和系统消息 是否显示红点
     *
     * @param type
     * @param userid
     * @return
     */
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
        imsysIsRead = getImsysIsReadCount(userid, imsysIsRead);
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

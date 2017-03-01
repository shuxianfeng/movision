package com.movision.mybatis.pointRecord.entity;

import java.io.Serializable;

/**
 * 新手任务
 *
 * @Author zhuangyuhao
 * @Date 2017/3/1 11:36
 */
public class NewTask implements Serializable {
    private boolean register;   //首次注册
    private boolean finishPersonalData; //晚上个人资料
    private boolean bindPhone;  //绑定手机号
    private boolean firstFocus; //首次关注
    private boolean firstCollect;   //首次收藏
    private boolean firstShare; //首次分享
    private boolean firstComment;   //首次评论
    private boolean firstSupport;   //首次点赞
    private boolean firstPost;  //首次发帖
    private boolean commentApp; //评价APP

    public NewTask() {
    }

    public NewTask(boolean register, boolean finishPersonalData, boolean bindPhone, boolean firstFocus, boolean firstCollect, boolean firstShare, boolean firstComment, boolean firstSupport, boolean firstPost, boolean commentApp) {
        this.register = register;
        this.finishPersonalData = finishPersonalData;
        this.bindPhone = bindPhone;
        this.firstFocus = firstFocus;
        this.firstCollect = firstCollect;
        this.firstShare = firstShare;
        this.firstComment = firstComment;
        this.firstSupport = firstSupport;
        this.firstPost = firstPost;
        this.commentApp = commentApp;
    }

    @Override
    public String toString() {
        return "NewTask{" +
                "register=" + register +
                ", finishPersonalData=" + finishPersonalData +
                ", bindPhone=" + bindPhone +
                ", firstFocus=" + firstFocus +
                ", firstCollect=" + firstCollect +
                ", firstShare=" + firstShare +
                ", firstComment=" + firstComment +
                ", firstSupport=" + firstSupport +
                ", firstPost=" + firstPost +
                ", commentApp=" + commentApp +
                '}';
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public void setFinishPersonalData(boolean finishPersonalData) {
        this.finishPersonalData = finishPersonalData;
    }

    public void setBindPhone(boolean bindPhone) {
        this.bindPhone = bindPhone;
    }

    public void setFirstFocus(boolean firstFocus) {
        this.firstFocus = firstFocus;
    }

    public void setFirstCollect(boolean firstCollect) {
        this.firstCollect = firstCollect;
    }

    public void setFirstShare(boolean firstShare) {
        this.firstShare = firstShare;
    }

    public void setFirstComment(boolean firstComment) {
        this.firstComment = firstComment;
    }

    public void setFirstSupport(boolean firstSupport) {
        this.firstSupport = firstSupport;
    }

    public void setFirstPost(boolean firstPost) {
        this.firstPost = firstPost;
    }

    public void setCommentApp(boolean commentApp) {
        this.commentApp = commentApp;
    }

    public boolean isRegister() {

        return register;
    }

    public boolean isFinishPersonalData() {
        return finishPersonalData;
    }

    public boolean isBindPhone() {
        return bindPhone;
    }

    public boolean isFirstFocus() {
        return firstFocus;
    }

    public boolean isFirstCollect() {
        return firstCollect;
    }

    public boolean isFirstShare() {
        return firstShare;
    }

    public boolean isFirstComment() {
        return firstComment;
    }

    public boolean isFirstSupport() {
        return firstSupport;
    }

    public boolean isFirstPost() {
        return firstPost;
    }

    public boolean isCommentApp() {
        return commentApp;
    }
}

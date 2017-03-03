package com.movision.mybatis.post.entity;

import java.util.Date;

/**
 * 用于帖子列表
 *
 * @Author zhurui
 * @Date 2017/2/7 9:53
 */
public class PostList {

    private Integer id;//帖子id

    private String pai;

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    private String title;//标题

    private String nickname;//发帖人

    private Integer collectsum;//收藏数

    private Integer share;//分享数

    private String mintime;

    private String maxtime;

    private Integer commentsum;//评论数

    private Integer zansum;//点赞数

    private Integer rewarded;//打赏积分

    private Integer accusation;//举报次数

    private Integer isessence;//是否为精选

    private Date istime;//精选时间

    private Integer sum;//帖子总数

    private String circlename;//所属圈子name

    private Integer circleid;

    private String subtitle;

    private String postcontent;

    private Integer forwardsum;

    private Integer isactive;

    private Integer activetype;

    private Double activefee;

    private Integer iscontribute;//是否需要投稿：0 不投稿 1 投稿

    private Integer type;

    private Integer ishot;

    private Integer isessencepool;

    private Integer orderid;//排序id

    private String coverimg;

    private String hotimgurl;

    private Date intime;//帖子添加时间

    private Integer totalpoint;//帖子综合评分

    private Integer isdel;

    private Date essencedate;//精选日期

    private Integer category;//圈子分类

    private Integer persum;//报名人数


    public String getMintime() {
        return mintime;
    }

    public void setMintime(String mintime) {
        this.mintime = mintime;
    }

    public String getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(String maxtime) {
        this.maxtime = maxtime;
    }

    public Integer getPersum() {
        return persum;
    }

    public void setPersum(Integer persum) {
        this.persum = persum;
    }

    private Integer userid;//用户id

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getActivestatue() {
        return activestatue;
    }

    public void setActivestatue(String activestatue) {
        this.activestatue = activestatue;
    }

    public String activestatue;
    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCollectsum() {
        return collectsum;
    }

    public void setCollectsum(Integer collectsum) {
        this.collectsum = collectsum;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getCommentsum() {
        return commentsum;
    }

    public void setCommentsum(Integer commentsum) {
        this.commentsum = commentsum;
    }

    public Integer getZansum() {
        return zansum;
    }

    public void setZansum(Integer zansum) {
        this.zansum = zansum;
    }

    public Integer getRewarded() {
        return rewarded;
    }

    public void setRewarded(Integer rewarded) {
        this.rewarded = rewarded;
    }

    public Integer getAccusation() {
        return accusation;
    }

    public void setAccusation(Integer accusation) {
        this.accusation = accusation;
    }

    public Integer getIsessence() {
        return isessence;
    }

    public void setIsessence(Integer isessence) {
        this.isessence = isessence;
    }

    public Date getIstime() {
        return istime;
    }

    public void setIstime(Date istime) {
        this.istime = istime;
    }

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public Integer getForwardsum() {
        return forwardsum;
    }

    public void setForwardsum(Integer forwardsum) {
        this.forwardsum = forwardsum;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Integer getActivetype() {
        return activetype;
    }

    public void setActivetype(Integer activetype) {
        this.activetype = activetype;
    }

    public Double getActivefee() {
        return activefee;
    }

    public void setActivefee(Double activefee) {
        this.activefee = activefee;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIshot() {
        return ishot;
    }

    public void setIshot(Integer ishot) {
        this.ishot = ishot;
    }

    public Integer getIsessencepool() {
        return isessencepool;
    }

    public void setIsessencepool(Integer isessencepool) {
        this.isessencepool = isessencepool;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getHotimgurl() {
        return hotimgurl;
    }

    public void setHotimgurl(String hotimgurl) {
        this.hotimgurl = hotimgurl;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getTotalpoint() {
        return totalpoint;
    }

    public void setTotalpoint(Integer totalpoint) {
        this.totalpoint = totalpoint;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getEssencedate() {
        return essencedate;
    }

    public void setEssencedate(Date essencedate) {
        this.essencedate = essencedate;
    }

    public Integer getIscontribute() {
        return iscontribute;
    }

    public void setIscontribute(Integer iscontribute) {
        this.iscontribute = iscontribute;
    }
}

package com.movision.mybatis.post.entity;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.user.entity.UserLike;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/17 20:01
 */
public class PostVo implements Serializable {

    private static final long serialVersionUID = 8888633313164601558L;

    private Integer id;

    private Integer circleid;

    private Integer countview;  //帖子浏览量

    private String title;

    private String subtitle;

    private String postcontent;

    private Integer zansum;

    private Integer commentsum;

    private Integer forwardsum;

    private Integer collectsum;

    private Integer isactive;

    private Integer partsumEnddays;//当前活动显示参与人数还是显示剩余结束天数 0 显示结束天数 1 显示活动参与人数

    private Integer activetype;

    private Double activefee;

    private Integer iscontribute;//是否需要投稿：0 不投稿 1 投稿

    private Integer type;

    private Integer ishot;

    private Integer isessence;

    private Integer isessencepool;

    private Integer orderid;

    private String coverimg;

    private String hotimgurl;

    private Date intime;

    private Integer totalpoint;

    private Integer isdel;

    private Date essencedate;//精选日期

    private Integer category;

    private Integer code;

    private String videourl;//存放原生视频链接

    private String videocoverimgurl;//原生视频封面图片（类型为原生视频时有返回值）

    private String isfollow;//该帖子所属的圈子是否被关注 0 可关注  1 已关注（用于判断帖子详情中的关注按钮是否可用）

    private List<Circle> hotcirclelist;//用于存放帖子详情最下方的4个热门推荐圈子

    private List<GoodsVo> shareGoodsList;//用户分享的商品列表

    private Date begintime;//活动开始时间（为活动时使用）

    private Date endtime;//活动结束时间（为活动时使用）

    private Integer enddays;//距离活动结束剩余天数（查询活动时该字段不为空）

    private Integer partsum;//已参与活动总人数

    private String circlename;//所属圈子名称

    private String maylikeimg;//圈子方形图url

    private Integer userid;//用户id(发帖人用户id)

    private String nickname;//发帖人昵称

    private String phone;//发帖人手机号

    private String photo;   //发帖人的头像

    private Integer isCollect;//该用户是否已收藏该帖子/活动 0 否 1 是

    private Integer isZan;//该用户是否已赞该帖子/活动 0 否 1 是

    private String introduction;//圈子简介

    private Integer rewardsum;//打赏热人数

    private List<UserLike> rewardpersonnickname;//打赏的10个人的昵称

    private String city;//城市code

    public UserLike userlike;

    private Integer heatvalue;//热度

    public CommentVo comments;

    public List<PostLabel> postLabels;

    private Integer ishotorder;//活动设为热门排序字段

    private Integer activeid;//活动id

    private String activename;  //活动名称

    private List<CommentVo> commentVos;
    public List<CommentVo> getCommentVos() {
        return commentVos;
    }

    public void setActivename(String activename) {
        this.activename = activename;
    }

    public String getActivename() {

        return activename;
    }

    public void setCommentVos(List<CommentVo> commentVos) {
        this.commentVos = commentVos;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {

        return photo;
    }

    public Integer getIshotorder() {
        return ishotorder;
    }

    public void setIshotorder(Integer ishotorder) {
        this.ishotorder = ishotorder;
    }

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRewardsum() {
        return rewardsum;
    }

    public void setRewardsum(Integer rewardsum) {
        this.rewardsum = rewardsum;
    }

    public List<UserLike> getRewardpersonnickname() {
        return rewardpersonnickname;
    }

    public void setRewardpersonnickname(List<UserLike> rewardpersonnickname) {
        this.rewardpersonnickname = rewardpersonnickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent == null ? null : postcontent.trim();
    }

    public Integer getZansum() {
        return zansum;
    }

    public void setZansum(Integer zansum) {
        this.zansum = zansum;
    }

    public Integer getCommentsum() {
        return commentsum;
    }

    public void setCommentsum(Integer commentsum) {
        this.commentsum = commentsum;
    }

    public Integer getForwardsum() {
        return forwardsum;
    }

    public void setForwardsum(Integer forwardsum) {
        this.forwardsum = forwardsum;
    }

    public Integer getCollectsum() {
        return collectsum;
    }

    public void setCollectsum(Integer collectsum) {
        this.collectsum = collectsum;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
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

    public Integer getIsessence() {
        return isessence;
    }

    public void setIsessence(Integer isessence) {
        this.isessence = isessence;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg == null ? null : coverimg.trim();
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

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHotimgurl() {
        return hotimgurl;
    }

    public void setHotimgurl(String hotimgurl) {
        this.hotimgurl = hotimgurl;
    }

    public Integer getIsessencepool() {
        return isessencepool;
    }

    public void setIsessencepool(Integer isessencepool) {
        this.isessencepool = isessencepool;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public List<Circle> getHotcirclelist() {
        return hotcirclelist;
    }

    public void setHotcirclelist(List<Circle> hotcirclelist) {
        this.hotcirclelist = hotcirclelist;
    }

    public Integer getEnddays() {
        return enddays;
    }

    public void setEnddays(Integer enddays) {
        this.enddays = enddays;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getActivetype() {
        return activetype;
    }

    public void setActivetype(Integer activetype) {
        this.activetype = activetype;
    }

    public Integer getPartsum() {
        return partsum;
    }

    public void setPartsum(Integer partsum) {
        this.partsum = partsum;
    }

    public Double getActivefee() {
        return activefee;
    }

    public void setActivefee(Double activefee) {
        this.activefee = activefee;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    private String activestatue;

    public String getActivestatue() {
        return activestatue;
    }

    public void setActivestatue(String activestatue) {
        this.activestatue = activestatue;
    }

    public Date getEssencedate() {
        return essencedate;
    }

    public void setEssencedate(Date essencedate) {
        this.essencedate = essencedate;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }

    public Integer getIscontribute() {
        return iscontribute;
    }

    public void setIscontribute(Integer iscontribute) {
        this.iscontribute = iscontribute;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<GoodsVo> getShareGoodsList() {
        return shareGoodsList;
    }

    public void setShareGoodsList(List<GoodsVo> shareGoodsList) {
        this.shareGoodsList = shareGoodsList;
    }

    public String getVideocoverimgurl() {
        return videocoverimgurl;
    }

    public void setVideocoverimgurl(String videocoverimgurl) {
        this.videocoverimgurl = videocoverimgurl;
    }

    public String getMaylikeimg() {
        return maylikeimg;
    }

    public void setMaylikeimg(String maylikeimg) {
        this.maylikeimg = maylikeimg;
    }

    public UserLike getUserlike() {
        return userlike;
    }

    public void setUserlike(UserLike userlike) {
        this.userlike = userlike;
    }

    public Integer getHeatvalue() {
        return heatvalue;
    }

    public void setHeatvalue(Integer heatvalue) {
        this.heatvalue = heatvalue;
    }

    public List<PostLabel> getPostLabels() {
        return postLabels;
    }

    public void setPostLabels(List<PostLabel> postLabels) {
        this.postLabels = postLabels;
    }

    public CommentVo getComments() {
        return comments;
    }

    public void setComments(CommentVo comments) {
        this.comments = comments;
    }

    public Integer getCountview() {
        return countview;
    }

    public void setCountview(Integer countview) {
        this.countview = countview;
    }

    public Integer getPartsumEnddays() {
        return partsumEnddays;
    }

    public void setPartsumEnddays(Integer partsumEnddays) {
        this.partsumEnddays = partsumEnddays;
    }

    /**
     * 重写equals方法，用于比对
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PostVo && this.id != null && this.id.equals(((PostVo) obj).getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }


}

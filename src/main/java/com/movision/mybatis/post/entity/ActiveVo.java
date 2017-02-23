package com.movision.mybatis.post.entity;

import com.movision.mybatis.goods.entity.GoodsVo;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/3 17:24
 */
public class ActiveVo {
    private Integer id;

    private Integer circleid;

    private String title;

    private String subtitle;

    private String postcontent;

    private Integer zansum;

    private Integer commentsum;

    private Integer forwardsum;

    private Integer collectsum;

    private Integer isactive;

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

    private Integer userid;//发帖人

    private Date begintime;//活动开始时间（为活动时使用）

    private Date endtime;//活动结束时间（为活动时使用）

    private Integer enddays;//距离活动结束剩余天数（查询活动时该字段不为空）

    private List<GoodsVo> promotionGoodsList;//活动促销商品列表

    private Integer partsum;//活动参与总人数

    private Integer isCollect;//该用户是否已收藏该帖子/活动 0 否 1 是

    private Integer isZan;//该用户是否已赞该帖子/活动 0 否 1 是

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
        this.title = title;
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

    public Integer getActivetype() {
        return activetype;
    }

    public void setActivetype(Integer activetype) {
        this.activetype = activetype;
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

    public Integer getEnddays() {
        return enddays;
    }

    public void setEnddays(Integer enddays) {
        this.enddays = enddays;
    }

    public List<GoodsVo> getPromotionGoodsList() {
        return promotionGoodsList;
    }

    public void setPromotionGoodsList(List<GoodsVo> promotionGoodsList) {
        this.promotionGoodsList = promotionGoodsList;
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

    public Date getEssencedate() {
        return essencedate;
    }

    public void setEssencedate(Date essencedate) {
        this.essencedate = essencedate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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
}

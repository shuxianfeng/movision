package com.movision.mybatis.activeH5.entity;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/8/30 15:19
 */
public class ActiveH5Vo {
    private Integer id;

    private Integer votesum;//投稿数量

    private Integer pageview;//访问量

    private Integer takesum;//报名数量

    private String photo;

    private Date begintime;

    private Date endime;

    private Date intime;

    private String name;

    private Integer isApply;//是否投稿 0否 1是

    private String awardsSetting;//奖项设置

    private String awardsRules;//评奖规则

    public String getAwardsSetting() {
        return awardsSetting;
    }

    public void setAwardsSetting(String awardsSetting) {
        this.awardsSetting = awardsSetting;
    }

    public String getAwardsRules() {
        return awardsRules;
    }

    public void setAwardsRules(String awardsRules) {
        this.awardsRules = awardsRules;
    }

    public Integer getIsApply() {
        return isApply;
    }

    public void setIsApply(Integer isApply) {
        this.isApply = isApply;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndime() {
        return endime;
    }

    public void setEndime(Date endyime) {
        this.endime = endyime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVotesum() {
        return votesum;
    }

    public void setVotesum(Integer votesum) {
        this.votesum = votesum;
    }

    public Integer getPageview() {
        return pageview;
    }

    public void setPageview(Integer pageview) {
        this.pageview = pageview;
    }

    public Integer getTakesum() {
        return takesum;
    }

    public void setTakesum(Integer takesum) {
        this.takesum = takesum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

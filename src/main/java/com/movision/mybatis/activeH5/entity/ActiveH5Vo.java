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

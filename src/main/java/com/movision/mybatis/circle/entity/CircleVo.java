package com.movision.mybatis.circle.entity;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/19 11:41
 */
public class CircleVo {
    private Integer id;

    private Integer isofficial;

    private Integer scope;

    private String phone;

    private String photo;

    private Integer category;//圈子分类

    private String categoryname;//圈主名称

    private String categorylevel;//判断V

    private Integer code;

    private String name;

    private String introduction;

    private String notice;

    private Integer permission;

    private Integer isrecommend;//推荐

    private String maylikeimg;

    private Date createtime;

    private Integer status;

    private Integer supportnum;//支持数

    private Integer isdiscover;//首页

    private Integer orderid;

    private List<Post> hotPostList;

    private Integer postnum;//圈子帖子数

    private Integer postnewnum;//圈子今日更新帖子数

    private Integer isfollow;//该圈子是否被关注 0 可关注  1 已关注

    private Integer follownum;//圈子关注数

    private Integer follownewnum;//圈子今日关注数

    private User circlemaster;//圈主

    private List<User> circlemanagerlist;//圈子管理员列表

    private Integer isessencenum;//圈子中精贴数

    private Integer isdel;

    private Integer issupport;//是否可支持 0 可支持 1 已支持

    private Date intime;//时间

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getPostnewnum() {
        return postnewnum;
    }

    public void setPostnewnum(Integer postnewnum) {
        this.postnewnum = postnewnum;
    }

    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    public Integer getFollownewnum() {
        return follownewnum;
    }

    public void setFollownewnum(Integer follownewnum) {
        this.follownewnum = follownewnum;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getIsessencenum() {
        return isessencenum;
    }

    public void setIsessencenum(Integer isessencenum) {
        this.isessencenum = isessencenum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsofficial() {
        return isofficial;
    }

    public void setIsofficial(Integer isofficial) {
        this.isofficial = isofficial;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getMaylikeimg() {
        return maylikeimg;
    }

    public void setMaylikeimg(String maylikeimg) {
        this.maylikeimg = maylikeimg;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getSupportnum() {
        return supportnum;
    }

    public void setSupportnum(Integer supportnum) {
        this.supportnum = supportnum;
    }

    public Integer getIsdiscover() {
        return isdiscover;
    }

    public void setIsdiscover(Integer isdiscover) {
        this.isdiscover = isdiscover;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public List<Post> getHotPostList() {
        return hotPostList;
    }

    public void setHotPostList(List<Post> hotPostList) {
        this.hotPostList = hotPostList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPostnum() {
        return postnum;
    }

    public void setPostnum(Integer postnum) {
        this.postnum = postnum;
    }

    public Integer getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(Integer isfollow) {
        this.isfollow = isfollow;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorylevel() {
        return categorylevel;
    }

    public void setCategorylevel(String categorylevel) {
        this.categorylevel = categorylevel;
    }

    public User getCirclemaster() {
        return circlemaster;
    }

    public void setCirclemaster(User circlemaster) {
        this.circlemaster = circlemaster;
    }

    public List<User> getCirclemanagerlist() {
        return circlemanagerlist;
    }

    public void setCirclemanagerlist(List<User> circlemanagerlist) {
        this.circlemanagerlist = circlemanagerlist;
    }

    public Integer getIssupport() {
        return issupport;
    }

    public void setIssupport(Integer issupport) {
        this.issupport = issupport;
    }
}

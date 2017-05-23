package com.movision.mybatis.user.entity;

import com.movision.utils.pagination.model.Paging;

/**
 * @Author zhurui
 * @Date 2017/5/23 15:37
 */
public class UserRole {

    private Integer userid;
    private Integer iscircle;//是否是圈主
    private Integer circlemanagement;//圈子管理员
    private Integer contributing;//特邀嘉宾
    private Integer common;//普通管理员
    private Integer vip;//用户是否大V
    private Integer categoryid;//圈子类型id
    private Integer type;//

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getIscircle() {
        return iscircle;
    }

    public void setIscircle(Integer iscircle) {
        this.iscircle = iscircle;
    }

    public Integer getCirclemanagement() {
        return circlemanagement;
    }

    public void setCirclemanagement(Integer circlemanagement) {
        this.circlemanagement = circlemanagement;
    }

    public Integer getContributing() {
        return contributing;
    }

    public void setContributing(Integer contributing) {
        this.contributing = contributing;
    }

    public Integer getCommon() {
        return common;
    }

    public void setCommon(Integer common) {
        this.common = common;
    }
}

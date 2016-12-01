package com.zhuhuibao.mybatis.memberReg.entity;

public class LoginMember {
    private Long id;
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 会员状态 0：未激活(只针对邮件)，1：注册成功，2：注销，3：完善资料，4：待认证会员，5：已认证会员
     */
    private int status;
    private String identify;
    private String role;
    private String isexpert;
    private int ordercount;
    private int msgcount;
    private Long companyId;
    private int vipLevel;

    private int parentVipLevel;

    private String registerTime;
    private int workType;
    private String headShot;
    private String nickname;
    private String companyName;

    /**
     * 联系人
     */
    private String enterpriseLinkman;
    /**
     * 固定电话
     */
    private String fixedTelephone;
    /**
     * 邮箱
     */
    private String email;

    public String getEnterpriseLinkman() {
        return enterpriseLinkman;
    }

    public void setEnterpriseLinkman(String enterpriseLinkman) {
        this.enterpriseLinkman = enterpriseLinkman;
    }

    public String getFixedTelephone() {
        return fixedTelephone;
    }

    public void setFixedTelephone(String fixedTelephone) {
        this.fixedTelephone = fixedTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public String getHeadShot() {
        return headShot;
    }

    public void setHeadShot(String headShot) {
        this.headShot = headShot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsexpert() {
        return isexpert;
    }

    public void setIsexpert(String isexpert) {
        this.isexpert = isexpert;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public int getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getParentVipLevel() {
        return parentVipLevel;
    }

    public void setParentVipLevel(int parentVipLevel) {
        this.parentVipLevel = parentVipLevel;
    }

}
package com.zhuhuibao.mybatis.common.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "调研")
public class SysResearch {
    @ApiModelProperty("id")
    private Integer id;

     @ApiModelProperty("是否有过B2B网上交易的经验")
    private String webTradeExp;

    @ApiModelProperty("B2B网上交易经验年限")
    private String webTradeYear;

    @ApiModelProperty("3个月网上交易次数")
    private String tradeCount;

    @ApiModelProperty("网上交易平均金额")
    private String tradeMoney;
    @ApiModelProperty("上网设备（多个逗号隔开） ")

    private String webDevices;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private String age;

    @ApiModelProperty("学历")
    private String education;

    @ApiModelProperty("收入")
    private String income;

    @ApiModelProperty("所在城市")
    private String city;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("QQ号码")
    private String qqNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebTradeExp() {
        return webTradeExp;
    }

    public void setWebTradeExp(String webTradeExp) {
        this.webTradeExp = webTradeExp == null ? null : webTradeExp.trim();
    }

    public String getWebTradeYear() {
        return webTradeYear;
    }

    public void setWebTradeYear(String webTradeYear) {
        this.webTradeYear = webTradeYear == null ? null : webTradeYear.trim();
    }

    public String getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(String tradeCount) {
        this.tradeCount = tradeCount == null ? null : tradeCount.trim();
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney == null ? null : tradeMoney.trim();
    }

    public String getWebDevices() {
        return webDevices;
    }

    public void setWebDevices(String webDevices) {
        this.webDevices = webDevices == null ? null : webDevices.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income == null ? null : income.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum == null ? null : qqNum.trim();
    }
}
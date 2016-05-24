package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class MeetingOrder {
    private String id;

    private String createid;

    private String account;

    @ApiModelProperty(value="省")
    private String province;

    @ApiModelProperty(value="市")
    private String city;

    private String publishTime;

    private String updateTime;

    private String updateManId;

    @ApiModelProperty(value="开始时间")
    private String startDate;

    @ApiModelProperty(value="截止时间")
    private String endDate;

    @ApiModelProperty(value="时长")
    private String day;

    @ApiModelProperty(value="场馆要求")
    private String venue;

    @ApiModelProperty(value="参会人数")
    private String meetingNum;

    @ApiModelProperty(value="会议类型")
    private String meetingType;

    @ApiModelProperty(value="场地布局")
    private String field;

    @ApiModelProperty(value="住宿：1：需要；0：不需要")
    private String hotel;

    @ApiModelProperty(value="房间数")
    private String roomSize;

    @ApiModelProperty(value="房价预算")
    private String roomPrice;

    @ApiModelProperty(value="用餐：1：需要；0：不需要")
    private String food;

    @ApiModelProperty(value="用餐人数")
    private String mealsNum;

    @ApiModelProperty(value="用餐类型")
    private String mealsType;

    @ApiModelProperty(value="用餐预算")
    private String mealsPrice;

    @ApiModelProperty(value="预算单位")
    private String priceType;

    @ApiModelProperty(value="礼品：1：需要；0：不需要")
    private String present;

    @ApiModelProperty(value="礼品份数")
    private String presentNum;

    @ApiModelProperty(value="礼品预算")
    private String presentPrice;

    @ApiModelProperty(value="周边游：1：需要；0：不需要")
    private String play;

    @ApiModelProperty(value="游玩人数")
    private String playerNum;

    @ApiModelProperty(value="游玩预算")
    private String playPrice;

    @ApiModelProperty(value="其他要求")
    private String otherRequriement;

    @ApiModelProperty(value="联系人名称")
    private String linkName;

    @ApiModelProperty(value="1:先生 0：女士")
    private String call;

    @ApiModelProperty(value="手机")
    private String mobile;

    @ApiModelProperty(value="座机")
    private String telephone;

    @ApiModelProperty(value="公司名称")
    private String company;

    @ApiModelProperty(value="邮件")
    private String email;

    @ApiModelProperty(value="审核状态：0：未处理；1：已处理")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMeetingNum() {
        return meetingNum;
    }

    public void setMeetingNum(String meetingNum) {
        this.meetingNum = meetingNum;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getMealsNum() {
        return mealsNum;
    }

    public void setMealsNum(String mealsNum) {
        this.mealsNum = mealsNum;
    }

    public String getMealsType() {
        return mealsType;
    }

    public void setMealsType(String mealsType) {
        this.mealsType = mealsType;
    }

    public String getMealsPrice() {
        return mealsPrice;
    }

    public void setMealsPrice(String mealsPrice) {
        this.mealsPrice = mealsPrice;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getPresentNum() {
        return presentNum;
    }

    public void setPresentNum(String presentNum) {
        this.presentNum = presentNum;
    }

    public String getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(String presentPrice) {
        this.presentPrice = presentPrice;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(String playerNum) {
        this.playerNum = playerNum;
    }

    public String getPlayPrice() {
        return playPrice;
    }

    public void setPlayPrice(String playPrice) {
        this.playPrice = playPrice;
    }

    public String getOtherRequriement() {
        return otherRequriement;
    }

    public void setOtherRequriement(String otherRequriement) {
        this.otherRequriement = otherRequriement;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateManId() {
        return updateManId;
    }

    public void setUpdateManId(String updateManId) {
        this.updateManId = updateManId;
    }
}
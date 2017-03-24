package com.movision.mybatis.afterservice.entity;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/3/3 18:07
 */
public class AfterServiceVo {
    private Integer id;

    private Integer orderid;

    private Integer addressid;

    private Integer goodsid;

    private Integer afterstatue;

    private Integer aftersalestatus;

    private Integer processingstatus;

    private Integer isdel;

    private Double refundamount;

    private String processingpeople;

    private Date processingtime;

    private Integer userid;
    private Double origprice;

    public Double getOrigprice() {
        return origprice;
    }

    public void setOrigprice(Double origprice) {
        this.origprice = origprice;
    }

    private Date proposertime;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Double amountdue;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    private String imgurl;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private Date intime;//发货时间
    private String logisticsid;//发货单号
    private String nickname;//昵称
    private String ordernumber;//订单号

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    private String name;//收货人
    private String province;
    private String city;
    private String district;
    private String street;
    private Integer takeway;//收货方式
    private Integer protype;//商品类型
    private String sname;//商品名称
    private String mintime;
    private String maxtime;
    private String typename;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

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

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLogisticsid() {
        return logisticsid;
    }

    public void setLogisticsid(String logisticsid) {
        this.logisticsid = logisticsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getTakeway() {
        return takeway;
    }

    public void setTakeway(Integer takeway) {
        this.takeway = takeway;
    }

    public Integer getProtype() {
        return protype;
    }

    public void setProtype(Integer protype) {
        this.protype = protype;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getAfterstatue() {
        return afterstatue;
    }

    public void setAfterstatue(Integer afterstatue) {
        this.afterstatue = afterstatue;
    }

    public Integer getAftersalestatus() {
        return aftersalestatus;
    }

    public void setAftersalestatus(Integer aftersalestatus) {
        this.aftersalestatus = aftersalestatus;
    }

    public Integer getProcessingstatus() {
        return processingstatus;
    }

    public void setProcessingstatus(Integer processingstatus) {
        this.processingstatus = processingstatus;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Double getRefundamount() {
        return refundamount;
    }

    public void setRefundamount(Double refundamount) {
        this.refundamount = refundamount;
    }

    public String getProcessingpeople() {
        return processingpeople;
    }

    public void setProcessingpeople(String processingpeople) {
        this.processingpeople = processingpeople == null ? null : processingpeople.trim();
    }

    public Date getProcessingtime() {
        return processingtime;
    }

    public void setProcessingtime(Date processingtime) {
        this.processingtime = processingtime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getProposertime() {
        return proposertime;
    }

    public void setProposertime(Date proposertime) {
        this.proposertime = proposertime;
    }

    public Double getAmountdue() {
        return amountdue;
    }

    public void setAmountdue(Double amountdue) {
        this.amountdue = amountdue;
    }
}

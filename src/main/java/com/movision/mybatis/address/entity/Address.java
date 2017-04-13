package com.movision.mybatis.address.entity;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Api("地址")
public class Address implements Serializable {
    private Integer id;
    private Integer userid;
    @ApiModelProperty(value = "收货人")
    private String name;
    @ApiModelProperty(value = "收货人手机号")
    private String phone;
    @ApiModelProperty(value = "省代码")
    private String province;
    @ApiModelProperty(value = "市代码")
    private String city;
    @ApiModelProperty(value = "区代码")
    private String district;
    @ApiModelProperty(value = "街道详细地址")
    private String street;
    @ApiModelProperty(value = "是否为默认收货地址：1是 0否")
    private Integer isdefault;//是否为默认地址：0 否 1 是

    private Date updatetime;//最后更新时间

    private Integer isdel;//是否被逻辑删除

    private BigDecimal lng;//地址经度

    private BigDecimal lat;//地址纬度

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {

        return lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}
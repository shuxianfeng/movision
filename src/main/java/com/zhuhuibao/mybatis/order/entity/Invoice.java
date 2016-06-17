package com.zhuhuibao.mybatis.order.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;
/**
 * 发票信息
 * @author  penglong
 * @create 2016-06-14
 */
@ApiModel(value = "Invoice",description = "发票信息")
public class Invoice {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "发票创建者ID")
    private Long createId;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "发票抬头公司名称")
    private String invoiceTitle;
    @ApiModelProperty(value = "发票抬头类型：1个人，2企业")
    private Integer invoiceTitleType;
    @ApiModelProperty(value = "发票类型：1增值税普通发票，2增值税专用发票")
    private Integer invoiceType;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "发票收件人名称")
    private String receiveName;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String area;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "收件人手机")
    private String mobile;
    @ApiModelProperty(value = "收件人固定电话")
    private String telephone;
    @ApiModelProperty(value = "状态：0未开票，1已开票，2已完成")
    private String status;
    @ApiModelProperty(value = "快递单号")
    private String expressNum;
    @ApiModelProperty(value = "发票编号")
    private String invoiceNum;
    @ApiModelProperty(value = "增值税发票公司信息")
    private InvoiceCompanyInfo invoiceCompanyInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public Integer getInvoiceTitleType() {
        return invoiceTitleType;
    }

    public void setInvoiceTitleType(Integer invoiceTitleType) {
        this.invoiceTitleType = invoiceTitleType;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName == null ? null : receiveName.trim();
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public InvoiceCompanyInfo getInvoiceCompanyInfo() {
        return invoiceCompanyInfo;
    }

    public void setInvoiceCompanyInfo(InvoiceCompanyInfo invoiceCompanyInfo) {
        this.invoiceCompanyInfo = invoiceCompanyInfo;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
}
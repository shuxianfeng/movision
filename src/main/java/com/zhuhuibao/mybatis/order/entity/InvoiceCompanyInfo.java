package com.zhuhuibao.mybatis.order.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 增值税专用发票公司信息
 * @author  penglong
 * @create 2016-06-14
 */
@ApiModel(value = "InvoiceCompanyInfo",description = "增值税专用发票公司信息")
public class InvoiceCompanyInfo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "发票ID")
    private Long invoiceId;
    @ApiModelProperty(value = "单位名称")
    private String companyName;
    @ApiModelProperty(value = "纳税人识别码")
    private String taxpayerCode;
    @ApiModelProperty(value = "基本开户银行名称")
    private String bankName;
    @ApiModelProperty(value = "基本开户账号")
    private String account;
    @ApiModelProperty(value = "注册场所地址")
    private String regAddress;
    @ApiModelProperty(value = "注册固定电话")
    private String regPhone;
    @ApiModelProperty(value = "营业执照复印件")
    private String businessLicense;
    @ApiModelProperty(value = "税务登记证复印件")
    private String taxCertificate;
    @ApiModelProperty(value = "一般纳税人资格认证复印件")
    private String taxpayerCertificate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode == null ? null : taxpayerCode.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public String getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(String regPhone) {
        this.regPhone = regPhone == null ? null : regPhone.trim();
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate == null ? null : taxCertificate.trim();
    }

    public String getTaxpayerCertificate() {
        return taxpayerCertificate;
    }

    public void setTaxpayerCertificate(String taxpayerCertificate) {
        this.taxpayerCertificate = taxpayerCertificate == null ? null : taxpayerCertificate.trim();
    }
}
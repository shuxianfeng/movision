package com.zhuhuibao.mybatis.memCenter.entity;

import  com.zhuhuibao.business.memCenter.AccountManage.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class Member {
    private Long id;

    private String mobile;

    private String email;

    private String emailCheckCode;

    private String password;

    private Date registerTime;

    private Integer status;

    private Integer identify;

    private Integer isValidatePass;

    private String enterpriseName;

    private Integer employeeType;

    private Long enterpriseEmployeeParentId;

    private Integer companyIdentify;

    private String province;

    private String city;

    private String area;

    private String address;

    private Integer enterpriseType;

    private String enterpriseLogo;

    private String enterpriseDesc;

    private String headShot;

    private String saleProductDesc;

    private String enterpriseCreaterTime;

    private String registerCapital;

    private String employeeNumber;

    private String companyBusinessLicenseImg;

    private Integer enterpriseTelephone;

    private Integer enterpriseFox;

    private String enterpriseWebSite;

    private String enterpriseLinkman;

    private String enterpriseLMDep;

    private String fixedTelephone;

    private String fixedMobile;

    private String QQ;

    private String personRealName;

    private Integer sex;

    private Integer personCompanyType;

    private Integer personPosition;

    private String personIdentifyCard;

    private String personIDFrontImgUrl;

    private String personIDBackImgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getEmailCheckCode() {
        return emailCheckCode;
    }

    public void setEmailCheckCode(String emailCheckCode) {
        this.emailCheckCode = emailCheckCode == null ? null : emailCheckCode.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdentify() {
        return identify;
    }

    public void setIdentify(Integer identify) {
        this.identify = identify;
    }

    public Integer getIsValidatePass() {
        return isValidatePass;
    }

    public void setIsValidatePass(Integer isValidatePass) {
        this.isValidatePass = isValidatePass;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public Integer getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }

    public Long getEnterpriseEmployeeParentId() {
        return enterpriseEmployeeParentId;
    }

    public void setEnterpriseEmployeeParentId(Long enterpriseEmployeeParentId) {
        this.enterpriseEmployeeParentId = enterpriseEmployeeParentId;
    }

    public Integer getCompanyIdentify() {
        return companyIdentify;
    }

    public void setCompanyIdentify(Integer companyIdentify) {
        this.companyIdentify = companyIdentify;
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

    public Integer getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(Integer enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }

    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo == null ? null : enterpriseLogo.trim();
    }

    public String getEnterpriseDesc() {
        return enterpriseDesc;
    }

    public void setEnterpriseDesc(String enterpriseDesc) {
        this.enterpriseDesc = enterpriseDesc == null ? null : enterpriseDesc.trim();
    }

    public String getHeadShot() {
        return headShot;
    }

    public void setHeadShot(String headShot) {
        this.headShot = headShot == null ? null : headShot.trim();
    }

    public String getSaleProductDesc() {
        return saleProductDesc;
    }

    public void setSaleProductDesc(String saleProductDesc) {
        this.saleProductDesc = saleProductDesc == null ? null : saleProductDesc.trim();
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public String getEnterpriseCreaterTime() {
        return enterpriseCreaterTime;
    }

    public void setEnterpriseCreaterTime(String enterpriseCreaterTime) {
        this.enterpriseCreaterTime = enterpriseCreaterTime;
    }

    public String getRegisterCapital() {
        return registerCapital;
    }

    public void setRegisterCapital(String registerCapital) {
        this.registerCapital = registerCapital == null ? null : registerCapital.trim();
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber == null ? null : employeeNumber.trim();
    }

    public String getCompanyBusinessLicenseImg() {
        return companyBusinessLicenseImg;
    }

    public void setCompanyBusinessLicenseImg(String companyBusinessLicenseImg) {
        this.companyBusinessLicenseImg = companyBusinessLicenseImg == null ? null : companyBusinessLicenseImg.trim();
    }

    public Integer getEnterpriseTelephone() {
        return enterpriseTelephone;
    }

    public void setEnterpriseTelephone(Integer enterpriseTelephone) {
        this.enterpriseTelephone = enterpriseTelephone;
    }

    public Integer getEnterpriseFox() {
        return enterpriseFox;
    }

    public void setEnterpriseFox(Integer enterpriseFox) {
        this.enterpriseFox = enterpriseFox;
    }

    public String getEnterpriseWebSite() {
        return enterpriseWebSite;
    }

    public void setEnterpriseWebSite(String enterpriseWebSite) {
        this.enterpriseWebSite = enterpriseWebSite == null ? null : enterpriseWebSite.trim();
    }

    public String getEnterpriseLinkman() {
        return enterpriseLinkman;
    }

    public void setEnterpriseLinkman(String enterpriseLinkman) {
        this.enterpriseLinkman = enterpriseLinkman == null ? null : enterpriseLinkman.trim();
    }

    public String getEnterpriseLMDep() {
        return enterpriseLMDep;
    }

    public void setEnterpriseLMDep(String enterpriseLMDep) {
        this.enterpriseLMDep = enterpriseLMDep == null ? null : enterpriseLMDep.trim();
    }

    public String getFixedTelephone() {
        return fixedTelephone;
    }

    public void setFixedTelephone(String fixedTelephone) {
        this.fixedTelephone = fixedTelephone;
    }

    public String getFixedMobile() {
        return fixedMobile;
    }

    public void setFixedMobile(String fixedMobile) {
        this.fixedMobile = fixedMobile;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getPersonRealName() {
        return personRealName;
    }

    public void setPersonRealName(String personRealName) {
        this.personRealName = personRealName == null ? null : personRealName.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getPersonCompanyType() {
        return personCompanyType;
    }

    public void setPersonCompanyType(Integer personCompanyType) {
        this.personCompanyType = personCompanyType;
    }

    public Integer getPersonPosition() {
        return personPosition;
    }

    public void setPersonPosition(Integer personPosition) {
        this.personPosition = personPosition;
    }

    public String getPersonIdentifyCard() {
        return personIdentifyCard;
    }

    public void setPersonIdentifyCard(String personIdentifyCard) {
        this.personIdentifyCard = personIdentifyCard;
    }

    public String getPersonIDFrontImgUrl() {
        return personIDFrontImgUrl;
    }

    public void setPersonIDFrontImgUrl(String personIDFrontImgUrl) {
        this.personIDFrontImgUrl = personIDFrontImgUrl == null ? null : personIDFrontImgUrl.trim();
    }

    public String getPersonIDBackImgUrl() {
        return personIDBackImgUrl;
    }

    public void setPersonIDBackImgUrl(String personIDBackImgUrl) {
        this.personIDBackImgUrl = personIDBackImgUrl == null ? null : personIDBackImgUrl.trim();
    }
}
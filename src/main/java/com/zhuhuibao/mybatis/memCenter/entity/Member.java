package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class Member implements Serializable{

    private static final long serialVersionUID = -814949502085519570L;

    private String id;

    private String mobile;

    private String email;

    private String emailCheckCode;

    private String password;

    private String registerTime;

    private String status;

    @ApiModelProperty(value="企业身份")
    private String identify;

    @ApiModelProperty(value="企业名称")
    private String enterpriseName;

    @ApiModelProperty(value="工作类别")
    private String workType;

    private String enterpriseEmployeeParentId;

    @ApiModelProperty(value="注册地址：省")
    private String province;

    @ApiModelProperty(value="注册地址：市")
    private String city;

    @ApiModelProperty(value="注册地址：区")
    private String area;

    @ApiModelProperty(value="注册地址")
    private String address;

    @ApiModelProperty(value="企业性质")
    private String enterpriseType;

    @ApiModelProperty(value="企业LOGO")
    private String enterpriseLogo;

    @ApiModelProperty(value="企业介绍")
    private String enterpriseDesc;

    @ApiModelProperty(value="企业或者个人头像")
    private String headShot;

    @ApiModelProperty(value="企业主营范围")
    private String saleProductDesc;

    @ApiModelProperty(value="企业成立时间")
    private String enterpriseCreaterTime;

    @ApiModelProperty(value="货币类型：1人民币，2美元")
    private String currency;

    @ApiModelProperty(value="企业注册资本")
    private String registerCapital;

    @ApiModelProperty(value="企业人员规模")
    private String employeeNumber;

    @ApiModelProperty(value="企业营业执照编号")
    private String coBusLicNum;

    @ApiModelProperty(value="公司所在省")
    private String enterpriseProvince;

    @ApiModelProperty(value="公司所在市")
    private String enterpriseCity;

    @ApiModelProperty(value="公司所在区")
    private String enterpriseArea;

    @ApiModelProperty(value="公司具体地址")
    private String enterpriseAddress;

    @ApiModelProperty(value="企业营业执照url")
    private String companyBusinessLicenseImg;

    @ApiModelProperty(value="公司电话")
    private String enterpriseTelephone;

    @ApiModelProperty(value="公司传真")
    private String enterpriseFox;

    @ApiModelProperty(value="公司网址")
    private String enterpriseWebSite;

    @ApiModelProperty(value="公司联系人名称")
    private String enterpriseLinkman;

    @ApiModelProperty(value="公司联系人所在部门")
    private String enterpriseLMDep;

    @ApiModelProperty(value="公司联系人或者个人座机")
    private String fixedTelephone;

    @ApiModelProperty(value="公司联系人或者个人手机号码")
    private String fixedMobile;

    @ApiModelProperty(value="公司联系人或者个人qq号码")
    private String QQ;

    @ApiModelProperty(value="个人真实名称")
    private String personRealName;

    @ApiModelProperty(value="个人昵称")
    private String nickname;

    @ApiModelProperty(value="性别：1：男，2：女(企业，个人统一使用)")
    private Integer sex;

    @ApiModelProperty(value="个人工作单位类别")
    private Integer personCompanyType;

    @ApiModelProperty(value="个人身份证号码")
    private String personIdentifyCard;

    @ApiModelProperty(value="个人身份证正面上传图片的路径")
    private String personIDFrontImgUrl;

    @ApiModelProperty(value="个人身份真反面图片上传的路径")
    private String personIDBackImgUrl;

    private String account;

    private String isrecommend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getEnterpriseEmployeeParentId() {
        return enterpriseEmployeeParentId;
    }

    public void setEnterpriseEmployeeParentId(String enterpriseEmployeeParentId) {
        this.enterpriseEmployeeParentId = enterpriseEmployeeParentId;
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

    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
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

    public String getEnterpriseTelephone() {
        return enterpriseTelephone;
    }

    public void setEnterpriseTelephone(String enterpriseTelephone) {
        this.enterpriseTelephone = enterpriseTelephone;
    }

    public String getEnterpriseFox() {
        return enterpriseFox;
    }

    public void setEnterpriseFox(String enterpriseFox) {
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

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getCoBusLicNum() {
        return coBusLicNum;
    }

    public void setCoBusLicNum(String coBusLicNum) {
        this.coBusLicNum = coBusLicNum;
    }

    public String getEnterpriseProvince() {
        return enterpriseProvince;
    }

    public void setEnterpriseProvince(String enterpriseProvince) {
        this.enterpriseProvince = enterpriseProvince;
    }

    public String getEnterpriseCity() {
        return enterpriseCity;
    }

    public void setEnterpriseCity(String enterpriseCity) {
        this.enterpriseCity = enterpriseCity;
    }

    public String getEnterpriseArea() {
        return enterpriseArea;
    }

    public void setEnterpriseArea(String enterpriseArea) {
        this.enterpriseArea = enterpriseArea;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
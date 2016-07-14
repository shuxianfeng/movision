package com.zhuhuibao.mybatis.product.entity;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
public class ProductWithMember extends ProductWithBLOBs {

    private String memberId;

    private String identify;

    private String memberName;

    private String address;

    private String enterpriseLogo;

    private String enterpriseWebSite;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }

    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo;
    }

    public String getEnterpriseWebSite() {
        return enterpriseWebSite;
    }

    public void setEnterpriseWebSite(String enterpriseWebSite) {
        this.enterpriseWebSite = enterpriseWebSite;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}

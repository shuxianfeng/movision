package com.zhuhuibao.mybatis.memCenter.entity;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public class MemberDetails extends Member {

    private static final long serialVersionUID = -4162871943106259331L;

    private String enterpriseTypeName;

    public String getEnterpriseTypeName() {
        return enterpriseTypeName;
    }

    public void setEnterpriseTypeName(String enterpriseTypeName) {
        this.enterpriseTypeName = enterpriseTypeName;
    }
}

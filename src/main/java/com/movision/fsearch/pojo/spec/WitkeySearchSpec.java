package com.movision.fsearch.pojo.spec;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author tongxinglong
 * @date 2017/1/17 0017.
 */
public class WitkeySearchSpec {
    @ApiModelProperty(value = "关键字")
    private String q;
    @ApiModelProperty(value = "合作类型")
    private String type;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "发布人身份，1：企业，0：个人")
    private String memberType;
    @ApiModelProperty(value = "发布类型，1：发布任务，2：发布服务，3：发布资质合作", required = true)
    private String parentId;
    @ApiModelProperty(value = "项目类别")
    private String category;
    @ApiModelProperty(value = "系统分类")
    private String systemType;
    private int offset;
    private int limit;
    @ApiModelProperty("排序字段")
    private String sort;
    @ApiModelProperty("true:降序;false:升序")
    private Boolean sortorder;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Boolean getSortorder() {
        return sortorder;
    }

    public void setSortorder(Boolean sortorder) {
        this.sortorder = sortorder;
    }
}

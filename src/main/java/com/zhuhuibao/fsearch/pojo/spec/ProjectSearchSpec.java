package com.zhuhuibao.fsearch.pojo.spec;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class ProjectSearchSpec {

    @ApiModelProperty(value = "开工时间开始")
    private String startDateA;
    @ApiModelProperty(value = "开工时间结束")
    private String startDateB;
    @ApiModelProperty(value = "竣工时间开始")
    private String endDateA;
    @ApiModelProperty(value = "竣工时间结束")
    private String endDateB;
    @ApiModelProperty(value = "省份编码")
    private String province;
    @ApiModelProperty(value = "市编码")
    private String city;
    @ApiModelProperty(value = "项目类型编码")
    private String category;

    @ApiModelProperty(value = "搜索关键字")
    private String q;
    @ApiModelProperty(value = "排序字段")
    private String sort;
    @ApiModelProperty(value = "是否排序 true|false")
    private String sortorder;
    @ApiModelProperty(value = "页码")
    private int offset;
    @ApiModelProperty(value = "所需结果数")
    private int limit;

    public String getStartDateA() {
        return startDateA;
    }

    public void setStartDateA(String startDateA) {
        this.startDateA = startDateA;
    }

    public String getStartDateB() {
        return startDateB;
    }

    public void setStartDateB(String startDateB) {
        this.startDateB = startDateB;
    }

    public String getEndDateA() {
        return endDateA;
    }

    public void setEndDateA(String endDateA) {
        this.endDateA = endDateA;
    }

    public String getEndDateB() {
        return endDateB;
    }

    public void setEndDateB(String endDateB) {
        this.endDateB = endDateB;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
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
}

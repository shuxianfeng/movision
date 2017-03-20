package com.movision.fsearch.pojo.spec;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class SupplierSearchSpec {

    @ApiModelProperty("省份编码")
    private String province;
    @ApiModelProperty("城市编码")
    private String city;
    @ApiModelProperty("身份标识 3:厂商 4:代理商 5:渠道商")
    private String identify;
    @ApiModelProperty("资质类型名称")
    private String assetlevel;
    @ApiModelProperty("系统分类")
    private String category;
    @ApiModelProperty("关键字")
    private String q;
    @ApiModelProperty("排序字段")
    private String sort;
    @ApiModelProperty("true:降序;false:升序")
    private String sortorder;
    @ApiModelProperty("当前页码")
    private int offset;
    @ApiModelProperty("显示记录数")
    private int limit;

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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getAssetlevel() {
        return assetlevel;
    }

    public void setAssetlevel(String assetlevel) {
        this.assetlevel = assetlevel;
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

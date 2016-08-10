package com.zhuhuibao.fsearch.pojo.spec;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContractorSearchSpec {

    @ApiModelProperty("省份编码")
    private String province;
    @ApiModelProperty("资质名称")
    private String assetlevel;
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

    public String getAssetlevel() {
        return assetlevel;
    }

    public void setAssetlevel(String assetlevel) {
        this.assetlevel = assetlevel;
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

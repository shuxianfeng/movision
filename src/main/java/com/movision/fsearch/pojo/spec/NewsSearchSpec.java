package com.movision.fsearch.pojo.spec;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author zhuangyuhao
 * @date 2016/12/21 0021.
 */
@ApiModel
public class NewsSearchSpec {

    /**
     * 搜索字段类型：title
     */
    public static final String SEARCH_FIELD_TITLE = "T";
    /**
     * 搜索字段类型：content
     */
    public static final String SEARCH_FIELD_CONTENT = "C";

    @ApiModelProperty("搜索词")
    private String q;
    @ApiModelProperty("检索字段：T-title，C-content,，空-默认")
    private String searchField;
    @ApiModelProperty("排序字段")
    private String sort;
    @ApiModelProperty("true:降序;false:升序,默认true")
    private boolean sortorder = true;
    @ApiModelProperty("当前页码")
    private int offset;
    @ApiModelProperty("显示记录数")
    private int limit;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean getSortorder() {
        return sortorder;
    }

    public void setSortorder(boolean sortorder) {
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

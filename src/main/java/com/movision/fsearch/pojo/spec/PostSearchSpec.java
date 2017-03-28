package com.movision.fsearch.pojo.spec;

import java.io.Serializable;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:30
 */
public class PostSearchSpec implements Serializable {

    /**
     * 搜索的词(输入框中的词)
     */
    private String q;
    /**
     * 排序的字段
     */
    private String sort;
    /**
     * 正序 or 倒序
     */
    private String sortorder;

    private int offset;

    private int limit;

    @Override
    public String toString() {
        return "PostSearchSpec{" +
                "q='" + q + '\'' +
                ", sort='" + sort + '\'' +
                ", sortorder='" + sortorder + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getQ() {

        return q;
    }

    public String getSort() {
        return sort;
    }

    public String getSortorder() {
        return sortorder;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}

package com.movision.fsearch.pojo.spec;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/20 16:00
 */
public class GoodsSearchSpec {

    private int protype;
    private int brandid;
    private String q;
    private String sort;
    private String sortorder;
    private int offset;
    private int limit;

    @Override
    public String toString() {
        return "GoodsSearchSpec{" +
                "protype=" + protype +
                ", brandid=" + brandid +
                ", q='" + q + '\'' +
                ", sort='" + sort + '\'' +
                ", sortorder='" + sortorder + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }

    public void setProtype(int protype) {
        this.protype = protype;
    }

    public void setBrandid(int brandid) {
        this.brandid = brandid;
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

    public int getProtype() {

        return protype;
    }

    public int getBrandid() {
        return brandid;
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

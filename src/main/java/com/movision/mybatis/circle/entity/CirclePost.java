package com.movision.mybatis.circle.entity;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/11 18:04
 */
public class CirclePost {
    private Integer circleid;
    private String circlename;
    private Integer cid;    //yw_circle_category id
    private String categoryname;

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Integer getCircleid() {

        return circleid;
    }

    public String getCirclename() {
        return circlename;
    }

    public Integer getCid() {
        return cid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    @Override
    public String toString() {
        return "CircleCategory{" +
                "circleid=" + circleid +
                ", circlename='" + circlename + '\'' +
                ", cid=" + cid +
                ", categoryname='" + categoryname + '\'' +
                '}';
    }

    /**
     * 重写equals方法，用于比对
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CirclePost
                && this.circleid != null
                && this.circleid.equals(((CirclePost) obj).getCircleid());
    }
}

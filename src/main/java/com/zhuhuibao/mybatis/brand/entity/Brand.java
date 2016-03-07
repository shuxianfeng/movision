package com.zhuhuibao.mybatis.brand.entity;

public class Brand {
    private Long id;

    private String brandCNName;

    private String brandENName;

    private Long views;

    private Integer weight;

    private String logourl;

    private Long createid;

    private String branddesc;

    private String imgurl;

    private Integer status;

    private String brandOwer;

    private String brandCertificate;

    private String brandWebSite;

    private String publishTime;

    private String checkTime;

    private String lastModifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandCNName() {
        return brandCNName;
    }

    public void setBrandCNName(String brandCNName) {
        this.brandCNName = brandCNName == null ? null : brandCNName.trim();
    }

    public String getBrandENName() {
        return brandENName;
    }

    public void setBrandENName(String brandENName) {
        this.brandENName = brandENName == null ? null : brandENName.trim();
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl == null ? null : logourl.trim();
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public String getBranddesc() {
        return branddesc;
    }

    public void setBranddesc(String branddesc) {
        this.branddesc = branddesc == null ? null : branddesc.trim();
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBrandOwer() {
        return brandOwer;
    }

    public void setBrandOwer(String brandOwer) {
        this.brandOwer = brandOwer == null ? null : brandOwer.trim();
    }

    public String getBrandCertificate() {
        return brandCertificate;
    }

    public void setBrandCertificate(String brandCertificate) {
        this.brandCertificate = brandCertificate == null ? null : brandCertificate.trim();
    }

    public String getBrandWebSite() {
        return brandWebSite;
    }

    public void setBrandWebSite(String brandWebSite) {
        this.brandWebSite = brandWebSite == null ? null : brandWebSite.trim();
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime == null ? null : publishTime.trim();
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime == null ? null : checkTime.trim();
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime == null ? null : lastModifyTime.trim();
    }
}
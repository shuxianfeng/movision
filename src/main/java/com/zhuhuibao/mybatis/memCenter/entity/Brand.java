package com.zhuhuibao.mybatis.memCenter.entity;

import com.zhuhuibao.mybatis.product.entity.Product;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.zhuhuibao.business.memCenter.BrandManage.JsonDateSerializer;
import java.util.Date;

public class Brand {
    private Product product;

    private Long id;

    private String CNName;

    private String ENName;

    private Long views;

    private Integer weight;

    private String logourl;

    private Long createid;

    private String desc;

    private String imgurl;

    private Integer status;

    private String owner;

    private String certificate;

    private String webSite;

    private Date publishTime;

    private Date checkTime;

    private Date lastModifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandCNName() {
        return CNName;
    }

    public void setBrandCNName(String brandCNName) {
        this.CNName = brandCNName == null ? null : brandCNName.trim();
    }

    public String getBrandENName() {
        return ENName;
    }

    public void setBrandENName(String brandENName) {
        this.ENName = brandENName == null ? null : brandENName.trim();
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
        return desc;
    }

    public void setBranddesc(String branddesc) {
        this.desc = branddesc == null ? null : branddesc.trim();
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
        return owner;
    }

    public void setBrandOwer(String brandOwer) {
        this.owner = brandOwer == null ? null : brandOwer.trim();
    }

    public String getBrandCertificate() {
        return certificate;
    }

    public void setBrandCertificate(String brandCertificate) {
        this.certificate = brandCertificate == null ? null : brandCertificate.trim();
    }

    public String getBrandWebSite() {
        return webSite;
    }

    public void setBrandWebSite(String brandWebSite) {
        this.webSite = brandWebSite == null ? null : brandWebSite.trim();
    }
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
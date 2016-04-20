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

    private Integer views;

    private Integer weight;

    private String logourl;

    private Long createid;

    private String enterpriseName;

    private String description;

    private String imgurl;

    private Integer status;

    private String owner;

    private String certificate;

    private String webSite;

    private Date publishTime;

    private Date checkTime;

    private Date lastModifyTime;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCNName() {
        return CNName;
    }

    public void setCNName(String CNName) {
        this.CNName = CNName;
    }

    public String getENName() {
        return ENName;
    }

    public void setENName(String ENName) {
        this.ENName = ENName;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
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
        this.logourl = logourl;
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
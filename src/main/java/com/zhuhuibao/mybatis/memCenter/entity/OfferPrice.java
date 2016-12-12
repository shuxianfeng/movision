package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "报价单", description = "报价单")
public class OfferPrice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价ID")
	private Long id;

    @ApiModelProperty(value = "询价ID")
    private Long askid;

    @ApiModelProperty(value = "报价者的ID")
    private Long createid;

    @ApiModelProperty(value = "报价的时间")
    private Date offerTime;

    @ApiModelProperty(value = "产品报价内容")
    private String content;

    @ApiModelProperty(value = "报价清单的url地址")
    private String billurl;

    @ApiModelProperty(value = "是否显示报价者联系信息。1：显示，0不显示")
    private Boolean isShow;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司联系人")
    private String linkMan;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "手机端上传询价单图片，传img")
    private String  mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAskid() {
		return askid;
	}

	public void setAskid(Long askid) {
		this.askid = askid;
	}

	public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public Date getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(Date offerTime) {
        this.offerTime = offerTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getBillurl() {
		return billurl;
	}

	public void setBillurl(String billurl) {
		this.billurl = billurl;
	}

	public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan == null ? null : linkMan.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getImgUrl() {
        String imgUrl = "";
        if(StringUtils.isNotEmpty(billurl) &&
                !this.billurl.contains("xlsx") && !this.billurl.contains("xls") && !this.billurl.contains("doc")){
            imgUrl = "//image.zhuhui8.com/upload/price/img/"+billurl;
        }
        return imgUrl;
    }
}
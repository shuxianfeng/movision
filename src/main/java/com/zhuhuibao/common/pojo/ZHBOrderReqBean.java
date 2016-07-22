package com.zhuhuibao.common.pojo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 订单请求参数
 */
@ApiModel(value="订单请求参数")
public class ZHBOrderReqBean {

    /**
     * goodsId :   商品ID
     * number :    购买数量
     * goodsType : 商品类型 1：技术培训，2：专家培训 ,3：VIP服务套餐订单，4:筑慧币
     * mobile :    下单手机号
     * needInvoice : 是否需要发票 true | false
     * invoiceTitle : 发票抬头
     * invoiceType :  发票类型 1普通发票（纸质），2：增值发票
     */
    @ApiModelProperty(value="商品ID",required = true)
    private String goodsId;
    @ApiModelProperty(value="商品数量",required = true)
    private String number;
    @ApiModelProperty(value="商品类型 3：VIP服务套餐订单 4：筑慧币",required = true)
    private String goodsType;


    //发票信息
    @ApiModelProperty(value="是否需要发票(true|false)",required = true)
    private String needInvoice;
    @ApiModelProperty(value="发票类型(1:增值税普通发票 2:增值税专用发票)")
    private String invoiceType;
    @ApiModelProperty(value="发票抬头类型(1:个人 2:企业)")
    private String invoiceTitleType;
    @ApiModelProperty(value="发票抬头(企业名称)")
    private String invoiceTitle;
    @ApiModelProperty(value="发票收件人")
    private String invoiceReceiveName;
    @ApiModelProperty(value="发票收件地址(省份编码)")
    private String invoiceProvince;
    @ApiModelProperty(value="发票收件地址(市编码)")
    private String invoiceCity;
    @ApiModelProperty(value="发票收件地址(地区编码)")
    private String invoiceArea;
    @ApiModelProperty(value="发票收件地址")
    private String invoiceAddress;
    @ApiModelProperty(value="发票收件人手机")
    private String invoiceMobile;
    @ApiModelProperty(value="发票收件人固话")
    private String invoiceTel;
    @ApiModelProperty(value="发票公司名称")
    private String invoiceCompanyName;
    @ApiModelProperty(value="发票公司纳税人识别码")
    private String invoiceTaxpayerCode;
    @ApiModelProperty(value="发票公司开户银行名称")
    private String invoiceBank;
    @ApiModelProperty(value="发票公司开户银行账号")
    private String invoiceBankAccount;
    @ApiModelProperty(value="发票公司注册场所地址")
    private String invoiceComregAddr;
    @ApiModelProperty(value="发票公司注册固定电话")
    private String invoiceComregTel;
    @ApiModelProperty(value="发票公司营业执照复印件")
    private String invoiceBusiLic;
    @ApiModelProperty(value="发票公司税务登记证复印件")
    private String invoiceTaxLic;
    @ApiModelProperty(value="发票公司纳税人资格认证复印件")
    private String invoiceCertLic;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getNeedInvoice() {
        return needInvoice;
    }

    public void setNeedInvoice(String needInvoice) {
        this.needInvoice = needInvoice;
    }


    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTitleType() {
        return invoiceTitleType;
    }

    public void setInvoiceTitleType(String invoiceTitleType) {
        this.invoiceTitleType = invoiceTitleType;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceReceiveName() {
        return invoiceReceiveName;
    }

    public void setInvoiceReceiveName(String invoiceReceiveName) {
        this.invoiceReceiveName = invoiceReceiveName;
    }

    public String getInvoiceProvince() {
        return invoiceProvince;
    }

    public void setInvoiceProvince(String invoiceProvince) {
        this.invoiceProvince = invoiceProvince;
    }

    public String getInvoiceCity() {
        return invoiceCity;
    }

    public void setInvoiceCity(String invoiceCity) {
        this.invoiceCity = invoiceCity;
    }

    public String getInvoiceArea() {
        return invoiceArea;
    }

    public void setInvoiceArea(String invoiceArea) {
        this.invoiceArea = invoiceArea;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceMobile() {
        return invoiceMobile;
    }

    public void setInvoiceMobile(String invoiceMobile) {
        this.invoiceMobile = invoiceMobile;
    }

    public String getInvoiceTel() {
        return invoiceTel;
    }

    public void setInvoiceTel(String invoiceTel) {
        this.invoiceTel = invoiceTel;
    }

    public String getInvoiceCompanyName() {
        return invoiceCompanyName;
    }

    public void setInvoiceCompanyName(String invoiceCompanyName) {
        this.invoiceCompanyName = invoiceCompanyName;
    }

    public String getInvoiceTaxpayerCode() {
        return invoiceTaxpayerCode;
    }

    public void setInvoiceTaxpayerCode(String invoiceTaxpayerCode) {
        this.invoiceTaxpayerCode = invoiceTaxpayerCode;
    }

    public String getInvoiceBank() {
        return invoiceBank;
    }

    public void setInvoiceBank(String invoiceBank) {
        this.invoiceBank = invoiceBank;
    }

    public String getInvoiceBankAccount() {
        return invoiceBankAccount;
    }

    public void setInvoiceBankAccount(String invoiceBankAccount) {
        this.invoiceBankAccount = invoiceBankAccount;
    }

    public String getInvoiceComregAddr() {
        return invoiceComregAddr;
    }

    public void setInvoiceComregAddr(String invoiceComregAddr) {
        this.invoiceComregAddr = invoiceComregAddr;
    }

    public String getInvoiceComregTel() {
        return invoiceComregTel;
    }

    public void setInvoiceComregTel(String invoiceComregTel) {
        this.invoiceComregTel = invoiceComregTel;
    }

    public String getInvoiceBusiLic() {
        return invoiceBusiLic;
    }

    public void setInvoiceBusiLic(String invoiceBusiLic) {
        this.invoiceBusiLic = invoiceBusiLic;
    }

    public String getInvoiceTaxLic() {
        return invoiceTaxLic;
    }

    public void setInvoiceTaxLic(String invoiceTaxLic) {
        this.invoiceTaxLic = invoiceTaxLic;
    }

    public String getInvoiceCertLic() {
        return invoiceCertLic;
    }

    public void setInvoiceCertLic(String invoiceCertLic) {
        this.invoiceCertLic = invoiceCertLic;
    }
}

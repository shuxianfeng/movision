package com.zhuhuibao.mybatis.product.entity;

import java.util.List;

public class ProductWithBLOBs extends Product {
    private String detailDesc;

    private String paras;

    private String service;

    private List<ProductParam> params;
    
    private List<ParamPrice> paramPrice;
    
    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc == null ? null : detailDesc.trim();
    }

    public String getParas() {
        return paras;
    }

    public void setParas(String paras) {
        this.paras = paras == null ? null : paras.trim();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

	public List<ProductParam> getParams() {
		return params;
	}

	public void setParams(List<ProductParam> params) {
		this.params = params;
	}

	public List<ParamPrice> getParamPrice() {
		return paramPrice;
	}

	public void setParamPrice(List<ParamPrice> paramPrice) {
		this.paramPrice = paramPrice;
	}
    
}
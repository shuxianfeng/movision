package com.zhuhuibao.common.constant;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/28
 * Time: 14:32
 */
public class PriceConstant {

    /**
     * 询价类型
     * CPXJ("产品询价"), PPXJ ("品牌询价"),DPXJ("店铺询价"),GKXJ("公开询价");
     */
    public enum ASKPRICETYPE {
        CPXJ("CPXJ"), PPXJ ("PPXJ"),DPXJ("DPXJ"),GKXJ("GKXJ");
        public final String value;

        ASKPRICETYPE(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static final String CPXJ = "CPXJ";   //产品询价
    public static final String PPXJ = "PPXJ";   //品牌询价
    public static final String DPXJ = "DPXJ";   //店铺询价
    public static final String GKXJ = "GKXJ";   //公开询价


}

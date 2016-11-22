package com.external.form;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public enum WeChatNewsTypeENum {
    Banner(1, "banner"),

    MeiLiShiSang(2, "魅力时尚"),

    JianKangRenSheng(3, "健康人生"),

    LiuQingSuiYue(4, "留情岁月"),

    ChenRenXingQu(5, "成人性趣");

    private Integer code;

    private String name;

    WeChatNewsTypeENum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @return the typeCode
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public static String getName(int code) {
        for (WeChatNewsTypeENum em : values()) {
            if (em.getCode() == code) {
                return em.getName();
            }
        }
        return null;
    }

    public static WeChatNewsTypeENum get(int code) {
        for (WeChatNewsTypeENum em : values()) {
            if (em.getCode() == code) {
                return em;
            }
        }
        return null;
    }

    public static WeChatNewsTypeENum get(String name) {
        for (WeChatNewsTypeENum em : values()) {
            if (em.getName() == name) {
                return em;
            }
        }
        return null;
    }
}

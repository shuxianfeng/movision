package com.movision.mybatis.imuser.entity;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/14 10:56
 */
public class ImuserAppuser {

    private String nickname;
    private String photo;
    private String sign;
    private String imName;
    private String icon;
    private String imSign;
    private String accid;
    private Integer userid;

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getAccid() {

        return accid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setImName(String imName) {
        this.imName = imName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setImSign(String imSign) {
        this.imSign = imSign;
    }

    public String getNickname() {

        return nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSign() {
        return sign;
    }

    public String getImName() {
        return imName;
    }

    public String getIcon() {
        return icon;
    }

    public String getImSign() {
        return imSign;
    }
}

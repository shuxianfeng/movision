package com.zhuhuibao.common.constant;

/**
 * 会员注册，登陆，找回密码常量表
 * @author pl
 * @version 2016/6/13 0013
 */
public class MemberConstant {

    /**
     * 会员注册标识图形验证码
     */
    public static final String SESSION_TYPE_REGISTER = "memberRegister";

    /**
     * 找回密码图形验证码
     */
    public static final String SESSION_TYPE_SEEKPWD = "seekPwdCode";

    /**
     * 找回密码时的用户名
     */
    public static final String SESSION_TYPE_SEEKPWD_USERNAME = "SEEKUSERNAME";

    /**
     * 登陆
     */
    public static final String SESSION_TYPE_LOGIN = "memberLogin";


    /**
     * 会员状态 0：未激活(只针对邮件)，1：注册成功，2：注销，5：完善资料待审核
     * 6：完善资料已审核，7：资料审核已拒绝，9：实名认证待审核，10：实名认证已认证  11：实名认证已拒绝
     */
    public enum MemberStatus {
        WJH("0"), ZCCG("1"),ZX("2"),WSZLDSH("5"),WSZLYSH("6"),ZLSHYJJ("7"),SMRZDSH("9"),SMRZYRZ("10"),SMRZYJJ("11");
        public final String value;

        MemberStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return  this.value;
        }

        public int intValue(){
            return Integer.valueOf(this.value);
        }
    }

    /**
     * 会员状态 0：未激活(只针对邮件)，1：注册成功，2：注销，5：完善资料待审核
     * 6：完善资料已审核，7：资料审核已拒绝，9：实名认证待审核，10：实名认证已认证  11：实名认证已拒绝
     */
    public enum MemberIdentify {
        QY("1"), GR("2"),CJ("3"),DLS("4"),QDSH("5"),GCS("6"),SYDW("7"),HYXH("8"),QTQY("9");
        public final String value;

        MemberIdentify(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return  this.value;
        }

    }

    /**
     * 商铺状态
     */
    public enum ShopStatus {
        DSH("1"),YSH("2"),YJJ("3"),YZX("4");
        public final String value;

        ShopStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return  this.value;
        }

    }

    /**
     * 企业注册时的身份管理员
     */
    public static final int MEMBER_WORK_TYPE_100 = 100;

    /**
     * 个人注册时的身份
     */
    public static final int MEMBER_WORK_TYPE_217 = 217;
}

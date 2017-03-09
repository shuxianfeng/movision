package com.movision.mybatis.imFirstDialogue.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 11:02
 */
@ApiModel(value = "IM消息实体")
public class ImMsg {
    @ApiModelProperty(value = "发送者accid，用户帐号，最大32字符", required = true)
    private String from;
    @ApiModelProperty(value = "0：点对点个人消息，1：群消息，其他返回414", required = true)
    private Integer ope;

    @ApiModelProperty(value = "ope==0是表示accid即用户id，ope==1表示tid即群id", required = true)
    private String to;

    @ApiModelProperty(value = "0 表示文本消息,\n" +
            "1 表示图片，\n" +
            "2 表示语音，\n" +
            "3 表示视频，\n" +
            "4 表示地理位置信息，\n" +
            "6 表示文件，\n" +
            "100 自定义消息类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "请参考下方消息示例说明中对应消息的body字段，\n" +
            "最大长度5000字符，为一个json串", required = true)
    private String body;

    @ApiModelProperty(value = "本消息是否需要过自定义反垃圾系统，true或false, 默认false")
    private String antispam;

    @ApiModelProperty(value = "自定义的反垃圾内容, 长度限制同body字段，不能超过5000字符")
    private String antispamCustom;

    @ApiModelProperty(value = "发消息时特殊指定的行为选项,Json格式，可用于指定消息的漫游，存云端历史，发送方多端同步，推送，消息抄送等特殊行为;option中字段不填时表示默认值 option示例:\n" +
            "\n" +
            "{\"push\":false,\"roam\":true,\"history\":false,\"sendersync\":true,\"route\":false,\"badge\":false,\"needPushNick\":true}\n" +
            "\n" +
            "字段说明：\n" +
            "1. roam: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）；\u2028\n" +
            "2. history: 该消息是否存云端历史，默认true；\n" +
            "\u20283. sendersync: 该消息是否需要发送方多端同步，默认true；\n" +
            "\u20284. push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；\n" +
            "\u20285. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能);\n" +
            "\u20286. badge:该消息是否需要计入到未读计数中，默认true;\n" +
            "7. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认true;\n" +
            "8. persistent: 是否需要存离线消息，不设置该参数时默认true。")
    private String option;

    @ApiModelProperty(value = "ios推送内容，不超过150字符，option选项中允许推送（push=true），此字段可以指定推送内容")
    private String pushcontent;

    @ApiModelProperty(value = "ios 推送对应的payload,必须是JSON,不能超过2k字符")
    private String payload;

    @ApiModelProperty(value = "开发者扩展字段，长度限制1024字符")
    private String ext;

    @ApiModelProperty(value = "发送群消息时的强推（@操作）用户列表，格式为JSONArray，如[\"accid1\",\"accid2\"]。若forcepushall为true，则forcepushlist为除发送者外的所有有效群成员")
    private String forcepushlist;

    @ApiModelProperty(value = "发送群消息时，针对强推（@操作）列表forcepushlist中的用户，强制推送的内容")
    private String forcepushcontent;

    @ApiModelProperty(value = "发送群消息时，强推（@操作）列表是否为群里除发送者外的所有有效成员，true或false，默认为false")
    private String forcepushall;

    @Override
    public String toString() {
        return "ImMsg{" +
                "from='" + from + '\'' +
                ", ope=" + ope +
                ", to='" + to + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", antispam='" + antispam + '\'' +
                ", antispamCustom='" + antispamCustom + '\'' +
                ", option='" + option + '\'' +
                ", pushcontent='" + pushcontent + '\'' +
                ", payload='" + payload + '\'' +
                ", ext='" + ext + '\'' +
                ", forcepushlist='" + forcepushlist + '\'' +
                ", forcepushcontent='" + forcepushcontent + '\'' +
                ", forcepushall='" + forcepushall + '\'' +
                '}';
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setOpe(Integer ope) {
        this.ope = ope;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAntispam(String antispam) {
        this.antispam = antispam;
    }

    public void setAntispamCustom(String antispamCustom) {
        this.antispamCustom = antispamCustom;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setPushcontent(String pushcontent) {
        this.pushcontent = pushcontent;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setForcepushlist(String forcepushlist) {
        this.forcepushlist = forcepushlist;
    }

    public void setForcepushcontent(String forcepushcontent) {
        this.forcepushcontent = forcepushcontent;
    }

    public void setForcepushall(String forcepushall) {
        this.forcepushall = forcepushall;
    }

    public String getFrom() {

        return from;
    }

    public Integer getOpe() {
        return ope;
    }

    public String getTo() {
        return to;
    }

    public Integer getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public String getAntispam() {
        return antispam;
    }

    public String getAntispamCustom() {
        return antispamCustom;
    }

    public String getOption() {
        return option;
    }

    public String getPushcontent() {
        return pushcontent;
    }

    public String getPayload() {
        return payload;
    }

    public String getExt() {
        return ext;
    }

    public String getForcepushlist() {
        return forcepushlist;
    }

    public String getForcepushcontent() {
        return forcepushcontent;
    }

    public String getForcepushall() {
        return forcepushall;
    }
}

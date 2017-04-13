package com.movision.mybatis.imSystemInform.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 20:22
 */
@ApiModel(value = "批量发送点对点自定义系统通知")
public class ImBatchAttachMsg implements Serializable {

    @ApiModelProperty(value = "发送者accid，用户帐号，最大32字符，APP内唯一", required = true)
    private String fromAccid;

    @ApiModelProperty(value = "[\"aaa\",\"bbb\"]（JSONArray对应的accid, 最大限500人", required = true)
    private String toAccids;

    @ApiModelProperty(value = "自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符")
    private String attach;

    @ApiModelProperty(value = "\tiOS推送内容，第三方自己组装的推送内容,不超过150字符")
    private String pushcontent;

    @ApiModelProperty(value = "iOS推送对应的payload,必须是JSON,不能超过2k字符")
    private String payload;

    @ApiModelProperty(value = "如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字符，如果不指定，会使用默认声音")
    private String sound;

    @ApiModelProperty(value = "1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线")
    private Integer save;

    @ApiModelProperty(value = "发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。\n" +
            "option示例：\n" +
            "{\"badge\":false,\"needPushNick\":false,\"route\":false}\n" +
            "字段说明：\n" +
            "1. badge:该消息是否需要计入到未读计数中，默认true;\n" +
            "2. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendBatchMsg.action接口有别)。\n" +
            "3. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能)")
    private String option;

    public void setFromAccid(String fromAccid) {
        this.fromAccid = fromAccid;
    }

    public void setToAccids(String toAccids) {
        this.toAccids = toAccids;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setPushcontent(String pushcontent) {
        this.pushcontent = pushcontent;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setSave(Integer save) {
        this.save = save;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getFromAccid() {
        return fromAccid;
    }

    public String getToAccids() {
        return toAccids;
    }

    public String getAttach() {
        return attach;
    }

    public String getPushcontent() {
        return pushcontent;
    }

    public String getPayload() {
        return payload;
    }

    public String getSound() {
        return sound;
    }

    public Integer getSave() {
        return save;
    }

    public String getOption() {
        return option;
    }
}

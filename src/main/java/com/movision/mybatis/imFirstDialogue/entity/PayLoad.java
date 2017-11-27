package com.movision.mybatis.imFirstDialogue.entity;

import java.io.Serializable;

/**
 * iOS推送对应的payload,必须是JSON,不能超过2k字符
 *
 * @Author zhuangyuhao
 * @Date 2017/11/27 14:26
 */
public class PayLoad implements Serializable {
    /**
     * 推送类型
     * 1 动态通知
     * 2 系统通知
     * 3 运营通知
     * 4 im消息
     */
    private Integer type;

    /**
     * 系统推送的id   yw_system_push
     */
    private Integer id;

    /**
     * pushcontent
     */
    private String msg;

    /**
     * 对应消息的body字段
     */
    private String body;

    /**
     * 运营通知的封面图
     */
    private String img;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getType() {

        return type;
    }

    public Integer getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getBody() {
        return body;
    }

    public String getImg() {
        return img;
    }
}

package com.movision.mybatis.post.entity;

/**
 * yw_post表中的postcontent字段的实体
 * 多了一个index,即在Document中的顺序
 *
 * @Author zhuangyuhao
 * @Date 2017/6/16 11:34
 */
public class PostContent {

    /**
     * 模块排序ID
     */
    private Integer orderid;

    /**
     * 模块类型：0文字，1图片，2视频
     */
    private Integer type;

    /**
     * 值字符串，文字/url/vid
     */
    private String value;

    /**
     * 宽高， 图片时不为空
     */
    private String wh;

    /**
     * 手机本地文件存放的绝对路径，图片或视频时不为空
     */
    private String dir;


    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setWh(String wh) {
        this.wh = wh;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getWh() {
        return wh;
    }

    public String getDir() {
        return dir;
    }

    @Override
    public String toString() {
        return "PostContent{" +
                "orderid=" + orderid +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", wh='" + wh + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}

package com.movision.common.pojo;

import java.util.Comparator;
import java.util.Date;

/**
 * 消息中心-动态消息
 *
 * @Author zhuangyuhao
 * @Date 2017/8/2 14:40
 */
public class InstantInfo {
    private Date intime;
    private Integer type;   //动态消息类型，0：评论， 1：赞， 2：关注
    private Object object;

    public InstantInfo(Date intime, Integer type, Object object) {
        this.intime = intime;
        this.type = type;
        this.object = object;
    }

    public InstantInfo() {
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {

        return type;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Date getIntime() {

        return intime;
    }

    public Object getObject() {
        return object;
    }

    public static Comparator intimeComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            InstantInfo u1 = (InstantInfo) o1;
            InstantInfo u2 = (InstantInfo) o2;

            return (u1.getIntime().before(u2.getIntime()) ? -1 :
                    (u1.getIntime().equals(u2.getIntime()) ? 0 : 1));
        }

    };

    @Override
    public String toString() {
        return "InstantInfo{" +
                "intime=" + intime +
                ", type=" + type +
                ", object=" + object +
                '}';
    }
}

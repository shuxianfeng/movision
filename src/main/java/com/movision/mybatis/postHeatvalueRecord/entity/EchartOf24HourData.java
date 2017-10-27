package com.movision.mybatis.postHeatvalueRecord.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author zhuangyuhao
 * @Date 2017/10/26 9:17
 */
public class EchartOf24HourData implements Serializable {
    /**
     * 帖子热度变化类型中文名称
     */
    private String name;

    /**
     * EChart 类型
     */
    private String type;

    /**
     *
     */
    private String stack;

    /**
     * Echart 中 series 中的每个对象的data
     */
    private int[] data;


    @Override
    public String toString() {
        return "EchartOf24HourData{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stack='" + stack + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getType() {

        return type;
    }

    public String getStack() {
        return stack;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public String getName() {

        return name;
    }

    public int[] getData() {
        return data;
    }
}

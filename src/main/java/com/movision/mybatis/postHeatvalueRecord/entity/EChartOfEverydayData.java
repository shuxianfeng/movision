package com.movision.mybatis.postHeatvalueRecord.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author zhuangyuhao
 * @Date 2017/10/26 17:30
 */
public class EChartOfEverydayData implements Serializable {
    /**
     * 横坐标的值
     */
    private String[] date;

    /**
     * 纵坐标的值
     */
    private int[] data;

    @Override
    public String toString() {
        return "EChartOfEverydayData{" +
                "date=" + Arrays.toString(date) +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public String[] getDate() {

        return date;
    }

    public void setData(int[] data) {
        this.data = data;
    }


    public int[] getData() {
        return data;
    }
}

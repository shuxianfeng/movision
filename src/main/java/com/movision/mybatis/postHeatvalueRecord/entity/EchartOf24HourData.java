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
    private String cName;
    /**
     * 帖子热度变化类型英文名称
     */
    private String eName;
    /**
     * Echart 中 series 中的每个对象的data
     */
    private int[] data;

    @Override
    public String toString() {
        return "EchartOf24HourData{" +
                "cName='" + cName + '\'' +
                ", eName='" + eName + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public String getcName() {

        return cName;
    }

    public String geteName() {
        return eName;
    }

    public int[] getData() {
        return data;
    }
}

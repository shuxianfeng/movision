package com.movision.mybatis.pageHelper.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/12/5 9:17
 */
public class Datagrid {
    private long total;
    private List rows = new ArrayList<>();

    public Datagrid() {
        super();
    }

    public Datagrid(long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

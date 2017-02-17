package com.movision.mybatis.combo.entity;

public class Combo {
    private Integer id;

    private Integer comboid;

    private String comboname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComboid() {
        return comboid;
    }

    public void setComboid(Integer comboid) {
        this.comboid = comboid;
    }

    public String getComboname() {
        return comboname;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname == null ? null : comboname.trim();
    }
}
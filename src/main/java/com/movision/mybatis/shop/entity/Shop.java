package com.movision.mybatis.shop.entity;

public class Shop {
    private Integer id;

    private String name;

    private String phone;

    private String owner;

    private String returnnote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getReturnnote() {
        return returnnote;
    }

    public void setReturnnote(String returnnote) {
        this.returnnote = returnnote == null ? null : returnnote.trim();
    }
}
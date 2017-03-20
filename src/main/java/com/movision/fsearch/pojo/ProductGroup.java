package com.movision.fsearch.pojo;

import java.util.List;

public class ProductGroup {
    private String key;
    private String name;
    private List<GroupValue> values;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupValue> getValues() {
        return values;
    }

    public void setValues(List<GroupValue> values) {
        this.values = values;
    }
}
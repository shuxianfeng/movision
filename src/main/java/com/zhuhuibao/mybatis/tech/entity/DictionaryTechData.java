package com.zhuhuibao.mybatis.tech.entity;
/**
 * 技术资料(解决方案，技术资料，培训资料)分类常量信息
 * @author  penglong
 * @create 2016-05-27
 */
public class DictionaryTechData {
    private Integer id;

    private Integer code;

    private String name;

    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
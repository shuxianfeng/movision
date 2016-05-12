package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value="职位",description = "职位属性")
public class Job {
    @ApiModelProperty(value="职位ID",required = true)
    private String id;

    @ApiModelProperty(value="发布人ID",required = true)
    private String createid;

    @ApiModelProperty(value="发布时间")
    private String publishTime;

    @ApiModelProperty(value="更新时间")
    private String updateTime;

    @ApiModelProperty(value="职位名称")
    private String name;

    private String enterpriseName;

    private String size;

    private String enterpriseDesc;

    @ApiModelProperty(value="职位类别")
    private String positionType;

    @ApiModelProperty(value="职位类别ID")
    private String postionTypeID;

    private String parentId;

    @ApiModelProperty(value="职位月薪")
    private Integer salary;

    private String salaryName;

    @ApiModelProperty(value="省份编码")
    private String province;

    @ApiModelProperty(value="城市编码")
    private String city;

    @ApiModelProperty(value="区域编码")
    private String area;

    @ApiModelProperty(value="工作区域")
    private String workArea;

    @ApiModelProperty(value="学历要求")
    private Integer education;

    private String educationName;

    @ApiModelProperty(value="工作经验")
    private Integer experience;

    private String experienceName;

    @ApiModelProperty(value="年龄要求")
    private Integer age;

    private String ageName;

    private String description;

    @ApiModelProperty(value="福利",notes="逗号隔开")
    private String welfare;

    @ApiModelProperty(value="部门")
    private String department;

    @ApiModelProperty(value="汇报对象上级")
    private String superior;

    @ApiModelProperty(value="下属人数")
    private Integer number;

    @ApiModelProperty(value="专业要求")
    private String profession;

    @ApiModelProperty(value="删除标识",hidden = true)
    private Integer is_deleted;

    @ApiModelProperty(value="企业logo")
    private String enterpriseLogo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior == null ? null : superior.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession == null ? null : profession.trim();
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getSalaryName() {
        return salaryName;
    }

    public void setSalaryName(String salaryName) {
        this.salaryName = salaryName;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getExperienceName() {
        return experienceName;
    }

    public void setExperienceName(String experienceName) {
        this.experienceName = experienceName;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public String getEnterpriseDesc() {
        return enterpriseDesc;
    }

    public void setEnterpriseDesc(String enterpriseDesc) {
        this.enterpriseDesc = enterpriseDesc;
    }

    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }

    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo;
    }

    public String getPostionTypeID() {
        return postionTypeID;
    }

    public void setPostionTypeID(String postionTypeID) {
        this.postionTypeID = postionTypeID;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
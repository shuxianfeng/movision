package com.zhuhuibao.mybatis.project.form;

import com.zhuhuibao.mybatis.project.entity.ProjectClues;

/**
 * 项目线索form
 *
 * @author liyang
 * @date 2016年10月28日
 */
public class ProjectCluesForm {

    /**
     * 项目线索实体
     */
    private ProjectClues projectClues;

    /**
     * 状态
     */
    private String statusText;

    /**
     * 提交时间
     */
    private String addTimeStr;

    /**
     * 审核人
     */
    private String username;

    public ProjectClues getProjectClues() {
        return projectClues;
    }

    public void setProjectClues(ProjectClues projectClues) {
        this.projectClues = projectClues;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public void setAddTimeStr(String addTimeStr) {
        this.addTimeStr = addTimeStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.zhuhuibao.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config {

    @Value("${file.template.path}")
    private String fileTemplatePath = "";

    public String getFileTemplatePath() {
        return fileTemplatePath;
    }

    public void setFileTemplatePath(String fileTemplatePath) {
        this.fileTemplatePath = fileTemplatePath;
    }

}

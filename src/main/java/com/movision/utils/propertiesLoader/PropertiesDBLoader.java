package com.movision.utils.propertiesLoader;

import com.movision.mybatis.systemLayout.service.SystemLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2018/1/31 14:48
 * 获取系统配置参数公共方法
 */
@Service
public class PropertiesDBLoader {

    @Autowired
    private SystemLayoutService systemLayoutService;

    public String getValue(String key){
        return systemLayoutService.getValue(key);
    }
}

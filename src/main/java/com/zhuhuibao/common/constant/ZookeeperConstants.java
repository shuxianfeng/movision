package com.zhuhuibao.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 接口平台调用参数
 * Created by cxx
 */
@Component
public class ZookeeperConstants {
    @Value("${zookeeper_hosts}")
    private  String hosts;
    @Value("${zookeeper_session_timeout}")
    private int timeout;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

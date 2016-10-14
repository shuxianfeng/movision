package com.zhuhuibao.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.service.AgentService;

/**
 * 代理商业务处理相关接口实现类
 *
 * @author liyang
 * @date 2016年10月13日
 */
@Service
@Transactional
public class MobileAgentService {

    private static final Logger log = LoggerFactory.getLogger(MobileAgentService.class);

    @Autowired
    private AgentService agentService;

    /**
     * 根据产品id获取代理商信息
     * 
     * @param id
     *            产品id
     * @return
     */
    public Map getAgentByProId(String id) {
        return agentService.getAgentByProId(id);
    }

}

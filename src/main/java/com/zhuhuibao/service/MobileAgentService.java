package com.zhuhuibao.service;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.pojo.AgentBean;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
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
     * @param id 产品id
     * @return
     */
    public Map getAgentByProId(String id) {
        return agentService.getAgentById(id);
    }

    /**
     * 根据会员id查询代理商
     *
     * @param memberId
     * @return
     */
    public List<AgentBean> getMyAgentListByMemId(Long memberId) {
        try {
            return agentService.findAgentByMemId(String.valueOf(memberId));
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 根据品牌id获取代理商信息
     *
     * @param id
     * @return
     */
    public Map getAgentByBrandid(String id) {
        return agentService.getAgentByProId(id);
    }
}

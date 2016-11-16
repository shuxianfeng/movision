package com.zhuhuibao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.MessageConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.MessageLogConstant;
import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 消息Service（系统信息、留言信息）
 *
 * @author tongxinglong
 * @since 2016/10/12
 */
@Service
@Transactional
public class MobileMessageService {
    private static Logger log = LoggerFactory.getLogger(MobileMessageService.class);

    @Autowired
    private SiteMailService siteMailService;

    /**
     * 查询当前登录人系统消息
     * 
     * @param pager
     * @param map
     * @return
     */
    public List<Map<String, Object>> getSysMsgList(Paging<Map<String, Object>> pager, Map<String, Object> map) {

        return siteMailService.findAllNewsList(pager, map);
    }

    /**
     * 查看系统消息详情
     * 
     * @param id
     * @return
     */
    public Map<String, Object> getSysMsgDetail(Long memberId, String id) {
        MessageLog messageLog = new MessageLog();
        messageLog.setRecID(memberId);
        messageLog.setMessageID(Long.parseLong(id));
        messageLog.setStatus(MessageLogConstant.NEWS_STATUS_TWO);
        siteMailService.updateStatus(messageLog);

        return siteMailService.queryNewsById(id);
    }

    /**
     * 查询当前登录人收到的留言信息
     * 
     * @param pager
     * @param memberId
     * @return
     */
    public List<Map<String, String>> getReceiveMsgList(Paging<Map<String, String>> pager, Long memberId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", memberId);

            return siteMailService.findAllMyReceiveMsgList(pager, map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    /**
     * 查看收到的留言详情
     * 
     * @param id
     * @return
     */
    public Map<String, String> getReceiveMsgDetail(String id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(MessageConstant.Status.YD.toString());
        siteMailService.updateMessage(message);

        return siteMailService.queryMyReceiveMsgById(id);
    }
}

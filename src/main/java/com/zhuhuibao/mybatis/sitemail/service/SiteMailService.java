package com.zhuhuibao.mybatis.sitemail.service;

import com.zhuhuibao.common.pojo.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.mapper.MessageLogMapper;
import com.zhuhuibao.mybatis.sitemail.mapper.MessageTextMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/21 0021.
 * 站内信处理类
 */
@Transactional
@Service
public class SiteMailService {
    private static Logger log = LoggerFactory.getLogger(SiteMailService.class);

    @Autowired
    MessageLogMapper msgLogMapper;

    @Autowired
    MessageTextMapper msgTextMapper;

    /**
     * 插入站内信信息
     * @param siteMail  站内信信息
     * @return
     */
    public JsonResult addSiteMail(MessageText siteMail)
    {
        JsonResult jsonResult  = new JsonResult();
        try
        {

            msgTextMapper.insertSelective(siteMail);
            MessageLog msgLog = new MessageLog();
            msgLog.setRecID(siteMail.getRecID());
            msgLog.setMessageID(siteMail.getId());
            msgLog.setStatus(1);
            msgLogMapper.insertSelective(msgLog);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 站内信查询未读的消息数目
     * @param map recID:消息接收者Id;type:站内信消息类型；status：状态：未读(1)
     * @return 未读消息数目
     */
    public Integer queryUnreadMsgCount(Map<String,Object> map)
    {
        log.info("query unread message count receiveid = "+map.get("recID")+" type = "+map.get("type")+" status = "+map.get("status"));
        Integer count = 0;
        try
        {
            count = msgLogMapper.queryUnreadMsgCount(map);
        }catch(Exception e)
        {
            throw e;
        }
        return count;
    }

}

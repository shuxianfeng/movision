package com.zhuhuibao.mybatis.sitemail.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MessageMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.mapper.NoticeMapper;
import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.mapper.MessageLogMapper;
import com.zhuhuibao.mybatis.sitemail.mapper.MessageTextMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    NoticeMapper noticeMapper;

    /**
     * 插入站内信信息
     *
     * @param siteMail 站内信信息
     * @return
     */
    public Response addSiteMail(MessageText siteMail) throws Exception {
        Response response = new Response();
        try {
            msgTextMapper.insertSelective(siteMail);
            MessageLog msgLog = new MessageLog();
            msgLog.setRecID(siteMail.getRecID());
            msgLog.setMessageID(siteMail.getId());
            msgLog.setStatus(1);
            msgLogMapper.insertSelective(msgLog);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
        }
        return response;
    }

    /**
     * 插入拒绝理由站内信
     *
     * @param sendId 发送者ID
     * @param recId  接收者ID
     * @param reason 拒绝理由
     * @return
     */
    public Response addRefuseReasonMail(Long sendId, Long recId, String reason, String source, String title) {
        Response response = new Response();
        try {
            MessageText msgText = new MessageText();
            msgText.setType(Constants.SITE_MAIL_TYPE_THREE);	//站内信类型：3
            msgText.setMessageText(reason);
            //审核对象
            msgText.setSource(source);
            msgText.setTitle(title);
            msgText.setSendID(sendId);
            msgText.setRecID(recId);
            this.addSiteMail(msgText);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
        }
        return response;
    }

    public Integer queryUnreadMsgCount(Map<String, Object> map) {
        log.info("query unread message count receiveid = " + map.get("recID") + " type = " + map.get("type") + " status = " + map.get("status"));
        Integer count = 0;
        try {
            count = msgLogMapper.queryUnreadMsgCount(map);
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    public List<Map<String, Object>> findAllNewsList(Paging<Map<String, Object>> pager, Map<String, Object> map) {
        try {
            return msgTextMapper.findAllNewsList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public int addNewsToLog(Map<String, Object> map) {
        try {
            return msgLogMapper.addNewsToLog(map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public int updateNewsStatus(MessageLog messageLog) {
        try {
            return msgLogMapper.updateByPrimaryKeySelective(messageLog);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public int updateStatus(MessageLog messageLog) {
        try {
            return msgLogMapper.updateNewsStatus(messageLog);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Integer selUnreadNewsCount(Map<String, Object> map) {
        try {
            return msgLogMapper.selUnreadNewsCount(map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<Map<String, String>> findAllMySendMsgList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return messageMapper.findAllMySendMsgList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<Map<String, String>> findAllMyReceiveMsgList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return messageMapper.findAllMyReceiveMsgList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public int updateMessage(Message message) {
        try {
            return messageMapper.updateMessage(message);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Map<String, Object> queryNewsById(String id) {
        try {
            return msgLogMapper.queryNewsById(id);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Map<String, String> queryMySendMsgById(String id) {
        try {
            return messageMapper.queryMySendMsgById(id);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Map<String, String> queryMyReceiveMsgById(String id) {
        try {
            return messageMapper.queryMyReceiveMsgById(id);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<Map<String, String>> findMember(Map<String, Object> map) {
        try {
            return memberMapper.findMemberOms(map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public Long addMsgText(MessageText messageText) {
        try {
            msgTextMapper.insertSelective(messageText);
            return messageText.getId();
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public int addMsgLog(MessageLog messageLog) {
        try {
            return msgLogMapper.insertSelective(messageLog);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<Map<String, String>> findAllNoticeList(Paging<Map<String, String>> pager) {
        try {
            return noticeMapper.findAllNoticeList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
    }

    public Map<String, String> queryNoticeById(String id) {
        try {
            return noticeMapper.queryNoticeById(id);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
    }

    public int selectUnreadMessageCount(Long id) {
        int count;
        try {
            count =  noticeMapper.selectUnreadMessageCount(id);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return count;
    }
}

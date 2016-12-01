package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.entity.Messages;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MessageMapper {
    int saveMessage(Message message);

    List<Map<String,String>> findAllMySendMsgList(RowBounds rowBounds,Map<String,Object> map);

    List<Map<String,String>> findAllMyReceiveMsgList(RowBounds rowBounds,Map<String,Object> map);

    int updateMessage(Message message);

    Map<String,String> queryMySendMsgById(String id);

    Map<String,String> queryMyReceiveMsgById(String id);

    int saveMessages(Messages message);
}
package com.movision.facade.msgCenter;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 消息中心
 *
 * @Author zhuangyuhao
 * @Date 2017/4/5 13:48
 */
@Service
public class MsgCenterFacade {

    private static Logger log = LoggerFactory.getLogger(MsgCenterFacade.class);


    /**
     * 获取消息中心的列表
     *
     * @return
     */
    public Map getMsgCenterList() {
        //都是查找最新的消息
        Map reMap = new HashedMap();
        //1 赞消息 。包含：帖子，活动，评论，快问（后期）

        //2 打赏消息

        //3 评论消息

        //4 系统通知

        //5 打招呼消息

        //6 客服消息

        return reMap;
    }
}

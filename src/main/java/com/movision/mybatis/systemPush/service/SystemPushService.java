package com.movision.mybatis.systemPush.service;

import com.movision.mybatis.systemPush.entity.SystemPush;
import com.movision.mybatis.systemPush.mapper.SystemPushMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/3/16 16:30
 */
@Service
@Transactional
public class SystemPushService {
    private static Logger log = LoggerFactory.getLogger(SystemPushService.class);

    @Autowired
    private SystemPushMapper systemPushMapper;

    /**
     * 查询列表
     *
     * @param pager
     * @return
     */
    public List<SystemPush> findAllSystemPush(Paging<SystemPush> pager) {
        try {
            log.info("查询列表");
            return systemPushMapper.findAllSystemPush(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询列表失败", e);
            throw e;
        }

    }

    /**
     * 消息条件搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<SystemPush> findAllPushCondition(Map map, Paging<SystemPush> pager) {
        try {
            log.info("消息条件搜索");
            return systemPushMapper.findAllPushCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("消息条件搜索");
            throw e;
        }
    }

    /**
     * 查询消息内容
     *
     * @param id
     * @return
     */
    public SystemPush queryPushBody(Integer id) {
        try {
            log.info("查询消息内容");
            return systemPushMapper.queryPushBody(id);
        } catch (Exception e) {
            log.error("查询消息内容失败", e);
            throw e;
        }
    }

    /**
     * 删除消息
     *
     * @param id
     * @return
     */
    public Integer deleteSystemPush(Integer id) {
        try {
            log.info("删除消息");
            return systemPushMapper.deleteSystemPush(id);
        } catch (Exception e) {
            log.error("删除消息失败", e);
            throw e;
        }
    }

    /**
     * 增加消息推送
     *
     * @param systemPush
     * @return
     */
    public Integer addPush(SystemPush systemPush) {
        try {
            log.info("增加消息推送");
            return systemPushMapper.addPush(systemPush);
        } catch (Exception e) {
            log.error("增加消息推送失败", e);
            throw e;
        }
    }

    /**
     * 查询所有电话
     *
     * @return
     */
    public List<String> findAllPhone() {
        try {
            log.info("查询所有电话");
            return systemPushMapper.findAllPhone();
        } catch (Exception e) {
            log.error("查询所有电话失败", e);
            throw e;
        }
    }

    /**
     * 查询所有电话
     *
     * @return
     */
    public List<String> findPhone(Integer pageNo, Integer pageSize) {
        try {
            log.info("查询所有电话");
            return systemPushMapper.findPhone(pageNo, pageSize);
        } catch (Exception e) {
            log.error("查询所有电话失败", e);
            throw e;
        }
    }
}

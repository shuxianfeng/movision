package com.movision.mybatis.systemToPush.service;

import com.movision.mybatis.systemPush.service.SystemPushService;
import com.movision.mybatis.systemToPush.entity.SystemToPush;
import com.movision.mybatis.systemToPush.mapper.SystemToPushMapper;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/3/22 19:53
 */
@Service
@Transactional
public class SystemToPushService {
    private static Logger log = LoggerFactory.getLogger(SystemPushService.class);

    @Autowired
    private SystemToPushMapper systemToPushMapper;


    /**
     * 查询系统推送
     *
     * @param pager
     * @return
     */
    public List<SystemToPush> findAllSystemToPush(Paging<SystemToPush> pager) {
        try {
            log.info("查询系统推送");
            return systemToPushMapper.findAllSystemToPush(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询系统推送失败", e);
            throw e;
        }
    }

    /**
     * 条件查询
     *
     * @param map
     * @param pager
     * @return
     */
    public List<SystemToPush> findAllSystenToPushCondition(Map map, Paging<SystemToPush> pager) {
        try {
            log.info("条件查询");
            return systemToPushMapper.findAllSystemCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询失败", e);
            throw e;
        }
    }

    /**
     * 根据id查询系统推送的内容
     *
     * @param id
     * @return
     */
    public SystemToPush querySystemToPushBody(Integer id) {
        try {
            log.info("根据id查询系统推送的内容");
            return systemToPushMapper.querySystemToPushBody(id);
        } catch (Exception e) {
            log.error("根据id查询系统推送的内容失败", e);
            throw e;
        }
    }

    /**
     * 删除系统推送
     *
     * @param id
     * @return
     */
    public Integer deleteSystemToPush(Integer id) {
        try {
            log.info("删除系统推送");
            return systemToPushMapper.deleteSystemToPush(id);
        } catch (Exception e) {
            log.error("删除系统推送失败", e);
            throw e;
        }
    }

    /**
     * 增加系统推送
     *
     * @param systemToPush
     * @return
     */
    public Integer addSystemToPush(SystemToPush systemToPush) {
        try {
            log.info("增加系统推送");
            return systemToPushMapper.addSystemToPush(systemToPush);
        } catch (Exception e) {
            log.error("增加系统推送失败", e);
            throw e;
        }
    }

}

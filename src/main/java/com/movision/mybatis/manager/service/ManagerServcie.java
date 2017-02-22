package com.movision.mybatis.manager.service;

import com.movision.mybatis.manager.mapper.ManagerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/22 9:45
 */
@Service
public class ManagerServcie {

    Logger logger = LoggerFactory.getLogger(ManagerServcie.class);
    @Autowired
    ManagerMapper managerMapper;

    /**
     * 删除圈子所用管理员
     *
     * @param circleid
     * @return
     */
    public int deleteManagerToCircleid(Integer circleid) {
        try {
            logger.info("删除圈子所用管理员");
            return managerMapper.deleteManagerToCircleid(circleid);
        } catch (Exception e) {
            logger.error("删除圈子所用管理员异常");
            throw e;
        }
    }

    /**
     * 添加圈子所用管理员
     *
     * @param map
     * @return
     */
    public int addManagerToCircleAndUserid(Map<String, Integer> map) {
        try {
            logger.info("添加圈子所用管理员");
            return managerMapper.addManagerToCircleAndUserid(map);
        } catch (Exception e) {
            logger.error("添加圈子所用管理员");
            throw e;
        }
    }
}

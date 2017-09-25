package com.movision.mybatis.robotNickname.service;

import com.movision.mybatis.robotNickname.mapper.RobotNicknameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/9/25 13:50
 */
@Service
public class RobotNicknameService {

    private static Logger log = LoggerFactory.getLogger(RobotNicknameService.class);

    @Autowired
    private RobotNicknameMapper robotNicknameMapper;

    /**
     * 随机查询机器人昵称
     *
     * @param number
     * @return
     */
    public List<String> queryRoboltNickname(Integer number) {
        try {
            log.info("随机查询机器人昵称");
            return robotNicknameMapper.queryRoboltNickname(number);
        } catch (Exception e) {
            log.error("随机查询机器人昵称异常", e);
            throw e;
        }
    }
}

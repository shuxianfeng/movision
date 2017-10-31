package com.movision.mybatis.robotOperationJob.service;

import com.movision.mybatis.robotOperationJob.entity.RobotOperationJob;
import com.movision.mybatis.robotOperationJob.mapper.RobotOperationJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/10/31 17:30
 */
@Service
public class RobotOperationJobService {
    static private Logger log = LoggerFactory.getLogger(RobotOperationJobService.class);

    @Autowired
    private RobotOperationJobMapper robotOperationJobMapper;

    public int add(RobotOperationJob robotOperationJob) {
        try {
            log.info("新增机器人处理任务");
            return robotOperationJobMapper.insertSelective(robotOperationJob);
        } catch (Exception e) {
            log.error("新增机器人处理任务失败", e);
            throw e;
        }
    }

    public RobotOperationJob selectCurrentPostidBatch(int postid) {
        try {
            log.info("查询最大的批次的指定帖子");
            return robotOperationJobMapper.selectCurrentPostidBatch(postid);
        } catch (Exception e) {
            log.error("查询最大的批次的指定帖子失败", e);
            throw e;
        }
    }


}

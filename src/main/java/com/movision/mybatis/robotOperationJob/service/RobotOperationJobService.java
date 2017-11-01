package com.movision.mybatis.robotOperationJob.service;

import com.movision.mybatis.robotOperationJob.entity.RobotOperationJob;
import com.movision.mybatis.robotOperationJob.entity.RobotOperationJobPage;
import com.movision.mybatis.robotOperationJob.mapper.RobotOperationJobMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public RobotOperationJob selectCurrentPostidBatch(Map map) {
        try {
            log.info("根据帖子id查询最大的批次的指定帖子");
            return robotOperationJobMapper.selectCurrentPostidBatch(map);
        } catch (Exception e) {
            log.error("根据帖子id查询最大的批次的指定帖子失败", e);
            throw e;
        }
    }

    public RobotOperationJob selectCurrentUseridBatch(Map map) {
        try {
            log.info("根据用户id查询最大的批次的指定帖子");
            return robotOperationJobMapper.selectCurrentUseridBatch(map);
        } catch (Exception e) {
            log.error("根据用户id查询最大的批次的指定帖子失败", e);
            throw e;
        }
    }

    public List<RobotOperationJobPage> findAllRobotJobPage(Map map, Paging<RobotOperationJobPage> pagePaging) {
        try {
            log.info("查询机器人任务分页");
            return robotOperationJobMapper.findAllRobotJobPage(map, pagePaging.getRowBounds());
        } catch (Exception e) {
            log.error("查询机器人任务分页失败", e);
            throw e;
        }
    }

}

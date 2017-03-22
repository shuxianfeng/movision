package com.movision.mybatis.record.service;

import com.movision.mybatis.record.entity.RecordVo;
import com.movision.mybatis.record.mapper.RecordMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/3 10:39
 */
@Service
public class RecordService {

    Logger logger = LoggerFactory.getLogger(RecordService.class);

    @Autowired
    private RecordMapper recordMapper;

    /**
     * 查询用户积分流水列表
     *
     * @param pager
     * @return
     */
    public List<RecordVo> queryIntegralList(String userid, Paging<RecordVo> pager) {
        try {
            logger.info("查询用户积分流水列表");
            return recordMapper.queryIntegralList(userid, pager.getRowBounds());
        } catch (Exception e) {
            logger.error("查询用户积分流水列表异常");
            throw e;
        }
    }

    /**
     * 添加用户积分操作流水
     *
     * @param map
     * @return
     */
    public Integer addIntegralRecord(Map map) {
        try {
            logger.info("添加用户积分操作流水");
            return recordMapper.addIntegralRecord(map);
        } catch (Exception e) {
            logger.error("添加用户积分操作流水异常");
            throw e;
        }
    }


    /**
     * 增加积分流水记录
     *
     * @param map
     * @return
     */
    public int insertRewardRecord(Map map) {
        try {
            logger.info("增加积分流水记录");
            return recordMapper.insertRewardRecord(map);
        } catch (Exception e) {
            logger.error("增加积分流水记录异常");
            throw e;
        }
    }
}

package com.movision.mybatis.record.service;

import com.movision.mybatis.record.entity.RecordVo;
import com.movision.mybatis.record.mapper.RecordMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

package com.movision.mybatis.pointRecord.service;

import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.mapper.PointRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/28 19:59
 */
@Service
@Transactional
public class PointRecordService {

    private static Logger log = LoggerFactory.getLogger(PointRecordService.class);

    @Autowired
    private PointRecordMapper pointRecordMapper;

    public int addPointRecord(PointRecord pointRecord) {
        try {
            log.info("增加积分记录");
            return pointRecordMapper.insertSelective(pointRecord);
        } catch (Exception e) {
            log.error("增加积分记录失败", e);
            throw e;
        }
    }

    public List<PointRecord> queryAllMyPointRecord(int userid) {
        try {
            log.info("查询我的所有积分记录");
            return pointRecordMapper.queryMyAllTypePoint(userid);
        } catch (Exception e) {
            log.error("查询我的所有积分记录失败", e);
            throw e;
        }
    }

    public List<PointRecord> queryMyTodayPoint(int userid) {
        try {
            log.info("查询今日的我的积分记录");
            return pointRecordMapper.queryMyTodayPoint(userid);
        } catch (Exception e) {
            log.error("查询今日的我的积分记录失败", e);
            throw e;
        }
    }


}

package com.movision.mybatis.imLoginRecord.service;

import com.movision.mybatis.imLoginRecord.entity.ImLoginRecord;
import com.movision.mybatis.imLoginRecord.mapper.ImLoginRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/10 15:47
 */
@Service
public class ImLoginRecordService {

    public static final Logger log = LoggerFactory.getLogger(ImLoginRecordService.class);

    @Autowired
    private ImLoginRecordMapper imLoginRecordMapper;

    public ImLoginRecord queryRecordByaccidAndTimestamp(ImLoginRecord record) {
        try {
            log.info("查询指定accid和时间戳的登录记录");
            return imLoginRecordMapper.queryRecordByaccidAndTimestamp(record);
        } catch (Exception e) {
            log.error("查询指定accid和时间戳的登录记录失败", e);
            throw e;
        }
    }

    public int addRecord(ImLoginRecord imLoginRecord) {
        try {
            log.info("插入im登录事件记录");
            return imLoginRecordMapper.insertSelective(imLoginRecord);
        } catch (Exception e) {
            log.error("插入im登录事件记录失败", e);
            throw e;
        }
    }
}

package com.movision.mybatis.postHeatvalueRecord.service;

import com.movision.mybatis.postHeatvalueRecord.entity.PostHeatvalueRecord;
import com.movision.mybatis.postHeatvalueRecord.mapper.PostHeatvalueRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author zhuangyuhao
 * @Date 2017/10/24 15:37
 */
@Service
public class PostHeatvalueRecordService {

    private static Logger log = LoggerFactory.getLogger(PostHeatvalueRecordService.class);

    @Autowired
    private PostHeatvalueRecordMapper postHeatvalueRecordMapper;

    public void add(PostHeatvalueRecord postHeatvalueRecord) {
        try {
            log.info("新增帖子热度流水");
            postHeatvalueRecordMapper.insertSelective(postHeatvalueRecord);
        } catch (Exception e) {
            log.error("新增帖子热度流水失败", e);
            throw e;
        }
    }
}

package com.movision.mybatis.pointRecord.service;

import com.movision.mybatis.pointRecord.entity.PointRecord;
import com.movision.mybatis.pointRecord.mapper.PointRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}

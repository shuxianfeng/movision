package com.movision.mybatis.complainsrecords.service;


import com.movision.mybatis.complainsrecords.entity.Complainsrecords;
import com.movision.mybatis.complainsrecords.mapper.ComplainsrecordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhanglei
 * @Date 2017/8/29 15:46
 */
@Service
@Transactional
public class ComplainsrecordsService {
    private static Logger log = LoggerFactory.getLogger(ComplainsrecordsService.class);
    @Autowired
    private ComplainsrecordsMapper complainsrecordsMapper;

    public int insertSelectiveCom(Complainsrecords complainsrecords) {
        try {
            log.info("插入投诉记录");
            return complainsrecordsMapper.insertSelective(complainsrecords);
        } catch (Exception e) {
            log.error("插入投诉记录失败");
            throw e;
        }
    }
}

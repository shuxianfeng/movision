package com.zhuhuibao.mybatis.oms.service;/**
 * @author Administrator
 * @version 2016/5/13 0013
 */

import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import com.zhuhuibao.mybatis.oms.mapper.TenderTonedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *招中标公告管理业务处理类
 *@author pl
 *@create 2016/5/13 0013
 **/
public class TenderTonedService {

    private final static Logger log = LoggerFactory.getLogger(TenderTonedService.class);

    private TenderTonedMapper tenderToned;

    /**
     * 插入招中标公共信息
     * @param tt 招中标公告
     * @return
     */
    public int insertTenderTone(TenderToned tt)
    {
        log.info("insert tender tone "+tt.toString());
        int result = 0;
        try {
            result = tenderToned.insertSelective(tt);
        }catch(Exception e)
        {
            log.error("insert into tender tone info error!");
            throw e;
        }
        return result;
    }

    /**
     * 插入招中标公共信息
     * @param tt 招中标公告
     * @return
     */
    public int updateTenderTone(TenderToned tt)
    {
        log.info("update tender tone "+tt.toString());
        int result = 0;
        try {
            result = tenderToned.updateByPrimaryKeySelective(tt);
        }catch(Exception e)
        {
            log.error("update tender tone info error!");
            throw e;
        }
        return result;
    }
}

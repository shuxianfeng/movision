package com.zhuhuibao.mybatis.oms.service;/**
 * @author Administrator
 * @version 2016/5/13 0013
 */

import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import com.zhuhuibao.mybatis.oms.mapper.TenderTonedMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *招中标公告管理业务处理类
 *@author pl
 *@create 2016/5/13 0013
 **/
@Service
public class TenderTonedService {

    private final static Logger log = LoggerFactory.getLogger(TenderTonedService.class);

    @Autowired
    private TenderTonedMapper tenderTonedMapper;

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
            result = tenderTonedMapper.insertSelective(tt);
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
            result = tenderTonedMapper.updateByPrimaryKeySelective(tt);
        }catch(Exception e)
        {
            log.error("update tender tone info error!");
            throw e;
        }
        return result;
    }

    /**
     * 预览招中标公共信息
     * @param id 招中标公告ID
     * @return
     */
    public TenderToned queryTenderToneByID(Long id)
    {
        log.info("preview tender tone info id = "+id.toString());
        TenderToned tt = null;
        try {
            tt = tenderTonedMapper.selectByPrimaryKey(id);
        }catch(Exception e)
        {
            log.error("preview tender tone info error!");
            throw e;
        }
        return tt;
    }

    /**
     * 根据条件查询分页信息
     * @param map 招中标公告查询条件
     * @return
     */
    public List<TenderToned> findAllTenderTonedPager(Map<String,Object> map, Paging<TenderToned> page)
    {
        log.info("search tender tone info for pager condition = "+ StringUtils.mapToString(map));
        List<TenderToned> ttList = null;
        try {
            ttList = tenderTonedMapper.findAllTenderTonedPager(map,page.getRowBounds());
        }catch(Exception e)
        {
            log.error("search tender tone info for pager error!");
            throw e;
        }
        return ttList;
    }
}

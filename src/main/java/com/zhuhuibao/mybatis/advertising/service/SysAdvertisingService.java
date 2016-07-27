package com.zhuhuibao.mybatis.advertising.service;

import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.mapper.SysAdvertisingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianglz
 * @since 16/6/20.
 */
@Service
public class SysAdvertisingService {

    private static final Logger log = LoggerFactory.getLogger(SysAdvertisingService.class);


    @Autowired
    private SysAdvertisingMapper mapper;

    /**
     * 根据条件查询广告信息
     * @param chanType 频道类型 1:平台主站  2：工程商 ，3：商城，4：项目，5：威客，6：人才，7：会展，8：技术，9：专家
     * @param page     频道子页面 index:首页
     * @param advArea  广告所在页面区域 F1:一楼层
     * @return  List<SysAdvertising>
     */
    @Cacheable(value = "advCache",key = "#chanType+'_'+#page+'_'+#advArea")
    public List<SysAdvertising> findListByCondition(String chanType, String page, String advArea) {
        List<SysAdvertising> list;
        try{
            list = mapper.findListByCondition(chanType,page,advArea);
        }catch (Exception  e){
            log.error("数据库操作失败:{}",e.getMessage());
            throw e;
        }

        return list;
    }
}

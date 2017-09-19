package com.movision.mybatis.weixinlist.service;

import com.movision.mybatis.weixinguangzhu.service.WeixinGuangzhuService;
import com.movision.mybatis.weixinlist.entity.WeixinList;
import com.movision.mybatis.weixinlist.mapper.WeixinListMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/8/10 19:48
 */
@Service
public class WeixinListService {
    @Autowired
    private WeixinListMapper weixinListMapper;
    private static Logger log = LoggerFactory.getLogger(WeixinListService.class);


    public int insertSelective(WeixinList weixinList) {
        try {
            log.info("插入列表");
            return weixinListMapper.insertSelective(weixinList);
        } catch (Exception e) {
            log.error("插入列表失败", e);
            throw e;
        }
    }

    public List<WeixinList> findAllList(Paging<WeixinList> paging) {
        try {
            log.info("差列表");
            return weixinListMapper.findAllList(paging.getRowBounds());
        } catch (Exception e) {
            log.error("差列表失败", e);
            throw e;
        }
    }

}

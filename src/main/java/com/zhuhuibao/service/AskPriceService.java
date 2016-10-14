package com.zhuhuibao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;

/**
 * 询价相关接口实现类
 *
 * @author liyang
 * @date 2016年10月13日
 */
@Service
@Transactional
public class AskPriceService {

    private static final Logger log = LoggerFactory.getLogger(AskPriceService.class);

    @Autowired
    private AskPriceMapper askPriceMapper;

    /**
     * 查询最新公开询价
     * 
     * @param count
     *            查询条数
     * @param createId
     *            询价提出的创建者id
     * @return
     */
    public List queryNewestAskPrice(int count, String createId) {
        List<AskPrice> askPriceList = askPriceMapper.queryNewPriceInfo(count, createId);
        List list = new ArrayList();
        Map map;
        for (AskPrice askPrice : askPriceList) {
            map = new HashMap();
            map.put(Constants.id, askPrice.getId());
            map.put(Constants.companyName, askPrice.getTitle());
            list.add(map);
        }
        return list;
    }

}

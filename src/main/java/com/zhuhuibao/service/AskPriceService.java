package com.zhuhuibao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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

    @Autowired
    private PriceService priceService;

    /**
     * 查询最新公开询价
     *
     * @param count    查询条数
     * @param createId 询价提出的创建者id
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

    /**
     * 根据条件分页查询询价信息
     *
     * @param askPriceSearch 查询条件
     * @param pager          分页对象
     * @return
     */
    public Paging<AskPriceResultBean> selEnquiryList(AskPriceSearchBean askPriceSearch, Paging<AskPriceResultBean> pager) {
        if (askPriceSearch.getTitle() != null) {
            if (askPriceSearch.getTitle().contains("_")) {
                askPriceSearch.setTitle(askPriceSearch.getTitle().replace("_", "\\_"));
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        askPriceSearch.setCreateid(principal.getId().toString());
        askPriceSearch.setEndTime(askPriceSearch.getEndTime() + " 23:59:59");
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager, askPriceSearch);
        pager.result(askPriceList);
        return pager;
    }

    /**
     * 查看具体某一条询价信息
     *
     * @param id
     * @return
     */
    public AskPriceBean queryAskPriceByID(String id) {
        return priceService.queryAskPriceByID(id);
    }
}

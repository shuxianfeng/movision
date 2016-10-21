package com.zhuhuibao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 询报价service
 * 
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@Transactional
@Service
public class MobileEnquiryService {

    @Autowired
    private PriceService priceService;

    @Autowired
    private OfferPriceService offerPriceService;

    @Autowired
    private AskPriceMapper askPriceMapper;

    /**
     * 查询询价列表
     * 
     * @param askPriceSearch
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<AskPriceResultBean> getEnquiryList(AskPriceSearchBean askPriceSearch, String pageNo, String pageSize) {
        Paging<AskPriceResultBean> enquiryPager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (askPriceSearch.getTitle() != null) {
            if (askPriceSearch.getTitle().contains("_")) {
                askPriceSearch.setTitle(askPriceSearch.getTitle().replace("_", "\\_"));
            }
        }

        askPriceSearch.setEndTime(askPriceSearch.getEndTime() + " 23:59:59");

        List<AskPriceResultBean> askPriceList = priceService.findAllEnquiryList(enquiryPager, askPriceSearch);
        enquiryPager.result(askPriceList);

        return enquiryPager;
    }

    /**
     * 查询收到的询价单
     * 
     * @param pager
     * @param price
     * @return
     */
    public List<AskPriceSimpleBean> getReceivedEnquiryList(Paging<AskPriceSimpleBean> pager, AskPrice price) {

        return offerPriceService.findAllAskingPriceInfo(pager, price);
    }

    /**
     * 根据askID、memberID查询询价信息
     * 
     * @param askId
     * @param memberId
     * @return
     */
    public AskPriceBean getAskPriceByAskidMemId(Long askId, Long memberId) {
        return priceService.findAskPriceByAskidMemId(askId, memberId);
    }

    /**
     * 根据ID查询询价信息
     * 
     * @param askId
     * @return
     */
    public AskPriceBean getAskPriceById(Long askId) {
        return priceService.findAskPriceById(askId);
    }

    /**
     * 根据ID查询报价信息
     * 
     * @param offerId
     * @return
     */
    public OfferPrice getOfferPriceById(Long offerId) {
        return offerPriceService.getOfferPriceByID(offerId);
    }

    /**
     * 根据询价ID查询报价信息
     * 
     * @param askId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, Object>> getOfferListByAskId(Long askId, String pageNo, String pageSize) {
        Paging<Map<String, Object>> offerPager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, Object>> offerList = offerPriceService.findAllOfferPriceByAskId(offerPager, askId);

        offerPager.result(offerList);

        return offerPager;
    }

    /**
     * 查询发送的报价信息
     * 
     * @param memberId
     * @param title
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<AskPriceSimpleBean> getSentOfferList(Long memberId, String title, String startDate, String endDate, String pageNo, String pageSize) {
        Paging<AskPriceSimpleBean> offerPager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, String> priceMap = MapUtil.convert2HashMap("startDate", startDate, "endDate", endDate, "createid", String.valueOf(memberId));
        if (title != null && !title.equals("")) {
            priceMap.put("title", title.replace("_", "\\_"));
        }
        List<AskPriceSimpleBean> priceList = offerPriceService.findAllOfferedPriceInfo(offerPager, priceMap);

        offerPager.result(priceList);
        return offerPager;
    }

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

    /**
     * 根据条件分页查询询价信息
     *
     * @param askPriceSearch
     *            查询条件
     * @param pager
     *            分页对象
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

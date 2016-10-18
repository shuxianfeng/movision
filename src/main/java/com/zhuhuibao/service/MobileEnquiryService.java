package com.zhuhuibao.service;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
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
     * 根据askID、memberID查询询价信息
     * 
     * @param askId
     * @param memberId
     * @return
     */
    public AskPriceBean getAskPriceById(Long askId, Long memberId) {
        return priceService.findAskPriceById(askId, memberId);
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
}

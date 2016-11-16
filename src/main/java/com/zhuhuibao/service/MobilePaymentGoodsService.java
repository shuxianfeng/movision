package com.zhuhuibao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;

/**
 * 触屏端付费商品相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobilePaymentGoodsService {

    private static final Logger log = LoggerFactory.getLogger(MobilePaymentGoodsService.class);

    @Autowired
    private PaymentGoodsService paymentGoodsService;

    /**
     * 插入我查看过的项目信息
     * 
     * @param goodsId
     *            商品ID
     * @param createId
     *            查看人的Id
     * @Param companyId 企业ID
     * @Param type 商品类型
     * @throws Exception
     */
    public int insertViewGoods(Long goodsId, Long createId, Long companyId, String type) {
        return paymentGoodsService.insertViewGoods(goodsId, createId, companyId, type);
    }
}

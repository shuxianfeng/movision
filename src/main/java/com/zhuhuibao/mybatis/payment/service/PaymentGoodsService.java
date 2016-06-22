package com.zhuhuibao.mybatis.payment.service;

import com.zhuhuibao.mybatis.payment.entity.PaymentGoods;
import com.zhuhuibao.mybatis.payment.mapper.PaymentGoodsMapper;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.zhuhuibao.common.constant.ZhbPaymentConstant.goodsType.*;

/**
 * 查看过的商品信息
 *
 * @author pl
 * @version 2016/6/16 0016
 */
@Service
@Transactional
public class PaymentGoodsService {
    private final static Logger log = LoggerFactory.getLogger(PaymentGoodsService.class);

    @Autowired
    PaymentGoodsMapper paymentMapper;

    /**
     * 插入我查看过的项目信息
     * @param goodsId  商品ID
     * @param createId  查看人的Id
     * @Param companyId 企业ID
     * @Param type 商品类型
     * @throws Exception
     */
    public int insertViewGoods(Long goodsId, Long createId, Long companyId, String type){
        log.info("insert view goodsId = "+goodsId+" createId = "+createId+" type = "+type);
        int result = 0;
        try {
            if(CXXZJL.toString().equals(type) || CKXMXX.toString().equals(type) || CKWKRW.equals(type)
                    || CKJSCG.equals(type) || CKZJJSCG.equals(type) || CKZJXX.equals(type)) {
                PaymentGoods vp = new PaymentGoods();
                vp.setGoodsId(goodsId);
                vp.setViewerId(createId);
                vp.setCompanyId(companyId);
                vp.setType(type);
                result = paymentMapper.insertSelective(vp);
            }
        }catch(Exception e)
        {
            log.error("insert view goodsId error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 是否存在查看过商品信息
     * @param map 查询条件
     * @return  0:未查看过，1：已
     */
    public int checkIsViewGoods(Map<String,Object> map)
    {
        log.info("check isview goods "+ StringUtils.mapToString(map));
        int isExist;
        try {
            isExist = paymentMapper.checkIsViewGoods(map);
        }catch(Exception e)
        {
            log.error("check isview project error!");
            throw e;
        }
        return isExist;
    }
}

package com.zhuhuibao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;

/**
 * 店铺相关接口实现类
 *
 * @author liyang
 * @date 2016年10月28日
 */
@Service
@Transactional
public class MobileMemberShopService {

    private static final Logger log = LoggerFactory.getLogger(MobileMemberShopService.class);

    @Autowired
    private MemShopService memShopService;

    /**
     * 查询商铺信息
     *
     * @param companyId
     * @return
     */
    public MemberShop getMemberShop(Long companyId) {
        return memShopService.findByCompanyID(companyId);
    }

}

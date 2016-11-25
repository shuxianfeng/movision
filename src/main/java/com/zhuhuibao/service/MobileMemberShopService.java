package com.zhuhuibao.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.service.MemShopService;

import java.util.ArrayList;
import java.util.List;

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
        MemberShop memberShop = memShopService.findByCompanyID(companyId);
        List<String> bannerList = new ArrayList<>();
        if (null != memberShop) {
            if (StringUtils.isNotBlank(memberShop.getMobileBannerUrlF())) {
                bannerList.add(memberShop.getMobileBannerUrlF());
            }
            if (StringUtils.isNotBlank(memberShop.getMobileBannerUrlS())) {
                bannerList.add(memberShop.getMobileBannerUrlS());
            }
            if (StringUtils.isNotBlank(memberShop.getMobileBannerUrlT())) {
                bannerList.add(memberShop.getMobileBannerUrlT());
            }
            memberShop.setBannerList(bannerList);
        } else {
            memberShop = new MemberShop();
            bannerList.add("//static.zhuhui8.com/images/mobile/banner/banner1.jpg");
            memberShop.setBannerList(bannerList);
        }
        return memberShop;
    }

}

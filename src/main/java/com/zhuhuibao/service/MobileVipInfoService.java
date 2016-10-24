package com.zhuhuibao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.mapper.VipInfoMapper;

/**
 * vip相关接口实现类
 *
 * @author liyang
 * @date 2016年10月24日
 */
@Service
@Transactional
public class MobileVipInfoService {

    private static final Logger log = LoggerFactory.getLogger(MobileVipInfoService.class);

    @Autowired
    private VipInfoMapper vipInfoMapper;

    /**
     * 根据ID获取会员VIP信息
     *
     * @param memberId
     * @return
     */
    public VipMemberInfo findVipMemberInfoById(Long memberId) {
        return vipInfoMapper.selectVipMemberInfoById(memberId);
    }

}

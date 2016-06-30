package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.vip.entity.VipMemberPrivilege;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心首页处理类
 *
 * @author pl
 * @version 2016/6/29 0029
 */
@Service
@Transactional
public class IndexService {
    @Autowired
    ZhbService zhbService;

    @Autowired
    VipInfoService vipInfoService;

    public Map<String,Object> getZhbInfo(Long createId)
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        ZhbAccount zhbCount = zhbService.getZhbAccount(createId);
        if(zhbCount != null) {
            BigDecimal zhbAmount = zhbCount.getAmount();
            resultMap.put("zhb",zhbAmount);
        }else {
            resultMap.put("zhb", 0);
        }
        List<VipMemberPrivilege> vipList =  vipInfoService.listVipMemberPrivilege(createId);
        resultMap.put("service",vipList);
        return resultMap;
    }
}

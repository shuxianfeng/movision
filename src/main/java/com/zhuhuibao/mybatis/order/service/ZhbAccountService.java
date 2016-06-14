package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.mybatis.order.entity.ZhbAccount;
import com.zhuhuibao.mybatis.order.mapper.ZhbAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 筑慧币账户
 */
@Service
public class ZhbAccountService {
    private static final Logger log = LoggerFactory.getLogger(ZhbAccountService.class);

    @Autowired
    ZhbAccountMapper mapper;


    /**
     * 根据用户ID获取账户信息
     * @param companyId
     * @return
     */
    public ZhbAccount findByMemberId(Long companyId) {
        ZhbAccount  account;
        try{
            account = mapper.selectByPrimaryKey(companyId);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
        return account;
    }
}

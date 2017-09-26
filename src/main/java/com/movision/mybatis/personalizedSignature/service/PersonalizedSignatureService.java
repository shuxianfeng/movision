package com.movision.mybatis.personalizedSignature.service;

import com.movision.mybatis.personalizedSignature.entity.PersonalizedSignature;
import com.movision.mybatis.personalizedSignature.mapper.PersonalizedSignatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/9/26 17:47
 */
@Service
public class PersonalizedSignatureService {

    private static Logger log = LoggerFactory.getLogger(PersonalizedSignatureService.class);

    @Autowired
    private PersonalizedSignatureMapper personalizedSignatureMapper;

    /**
     * 随机查询机器人个人签名异常
     *
     * @param number
     * @return
     */
    public List<PersonalizedSignature> queryRoboltSignature(Integer number) {
        try {
            log.info("查询随机机器人个人签名");
            return personalizedSignatureMapper.queryRoboltSignature(number);
        } catch (Exception e) {
            log.error("随机查询机器人个性签名异常", e);
            throw e;
        }
    }
}

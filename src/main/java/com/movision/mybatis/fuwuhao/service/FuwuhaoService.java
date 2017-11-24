package com.movision.mybatis.fuwuhao.service;

import com.movision.mybatis.dinyuehao.service.DinyuehaoService;
import com.movision.mybatis.fuwuhao.entity.Fuwuhao;
import com.movision.mybatis.fuwuhao.mapper.FuwuhaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/11/24 11:10
 */
@Service
public class FuwuhaoService {
    private static Logger log = LoggerFactory.getLogger(FuwuhaoService.class);
    @Autowired
    private FuwuhaoMapper fuwuhaoMapper;

    public int insertSelective(Fuwuhao fuwuhao) {
        try {
            log.info("服务号插入");
            return fuwuhaoMapper.insertSelective(fuwuhao);
        } catch (Exception e) {
            log.error("服务号插入失败");
            throw e;
        }

    }


    public int openidByUnionid(int openid) {
        try {
            log.info("根据openid查询unionid");
            return fuwuhaoMapper.openidByUnionid(openid);
        } catch (Exception e) {
            log.error("根据openid查询unionid失败");
            throw e;
        }
    }
}


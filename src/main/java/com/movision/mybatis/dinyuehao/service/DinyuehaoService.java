package com.movision.mybatis.dinyuehao.service;

import com.movision.mybatis.dinyuehao.entity.Dinyuehao;
import com.movision.mybatis.dinyuehao.mapper.DinyuehaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/11/24 11:06
 */
@Service
public class DinyuehaoService {
    private static Logger log = LoggerFactory.getLogger(DinyuehaoService.class);
    @Autowired
    private DinyuehaoMapper dinyuehaoMapper;

    public int insertSelective(Dinyuehao dinyuehao) {
        try {
            log.info("插入订阅号");
            return dinyuehaoMapper.insertSelective(dinyuehao);
        } catch (Exception e) {
            log.error("插入订阅号失败");
            throw e;
        }
    }


    public int unionidByOpenid(String unionid) {
        try {
            log.info("根据unionid查询存在openid");
            return dinyuehaoMapper.unionidByOpenid(unionid);
        } catch (Exception e) {
            log.error("根据unionid查询存在openid失败");
            throw e;
        }
    }

}

package com.movision.mybatis.dinyuehao.service;

import com.movision.mybatis.dinyuehao.entity.Dinyuehao;
import com.movision.mybatis.dinyuehao.mapper.DinyuehaoMapper;
import com.movision.mybatis.fuwuhao.entity.Fuwuhao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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


    public int unionidByD(String unionid) {
        try {
            log.info("根据unionid查询存在openid");
            return dinyuehaoMapper.unionidByD(unionid);
        } catch (Exception e) {
            log.error("根据unionid查询存在openid失败");
            throw e;
        }
    }

    public String unionidByOpenids(String unionid) {
        try {
            log.info("根据unionid查询存在openid");
            return dinyuehaoMapper.unionidByOpenids(unionid);
        } catch (Exception e) {
            log.error("根据unionid查询存在openid失败");
            throw e;
        }
    }



    public int updateFU(Fuwuhao fuwuhao) {
        try {
            log.info("根据openid修改");
            return dinyuehaoMapper.updateFU(fuwuhao);
        } catch (Exception e) {
            log.error("根据openid修改失败");
            throw e;
        }
    }

    public int selectO(String map) {
        try {
            log.info("根据openid修改");
            return dinyuehaoMapper.selectO(map);
        } catch (Exception e) {
            log.error("根据openid修改失败");
            throw e;
        }
    }

    public Fuwuhao selectOc(String code) {
        try {
            log.info("根据openid查询所有");
            return dinyuehaoMapper.selectOc(code);
        } catch (Exception e) {
            log.error("根据openid查询所有失败");
            throw e;
        }
    }

}

package com.movision.mybatis.weixinguangzhu.service;

import com.movision.mybatis.weixinguangzhu.entity.WeixinGuangzhu;
import com.movision.mybatis.weixinguangzhu.mapper.WeixinGuangzhuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhanglei
 * @Date 2017/8/10 15:31
 */
@Service
public class WeixinGuangzhuService {
    @Autowired
    private WeixinGuangzhuMapper weixinGuangzhuMapperw;
    private static Logger log = LoggerFactory.getLogger(WeixinGuangzhuService.class);

    public int insertSelective(WeixinGuangzhu weixinGuangzhu) {
        try {
            log.info("插入关注信息");
            return weixinGuangzhuMapperw.insertSelective(weixinGuangzhu);
        } catch (Exception e) {
            log.error("插入关注信息失败", e);
            throw e;
        }
    }


    public int updateCount(int id) {
        try {
            log.info("修改关注次数");
            return weixinGuangzhuMapperw.updateCount(id);
        } catch (Exception e) {
            log.error("修改关注次数失败", e);
            throw e;
        }
    }

    public int selectCount(String openids) {
        try {
            log.info("查询关注表中有没有记录");
            return weixinGuangzhuMapperw.selectCount(openids);
        } catch (Exception e) {
            log.error("查询关注表中有没有记录失败", e);
            throw e;
        }
    }


    public int manyC(String openids) {
        try {
            log.info("抽到几等奖");
            return weixinGuangzhuMapperw.manyC(openids);
        } catch (Exception e) {
            log.error("抽到几等奖失败", e);
            throw e;
        }
    }

    public int overplusMany(String openids) {
        try {
            log.info("剩余次数");
            return weixinGuangzhuMapperw.overplusMany(openids);
        } catch (Exception e) {
            log.error("剩余次数失败", e);
            throw e;
        }
    }

    public int lessCount(String openids) {
        try {
            log.info("减少抽奖次数");
            return weixinGuangzhuMapperw.lessCount(openids);
        } catch (Exception e) {
            log.error("减少抽奖次数失败", e);
            throw e;
        }
    }


    public String selectOpenid(int userid) {
        try {
            log.info("查出openid");
            return weixinGuangzhuMapperw.selectOpenid(userid);
        } catch (Exception e) {
            log.error("查出openid失败", e);
            throw e;
        }
    }


    public int updateC(String openid) {
        try {
            log.info("增加抽奖次数");
            return weixinGuangzhuMapperw.updateC(openid);
        } catch (Exception e) {
            log.error("增加抽奖次数失败", e);
            throw e;
        }
    }

}

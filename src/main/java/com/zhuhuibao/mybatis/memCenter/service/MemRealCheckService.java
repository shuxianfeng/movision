package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.MemRealCheck;
import com.zhuhuibao.mybatis.memCenter.mapper.MemRealCheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 用户基本资料审核
 *
 * @author jianglz
 * @since 2016/8/4.
 */
@Service
public class MemRealCheckService {
    private static final Logger log = LoggerFactory.getLogger(MemRealCheckService.class);

    @Autowired
    MemRealCheckMapper realCheckMapper;

    public MemRealCheck findMemById(String id) {

        return realCheckMapper.selectByPrimaryKey(Long.valueOf(id));
    }

    public Map<String, Object> findMemrealCheck(String id) {
        return realCheckMapper.findMemrealCheck(id);
    }

    public void update(MemRealCheck member) {
        try {

            member.setUpdateTime(new Date());
            realCheckMapper.updateByPrimaryKeySelective(member);

        } catch (Exception e) {
            log.error("t_m_member_real_check update error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }
}

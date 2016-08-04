package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemInfoCheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户基本资料审核
 * @author jianglz
 * @since 2016/8/4.
 */
@Service
public class MemInfoCheckService {
    private static final Logger log = LoggerFactory.getLogger(MemInfoCheckService.class);

    @Autowired
    MemInfoCheckMapper memInfoCheckMapper;

    public MemInfoCheck findMemById(String id) {
        try {
            return memInfoCheckMapper.findMemById(id);
        } catch (Exception e) {
            log.error("findMemById 查询异常:{id=" + id + " }>>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 用户基本资料保存  提交审核
     * @param member
     */
    public void update(MemInfoCheck member) {
        try {

            member.setUpdateTime(new Date());
            memInfoCheckMapper.updateByPrimaryKeySelective(member);

        } catch (Exception e) {
            log.error("t_m_member_info_check update error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }
}

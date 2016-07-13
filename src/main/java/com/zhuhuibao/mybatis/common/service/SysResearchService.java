package com.zhuhuibao.mybatis.common.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.common.entity.SysResearch;
import com.zhuhuibao.mybatis.common.mapper.SysResearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jianglz
 * @since 16/7/13.
 */
@Service
public class SysResearchService {
    private static final Logger log = LoggerFactory.getLogger(SysResearchService.class);

    @Autowired
    SysResearchMapper mapper;

    public void insert(SysResearch sysResearch) {
        try {
            int count = mapper.insertSelective(sysResearch);
            if (count != 1) {
                log.error("t_sys_research 插入失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入失败");
        }

    }
}

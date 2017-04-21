package com.movision.mybatis.auditVipDetail.service;

import com.movision.mybatis.auditVipDetail.entity.AuditVipDetail;
import com.movision.mybatis.auditVipDetail.mapper.AuditVipDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhuangyuhao
 * @Date 2017/4/21 10:20
 */
@Service
@Transactional
public class AuditVipDetailService {

    private static Logger log = LoggerFactory.getLogger(AuditVipDetailService.class);

    @Autowired
    private AuditVipDetailMapper auditVipDetailMapper;

    public AuditVipDetail selectByApplyId(Integer applyid) {
        try {
            log.info("根据应用id查询审核表的记录");
            return auditVipDetailMapper.selectByApplyId(applyid);
        } catch (Exception e) {
            log.error("根据应用id查询审核表的记录失败", e);
            throw e;
        }
    }

}

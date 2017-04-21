package com.movision.mybatis.auditVipDetail.service;

import com.movision.mybatis.auditVipDetail.entity.AuditVipDetail;
import com.movision.mybatis.auditVipDetail.mapper.AuditVipDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    /**
     * 加V申请审核
     *
     * @param map
     * @return
     */
    public Integer insertVIPDetail(Map map) {
        try {
            log.info("加V申请审核");
            return auditVipDetailMapper.insertVIPDetail(map);
        } catch (Exception e) {
            log.error("加V申请审核异常", e);
            throw e;
        }
    }

    /**
     * 查询用户是否申请过加V
     *
     * @param map
     * @return
     */
    public Integer queryVipDetail(Map map) {
        try {
            log.info("查询用户是否申请过加V");
            return auditVipDetailMapper.queryVipDetail(map);
        } catch (Exception e) {
            log.error("查询用户是否申请过加V异常", e);
            throw e;
        }
    }

    /**
     * 更新加V申请
     *
     * @param map
     * @return
     */
    public Integer updateVipDetail(Map map) {
        try {
            log.info("更新加V申请");
            return auditVipDetailMapper.updateVipDetail(map);
        } catch (Exception e) {
            log.error("更新加V申请异常", e);
            throw e;
        }
    }


}

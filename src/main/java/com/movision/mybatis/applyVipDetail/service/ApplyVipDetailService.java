package com.movision.mybatis.applyVipDetail.service;

import com.movision.mybatis.applyVipDetail.entity.ApplyVipDetail;
import com.movision.mybatis.applyVipDetail.mapper.ApplyVipDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/28 17:18
 */
@Service
@Transactional
public class ApplyVipDetailService {

    private static Logger log = LoggerFactory.getLogger(ApplyVipDetailService.class);

    @Autowired
    private ApplyVipDetailMapper applyVipDetailMapper;

    public int addVipApplyRecord(ApplyVipDetail applyVipDetail) {
        try {
            log.info("新增申请vip记录");
            return applyVipDetailMapper.insertSelective(applyVipDetail);
        } catch (Exception e) {
            log.error("新增申请vip记录失败", e);
            throw e;
        }
    }

    public ApplyVipDetail selectLatestVipApplyRecord(Integer userid) {
        try {
            log.info("查询本人最新申请VIP的一条记录");
            return applyVipDetailMapper.selectLatestVipApplyRecord(userid);
        } catch (Exception e) {
            log.error("查询本人最新申请VIP的一条记录失败", e);
            throw e;
        }
    }


}

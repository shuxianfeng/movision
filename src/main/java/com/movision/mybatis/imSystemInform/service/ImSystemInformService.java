package com.movision.mybatis.imSystemInform.service;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.mapper.ImSystemInformMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/9 20:16
 */
@Service
@Transactional
public class ImSystemInformService {
    private static Logger log = LoggerFactory.getLogger(ImSystemInformService.class);

    @Autowired
    private ImSystemInformMapper imSystemInformMapper;

    public int add(ImSystemInform imSystemInform) {
        try {
            log.info("添加系统消息");
            return imSystemInformMapper.insertSelective(imSystemInform);
        } catch (Exception e) {
            log.error("添加系统消息失败", e);
            throw e;
        }
    }

    public List<ImSystemInform> queryAll(Paging<ImSystemInform> paging) {
        try {
            log.info("查询所有的系统通知");
            return imSystemInformMapper.selectAll(paging.getRowBounds());
        } catch (Exception e) {
            log.error("查询所有的系统通知失败", e);
            throw e;
        }
    }

    public ImSystemInform queryDetail(Integer id) {
        try {
            log.info("查询系统通知详情");
            return imSystemInformMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("查询系统通知详情失败", e);
            throw e;
        }
    }

    /**
     * 删除系统通知
     *
     * @param id
     * @return
     */
    public Integer deleteImSystem(Integer id) {
        try {
            log.info("删除系统通知");
            return imSystemInformMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error("删除系统通知失败", e);
            throw e;
        }

    }
}

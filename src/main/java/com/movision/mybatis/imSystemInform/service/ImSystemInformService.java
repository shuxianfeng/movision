package com.movision.mybatis.imSystemInform.service;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imSystemInform.entity.ImSystemInformVo;
import com.movision.mybatis.imSystemInform.mapper.ImSystemInformMapper;
import com.movision.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public List<ImSystemInformVo> queryAll(Paging<ImSystemInformVo> paging) {
        try {
            log.info("查询所有的系统通知");
            return imSystemInformMapper.findAll(paging.getRowBounds());
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

    /**
     * 条件搜索
     *
     * @param map
     * @param pager
     * @return
     */
    public List<ImSystemInform> findAllSystemForm(Map map, Paging<ImSystemInform> pager) {
        try {
            log.info("条件搜索");
            return imSystemInformMapper.findAllSystemInform(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件搜索失败");
            throw e;
        }
    }

    /**
     * 查询内容全部
     *
     * @param id
     * @return
     */
    public ImSystemInform queryBodyAll(Integer id) {
        try {
            log.info("查询内容全部");
            return imSystemInformMapper.queryBodyAll(id);
        } catch (Exception e) {
            log.error("查询内容全部失败", e);
            throw e;
        }
    }

    /**
     * 查询最新一条记录
     *
     * @return
     */
    public ImSystemInformVo queryByUserid() {
        try {
            log.info("查询最新一条记录");
            return imSystemInformMapper.queryByUserid();
        } catch (Exception e) {
            log.error("查询最新一条记录失败", e);
            throw e;
        }
    }

    /**
     * 查询是否有未读系统通知
     *
     * @param userid
     * @return
     */
    public Integer querySystemPushByUserid(Integer userid) {
        try {
            log.info("查询是否有未读系统通知");
            return imSystemInformMapper.querySystemPushByUserid(userid);
        } catch (Exception e) {
            log.error("查询是否有未读系统通知异常", e);
            throw e;
        }
    }


    /**
     * 查询是否有未读系统通知
     *
     * @param indity
     * @return
     */
    public Integer queryInform(String indity) {
        try {
            log.info("查询是否有未读系统通知");
            return imSystemInformMapper.queryInform(indity);
        } catch (Exception e) {
            log.error("查询是否有未读系统通知异常", e);
            throw e;
        }
    }
}

package com.movision.mybatis.take.service;

import com.movision.mybatis.systemPush.service.SystemPushService;
import com.movision.mybatis.take.entity.Take;
import com.movision.mybatis.take.entity.TakeVo;
import com.movision.mybatis.take.mapper.TakeMapper;
import com.movision.utils.L;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/8/29 16:09
 */
@Service
@Transactional
public class TakeService {
    private static Logger log = LoggerFactory.getLogger(SystemPushService.class);
    @Autowired
    private TakeMapper takeMapper;

    /**
     * 添加参赛人员
     *
     * @param take
     * @return
     */
    public int insertSelectiveTP(Take take) {
        try {
            log.info("添加投稿");
            return takeMapper.insertSelective(take);
        } catch (Exception e) {
            log.error("添加参赛人员失败", e);
            throw e;
        }
    }

    /**
     * 编辑投稿
     *
     * @param take
     */
    public void updateTakeById(Take take) {
        try {
            log.info("编辑投稿");
            takeMapper.updateByPrimaryKeySelective(take);
        } catch (Exception e) {
            log.error("编辑投稿异常", e);
            throw e;
        }
    }

    /**
     * 投稿序号+1
     *
     * @param take
     */
    public void updateTakeByNumber(Take take) {
        try {
            log.info("投稿序号+1操作");
            takeMapper.updateTakeByNumber(take);
        } catch (Exception e) {
            log.error("投稿序号+1操作异常", e);
            throw e;
        }
    }

    /**
     * 投稿审核
     *
     * @param take
     */
    public void updateTakeByAudit(Take take) {
        try {
            log.info("投稿审核");
            takeMapper.updateTakeByAudit(take);
        } catch (Exception e) {
            log.error("投稿审核异常", e);
            throw e;
        }
    }

    /**
     * 查询投稿详情
     *
     * @param
     * @return
     */
    public TakeVo queryTakeById(Map map) {
        try {
            log.info("查询投稿详情");
            return takeMapper.queryTakeById(map);
        } catch (Exception e) {
            log.error("查询投稿详情异常", e);
            throw e;
        }
    }


    /**
     * 删除参赛人员
     *
     * @param id
     * @return
     */
    public int deleteTakePeople(int id) {
        try {
            log.info("删除参赛人员");
            return takeMapper.deleteTakePeople(id);
        } catch (Exception e) {
            log.error("删除参赛人员失败", e);
            throw e;
        }
    }


    /**
     * 查询全部参赛人员
     *
     * @param paging
     * @return
     */
    public List<TakeVo> findAllTake(Paging<TakeVo> paging, Take take) {
        try {
            log.info("查询全部参赛人员");
            return takeMapper.findAllTake(paging.getRowBounds(), take);
        } catch (Exception e) {
            log.error("查询全部参赛人员失败", e);
            throw e;
        }
    }

    public List<TakeVo> findAll(int activeid) {
        try {
            log.info("查询全部参赛人员");
            return takeMapper.findAll(activeid);
        } catch (Exception e) {
            log.error("查询全部参赛人员失败", e);
            throw e;
        }
    }

    /**
     * 根据编号或名字查询
     *
     * @param paging
     * @param map
     * @return
     */
    public List<TakeVo> findAllTakeCondition(Paging<TakeVo> paging, Map map) {
        try {
            log.info("根据编号或名字查询");
            return takeMapper.findAllTakeCondition(paging.getRowBounds(),map);
        } catch (Exception e) {
            log.error("根据编号或名字查询失败", e);
            throw e;
        }
    }

    /**
     * 投票排行
     *
     * @param paging
     * @return
     */
    public List<TakeVo> voteDesc(Paging<TakeVo> paging, int activeid) {
        try {
            log.info("投票排行");
            return takeMapper.voteDesc(paging.getRowBounds(), activeid);
        } catch (Exception e) {
            log.error("投票排行失败", e);
            throw e;
        }
    }

}

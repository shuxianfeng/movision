package com.movision.mybatis.bossOrders.servic;

import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.mapper.BossOrdersMapper;
import com.movision.mybatis.post.entity.Post;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/6 10:55
 */
@Service
public class BossOrderService {

    @Autowired
    BossOrdersMapper bossOrdersMapper;
    Logger loger = LoggerFactory.getLogger(BossOrderService.class);

    /**
     * 查询订单列表
     *
     * @param pager
     * @return
     */
    public List<BossOrdersVo> queryOrderList(Paging<Post> pager) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("查询订单列表");
            }
            return bossOrdersMapper.queryOrdersByList(pager.getRowBounds());
        } catch (Exception e) {
            loger.error("查询订单列表异常");
            throw e;
        }
    }

    /**
     * 根据条件查询订单
     *
     * @param map
     * @return
     */
    public List<BossOrdersVo> queryOrderByCondition(Map map) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("根据条件查询订单");
            }
            return bossOrdersMapper.queryOrderByCondition(map);
        } catch (Exception e) {
            loger.error("根据条件查询订单");
            throw e;
        }
    }

    /**
     * 精确查询订单
     *
     * @param map
     * @return
     */
    public List<BossOrdersVo> queryAccuracyConditionByOrder(Map map) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("精确查找订单");
            }
            return bossOrdersMapper.queryAccuracyConditionByOrder(map);
        } catch (Exception e) {
            loger.error("精确查询订单异常");
            throw e;
        }
    }

    public BossOrdersVo queryOrderParticulars(Integer ordernumber) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("查询订单基本信息");
            }
            return bossOrdersMapper.queryOrderParticulars(ordernumber);
        } catch (Exception e) {
            loger.error("订单基本信息查询异常");
            throw e;
        }
    }
}

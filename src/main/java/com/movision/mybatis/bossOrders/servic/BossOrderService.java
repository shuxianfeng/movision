package com.movision.mybatis.bossOrders.servic;

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
            System.out.print(map.get("name"));
            return bossOrdersMapper.queryOrderByCondition(map);
        } catch (Exception e) {
            loger.error("根据条件查询订单");
            throw e;
        }
    }
}

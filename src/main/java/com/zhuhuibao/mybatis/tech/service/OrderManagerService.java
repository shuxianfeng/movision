package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.mybatis.tech.entity.OrderOms;
import com.zhuhuibao.mybatis.tech.mapper.OrderManagerMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author Administrator
 * @version 2016/6/6 0006
 */
@Service
@Transactional
public class OrderManagerService {

    private final static Logger log = LoggerFactory.getLogger(OrderManagerService.class);

    @Autowired
    OrderManagerMapper orderMapper;

    /**
     * 查询已发布的课程
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String,String>> findAllOmsTechOrder(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all oms order for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> orderList;
        try{
            orderList = orderMapper.findAllOmsTechOrder(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all oms order for pager error!",e);
            throw e;
        }
        return orderList;
    }

    /**
     * 查询已发布的课程
     * @param condition
     * @return
     */
    public Map<String,Object> selectOrderDetail(Map<String,Object> condition)
    {
        log.info("find all oms order for pager "+ StringUtils.mapToString(condition));
        Map<String,Object> orderList;
        try{
            orderList = orderMapper.selectByPrimaryKey(condition);
        }catch(Exception e)
        {
            log.error("find all oms order for pager error!",e);
            throw e;
        }
        return orderList;
    }

    /**
     * 更新订单状态
     * @param orderNo
     * @param status
     * @return
     */
    public int updateByPrimaryKeySelective(String orderNo,String status)
    {
        log.info("update order status = "+status+" orderNo = "+orderNo);
        int result = 0;
        try {
            OrderOms record = new OrderOms();
            record.setStatus(status);
            record.setOrderNo(orderNo);
            result = orderMapper.updateByPrimaryKeySelective(record);
        }catch(Exception e)
        {
            log.error("update order status error!");
            throw e;
        }
        return result;
    }
}

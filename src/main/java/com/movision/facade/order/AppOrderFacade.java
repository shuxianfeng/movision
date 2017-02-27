package com.movision.facade.order;

import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 14:11
 */
@Service
public class AppOrderFacade {
    @Autowired
    private OrderService orderService;

    public List<Orders> getMyOrderList(Paging<Orders> paging, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return orderService.findAllMyOrderList(paging, map);
    }
}

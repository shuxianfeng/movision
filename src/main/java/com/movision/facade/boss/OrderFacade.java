package com.movision.facade.boss;

import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.servic.BossOrderService;
import com.movision.mybatis.post.entity.Post;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/6 10:31
 */
@Service
public class OrderFacade {
    @Autowired
    BossOrderService bossOrderService = new BossOrderService();

    /**
     * 查询订单列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<BossOrdersVo> queryOrderList(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        return bossOrderService.queryOrderList(pager);
    }


    /**
     * 根据条件查询订单
     *
     * @param ordernumber
     * @param name
     * @param status
     * @param takeway
     * @return
     */
    public List<BossOrdersVo> queryOrderByCondition(String ordernumber, String name, String status, String takeway) {
        Map<String, String> map = new HashedMap();
        map.put("ordernumber", ordernumber);
        map.put("name", name);
        map.put("status", status);
        map.put("takeway", takeway);
        return bossOrderService.queryOrderByCondition(map);
    }
}

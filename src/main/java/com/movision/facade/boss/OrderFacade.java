package com.movision.facade.boss;

import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.servic.BossOrderService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.service.UserService;
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

    @Autowired
    UserService userService = new UserService();

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

    /**
     * 订单查询
     *
     * @param ordernumber
     * @param name
     * @param street
     * @param province
     * @param city
     * @param district
     * @param takeway
     * @param mintime
     * @param maxtime
     * @param email
     * @param cnee
     * @param phone
     * @param paytype
     * @return
     */
    public List<BossOrdersVo> queryAccuracyConditionByOrder(String ordernumber, String name,
                                                            String street, String province, String city, String district, String takeway, String mintime,
                                                            String maxtime, String email, String cnee, String phone, String paytype) {
        Map<String, String> map = new HashedMap();
        map.put("ordernumber", ordernumber);
        map.put("name", name);
        map.put("street", street);
        map.put("province", province);
        map.put("city", city);
        map.put("district", district);
        map.put("takeway", takeway);
        map.put("mintime", mintime);
        map.put("maxtime", maxtime);
        map.put("email", email);
        map.put("cnee", cnee);
        map.put("phone", phone);
        map.put("paytype", paytype);
        return bossOrderService.queryAccuracyConditionByOrder(map);
    }

    public BossOrdersVo queryOrderParticulars(String ordernumber) {
        BossOrdersVo bo = bossOrderService.queryOrderParticulars(Integer.parseInt(ordernumber));
        Integer userid = bo.getUserid();
        String wxid = userService.queryUserByOpenid(userid);
        bo.setOpenid(wxid);
        return bo;
    }
}

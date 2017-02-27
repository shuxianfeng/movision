package com.movision.controller.app.mine;

import com.movision.common.util.ShiroUtil;
import com.movision.facade.coupon.CouponFacade;
import com.movision.facade.order.AppOrderFacade;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.test.SpringTestCase;
import com.movision.utils.pagination.model.Paging;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 14:17
 */
public class MyInfoControllerTest extends SpringTestCase {
    @Autowired
    private AppOrderFacade orderFacade;

    @Autowired
    private CouponFacade couponFacade;

/*
    @Test
    public void getMyOrderList() throws Exception {
        Paging<Orders> paging = new Paging<Orders>(Integer.valueOf("1"), Integer.valueOf("10"));
        List<Orders> ordersList = orderFacade.getMyOrderList(paging, 1);
        paging.result(ordersList);
        System.out.println(paging);
    }
*/

    @Test
    public void getMyCouponList() throws Exception {
        Paging<Coupon> paging = new Paging<Coupon>(Integer.valueOf("1"), Integer.valueOf("10"));
        List<Coupon> couponList = couponFacade.findAllMyCouponList(paging, 1);
        paging.result(couponList);
        System.out.println(paging);
    }

}
package com.movision.controller.app.mine;

import com.movision.common.util.ShiroUtil;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.coupon.CouponFacade;
import com.movision.facade.order.OrderAppFacade;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.test.SpringTestCase;
import com.movision.utils.pagination.model.Paging;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 14:17
 */
public class MyInfoControllerTest extends SpringTestCase {
    @Autowired
    private OrderAppFacade orderFacade;

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CircleAppFacade circleAppFacade;

/*
    @Test
    public void getMyOrderList() throws Exception {
        Paging<Orders> paging = new Paging<Orders>(Integer.valueOf("1"), Integer.valueOf("10"));
        List<Orders> ordersList = orderFacade.getMyOrderList(paging, 1);
        paging.result(ordersList);
        System.out.println(paging);
    }
*/

/*
    @Test
    public void getMyCouponList() throws Exception {
        Paging<Coupon> paging = new Paging<Coupon>(Integer.valueOf("1"), Integer.valueOf("10"));
        List<Coupon> couponList = couponFacade.findAllMyCouponList(paging, 1);
        paging.result(couponList);
        System.out.println(paging);
    }
*/

    @Test
    public void getMyFollowCircleList() throws Exception {
        Paging<Circle> paging = new Paging<Circle>(Integer.valueOf("1"), Integer.valueOf("10"));
        List<Circle> circleList = circleAppFacade.findAllMyFollowCircleList(paging, 1);
        paging.result(circleList);
        System.out.println(paging);
    }


}
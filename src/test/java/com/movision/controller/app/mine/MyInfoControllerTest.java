package com.movision.controller.app.mine;

import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.coupon.CouponFacade;
import com.movision.facade.order.OrderAppFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.PersonInfo;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private UserFacade userFacade;

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

    /*
        @Test
        public void getMyFollowCircleList() throws Exception {
            Paging<Circle> paging = new Paging<Circle>(Integer.valueOf("1"), Integer.valueOf("10"));
            List<Circle> circleList = circleAppFacade.findAllMyFollowCircleList(paging, 1);
            paging.result(circleList);
            System.out.println(paging);
        }
    */
    @Test
    public void updateMyInfo() throws Exception {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setId(10);
        personInfo.setBirthday("1992-01-01");
        personInfo.setSign("我是一个小鸡鸡");
        personInfo.setSex(0);
        personInfo.setNickname("小鸡鸡JJ");
        userFacade.finishPersonDataProcess(personInfo);
    }


}
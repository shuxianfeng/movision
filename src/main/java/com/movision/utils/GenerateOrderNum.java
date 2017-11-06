package com.movision.utils;

import com.movision.mybatis.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Author shuxf
 * @Date 2017/11/2 14:08
 * 主要用于根据不同店铺生成订单编号（次订单号用于展示给客户和方便运营查询订单时使用）
 */
@Service
public class GenerateOrderNum {

    private static Logger log = LoggerFactory.getLogger(GenerateOrderNum.class);

    @Autowired
    private OrderService orderService;

    public String getOrderNum(int shopid) {
        //拼装订单号模糊查询入参（店铺id+年月日20170309）
        StringBuffer paramstr = new StringBuffer();
        if (shopid == -1) {
            paramstr.append(0);
        } else {
            paramstr.append(shopid);
        }
        Format format = new SimpleDateFormat("yyMMdd");
        paramstr.append(format.format(new Date()));

        //使用 店铺id+年月日+8位随机数字
        //拼接之后再去数据库校验，是否重复，如果重复循环生成
        boolean mark = false;//定义标志位：true为无重复；false为有重复
        while (!mark) {
            StringBuffer str = new StringBuffer();
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                str.append(random.nextInt(10));
            }
            paramstr.append(str.toString());
            //根据订单号查询订单库中是否存在订单重复的问题
            int num = orderService.queryDuplicateOrdNum(paramstr.toString());
            if (num > 0) {
                mark = false;//有重复
            } else {
                mark = true;//无重复
            }
        }

        return paramstr.toString();
    }
}

package com.movision.utils;

import com.movision.mybatis.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/3/9 13:53
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
        Format format = new SimpleDateFormat("yyyyMMdd");
        paramstr.append(format.format(new Date()));

        String maxordernumber = orderService.queryMaxOrderNumber(paramstr.toString() + "%");//根据店铺id+年月日字符串模糊查询最大的订单编号

        if ("".equals(maxordernumber) || "null".equals(maxordernumber) || maxordernumber == null) {
            paramstr.append("00001");
            log.info("生成的最新的订单编号为>>>>>>>>>>>>>>>>" + paramstr.toString());
            return paramstr.toString();
        } else {
            String suf = maxordernumber.substring(maxordernumber.length() - 5);//截取后5位
            String newsuf = Integer.toString(Integer.parseInt(suf) + 1);//新的后缀（缺0）
            if (newsuf.length() == 1) {
                newsuf = "0000" + newsuf;
            } else if (newsuf.length() == 2) {
                newsuf = "000" + newsuf;
            } else if (newsuf.length() == 3) {
                newsuf = "00" + newsuf;
            } else if (newsuf.length() == 4) {
                newsuf = "0" + newsuf;
            }
            log.info("生成的最新的订单编号为>>>>>>>>>>>>>>>>" + paramstr.append(newsuf).toString());
            return paramstr.append(newsuf).toString();
        }
    }
}

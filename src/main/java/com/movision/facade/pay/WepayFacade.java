package com.movision.facade.pay;

import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.utils.HttpClientUtils;
import com.movision.utils.UUIDGenerator;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import com.movision.utils.wechat.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 11:41
 */
@Service
public class WepayFacade {

    private static Logger log = LoggerFactory.getLogger(WepayFacade.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    PropertiesDBLoader propertiesDBLoader;

    /**
     * 用户拼装微信支付的请求入参（仅适用于小程序的租赁业务）
     * @param openid
     * @param ordersid
     * @return
     */
    public Map<String, Object> getWePay(String openid, String ordersid) {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        int[] ids = {Integer.parseInt(ordersid)};
        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        if (ordersList.size() >0) {//传入的订单存在

            String url = propertiesDBLoader.getValue("unifiedorder");//---------------1.微信统一下单接口

            //------------------------------------------------------------------------2.计算订单实际支付总金额
            double totalamount = 0;
            if (ordersList.get(0).getId() == ids[0]) {
                //计算实际支付总金额
                totalamount = ordersList.get(0).getMoney();
                if (ordersList.get(0).getIsdiscount() == 1) {//如果有使用优惠券，扣减优惠券金额
                    totalamount = totalamount - ordersList.get(0).getDiscouponmoney();
                }
                if (null != ordersList.get(0).getDispointmoney()) {//如果有使用积分抵扣，扣减积分抵扣金额
                    totalamount = totalamount - ordersList.get(0).getDispointmoney();
                }
            }
            log.info("打印订单号为：" + ids[0] + "的实际需要支付的总金额=================================>" + totalamount);

            //------------------------------------------------------------------------3.获取微信签名
            String appid = propertiesDBLoader.getValue("appid");
            String mchid = propertiesDBLoader.getValue("mchid");
            String notify_url = propertiesDBLoader.getValue("wepay_notify_url");
            String key = propertiesDBLoader.getValue("secret");
            String nonce_str = UUIDGenerator.genUUIDRemoveSep(1)[0];//生成32位UUID随机字符串
            String sign = WechatUtils.getSign(nonce_str, ordersid, totalamount, notify_url, openid, appid, mchid, key);

            //-------------------------------------------------------------------------4.封装入参xml
            Map<String, String> parammap = new HashMap<>();
            parammap = WechatUtils.getMap(parammap, nonce_str, sign, ordersid, totalamount, notify_url, openid, appid, mchid);//封装入参map对象
            String xml = WechatUtils.map2XmlString(parammap);//转为微信服务器需要的xml格式

            //-------------------------------------------------------------------------5.请求支付
            Map<String, String> resmap = HttpClientUtils.doPostByXML(url, xml, "UTF-8");
            if (resmap.get("status").equals("200")) {
                map.put("code", 200);//请求成功
                map.put("data", "");//返回客户端需要的数据
            }

        }else {
            map.put("code", 300);//订单号不存在或已取消
        }
        return map;
    }


}

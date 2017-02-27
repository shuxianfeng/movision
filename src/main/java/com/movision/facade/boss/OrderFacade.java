package com.movision.facade.boss;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.servic.BossOrderService;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.orderoperation.entity.Orderoperation;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.province.entity.Province;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Map<String, Object> queryOrderList(String pageNo, String pageSize) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Map<String, Object> map = new HashedMap();
        Paging<Post> pager = new Paging<Post>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<BossOrdersVo> ordersVos = bossOrderService.queryOrderList(pager);
        int total = bossOrderService.queryOrderAll();
        map.put("ordersVos", ordersVos);
        map.put("total", total);
        return map;
    }

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    public int deleteOrder(Integer id) {
        return bossOrderService.deleteOrder(id);
    }


    /**
     * 根据条件查询订单
     *
     * @param ordernumber
     * @param name
     * @param status
     * @param
     * @return
     */
    public List<BossOrdersVo> queryOrderByCondition(String ordernumber, String name, String status, String position, String logisticid, String mintime, String maxtime, Paging<BossOrdersVo> pager) {
        Map<String, Object> map = new HashedMap();
        if (ordernumber != null) {
            map.put("ordernumber", ordernumber);
        }
        if (name != null) {
            map.put("name", name);
        }
        if (status != null) {
            map.put("status", status);
        }
        if (position != null) {
            map.put("position", position);
        }
        if (logisticid != null) {
            map.put("logisticid", logisticid);
        }
        Date isessencetime = null;//开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        return bossOrderService.queryOrderByCondition(map, pager);
    }

    /**
     * 订单查询
     *
     * @param ordernumber
     * @param province
     * @param city
     * @param district
     * @param takeway
     * @param mintime
     * @param maxtime
     * @param email
     * @param name
     * @param phone
     * @param paytype
     * @return
     */
    public List<BossOrdersVo> queryAccuracyConditionByOrder(HttpServletRequest request, String ordernumber,
                                                            String province, String city, String district, String takeway, String mintime,
                                                            String maxtime, String email, String name, String phone, String paytype, Paging<BossOrdersVo> pager) {
        Map<String, Object> map = new HashedMap();
        try {
            request.setCharacterEncoding("utf-8");

            if (ordernumber != null) {
                map.put("ordernumber", ordernumber);
            }
            if (province != null) {
                map.put("province", province);
            }
            if (city != null) {
                map.put("city", city);
            }
            if (district != null) {
                map.put("district", district);
            }
            if (takeway != null) {
                map.put("takeway", takeway);
            }
            Date isessencetime = null;//开始时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (mintime != null) {
                try {
                    isessencetime = format.parse(mintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            map.put("mintime", isessencetime);
            Date max = null;//最大时间
            if (maxtime != null) {
                try {
                    max = format.parse(maxtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            map.put("maxtime", max);
            map.put("email", email);
            map.put("name", name);
            map.put("phone", phone);
            if (paytype != null) {
                map.put("paytype", paytype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bossOrderService.queryAccuracyConditionByOrder(map, pager);

    }

    public BossOrdersVo queryOrderParticulars(String ordernumber) {
        BossOrdersVo bo = bossOrderService.queryOrderParticulars(Integer.parseInt(ordernumber));
        Integer userid = bo.getUserid();
        String wxid = userService.queryUserByOpenid(userid);
        bo.setOpenid(wxid);
        return bo;
    }

    /**
     * 查询省名
     *
     * @return
     */
    public Map<String, Object> queryPostProvince() {
        Map<String, Object> map = new HashedMap();
        List<Province> province = bossOrderService.queryPostProvinceName();
        map.put("proname", province);
        return map;
    }


    /**
     * 查询区名
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryOrderArea(Integer id) {
        Map<String, Object> map = new HashedMap();
        List<Area> list = bossOrderService.queryPostAreaName(id);
        map.put("list", list);
        return map;
    }

    /**
     * 查询市名
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryOrderCity(Integer id) {
        Map<String, Object> map = new HashedMap();
        List<City> list = bossOrderService.queryPostCityName(id);
        map.put("list", list);
        return map;
    }
    /**
     * 订单管理--订单详情
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryOrderDetail(Integer id) {
        Map<String, Object> map = new HashedMap();
        Invoice invoice = bossOrderService.queryOrderInvoiceInfo(id);//查询发票信息
        BossOrders bossOrders = bossOrderService.queryOrderInfo(id);//查询基本信息(包含其他信息)
        Address bossOrdersGet = bossOrderService.queryOrderGetInfo(id);//查询收货人信息
        List<Goods> goods = bossOrderService.queryOrderGoods(id);//查询商品信息
        Double money = 0.0;//小计
        Double summoney = 0.0;//总价
        Goods good = new Goods();
        for (int i = 0; i < goods.size(); i++) {
            Double price = goods.get(i).getPrice();//折后价
            Double origprice = goods.get(i).getOrigprice();//原价
            int num = goods.get(i).getNum();//数量
            if (price != null && num != 0) {
                money = num * price;//小计
            }
            summoney += money;//总价
            goods.get(i).setMoney(money);
        }
        good.setSummoney(summoney);
        Orders orderses = bossOrderService.queryOrderMoney(id);//查询费用信息
        List<Orderoperation> orderoperationrs = bossOrderService.queryOrderoperation(id);//查询操作信息
        map.put("invoice", invoice);
        map.put("bossOrders", bossOrders);
        map.put("bossOrdersGet", bossOrdersGet);
        map.put("goods", goods);
        map.put("good", good);
        map.put("orderses", orderses);
        map.put("orderoperationrs", orderoperationrs);
        return map;
    }


    /**
     * 修改收货地址
     *
     * @param orderid
     * @param phone
     * @param name
     * @param email
     * @param province
     * @param city
     * @param district
     * @param
     * @return
     */
    public Map<String, Integer> updateOrderAddress(String orderid, String phone, String name, String email, String province, String city, String district, String street) {
        Address address = new Address();
        Map<String, Integer> map = new HashedMap();
        address.setCity(city);
        address.setDistrict(district);
        address.setOrderid(Integer.parseInt(orderid));
        address.setProvince(province);
        address.setStreet(street);
        address.setPhone(phone);
        address.setName(name);
        int result = bossOrderService.updateOrderGet(address);
        User user = new User();
        user.setEmail(email);
        user.setOrderid(Integer.parseInt(orderid));
        int res = bossOrderService.updateOrderEmail(user);
        map.put("result", result);
        map.put("res", res);
        return map;
    }

    /**
     * 订单管理-编辑发票
     *
     * @param head
     * @param kind
     * @param content
     * @param
     * @return
     */
    public Map<String, Integer> updateOrderInvoice(String head, String kind, String content, String orderid, String companyname, String rigaddress, String rigphone, String bank, String banknum, String code, String onlystatue) {
        Invoice invoice = new Invoice();
        Map<String, Integer> map = new HashedMap();
        int kinds = Integer.parseInt(kind);
        if (kinds == 1) {
            invoice.setKind(kinds);
            invoice.setOrderid(Integer.parseInt(orderid));
            invoice.setContent(content);
            invoice.setHead(head);
            invoice.setOnlystatue(Integer.parseInt(onlystatue));
        }
        if (kinds == 2) {
            invoice.setOrderid(Integer.parseInt(orderid));
            invoice.setHead(head);
            invoice.setContent(content);
            invoice.setKind(kinds);
            invoice.setBank(bank);
            invoice.setBanknum(banknum);
            invoice.setCompanyname(companyname);
            invoice.setCode(code);
            invoice.setRigaddress(rigaddress);
            invoice.setRigphone(rigphone);
        }
        int result = bossOrderService.updateOrderInvoice(invoice);
        map.put("result", result);
        return map;
    }

    /**
     * 订单管理--编辑费用
     *
     * @param id
     * @param discouponmoney
     * @param dispointmoney
     * @param
     * @param sendmoney
     * @param
     * @return
     */
    public Map<String, Integer> updateOrderMoney(String id, String discouponmoney, String dispointmoney, String sendmoney) {
        Map<String, Integer> map = new HashedMap();
        Orders orders = new Orders();
        orders.setId(Integer.parseInt(id));
        orders.setDiscouponmoney(Double.parseDouble(discouponmoney));
        orders.setDispointmoney(Double.parseDouble(dispointmoney));
        orders.setSendmoney(Double.parseDouble(sendmoney));
        int result = bossOrderService.updateOrderMoney(orders);
        map.put("result", result);
        return map;
    }

    /**
     * 订单管理-返回发票
     *
     * @param orderid
     * @return
     */
    public Invoice queryOrderInvoice(Integer orderid) {
        return bossOrderService.queryOrderInvoice(orderid);
    }

    /**
     * 查询历史地址
     *
     * @param orderid
     * @return
     */
    public List<Address> queryOrderAddress(Integer orderid) {
        List<Address> list = bossOrderService.queryOrders(orderid);
        return list;
    }

    /**
     * 返回地址
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryOrderByAddress(Integer id) {
        Map<String, Object> map = new HashedMap();
        Address address = bossOrderService.queryOrdersByAddress(id);
        map.put("address", address);
        return map;
    }

    /**
     * 售后列表
     *
     * @param
     * @return
     */
    public List<Afterservice> queryAfterService(Paging<Afterservice> pager) {
        return bossOrderService.queryAfterSevice(pager);
    }


    /**
     * 售后管理*--根据id查询售后信息
     *
     * @param id
     * @return
     */
    public Afterservice queryAfterServiceById(Integer id) {
        Afterservice afterservice = bossOrderService.queryAfterServiceById(id);
        return afterservice;
    }

    /**
     * 售后管理--修改售后信息
     *
     * @param processingstatus
     * @param refundamount
     * @param id
     * @return
     */
    public Map<String, Integer> updateAfterService(Integer processingstatus, String refundamount, String id) {
        Map<String, Integer> map = new HashedMap();
        Afterservice afterservice = new Afterservice();
        afterservice.setId(Integer.parseInt(id));
        if (processingstatus == 1 && refundamount != null) {
            afterservice.setProcessingstatus(1);
            afterservice.setAftersalestatus(4);
        }
        if (processingstatus == 1 && refundamount == null) {
            afterservice.setProcessingstatus(1);
            afterservice.setAftersalestatus(3);
        }
        if (processingstatus == 2) {
            afterservice.setProcessingstatus(1);
            afterservice.setAftersalestatus(5);
        }
        int result = bossOrderService.updateAfterService(afterservice);
        map.put("result", result);
        return map;
    }

    /**
     * 售后管理--售后预览
     *
     * @param id
     * @return
     */
    public Afterservice queryByIdAfterService(Integer id) {
        Afterservice afterservice = bossOrderService.queryByIdAfterService(id);
        return afterservice;
    }

    /**
     * 售后管理--根据条件查询
     *
     * @param ordernumber
     * @param name
     * @param aftersalestatus
     * @param afterstatue
     * @param processingstatus
     * @param mintime
     * @param maxtime
     * @param pager
     * @return
     */
    public List<Afterservice> queryOrderByConditionAfterService(String ordernumber, String name, String aftersalestatus, String afterstatue, String processingstatus, String mintime, String maxtime, Paging<Afterservice> pager) {
        Map<String, Object> map = new HashedMap();
        if (ordernumber != null) {
            map.put("ordernumber", ordernumber);
        }
        if (name != null) {
            map.put("name", name);
        }
        if (aftersalestatus != null) {
            map.put("aftersalestatus", aftersalestatus);
        }
        if (afterstatue != null) {
            map.put("afterstatue", afterstatue);
        }
        if (processingstatus != null) {
            map.put("processingstatus", processingstatus);
        }
        Date isessencetime = null;//开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        return bossOrderService.queryOrderByConditionAfterService(map, pager);
    }


    /**
     * 售后管理*--留言
     *
     * @param id
     * @return
     */
    public Afterservice queryRemark(Integer id) {
        Afterservice afterservice = bossOrderService.queryRemark(id);
        return afterservice;
    }
}


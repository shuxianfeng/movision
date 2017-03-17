package com.movision.facade.boss;


import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.entity.AddressVo;
import com.movision.mybatis.afterservice.entity.AfterServiceVo;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservicestream.entity.AfterserviceStream;
import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.servic.BossOrderService;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.goods.entity.GoodsTo;
import com.movision.mybatis.invoice.entity.InvoiceVo;
import com.movision.mybatis.logidticsRelation.entity.LogidticsRelation;
import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import com.movision.mybatis.orderoperation.entity.Orderoperation;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.entity.OrdersVo;
import com.movision.mybatis.province.entity.Province;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BossOrderService bossOrderService;

    @Autowired
    private UserService userService;

    /**
     * 查询订单列表
     *
     * @param
     * @param
     * @return
     */
    public List<BossOrdersVo> queryOrderList(Paging<BossOrdersVo> pager) {
        List<BossOrdersVo> ordersVos = bossOrderService.queryOrderList(pager);
        if (ordersVos != null) {
            for (int i = 0; i < ordersVos.size(); i++) {
                String provice = ordersVos.get(i).getProvince();
                String city = ordersVos.get(i).getCity();
                String district = ordersVos.get(i).getDistrict();
                String prov = bossOrderService.queryprovice(provice);
                String cit = bossOrderService.querycity(city);
                String distr = bossOrderService.querydistrict(district);
                ordersVos.get(i).setProvince(prov);
                ordersVos.get(i).setCity(cit);
                ordersVos.get(i).setDistrict(distr);
            }
        }
        return ordersVos;
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
        InvoiceVo invoice = bossOrderService.queryOrderInvoiceInfo(id);//查询发票信息
        if (invoice != null) {
            String provice = invoice.getProvince();
            String city = invoice.getCity();
            String district = invoice.getDistrict();
            String prov = bossOrderService.queryprovice(provice);
            String cit = bossOrderService.querycity(city);
            String distr = bossOrderService.querydistrict(district);
            invoice.setProvince(prov);
            invoice.setCity(cit);
            invoice.setDistrict(distr);
        }
        BossOrders bossOrders = bossOrderService.queryOrderInfo(id);//查询基本信息(包含其他信息)
        AddressVo bossOrdersGet = bossOrderService.queryOrderGetInfo(id);//查询收货人信息
        if (bossOrdersGet != null) {
            String provice = bossOrdersGet.getProvince();
            String city = bossOrdersGet.getCity();
            String district = bossOrdersGet.getDistrict();
            String prov = bossOrderService.queryprovice(provice);
            String cit = bossOrderService.querycity(city);
            String distr = bossOrderService.querydistrict(district);
            bossOrdersGet.setProvince(prov);
            bossOrdersGet.setCity(cit);
            bossOrdersGet.setDistrict(distr);
            bossOrdersGet.setProvicecode(provice);
            bossOrdersGet.setCitycode(city);
            bossOrdersGet.setDistrictcode(district);
        }
        List<GoodsTo> goods = bossOrderService.queryOrderGoods(id);//查询商品信息
        Double money = 0.0;//小计
        Double summoney = 0.0;//总价
        GoodsTo good = new GoodsTo();
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
        OrdersVo orderses = bossOrderService.queryOrderMoney(id);//查询费用信息
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
        AddressVo address = new AddressVo();
        Map<String, Integer> map = new HashMap<>();
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
        InvoiceVo invoice = new InvoiceVo();
        Map<String, Integer> map = new HashMap<>();
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
        Map<String, Integer> map = new HashMap<>();
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
    public InvoiceVo queryOrderInvoice(Integer orderid) {
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
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String provice = list.get(i).getProvince();
                String city = list.get(i).getCity();
                String district = list.get(i).getDistrict();
                String prov = bossOrderService.queryprovice(provice);
                String cit = bossOrderService.querycity(city);
                String distr = bossOrderService.querydistrict(district);
                list.get(i).setProvince(prov);
                list.get(i).setCity(cit);
                list.get(i).setDistrict(distr);
            }
        }
        return list;
    }

    /**
     * 返回地址
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryOrderByAddress(Integer id) {
        Map<String, Object> map = new HashMap<>();
        AddressVo address = bossOrderService.queryOrdersByAddress(id);
        if (address != null) {
            String provice = address.getProvince();
            String city = address.getCity();
            String district = address.getDistrict();
            String prov = bossOrderService.queryprovice(provice);
            String cit = bossOrderService.querycity(city);
            String distr = bossOrderService.querydistrict(district);
            address.setProvince(prov);
            address.setCity(cit);
            address.setDistrict(distr);
            address.setProvicecode(provice);
            address.setCitycode(city);
            address.setDistrictcode(district);
            map.put("address", address);
        }
        return map;
    }

    /**
     * 售后列表
     *
     * @param
     * @return
     */
    public List<AfterServiceVo> queryAfterService(Paging<AfterServiceVo> pager) {
        return bossOrderService.queryAfterSevice(pager);
    }


    /**
     * 售后管理*--根据id查询售后信息
     *
     * @param id
     * @return
     */
    public AfterServiceVo queryAfterServiceById(Integer id) {
        AfterServiceVo afterservice = bossOrderService.queryAfterServiceById(id);
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
        Map<String, Integer> map = new HashMap<>();
        AfterServiceVo afterservice = new AfterServiceVo();
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
        afterservice.setRefundamount(Double.parseDouble(refundamount));
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
    public AfterServiceVo queryByIdAfterService(Integer id) {
        AfterServiceVo afterservice = bossOrderService.queryByIdAfterService(id);
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
    public List<AfterServiceVo> queryOrderByConditionAfterService(String ordernumber, String name, String aftersalestatus, String afterstatue, String processingstatus, String mintime, String maxtime, Paging<AfterServiceVo> pager) {
        Map<String, Object> map = new HashMap<>();
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
    public AfterServiceVo queryRemark(Integer id) {
        AfterServiceVo afterservice = bossOrderService.queryRemark(id);
        return afterservice;
    }


    /**
     * 查询售后操作信息
     *
     * @param afterserviceid
     * @return
     */
    public List<AfterserviceStream> queryAlloperate(Integer afterserviceid) {
        return bossOrderService.queryAlloperate(afterserviceid);
    }

    /**
     * 发货
     * @param id
     * @param takeway
     * @param
     * @param
     * @param remark
     * @param orderid
     * @return
     */
    public Map<String, Integer> adddelivery(String id, String takeway, String remark, String orderid, String replacementnumber) {
        Map<String, Integer> map = new HashMap<>();
        Afterservice afterservice = new Afterservice();
        afterservice.setId(Integer.parseInt(id));
        afterservice.setTakeway(Integer.parseInt(takeway));
        afterservice.setAftersalestatus(3);
        afterservice.setProcessingstatus(2);
        afterservice.setReplacementnumber(replacementnumber);
        int res = bossOrderService.updateAfterService(afterservice);
        AfterserviceStream afterserviceStream = new AfterserviceStream();
        afterserviceStream.setAfterserviceid(Integer.parseInt(id));
        afterserviceStream.setProcessingstatus(3);
        afterserviceStream.setAftersalestatus(2);
        afterserviceStream.setOrderid(Integer.parseInt(orderid));
        afterserviceStream.setRemark(remark);
        afterserviceStream.setProcessingpeople(ShiroUtil.getBossUser().getUsername());
        afterserviceStream.setProcessingtime(new Date());
        int result = bossOrderService.addAfterService(afterserviceStream);
        Orders orders = new Orders();
        orders.setLogisticsid(replacementnumber);
        orders.setId(Integer.parseInt(id));
        int ress = bossOrderService.addLogistic(orders);
        LogidticsRelation relation = new LogidticsRelation();
        relation.setLogisticsid(replacementnumber);
        relation.setCompanyid(Integer.parseInt(takeway));
        int red = bossOrderService.addLogisticsRalation(relation);
        map.put("result", result);
        map.put("res", res);
        map.put("ress", ress);
        map.put("red", red);
        return map;
    }

    /**
     * 已退回
     *
     * @param id
     * @param
     * @return
     */
    public Map<String, Integer> updateAfterStatus(String id) {
        Map<String, Integer> map = new HashMap<>();
        Afterservice afterservice = new Afterservice();
        afterservice.setId(Integer.parseInt(id));
        afterservice.setAftersalestatus(3);
        int result = bossOrderService.updateAfterStatus(afterservice);
        map.put("result", result);
        return map;
    }

    /**
     *  订单发货
     * @param id
     * @param remark
     * @param logisticsid
     * @return
     */
    public Map<String, Integer> updateOperater(String id, String remark, String takeway, String logisticsid) {
        Map<String, Integer> map = new HashMap<>();
        Orderoperation orderoperation = new Orderoperation();
        orderoperation.setOrderid(Integer.parseInt(id));
        orderoperation.setLogisticstatue(0);
        orderoperation.setOrderoperationtime(new Date());
        orderoperation.setOrderoperation(ShiroUtil.getBossUser().getUsername());
        orderoperation.setOrderstatue(0);
        orderoperation.setRemark(remark);
        int result = bossOrderService.updateOperater(orderoperation);
        Orders orders = new Orders();
        orders.setLogisticsid(logisticsid);
        orders.setId(Integer.parseInt(id));
        int res = bossOrderService.addLogistic(orders);
        LogidticsRelation relation = new LogidticsRelation();
        relation.setLogisticsid(logisticsid);
        relation.setCompanyid(Integer.parseInt(takeway));
        int red = bossOrderService.addLogisticsRalation(relation);
        map.put("result", result);
        map.put("res", res);
        return map;
    }

    /**
     * 查询所有快递公司
     *
     * @return
     */
    public List<LogisticsCompany> findAllLogisticsCompany() {
        return bossOrderService.findAllLogisticsCompany();
    }
}


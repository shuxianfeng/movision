package com.movision.mybatis.bossOrders.mapper;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.entity.AddressVo;
import com.movision.mybatis.afterservice.entity.AfterServiceVo;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservicestream.entity.AfterserviceStream;
import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsTo;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.invoice.entity.InvoiceVo;
import com.movision.mybatis.logidticsRelation.entity.LogidticsRelation;
import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import com.movision.mybatis.orderLogistics.entity.OrderLogistics;
import com.movision.mybatis.orderSubLogistics.entity.OrderSubLogistics;
import com.movision.mybatis.orderoperation.entity.Orderoperation;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.entity.OrdersVo;
import com.movision.mybatis.province.entity.Province;
import com.movision.mybatis.shopAddress.entity.ShopAddress;
import com.movision.mybatis.user.entity.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BossOrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossOrders record);

    int insertSelective(BossOrders record);

    BossOrders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossOrders record);

    int updateByPrimaryKey(BossOrders record);

    List<BossOrdersVo> findAllOrdersByList(RowBounds rowBounds);

    List<BossOrdersVo> findAllOrderByCondition(Map map, RowBounds rowBounds);

    List<BossOrdersVo> findAllAccuracyConditionByOrder(Map map, RowBounds rowBounds);

    BossOrdersVo queryOrderParticulars(Integer ordernumber);

    List<Province> findAllProvinceName();//查询省名

    List<City> findAllCityName(Integer id);//查询市名

    List<Area> findAllAreaName(Integer id);//查询区名

    Integer findAllBoss();//查询所有订单

    BossOrders findAllPerInfo(Integer id);//基本信息

    InvoiceVo findAllInvoiceInfo(Integer id);//发票信息

    AddressVo findAllGetInfo(Integer id);//收货人信息

    int deleteOrder(Integer id);//删除订单

    int updateInvoice(InvoiceVo invoice);//编辑发票

    InvoiceVo queryInvoice(Integer orderid);//返回发票信息

    int updateAddress(AddressVo address);//编辑收货人信息

    OrdersVo queryMoney(Integer id);//费用信息

    int updateOrdersMoney(Orders orders);//编辑费用信息

    List<Address> queryAddress(Integer orderid);//查询历史地址

    List<GoodsTo> queryGoods(Integer id);//查询商品信息

    int updateEmail(User user);//修改邮箱

    List<Orderoperation> queryOrderOperation(Integer id);//查看操作信息

    int updateOperation(Orderoperation orderoperation);//修改发货状态

    int addLogistic(Orders orders);//增加快递单号

    int addLogisticsRalation(LogidticsRelation logidticsRelation);

    AddressVo queryByAddress(Integer id);//返回地址

    List<AfterServiceVo> findAllAfterService(RowBounds rowBounds);//售后服务r

    AfterServiceVo queryAfterService(Integer id);//售后处理

    int updateAfterService(AfterServiceVo afterservice);//修改售后

    AfterServiceVo queryByIdAfterService(Integer id);//售后预览

    List<AfterServiceVo> findAllOrderByConditionAfterService(Map map, RowBounds rowBounds);//售后按条件查询

    AfterServiceVo queryRemark(Integer id);//查询留言

    List<AfterserviceStream> queryoperate(Integer afterserviceid);//查询操作信息

    String queryprovice(String code);//根据code查询省名

    String querycity(String code);//根据code查询市名

    String querydistrict(String code);//区名

    Integer updateAfterServiceStream(Afterservice afterservice);//修改售后信息

    Integer addAfterService(AfterserviceStream afterserviceStream);//增加一条售后信息

    Integer updateAfterStatus(Afterservice afterservice);//已退回

    List<LogisticsCompany> findAllLogisticsCompany();//查询所有快递公司

    String queryLogisticsid(String ordernumber);//根据订单号查询快递单号

    String queryLogisticsCode(String logisticsid);//根据订单号查询物流编码

    Integer addLogistics(OrderLogistics orderLogistics);//增加物流信息

    Integer addSubLogistics(OrderSubLogistics orderLogistics);//增加子表物流信息

    Integer updateLogistics(OrderLogistics orderLogistics);

    String logisticsCompany(String code);//根据code查快递公司

    Afterservice queryReturnLogistics(String ordernumber, String id);//查询退回单号

    String queryReturnWay(Integer logisticsS);//查询退回方式

    String queryReplaceLogistic(String ordernumber);//查询换货单号

    String queryReplaceCode(String logisticsid);

    InvoiceVo queryInvoiceByOrderNumber(String ordernumber);

    Integer updateAfterServiceH(Afterservice afterservice);//退货处理


    Integer queryLastOrder(Integer id);//上一个订单

    Integer queryNextOrder(Integer id);//下一个订单



    Integer queryLastAfterService(Integer id);//上一个售后

    Integer queryNextAfterService(Integer id);//下一个售后





}
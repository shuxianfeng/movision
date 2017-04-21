package com.movision.mybatis.orders.service;

import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.invoice.entity.InvoiceVo;
import com.movision.mybatis.invoice.mapper.InvoiceMapper;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.mapper.OrdersMapper;
import com.movision.mybatis.subOrder.entity.SubOrder;
import com.movision.mybatis.subOrder.entity.SubOrderVo;
import com.movision.mybatis.subOrder.mapper.SubOrderMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 13:50
 */
@Service
@Transactional
public class OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private SubOrderMapper subOrderMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    public List<Orders> findAllMyOrderList(Paging<Orders> paging, Map<String, Object> map) {
        try {
            log.info("查询我的订单列表");
            return ordersMapper.findAllMyOrderList(paging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我的订单列表失败", e);
            throw e;
        }
    }

    public Orders getOrderById(int id) {
        try {
            log.info("根据订单id获取订单");
            return ordersMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("根据订单id获取订单失败", e);
            throw e;
        }
    }

    public String queryMaxOrderNumber(String paramstr) {
        try {
            log.info("根据订单号前缀查询最大的订单号");
            return ordersMapper.queryMaxOrderNumber(paramstr);
        } catch (Exception e) {
            log.error("根据订单号前缀查询最大的订单号失败");
            throw e;
        }
    }

    public int insertOrders(Orders orders) {
        try {
            log.info("插入主订单");
            return ordersMapper.insertOrders(orders);
        } catch (Exception e) {
            log.error("插入主订单失败");
            throw e;
        }
    }

    public void batchInsertOrders(List<SubOrder> subOrderList) {
        try {
            log.info("批量提交子订单");
            subOrderMapper.batchInsertOrders(subOrderList);
        } catch (Exception e) {
            log.error("批量提交子订单失败");
            throw e;
        }
    }

    public void insertInvoice(Invoice invoice) {
        try {
            log.info("插入订单的发票信息");
            invoiceMapper.insertSelective(invoice);
        } catch (Exception e) {
            log.error("插入订单的发票信息失败");
            throw e;
        }
    }

    public int updateInvoiceInfo(Invoice invoice) {
        try {
            log.info("根据发票id更新发票信息");
            return invoiceMapper.updateByPrimaryKeySelective(invoice);
        } catch (Exception e) {
            log.error("根据发票id更新发票信息失败");
            throw e;
        }
    }

    public List<Orders> queryOrdersListByIds(int[] ids) {
        try {
            log.info("根据主订单id数组查询所有的主订单列表");
            return ordersMapper.queryOrdersListByIds(ids);
        } catch (Exception e) {
            log.error("根据主订单id数组查询所有的主订单列表失败");
            throw e;
        }
    }

    public List<SubOrder> queryAllSubOrderList(int id) {
        try {
            log.info("根据订单id数组查询所有子订单列表");
            return subOrderMapper.queryAllSubOrderList(id);
        } catch (Exception e) {
            log.error("根据订单id数组查询所有子订单列表失败");
            throw e;
        }
    }

    public List<SubOrderVo> querySubOrderListById(int orderid) {
        try {
            log.info("根据订单id查询子订单列表");
            return subOrderMapper.querySubOrderListById(orderid);
        } catch (Exception e) {
            log.error("根据订单id查询子订单列表失败");
            throw e;
        }
    }

    public SubOrderVo querySubOrderInfo(Map<String, Object> parammap) {
        try {
            log.info("查询子订单相关信息");
            return subOrderMapper.querySubOrderInfo(parammap);
        } catch (Exception e) {
            log.error("查询子订单相关信息失败");
            throw e;
        }
    }

    public Invoice queryInvoiceInfo(int orderid) {
        try {
            log.info("根据订单id查询发票信息");
            return invoiceMapper.queryInvoiceInfo(orderid);
        } catch (Exception e) {
            log.error("根据订单id查询发票信息失败");
            throw e;
        }
    }

    public InvoiceVo queryInvoice(String ordernumber) {
        try {
            log.info("通过订单编号查询发票相关信息");
            return invoiceMapper.queryInvoice(ordernumber);
        } catch (Exception e) {
            log.error("通过订单编号查询发票相关信息失败");
            throw e;
        }
    }

    public void confirmReceipt(String ordernumber) {
        try {
            log.info("APP端根据14位订单编号对订单进行确认");
            ordersMapper.confirmReceipt(ordernumber);
        } catch (Exception e) {
            log.error("APP端根据14位订单编号对订单进行确认失败");
            throw e;
        }
    }

    public void updateOrder(Map<String, Object> parammap) {
        try {
            log.info("支付回调更新订单信息");
            ordersMapper.updateOrder(parammap);
        } catch (Exception e) {
            log.error("支付回调更新订单信息失败");
            throw e;
        }
    }

    public void updateStock(Map<String, Object> map) {
        try {
            log.info("更新数据库商品销量数据");
            goodsMapper.updateStock(map);
        } catch (Exception e) {
            log.error("更新数据库商品销量数据失败");
            throw e;
        }
    }

    public void addPoints(Map<String, Object> map) {
        try {
            log.info("购买租赁商品后增加用户积分数");
            ordersMapper.addPoints(map);
        } catch (Exception e) {
            log.error("购买租赁商品后增加用户积分数失败");
            throw e;
        }
    }

    public void addPointsRecored(Map<String, Object> map) {
        try {
            log.info("购买租赁商品后增加用户积分记录");
            ordersMapper.addPointsRecored(map);
        } catch (Exception e) {
            log.error("购买租赁商品后增加用户积分记录失败");
            throw e;
        }
    }

    public void updateOrderByIntegral(Integer id) {
        try {
            log.info("退款成功后变更使用优惠券状态");
            ordersMapper.updateOrderByIntegral(id);
        } catch (Exception e) {
            log.error("退款成功后变更使用优惠券状态异常");
            throw e;
        }
    }

    public void updateOrderDiscount(String couponsid) {
        try {
            log.info("退款成功后退还优惠券");
            ordersMapper.updateOrderDiscount(couponsid);
        } catch (Exception e) {
            log.error("退款成功后退还优惠券异常");
            throw e;
        }
    }

    public List<Integer> queryOrdersListByTradingAccount(String tradingAccount) {
        try {
            log.info("根据订单的支付宝交易号查询在同一次支付的所有订单的主订单号列表");
            return ordersMapper.queryOrdersListByTradingAccount(tradingAccount);
        } catch (Exception e) {
            log.error("根据订单的支付宝交易号查询在同一次支付的所有订单的主订单号列表失败");
            throw e;
        }
    }
}

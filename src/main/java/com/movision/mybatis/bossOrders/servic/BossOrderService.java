package com.movision.mybatis.bossOrders.servic;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.entity.AddressVo;
import com.movision.mybatis.afterservice.entity.AfterServiceVo;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservicestream.entity.AfterserviceStream;
import com.movision.mybatis.area.entity.Area;
import com.movision.mybatis.bossOrders.entity.BossOrders;
import com.movision.mybatis.bossOrders.entity.BossOrdersVo;
import com.movision.mybatis.bossOrders.mapper.BossOrdersMapper;
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsTo;
import com.movision.mybatis.invoice.entity.Invoice;
import com.movision.mybatis.invoice.entity.InvoiceVo;
import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import com.movision.mybatis.orderoperation.entity.Orderoperation;
import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.entity.OrdersVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.province.entity.Province;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author zhurui
 * @Date 2017/2/6 10:55
 */
@Service
@Transactional
public class BossOrderService {

    @Autowired
    BossOrdersMapper bossOrdersMapper;
    Logger loger = LoggerFactory.getLogger(BossOrderService.class);

    /**
     * 查询订单列表
     *
     * @param pager
     * @return
     */
    public List<BossOrdersVo> queryOrderList(Paging<BossOrdersVo> pager) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("查询订单列表");
            }
            return bossOrdersMapper.findAllOrdersByList(pager.getRowBounds());
        } catch (Exception e) {
            loger.error("查询订单列表异常");
            throw e;
        }
    }

    /**
     * 订单管理--查询历史地址
     *
     * @param orderid
     * @return
     */
    public List<Address> queryOrders(Integer orderid) {
        try {
            loger.info("查询历史地址成功");
            return bossOrdersMapper.queryAddress(orderid);
        } catch (Exception e) {
            loger.error("查询历史地址失败");
            throw e;
        }
    }

    /**
     * 订单管理--返回地址
     * @param id
     * @return
     */
    public AddressVo queryOrdersByAddress(Integer id) {
        try {
            loger.info("返回地址");
            return bossOrdersMapper.queryByAddress(id);
        } catch (Exception e) {
            loger.error("返回地址失败");
            throw e;
        }
    }

    /**
     * 所有订单
     *
     * @return
     */
    public int queryOrderAll() {
        try {
            loger.info("查询成功");
            return bossOrdersMapper.findAllBoss();
        } catch (Exception e) {
            loger.error("查询失败");
            throw e;
        }
    }
    /**
     * 订单管理-查询收货人信息
     *
     * @param id
     * @return
     */
    public AddressVo queryOrderGetInfo(Integer id) {
        try {
            loger.info("查询收货人信息");
            return bossOrdersMapper.findAllGetInfo(id);
        } catch (Exception e) {
            loger.error("查询收货人信息失败");
            throw e;
        }
    }

    /**
     * 订单管理--返回发票信息
     *
     * @param orderid
     * @return
     */
    public InvoiceVo queryOrderInvoice(Integer orderid) {
        try {
            loger.info("返回发票成功");
            return bossOrdersMapper.queryInvoice(orderid);
        } catch (Exception e) {
            loger.error("返回发票成功");
            throw e;
        }
    }

    /**
     * 订单管理-编辑发票
     *
     * @param
     * @return
     */
    public int updateOrderInvoice(InvoiceVo invoice) {
        try {
            loger.info("修改发票");
            return bossOrdersMapper.updateInvoice(invoice);
        } catch (Exception e) {
            loger.error("修改发票失败");
            throw e;
        }
    }

    /**
     * 订单管理--编辑收货地址
     *
     * @param address
     * @return
     */
    public int updateOrderGet(AddressVo address) {
        try {
            loger.info("编辑收货地址成功");
            return bossOrdersMapper.updateAddress(address);
        } catch (Exception e) {
            loger.error("编辑收货地址失败");
            throw e;
        }
    }

    /**
     * 售后列表
     *
     * @param pager
     * @return
     */
    public List<AfterServiceVo> queryAfterSevice(Paging<AfterServiceVo> pager) {
        try {

            loger.info("售后列表");
            return bossOrdersMapper.findAllAfterService(pager.getRowBounds());
        } catch (Exception e) {
            loger.error("售后列表失败");
            throw e;
        }
    }
    /**
     * 订单管理-编辑费用信息
     * @param orders
     * @return
     */
    public int updateOrderMoney(Orders orders) {
        try {
            loger.info("编辑费用信息成功");
            return bossOrdersMapper.updateOrdersMoney(orders);
        } catch (Exception e) {
            loger.error("编辑费用信息失败");
            throw e;
        }
    }

    /**
     * 修改邮箱
     *
     * @param user
     * @return
     */
    public int updateOrderEmail(User user) {
        try {
            loger.info("修改邮箱");
            return bossOrdersMapper.updateEmail(user);
        } catch (Exception e) {
            loger.info("修改邮箱失败");
            throw e;
        }
    }

    /**
     * 订单管理*--查询发票
     *
     * @param id
     * @return
     */
    public InvoiceVo queryOrderInvoiceInfo(Integer id) {
        try {
            loger.info("查询发票");
            return bossOrdersMapper.findAllInvoiceInfo(id);
        } catch (Exception e) {
            loger.error("查询发票失败");
            throw e;
        }
    }
    /**
     * 订单管理-查询基本信息
     * @param
     * @return
     */
    public  BossOrders queryOrderInfo(Integer id){
        try{
            loger.info("查询基本信息");
            return bossOrdersMapper.findAllPerInfo(id);
        }catch (Exception e){
            loger.error("查询基本信息失败");
            throw e;
        }
    }

    /**
     * 订单管理--查看操作信息
     * @param id
     * @return
     */
    public List<Orderoperation> queryOrderoperation(Integer id) {
        try {
            loger.info("查看操作信息");
            return bossOrdersMapper.queryOrderOperation(id);
        } catch (Exception e) {
            loger.error("查看操作信息失败");
            throw e;
        }
    }

    /**
     * 订单管理*--删除订单
     * @param id
     * @return
     */
    public  int  deleteOrder(Integer id){
        try{
            loger.info("删除订单");
            return bossOrdersMapper.deleteOrder(id);
        }catch (Exception e){
            loger.error("删除订单失败");
            throw e;
        }

    }
    /**
     * 根据条件查询订单
     *
     * @param map
     * @return
     */
    public List<BossOrdersVo> queryOrderByCondition(Map map, Paging<BossOrdersVo> pager) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("根据条件查询订单");
            }
            return bossOrdersMapper.findAllOrderByCondition(map, pager.getRowBounds());
        } catch (Exception e) {
            loger.error("根据条件查询订单");
            throw e;
        }
    }

    /**
     * 售后管理--根据条件查询
     * @param map
     * @param pager
     * @return
     */
    public List<AfterServiceVo> queryOrderByConditionAfterService(Map map, Paging<AfterServiceVo> pager) {
        try {
            loger.info("根据条件查询列表");
            return bossOrdersMapper.findAllOrderByConditionAfterService(map, pager.getRowBounds());
        } catch (Exception e) {
            loger.error("根据条件查询列表失败");
            throw e;
        }
    }

    /**
     * 订单管理*--查询费用信息
     *
     * @param id
     * @return
     */
    public OrdersVo queryOrderMoney(Integer id) {
        try {
            loger.info("查询费用信息成功");
            return bossOrdersMapper.queryMoney(id);
        } catch (Exception e) {
            loger.error("查询费用信息失败");
            throw e;
        }
    }
    /**
     * 精确查询订单
     *
     * @param map
     * @return
     */
    public List<BossOrdersVo> queryAccuracyConditionByOrder(Map map, Paging<BossOrdersVo> pager) {
        try {
            loger.info("精确查询订单");
            return bossOrdersMapper.findAllAccuracyConditionByOrder(map, pager.getRowBounds());
        } catch (Exception e) {
            loger.error("精确查询订单异常");
            throw e;
        }
    }

    /**
     * 后台管理-查询市名
     * @param id
     * @return
     */
    public List<City> queryPostCityName(Integer id){
        try{
            loger.error("查询市名成功");
            return  bossOrdersMapper.findAllCityName(id);
        }catch (Exception e){

            loger.error("查询市名失败");
            throw e;
        }


    }
    /**
     * 后台管理-查询区名
     * @param id
     * @return
     */
    public List<Area> queryPostAreaName(Integer id){
        try{
            loger.error("查询区名成功");
            return  bossOrdersMapper.findAllAreaName(id);
        }catch (Exception e){

            loger.error("查询区名失败");
            throw e;
        }


    }
    /**
     * 后台管理-查询省名
     * @param
     * @return
     */
    public List<Province> queryPostProvinceName() {
        try{
            loger.error("查询省名成功");
            return bossOrdersMapper.findAllProvinceName();
        }catch (Exception e){

            loger.error("查询省名失败");
            throw e;
        }


    }

    public BossOrdersVo queryOrderParticulars(Integer ordernumber) {
        try {
            if (loger.isDebugEnabled()) {
                loger.debug("查询订单基本信息");
            }
            return bossOrdersMapper.queryOrderParticulars(ordernumber);
        } catch (Exception e) {
            loger.error("订单基本信息查询异常");
            throw e;
        }
    }

    /**
     * 订单管理-查询商品信息
     *
     * @param id
     * @return
     */
    public List<GoodsTo> queryOrderGoods(Integer id) {
        try {
            loger.info("查询商品信息成功");
            return bossOrdersMapper.queryGoods(id);
        } catch (Exception e) {
            loger.error("查询商品信息失败");
            throw e;
        }
    }

    /**
     * 售后管理--根据id查询
     *
     * @param id
     * @return
     */
    public AfterServiceVo queryAfterServiceById(Integer id) {
        try {
            loger.info("根据id查售后信息");
            return bossOrdersMapper.queryAfterService(id);
        } catch (Exception e) {
            loger.error("根据id查售后信息失败");
            throw e;
        }
    }

    /**
     * 售后管理--修改售后信息
     *
     * @param afterservice
     * @return
     */
    public int updateAfterService(AfterServiceVo afterservice) {
        try {
            loger.info("修改售后信息");
            return bossOrdersMapper.updateAfterService(afterservice);
        } catch (Exception e) {
            loger.error("修改售后信息失败");
            throw e;
        }
    }

    /**
     * 售后管理*--售后预览
     *
     * @param id
     * @return
     */
    public AfterServiceVo queryByIdAfterService(Integer id) {
        try {
            loger.info("售后预览");
            return bossOrdersMapper.queryByIdAfterService(id);
        } catch (Exception e) {
            loger.error("售后预览失败");
            throw e;
        }
    }

    /**
     * 售后管理--查询留言
     *
     * @param id
     * @return
     */
    public AfterServiceVo queryRemark(Integer id) {
        try {
            loger.info("查询留言成功");
            return bossOrdersMapper.queryRemark(id);
        } catch (Exception e) {
            loger.error("查询留言失败");
            throw e;
        }
    }

    /**
     * 查询售后操作信息
     *
     * @param afterserviceid
     * @return
     */
    public List<AfterserviceStream> queryAlloperate(Integer afterserviceid) {
        try {
            loger.info("查询售后操作信息");
            return bossOrdersMapper.queryoperate(afterserviceid);
        } catch (Exception e) {
            loger.error("查询售后操作信息失败", e);
            throw e;
        }
    }

    /**
     * 根据code查询省名
     *
     * @param code
     * @return
     */
    public String queryprovice(String code) {
        try {
            loger.info("根据code查询省名");
            return bossOrdersMapper.queryprovice(code);
        } catch (Exception e) {
            loger.error("根据code查询省名失败", e);
            throw e;
        }
    }

    /**
     * 根据code查询市名
     *
     * @param code
     * @return
     */
    public String querycity(String code) {
        try {
            loger.info("根据code查询市名");
            return bossOrdersMapper.querycity(code);
        } catch (Exception e) {
            loger.error("根据code查询市名失败", e);
            throw e;
        }
    }

    /**
     * 根据code查询区名
     *
     * @param code
     * @return
     */
    public String querydistrict(String code) {
        try {
            loger.info("根据code查询区名");
            return bossOrdersMapper.querydistrict(code);
        } catch (Exception e) {
            loger.error("根据code查询区名失败", e);
            throw e;
        }
    }

    /**
     * 修改售后信息
     *
     * @param afterserviceStream
     * @return
     */
    public Integer updateAfterService(Afterservice afterserviceStream) {
        try {
            loger.info("修改售后信息");
            return bossOrdersMapper.updateAfterServiceStream(afterserviceStream);
        } catch (Exception e) {
            loger.error("修改售后信息失败", e);
            throw e;
        }
    }

    /**
     * 增加售后信息
     *
     * @param afterserviceStream
     * @return
     */
    public Integer addAfterService(AfterserviceStream afterserviceStream) {
        try {
            loger.info("增加售后信息");
            return bossOrdersMapper.addAfterService(afterserviceStream);
        } catch (Exception e) {
            loger.error("增加售后信息失败", e);
            throw e;
        }
    }

    /**
     * 已退回
     *
     * @param afterservice
     * @return
     */
    public Integer updateAfterStatus(Afterservice afterservice) {
        try {
            loger.info("已退回");
            return bossOrdersMapper.updateAfterStatus(afterservice);
        } catch (Exception e) {
            loger.error("已退回失败", e);
            throw e;
        }
    }

    /**
     * 修改发货状态
     *
     * @param orderoperation
     * @return
     */
    public Integer updateOperater(Orderoperation orderoperation) {
        try {
            loger.info("修改发货状态");
            return bossOrdersMapper.updateOperation(orderoperation);
        } catch (Exception e) {
            loger.error("修改发货状态失败", e);
            throw e;
        }
    }

    /**
     * 增加快递单号
     *
     * @param orders
     * @return
     */
    public Integer addLogistic(Orders orders) {
        try {
            loger.info("增加快递单号");
            return bossOrdersMapper.addLogistic(orders);
        } catch (Exception e) {
            loger.error("增加快递单号失败", e);
            throw e;
        }
    }

    /**
     * 查询所有快递公司
     *
     * @return
     */
    public List<LogisticsCompany> findAllLogisticsCompany() {
        try {
            loger.info("查询所有快递公司");
            return bossOrdersMapper.findAllLogisticsCompany();
        } catch (Exception e) {
            loger.error("查询所有快递公司失败", e);
            throw e;
        }
    }
}

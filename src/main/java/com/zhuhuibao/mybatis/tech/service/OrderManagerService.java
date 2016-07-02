package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import com.zhuhuibao.mybatis.order.service.OrderFlowService;
import com.zhuhuibao.mybatis.tech.entity.OrderOms;
import com.zhuhuibao.mybatis.tech.mapper.OrderManagerMapper;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author Administrator
 * @version 2016/6/6 0006
 */
@Service
@Transactional
public class OrderManagerService {

    private final static Logger log = LoggerFactory.getLogger(OrderManagerService.class);

    @Autowired
    OrderManagerMapper orderMapper;

    @Autowired
    OrderFlowService orderFlowService;

    @Autowired
    ZhbService zhbService;

    /**
     * 查询已发布的课程
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String,String>> findAllOmsTechOrder(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all oms order for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> orderList;
        try{
            orderList = orderMapper.findAllOmsTechOrder(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all oms order for pager error!",e);
            throw e;
        }
        return orderList;
    }

    /**
     * 查询已发布的课程
     * @param condition
     * @return
     */
    public Map<String,Object> selectOrderDetail(Map<String,Object> condition)
    {
        log.info("find all oms order for pager "+ StringUtils.mapToString(condition));
        Map<String,Object> orderMap;
        try{
            List<OrderFlow> orderFlows = orderFlowService.findByOrderNo((String) condition.get("orderNo"));
            orderMap = orderMapper.selectByPrimaryKey(condition);
            if(!orderFlows.isEmpty())
            {
                Map<String,Object> paymentInfo = new HashMap<String,Object>();
                for(OrderFlow flow : orderFlows)
                {
                    if(OrderConstants.OrderFlowTradeMode.ALIPAY.toString().equals(flow.getTradeMode()) ) {
                        paymentInfo.put("aliTradeMode", OrderConstants.OrderFlowTradeModeName.ALIPAY.toString());
                        paymentInfo.put("aliTradeFee", flow.getTradeFee());
                        paymentInfo.put("aliTradeTime", DateUtils.date2Str(flow.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
                    }else if(OrderConstants.OrderFlowTradeMode.ZHB.toString().equals(flow.getTradeMode()))
                    {
                        paymentInfo.put("zhbTradeMode", OrderConstants.OrderFlowTradeModeName.ZHB.toString());
                        paymentInfo.put("zhbTradeFee", flow.getTradeFee());
                        paymentInfo.put("zhbTradeTime", DateUtils.date2Str(flow.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
                if(orderMap==null)
                {
                	orderMap=new HashMap<String,Object>();
                }
                orderMap.put("paymentInfo",paymentInfo);
            }
        }catch(Exception e)
        {
            log.error("find all oms order for pager error!",e);
            throw e;
        }
        return orderMap;
    }

    /**
     * 更新订单状态
     * @param orderNo
     * @param status
     * @return
     */
    public int updateByPrimaryKeySelective(String orderNo,String status)
    {
        log.info("update order status = "+status+" orderNo = "+orderNo);
        int result = 0;
        try {
            OrderOms record = new OrderOms();
            record.setStatus(status);
            record.setOrderNo(orderNo);
            result = orderMapper.updateByPrimaryKeySelective(record);
        }catch(Exception e)
        {
            log.error("update order status error!");
            throw e;
        }
        return result;
    }

    /**
     * 查询收银台初始信息  1:培训课程购买使用筑慧币消费的情况.  0:VIP充值，筑慧币购买不使用筑慧币的情况
     * @param orderNo  订单编号
     * @return
     */
    public Map<String,Object> selectCashierDeskInfo(String orderNo)
    {
        log.info("select casher desk init info orderNo = "+orderNo);
        Map<String,Object> deskInfoMap = orderMapper.selectCashierDeskInfo(orderNo);
        //1:使用筑慧币消费的情况.  0:VIP充值，筑慧币购买不使用筑慧币的情况
        if (!deskInfoMap.isEmpty()) {
            String duration = OrderConstants.CASHIER_PAYMENT_DURATION_24;
            if(OrderConstants.GoodsType.VIP.toString().equals(deskInfoMap.get("goodsType")) || OrderConstants.GoodsType.ZHB.toString().equals(deskInfoMap.get("goodsType")))
            {
                this.useZhbByCashierDesk(deskInfoMap,0);
            }
            else if(OrderConstants.GoodsType.JSPX.toString().equals(deskInfoMap.get("goodsType")) || OrderConstants.GoodsType.ZJPX.toString().equals(deskInfoMap.get("goodsType")))
            {
                this.useZhbByCashierDesk(deskInfoMap,1);
                duration = OrderConstants.CASHIER_PAYMENT_DURATION_HALF;
            }
            deskInfoMap.put("duration",duration);
            deskInfoMap.put("currency",OrderConstants.CASHIER_PAYMENT_CURRENCY_YUAN);
            deskInfoMap.remove("goodsType");
        }
        return deskInfoMap;
    }

    /**
     * 查询收银台使用筑慧币支付
     * @return
     */
    public Map<String,Object> useZhbByCashierDesk(Map<String,Object> deskInfoMap,int isUseZhb)
    {
        if(!deskInfoMap.isEmpty())
        {
            //实付金额
            BigDecimal payAmount = (BigDecimal) deskInfoMap.get("payAmount");
            Map<String,Object> payMap = null;
            if(isUseZhb == 1) {
                ZhbAccount zhbAccount = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
                if (zhbAccount != null && zhbAccount.getAmount() != null) {
                    deskInfoMap.put("zhbtotal", zhbAccount.getAmount());
                    payMap = new HashMap<String,Object>();
                    BigDecimal zhbAmount = zhbAccount.getAmount();
                    //筑慧币小于支付金额
                    if (zhbAmount.compareTo(payAmount) == -1) {
                        payMap.put("zhb", zhbAmount);
                        BigDecimal alipay = payAmount.subtract(zhbAmount);
                        payMap.put("pay", alipay);
                    }
                    //1:筑慧币大于支付金额,0:筑慧币等于支付金额
                    else if (zhbAmount.compareTo(payAmount) == 1 || zhbAmount.compareTo(payAmount) == 0) {
                        payMap.put("zhb", payAmount);
                    }
                }else{
                }
            }else{
            }
            deskInfoMap.put("pay",payMap);
        }
        return deskInfoMap;
    }
}

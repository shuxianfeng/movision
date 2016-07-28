package com.zhuhuibao.mybatis.tech.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Area;
import com.zhuhuibao.mybatis.memCenter.entity.City;
import com.zhuhuibao.mybatis.memCenter.entity.Province;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.mybatis.tech.entity.OrderOms;
import com.zhuhuibao.mybatis.tech.mapper.OrderManagerMapper;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    OrderGoodsService orderGoodsService;

    @Autowired
    OrderFlowService orderFlowService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ZhbService zhbService;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    PublishCourseService publishCourseService;

    @Autowired
    PwdTicketService pwdTicketService;

    @Autowired
    VipInfoService vipInfoService;

    /**
     * 查询已发布的课程
     *
     * @param pager
     * @param condition
     * @return
     */
    public List<Map<String, String>> findAllOmsTechOrder(Paging<Map<String, String>> pager, Map<String, Object> condition) {
        log.info("find all oms order for pager " + StringUtils.mapToString(condition));
        List<Map<String, String>> orderList;
        try {
            orderList = orderMapper.findAllOmsTechOrder(pager.getRowBounds(), condition);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return orderList;
    }

    /**
     * 查询已发布的课程      (废弃)
     *
     * @param condition
     * @return
     */
    public Map<String, Object> selectOrderDetail(Map<String, Object> condition) {
        log.info("find all oms order for pager " + StringUtils.mapToString(condition));
        Map<String, Object> orderMap;
        try {
            List<OrderFlow> orderFlows = orderFlowService.findByOrderNo((String) condition.get("orderNo"));
            orderMap = orderMapper.selectByPrimaryKey(condition);
            if (!orderFlows.isEmpty()) {
                Map<String, Object> paymentInfo = new HashMap<>();
                for (OrderFlow flow : orderFlows) {
                    if (OrderConstants.OrderFlowTradeMode.ALIPAY.toString().equals(flow.getTradeMode())) {
                        paymentInfo.put("aliTradeMode", OrderConstants.OrderFlowTradeModeName.ALIPAY.toString());
                        paymentInfo.put("aliTradeFee", flow.getTradeFee());
                        paymentInfo.put("aliTradeTime", DateUtils.date2Str(flow.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
                    } else if (OrderConstants.OrderFlowTradeMode.ZHB.toString().equals(flow.getTradeMode())) {
                        paymentInfo.put("zhbTradeMode", OrderConstants.OrderFlowTradeModeName.ZHB.toString());
                        paymentInfo.put("zhbTradeFee", flow.getTradeFee());
                        paymentInfo.put("zhbTradeTime", DateUtils.date2Str(flow.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
                if (orderMap == null) {
                    orderMap = new HashMap<>();
                }
                orderMap.put("paymentInfo", paymentInfo);
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
        return orderMap;
    }

    /**
     * 更新订单状态
     *
     * @param orderNo
     * @param status
     * @return
     */
    public int updateByPrimaryKeySelective(String orderNo, String status) {
        log.info("update order status = " + status + " orderNo = " + orderNo);
        int result;
        try {
            OrderOms record = new OrderOms();
            record.setStatus(status);
            record.setOrderNo(orderNo);
            result = orderMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw e;
        }
        return result;
    }

    /**
     * 查询收银台初始信息  1:培训课程购买使用筑慧币消费的情况.  0:VIP充值，筑慧币购买不使用筑慧币的情况
     *
     * @param orderNo 订单编号
     * @return
     */
    public Map<String, Object> selectCashierDeskInfo(String orderNo) {
        log.info("select casher desk init info orderNo = " + orderNo);
        Map<String, Object> deskInfoMap = orderMapper.selectCashierDeskInfo(orderNo);
        //1:使用筑慧币消费的情况.  0:VIP充值，筑慧币购买不使用筑慧币的情况
        if (!deskInfoMap.isEmpty()) {
            String duration = OrderConstants.CASHIER_PAYMENT_DURATION_24;
            if (OrderConstants.GoodsType.VIP.toString().equals(deskInfoMap.get("goodsType")) || OrderConstants.GoodsType.ZHB.toString().equals(deskInfoMap.get("goodsType"))) {
                this.useZhbByCashierDesk(deskInfoMap, 0);
            } else if (OrderConstants.GoodsType.JSPX.toString().equals(deskInfoMap.get("goodsType")) || OrderConstants.GoodsType.ZJPX.toString().equals(deskInfoMap.get("goodsType"))) {
                this.useZhbByCashierDesk(deskInfoMap, 1);
                duration = OrderConstants.CASHIER_PAYMENT_DURATION_HALF;
            }
            deskInfoMap.put("duration", duration);
            deskInfoMap.put("currency", OrderConstants.CASHIER_PAYMENT_CURRENCY_YUAN);
            deskInfoMap.remove("goodsType");
        }
        return deskInfoMap;
    }

    /**
     * 查询收银台使用筑慧币支付
     *
     * @return
     */
    public Map<String, Object> useZhbByCashierDesk(Map<String, Object> deskInfoMap, int isUseZhb) {
        if (!deskInfoMap.isEmpty()) {
            //实付金额
            BigDecimal payAmount = (BigDecimal) deskInfoMap.get("payAmount");
            Map<String, Object> payMap = null;
            if (isUseZhb == 1) {
                ZhbAccount zhbAccount = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
                if (zhbAccount != null && zhbAccount.getAmount() != null) {
                    deskInfoMap.put("zhbtotal", zhbAccount.getAmount());
                    payMap = new HashMap<>();
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
                }
            }
            deskInfoMap.put("pay", payMap);
        }
        return deskInfoMap;
    }

    /**
     * 查询订单详情
     *
     * @param orderNo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public Map<String, Object> getOrderDetail(String orderNo, Long memberId) {
        Map<String, Object> detailMap = new HashMap<>();

        try {
            //订单信息
            Order order = orderMapper.findByOrderNo(orderNo);
            if (order == null || (order.getBuyerId().compareTo(ShiroUtil.getCompanyID()) != 0 && order.getBuyerId().compareTo(ShiroUtil.getCreateID()) != 0)) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "订单不存在");
            }
            Map<String, Object> baseInfoMap = new HashMap<>();
            baseInfoMap.put("orderNo", order.getOrderNo());
            baseInfoMap.put("status", order.getStatus());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dtime = order.getDealTime();
            baseInfoMap.put("dealTime", sf.format(dtime));
            baseInfoMap.put("goodsType",order.getGoodsType());

            detailMap.put("baseInfo", baseInfoMap);

            //订单商品信息
            genOrderGoodsInfo(orderNo, order.getGoodsType(), memberId, detailMap);

            //订单付款信息
            genOrderPayInfo(orderNo, detailMap);
            //发票信息
            genInvoiceInfo(orderNo, detailMap);

            //培训课程 && 已支付订单  查询培训凭证
            genPwdticketInfo(orderNo, detailMap, order);


        } catch (Exception e) {
            log.error("查询异常>>>",e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "查询失败");
        }

        return detailMap;
    }

    /**
     * 获取发票信息
     *
     * @param orderNo
     * @param detailMap
     * @param order
     */
    private void genPwdticketInfo(String orderNo, Map<String, Object> detailMap, Order order) {
        if (order.getGoodsType().equals(OrderConstants.GoodsType.JSPX.toString()) ||
                order.getGoodsType().equals(OrderConstants.GoodsType.ZJPX.toString())) {
            if (order.getStatus().equals(PayConstants.OrderStatus.YZF.toString())) {
                List<PwdTicket> pwdticks = pwdTicketService.findByOrderNo(orderNo);
                Map<String, Object> pwdtickMap = new HashMap<>();
                pwdtickMap.put("mobile", pwdticks.get(0).getMobile());
                StringBuilder sb = new StringBuilder();
                for (PwdTicket ticket : pwdticks) {
                    sb.append(ticket.getSnCode()).append(",");
                }
                String snCodes = sb.toString();
                pwdtickMap.put("snCodes", snCodes.substring(0, snCodes.length() - 1));

                detailMap.put("pwdticketInfo", pwdtickMap);
            }
        }
    }

    /**
     * 获取商品订单信息
     *
     * @param orderNo
     * @param detailMap
     */
    private void genOrderGoodsInfo(String orderNo, String goodsType, Long memberId, Map<String, Object> detailMap) {
        OrderGoods orderGoods = orderGoodsService.findByOrderNo(orderNo);
        if (orderGoods != null) {
            Map<String, Object> goodsMap = new HashMap<>();
            goodsMap.put("goodsName", orderGoods.getGoodsName());
            goodsMap.put("goodsPrice", orderGoods.getGoodsPrice().toString());
            goodsMap.put("number", orderGoods.getNumber());
            //课程详细信息
            if (goodsType.equals(OrderConstants.GoodsType.JSPX.toString()) ||
                    goodsType.equals(OrderConstants.GoodsType.ZJPX.toString())) {

                PublishCourse course = publishCourseService.getCourseById(orderGoods.getGoodsId());
                Map<String, Object> courseMap = new HashMap<>();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                courseMap.put("startTime", sf.format(course.getStartTime()));
                courseMap.put("endTime", sf.format(course.getEndTime()));
                String address = genAddress(course.getProvince(), course.getCity(), course.getArea(), course.getAddress());
                courseMap.put("address", address);
                courseMap.put("price", course.getPrice().toString());
                courseMap.put("minBuyNumber", course.getMinBuyNumber());
                courseMap.put("imgUrl", course.getImgUrl());
                courseMap.put("courseStatus",course.getStatus());

                goodsMap.put("courseDetailInfo", courseMap);
            } else if (goodsType.equals(OrderConstants.GoodsType.VIP.toString())) {
                //订单类型是VIP goodsType = 3
                //查询所购买的VIP特权内容
                Map<String, Object> vipinfoMap = new HashMap<>();
                List<Map<String, String>> viprecords = vipInfoService.findVipInfoByID(orderGoods.getGoodsId());
                List<String> vippNames = new ArrayList<>();
                for (Map<String, String> record : viprecords) {
                    vippNames.add(record.get("name"));
                }
                vipinfoMap.put("vipnames", vippNames);
                //所购VIP年限 登陆用户
                Calendar cal = Calendar.getInstance();
                cal.setTime(orderGoods.getCreateTime());
                cal.add(Calendar.YEAR, 1);
                Date expireTime = cal.getTime();
                Date activeTime = orderGoods.getCreateTime();
                long days = DateUtils.dayDiff(activeTime,expireTime);
                goodsMap.put("expireTime", days);
                goodsMap.put("vipinfo", vipinfoMap);
            }
            detailMap.put("goodsInfo", goodsMap);
        } else {
            detailMap.put("goodsInfo", "");
        }
    }

    /**
     * 获取订单支付信息
     *
     * @param orderNo
     * @param detailMap
     */
    private void genOrderPayInfo(String orderNo, Map<String, Object> detailMap) {
        List<OrderFlow> orderFlows = orderFlowService.findByOrderNo(orderNo);
        if (orderFlows.size() > 0) {
            List<Map<String, Object>> payinfoList = new ArrayList<>();
            for (OrderFlow flow : orderFlows) {
                Map<String, Object> payinfoMap = new HashMap<>();
                payinfoMap.put("tradeMode", flow.getTradeMode());
                payinfoMap.put("tradeStatus", flow.getTradeStatus());
                payinfoMap.put("tradeFee", flow.getTradeFee().toString());
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(flow.getTradeTime() != null){
                    payinfoMap.put("tradeTime", sf.format(flow.getTradeTime()));
                }else{
                    payinfoMap.put("tradeTime", "");
                }

                payinfoList.add(payinfoMap);
            }

            detailMap.put("payInfo", payinfoList);
        } else {
            detailMap.put("payInfo", "");
        }
    }

    /**
     * 获取发票信息
     *
     * @param orderNo
     * @param detailMap
     */
    private void genInvoiceInfo(String orderNo, Map<String, Object> detailMap) {
        Invoice invoice = invoiceService.findByOrderNo(orderNo);
        if (invoice != null) {
            Map<String, Object> invoiceinfoMap = new HashMap<>();
            invoiceinfoMap.put("invoiceType", invoice.getInvoiceType());
            invoiceinfoMap.put("invoiceTitleType", invoice.getInvoiceTitleType());
            invoiceinfoMap.put("invoiceTitle", invoice.getInvoiceTitle());
            invoiceinfoMap.put("receiveName", invoice.getReceiveName());
            String address = genAddress(invoice.getProvince(), invoice.getCity(), invoice.getArea(), invoice.getAddress());
            invoiceinfoMap.put("address", StringUtils.isEmpty(address) ? "" : address);
            invoiceinfoMap.put("mobile", invoice.getMobile());
            invoiceinfoMap.put("telephone", invoice.getTelephone());
            invoiceinfoMap.put("status", invoice.getStatus());
            invoiceinfoMap.put("invoiceNum", invoice.getInvoiceNum());
            invoiceinfoMap.put("expressNum", invoice.getExpressNum());

            detailMap.put("invoiceInfo", invoiceinfoMap);
        } else {
            detailMap.put("invoiceInfo", "");
        }
    }

    /**
     * 拼接完整地址
     *
     * @param provinceCode
     * @return
     */
    private String genAddress(String provinceCode, String cityCode, String areaCode, String oaddress) {
        String address = null;
        if (!StringUtils.isEmpty(provinceCode)) {
            Province province = dictionaryService.selectProvinceByCode(provinceCode);
            String provinceName = StringUtils.isEmpty(province.getName()) ? "" : province.getName();

            String cityName = "";
            if (!StringUtils.isEmpty(cityCode)) {
                City city = dictionaryService.selectCityByCode(cityCode);
                cityName = city.getName();
            }

            String areaName = "";
            if (!StringUtils.isEmpty(areaCode)) {
                Area area = dictionaryService.selectAreaByCode(areaCode);
                areaName = area.getName();
            }
            String addressO = StringUtils.isEmpty(oaddress) ? "" : oaddress;
            address = provinceName + cityName + areaName + addressO;

        }
        return address;
    }
}

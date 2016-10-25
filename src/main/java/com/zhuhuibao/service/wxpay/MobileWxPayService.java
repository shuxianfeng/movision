package com.zhuhuibao.service.wxpay;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.AlipayCallbackLog;
import com.zhuhuibao.mybatis.order.entity.AlipayRefundCallbackLog;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import com.zhuhuibao.mybatis.order.service.OrderFlowService;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.wxpayLog.entity.WxPayNotifyLog;
import com.zhuhuibao.mybatis.wxpayLog.mapper.WxPayNotifyLogMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.CommonUtils;
import com.zhuhuibao.utils.HttpClientUtils;
import com.zhuhuibao.utils.SignUtil;
import com.zhuhuibao.utils.XmlUtil;
import com.zhuhuibao.utils.convert.DateConvert;
import com.zhuhuibao.utils.wxpay.WxpayPropertiesLoader;
import com.zhuhuibao.zookeeper.DistributedLock;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class MobileWxPayService {
	@Autowired
	OrderFlowService orderFlowSV;
	
	@Autowired
	OrderService orderSV;
	
	@Autowired
	ZhbService zhbService;

	@Autowired
	private WxPayNotifyLogMapper wxPayNotifyLogMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderFlowService orderFlowService;

	private static final Logger log = LoggerFactory
			.getLogger(MobileWxPayService.class);

	private static final String GET_OPENID_URL = WxpayPropertiesLoader
			.getPropertyValue("get_openid_url");

	private static final String APPID = WxpayPropertiesLoader
			.getPropertyValue("app_id");

	private static final String SECRET = WxpayPropertiesLoader
			.getPropertyValue("secret");

	private static final String AUTHORIZATION_CODE = WxpayPropertiesLoader
			.getPropertyValue("authorization_code");

	private static final String MCH_ID = WxpayPropertiesLoader
			.getPropertyValue("mch_id");

	private static final String WX_DO_ORDER_URL = WxpayPropertiesLoader
			.getPropertyValue("wx_do_order_url");
	
	private static final String WEI_XIN_NOTIFY_URL = WxpayPropertiesLoader
			.getPropertyValue("wei_xin_notify_url");

	private static final String MD5 = "MD5";
	
	public static final String LOCK_NAME = "wxpay_notify_orderno";
	
	public static final String SUCCESS = "SUCCESS";
	
	public static final String FAIL = "FAIL";
	
	public static final String WEB = "WEB";
	
	public static final String CNY = "CNY";

	/**
	 * 处理微信支付完成的通知回调
	 * 
	 * 业务锁控制并发：在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public ModelAndView handleWxPayNotify(HttpServletRequest request) throws ParseException, JDOMException, IOException {
		
		// 交易类型
		String tradeType = PayConstants.TradeType.PAY.toString();
		ModelAndView modelAndView = new ModelAndView();
		
		Map requestParams = getRequestParams(request);
		Object returnObj = getReturnObj(requestParams);
		Map requestMap = getRequestMap(returnObj);
		
		log.info("处理微信支付完成的通知回调，requestMap="+requestMap);
		log.info("加锁");
		DistributedLock lock = null;
        try {
            lock = new DistributedLock(LOCK_NAME);
            lock.lock();
            //支付业务处理
            payHandler(tradeType, modelAndView, requestParams, requestMap);
            
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, e.getMessage());
        } finally {
            if (lock != null) {
                lock.unlock();
                log.info("解锁");
            }
        }
        
		return modelAndView;
	}

	private Map getRequestMap(Object returnObj) throws JDOMException,
			IOException {
		Map requestMap = new HashMap<>();
		//通知参数 
		requestMap = XmlUtil.doXMLParse(String.valueOf(returnObj));
		if (null == requestMap) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}
		return requestMap;
	}

	private Object getReturnObj(Map requestParams) {
		Object returnObj = requestParams.get("return");
		if (null == returnObj) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}
		return returnObj;
	}

	private Map getRequestParams(HttpServletRequest request) {
		Map requestParams = request.getParameterMap();
		log.info("处理微信支付完成的通知回调,requestParams="+requestParams);
		
		if (null == requestParams) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}
		return requestParams;
	}

	/**
	 * 微信支付处理：判断返回值+返回成功时的业务处理
	 * 
	 * TODO  签名验证
	 * 商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失
	 * @param tradeType
	 * @param modelAndView
	 * @param requestParams	通知参数 
	 * @param requestMap	通知参数 
	 * @throws ParseException
	 */
	private void payHandler(String tradeType, ModelAndView modelAndView,
			Map requestParams, Map<String,String> requestMap) {
		//获取返回状态码【通信标识】
		String return_code = (String) requestParams.get("return_code");
		//获取业务结果【交易标识】，判断交易是否成功
		String result_code = (String) requestParams.get("result_code");
		
		/**
		 * 【通信标识】和【交易标识】都成功才说明返回成功
		 */
		if (SUCCESS.equals(return_code) && SUCCESS.equals(result_code)) {
			//先进行签名校验
			if(signValidating(requestMap)){
				//再进行业务处理,判断是否更新了order的状态
				boolean isOrderUpdate = wxpayNofifySuccessHandle(tradeType, modelAndView, requestMap);
				
				if (isOrderUpdate) {
					modelAndView.addObject("return_code", SUCCESS);
					modelAndView.addObject("return_msg", "支付成功");
				}else{
					modelAndView.addObject("return_code", FAIL);
					modelAndView.addObject("return_msg", "支付失败");
				}
			}
		} else {
			modelAndView.addObject("return_code", FAIL);
			modelAndView.addObject("return_msg", "支付失败");
		}
	}
	
	/**
	 * 微信回调通知业务处理
	 * @param tradeType
	 * @param modelAndView
	 * @param requestMap
	 */
	private boolean wxpayNofifySuccessHandle(String tradeType,
			ModelAndView modelAndView, Map<String, String> requestMap) {
		// 返回成功时业务逻辑处理
		boolean isOrderUpdateFlag = false;
		String orderno = (String) requestMap.get("out_trade_no");
		Order order =  orderSV.findByOrderNo(orderno);
		
		if(null == order){
			/**
			 * 若不存在微信支付的该订单 TODO
			 * 则抛出异常
			 * 应该调退款请求
			 */
			throw new BusinessException(MsgCodeConstant.NOT_EXIST_ORDER_FOR_WXPAY, "微信支付回调接口调用时，微信端的请求参数中不存在该订单");
		}
		
		if(!order.getStatus().equals(PayConstants.OrderStatus.YZF.toString())){
			if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
				log.info("微信支付交易的处理，开始。。。");
				
				OrderFlow orderFlow = orderFlowService.findByOrderNoAndTradeMode((String)requestMap.get("out_trade_no"),
			            PayConstants.PayMode.WXPAY.toString());
				
				handleOrderFlow(requestMap, orderFlow);
				
			    recordPayAsyncCallbackLog(requestMap);
				
			    isOrderUpdateFlag = updateOrder(order);
				
				log.info("微信支付交易的处理，结束。");
			}
		}
		return isOrderUpdateFlag;
	}

	/**
	 * 修改订单状态为已支付
	 * @param order
	 * @return
	 */
	private boolean updateOrder(Order order) {
		boolean isOrderUpdateFlag;
		order.setStatus(PayConstants.OrderStatus.YZF.toString());
		order.setUpdateTime(new Date());
		isOrderUpdateFlag =  orderSV.update(order);
		log.error("【修改】t_o_orde中的订单状态为已支付！");
		return isOrderUpdateFlag;
	}

	/**
	 * 订单存在，先判断是否存在该订单的流水：
	 * 若流水存在，则修改流水的状态=已支付 
	 * 若流失不存在，则新增已支付的流水 
	 * @param requestMap
	 * @param orderFlow
	 */
	private void handleOrderFlow(Map<String, String> requestMap,
			OrderFlow orderFlow) {
		
		if (orderFlow != null) {
		    //订单流水不为空，修改流水的状态为已支付
			if(!orderFlow.getTradeStatus().equals(PayConstants.OrderStatus.YZF.toString())){
				orderFlow.setTradeStatus(PayConstants.OrderStatus.YZF.toString());
				orderFlowSV.update(orderFlow);	
				log.info("【修改】t_o_order_flow中一条微信支付流水记录，status=已支付");
			}
		} else {
			//生成一条订单流水，状态为已支付
			addOrderFlowForWxPaySuccess(requestMap);
			log.error("【新增】t_o_order_flow中一条微信支付流水记录, status=已支付");
		}
	}

	/**
	 * 微信支付完回调接口的签名校验
	 * @param requestMap
	 * @return
	 */
	private boolean signValidating(Map<String, String> requestMap) {
		boolean isSignValidationFlag = false;
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		for (Map.Entry<String, String> entry : requestMap.entrySet()) {
			if(!"sign".equals(entry.getKey())){
				signParams.put(entry.getKey(), entry.getValue());
			}
		}
		String sign = SignUtil.createSign("UTF-8", signParams);
		if(sign.equals(requestMap.get("sign"))){
			isSignValidationFlag = true;
		}
		return isSignValidationFlag;
	}

	private void addOrderFlowForWxPaySuccess(Map requestMap) {
		OrderFlow wepayFlow = new OrderFlow();
		wepayFlow.setOrderNo((String)requestMap.get("out_trade_no"));
		wepayFlow.setTradeStatus(PayConstants.OrderStatus.YZF.toString());
		wepayFlow.setTradeTime(new Date());
		wepayFlow.setUpdateTime(new Date());
		orderFlowService.update(wepayFlow);
	}

	/**
	 * 微信支付返回成功,业务逻辑处理
	 * @param params	
	 * @param notifyType
	 * @param tradeType
	 * @return
	 * @throws ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, String> tradeSuccessDeal(Map<String, String> params,
			String notifyType, String tradeType) throws ParseException {
		
		log.info("支付返回成功,业务逻辑处理...");
		Map<String, String> resultMap = new HashMap<>();

		try {
			// 1-> 记录微信支付通知信息 交易流水信息
			// 订单：此处是生成一个订单 
			Order order = new Order();
			order.setOrderNo(params.get("out_trade_no"));
			order.setUpdateTime(new Date());
			// 异步通知
			if (notifyType.equals(PayConstants.NotifyType.ASYNC.toString())) {
				log.error("异步通知返回记录处理...[{}]", params.get("out_trade_no"));
				callbackNotice(params, tradeType, order);

			}
			// 同步通知
			if (notifyType.equals(PayConstants.NotifyType.SYNC.toString())) {
				log.error("同步通知返回记录处理...[{}]", params.get("out_trade_no"));
				callbackNotice(params, tradeType, order);
			}
			
			resultMap.put("statusCode",
					String.valueOf(PayConstants.HTTP_SUCCESS_CODE));

		} catch (Exception e) {
			log.error("微信回调接口业务处理异常:", e);
			resultMap.put("statusCode",
					String.valueOf(PayConstants.HTTP_SYSTEM_EXCEPTION_CODE));
		}

		return resultMap;
	}
	
	/**
	 * 微信支付回调通知业务处理
	 * @param params
	 * @param tradeType	交易类型，1：支付，2：退款
	 * 				目前只有支付
	 * @param order	
	 */
	private void callbackNotice(Map<String, String> params, String tradeType,
			Order order) {
		// 即时到账支付
		if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
			
			// 记录 微信支付结果通用通知 返回记录
			recordPayAsyncCallbackLog(params);
			
			// 2-> 判断是否存在筑慧币支付方式——微信支付
			//TODO 为什么 ？？
			OrderFlow orderFlow = orderFlowService.findByOrderNoAndTradeMode(
					params.get("out_trade_no"),
					PayConstants.PayMode.WXPAY.toString());
			
			if (orderFlow != null) {
				log.info("存在筑慧币支付方式");
				String tradeStatus = orderFlow.getTradeStatus();
				if (tradeStatus.equals(PayConstants.OrderStatus.YZF.toString())) {
					// 3-> 修改订单状态为已支付
					order.setStatus(PayConstants.OrderStatus.YZF.toString());
				}
			} else {
				log.info("不存在筑慧币支付方式");
				// 3-> 修改订单状态为已支付
				OrderFlow alFlow = new OrderFlow();
				alFlow.setOrderNo(params.get("out_trade_no"));
				alFlow.setTradeStatus(PayConstants.OrderStatus.YZF.toString());	//交易状态
				alFlow.setTradeTime(new Date());	//支付时间
				alFlow.setUpdateTime(new Date());	//交易更新时间
				orderFlowService.update(alFlow);
				log.error("修改t_o_order_flow status>>>");
				order.setStatus(PayConstants.OrderStatus.YZF.toString());
			}

			// 2. 修改订单状态
			boolean suc = orderService.update(order);
			log.error("update t_o_order status :>>>>" + suc);
			// 购买筑慧币,VIP 需要回调
			if (suc) {
				String orderNo = params.get("out_trade_no");
				callbackZhbPay(orderNo);
			} else {
				throw new BusinessException(MsgCodeConstant.PAY_ERROR, "业务处理失败");
			}

		}
		// 退款 TODO 暂时手机端不支持
	}

	/**
	 * 记录 支付宝即时到账退款接口 异步通知返回记录
	 *
	 * @param params
	 */
	/*
	 * public void recordRefundAsyncCallbackLog(Map<String, String> params) {
	 * log.info("支付宝即时到账退款接口,异步通知返回记录 入表操作..."); AlipayRefundCallbackLog
	 * refundCallbackLog = new AlipayRefundCallbackLog();
	 * ConvertUtils.register(new DateConvert(), Date.class); Map<String, String>
	 * pMap = Maps.newHashMap(); for (String key : params.keySet()) {
	 * pMap.put(CommonUtils.getCamelString(key), params.get(key)); }
	 * log.info("需转换为bean的pMap=" + pMap); try {
	 * BeanUtils.populate(refundCallbackLog, pMap); } catch (Exception e) {
	 * e.printStackTrace(); log.error("支付宝回调参数map转换为bean异常>>>", e); }
	 * 
	 * refundCallbackLogService.insert(refundCallbackLog);
	 * 
	 * }
	 */

	private void callbackZhbPay(String orderNo) {
		try {

			Order endOrder = orderService.findByOrderNo(orderNo);
			if (endOrder != null) {
				if (endOrder.getGoodsType().equals(
						OrderConstants.GoodsType.ZHB.toString())) {

					int result = zhbService.zhbPrepaidByOrder(orderNo);
					if (result == 0) {
						throw new BusinessException(MsgCodeConstant.PAY_ERROR,
								"筑慧币充值失败");
					}
				} else if (endOrder.getGoodsType().equals(
						OrderConstants.GoodsType.VIP.toString())) {
					int result = zhbService.openVipService(orderNo);
					if (result == 0) {
						throw new BusinessException(MsgCodeConstant.PAY_ERROR,
								"VIP购买失败失败");
					}
				}
			}
		} catch (Exception e) {
			log.error("筑慧币充值失败:", e);
		}
	}

	/**
	 * 微信支付结果通知 , 入表 t_o_wxpay_log 操作
	 * @param params
	 */
	public void recordPayAsyncCallbackLog(Map<String, String> params) {
		log.info("微信支付结果通知 , 入表操作... ");
		WxPayNotifyLog wxpayNotifyLog = new WxPayNotifyLog();

		ConvertUtils.register(new DateConvert(), Date.class);
		Map<String, String> pMap = Maps.newHashMap();
		for (String key : params.keySet()) {
			pMap.put(CommonUtils.getCamelString(key), params.get(key));
		}
		// pMap.put("price", String.valueOf(new
		// BigDecimal(pMap.get("price")).multiply(new
		// BigDecimal(1000)).longValue()));
		pMap.put(
				"totalFee",
				String.valueOf(new BigDecimal(pMap.get("totalFee")).multiply(
						new BigDecimal(1000)).longValue()));
		log.info("需转换为bean的pMap>>{}", pMap);
		try {
			BeanUtils.populate(wxpayNotifyLog, pMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("微信支付结果通知参数map转换为bean异常" + e.getMessage());
		}
		wxPayLoginsert(wxpayNotifyLog);
		log.info("微信支付结果通知 , 入表操作——成功！");
	}

	public void wxPayLoginsert(WxPayNotifyLog record) {
		int num;
		try {
			num = wxPayNotifyLogMapper.insertSelective(record);
			if (num != 1) {
				log.error("t_o_wxpay_log:插入数据失败");
				throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL,
						"插入数据失败");
			}
		} catch (Exception e) {
			log.error("执行异常>>>", e);
			throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL,
					"插入数据失败");
		}
	}

	/**
	 * 获取微信支付中统一下单接口的传参——openID
	 * 
	 * GET_OPENID_URL=https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	 * @param code
	 * @return
	 */
	public String getOpenId(String code) {

		Map<String, String> map = new HashMap<String, String>();
		
		prepareMapParams(code, map);
		
		String openid = "";
		log.info("【调openid的接口】，开始");
		Map<String, String> queryOpenidResult = HttpClientUtils.doGet(GET_OPENID_URL,
				map, "UTF-8");
		log.info("【调openid的接口】，结束");
		log.info("【调openid的接口】,返回值="+queryOpenidResult);
		
		return parseOpenid(openid, queryOpenidResult);
	}

	/**
	 * 解析返回值获取openid
	 * @param openid
	 * @param queryOpenidResult = {result={"access_token":"JPNXAj_cpfu6QXzC5w5KdIMuEGlq3fiKCh2LlU4gCJq-yoU8AAXdE9FL9sjqDa-T5yvmCpvx-d0XpNSTJdpqaxijOFeLBeC2QL3m7ml3I8M",
		 "expires_in":7200,"refresh_token":"aoXKwIJ-zGCGffNWB8dPqjXOK1Pm2E57ZHQe8gbdWaYEWnlX84oCngejTUK-4Nu5dA4C4xmyXkFh-uo9jnHpITItZw8asP-cASM77gmf0dI",
		 "openid":"o3eXzv14h_YilIZB3JDomt0Zutao","scope":"snsapi_userinfo"}, status=200}
	 * @return
	 */
	private String parseOpenid(String openid,
			Map<String, String> queryOpenidResult) {
		
		if (null != queryOpenidResult && null != queryOpenidResult.get("result")) {
			String result =  queryOpenidResult.get("result");
			Gson gson = new Gson();
	        Map resultMap = gson.fromJson(result, Map.class);
	        openid =  (String)resultMap.get("openid");
	        log.info("【调openid的接口】，openid="+openid);
		}
		return openid;
	}

	private void prepareMapParams(String code, Map<String, String> map) {
		map.put("appid", APPID);
		map.put("secret", SECRET);
		map.put("code", code);
		map.put("grant_type", AUTHORIZATION_CODE);
		log.info("【调openid的接口】的参数："+map);
	}

	/**
	 * 调微信统一下单接口，并处理返回数据
	 * 
	 * @param openid
	 * @param orderid
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Map<String, String> handleOrder(String openid, String orderid,
			HttpServletRequest request) throws JDOMException, IOException {
		// 生成随机数
		String nonce_str = SignUtil.generateString(32);
		log.info("【调微信统一下单接口】生成的【随机数】，【nonce_str】=" + nonce_str);
		
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		
		prepareParameters(openid, orderid, request, WEI_XIN_NOTIFY_URL,
				nonce_str, signParams);
		
		String sign = SignUtil.createSign("UTF-8", signParams);
		signParams.put("sign", sign);
		log.info("【调用微信统一下单接口】的签名——【sign】=" + sign);

		signParams.remove("key");// 调用统一下单无需key（商户应用密钥）
		log.info("【调用微信统一下单接口】的传参——【signParams】=" + signParams);

		log.info("调用微信统一下单接口:【开始】");
		Map<String, String> doOrderResultMap = HttpClientUtils.doPostForXml(
				WX_DO_ORDER_URL, signParams, "UTF-8");
		log.info("调用微信统一下单接口:【结束】");

		log.info("调用微信统一下单接口的【xml形式的返回值】,doOrderResultMap =" + doOrderResultMap);
		// 准备前端调用getBrandWCPayRequest接口所需的参数
		SortedMap<String, String> jsAPIsignParam = prepareJSAPIParams(
				nonce_str, doOrderResultMap, orderid);
		log.info("最终返回给前端的结果集：【jsAPIsignParam】=" + jsAPIsignParam);
		
		return jsAPIsignParam;
	}

	/**
	 * 准备前端调用getBrandWCPayRequest接口所需的参数
	 * 
	 * @param nonce_str
	 * @param doOrderResultMap 
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	private SortedMap<String, String> prepareJSAPIParams(String nonce_str,
			Map<String, String> doOrderResultMap, String orderid) throws JDOMException, IOException {
		/**
		 * 解析结果:返回正确信息
		 * {result=<xml><return_code><![CDATA[SUCCESS]]></return_code>
		 * <return_msg><![CDATA[OK]]></return_msg>
		 * <appid><![CDATA[wx5349d63f9159c0bb]]></appid>
		 * <mch_id><![CDATA[1393755702]]></mch_id>
		 * <nonce_str><![CDATA[ZvYIcsfD0qeey76c]]></nonce_str>
		 * <sign><![CDATA[D121BF10B124CFDB65EBCFCFE5C86B2A]]></sign>
		 * <result_code><![CDATA[SUCCESS]]></result_code>
		 * <prepay_id><![CDATA[wx201610131433133ac4c0257f0243044464
		 * ]]></prepay_id> <trade_type><![CDATA[JSAPI]]></trade_type> </xml>,
		 * status=200}
		 */
		Object resultObj = doOrderResultMap.get("result");
		SortedMap<String, String> jsAPIsignParam = new TreeMap<String, String>();
		if (null != resultObj) {
			
			Map map = XmlUtil.doXMLParse(String.valueOf(resultObj));
			log.info("调用微信统一下单接口的【map形式的返回值】，map=" + map);
			String return_code = (String) map.get("return_code"); // 返回状态码
			String result_code = (String) map.get("result_code"); // 业务结果
			String signAgain = null;
			if(return_code.equals("SUCCESS")){
				
				if(result_code.equals("SUCCESS")){
					//返回成功的处理
					signAgain = returnSuccessStep(nonce_str, orderid,
							jsAPIsignParam, map);
				}else{
					//返回失败的处理：直接抛出errorcode和errormsg给前台
					exceptionHandle(map);
				}
			}
			jsAPIsignParam.put("paySign", signAgain);
			log.info("第二次生产签名：【jsAPIsignParam】=" + jsAPIsignParam);
		}
		return jsAPIsignParam;
	}

	private String returnSuccessStep(String nonce_str, String orderid,
			SortedMap<String, String> jsAPIsignParam, Map map) {
		String prepay_id;
		String signAgain;
		// 获取到prepay_id
		prepay_id = (String) map.get("prepay_id");
		log.info("成功获取到prepay_id=" + prepay_id);
		
		Order order = orderService.findByOrderNo(orderid);
		
		OrderFlow orderFlow = prepareOrderFlowParams(orderid,
				prepay_id, order);
		
		//第二次生成签名（该签名需要返回给前端）
		signAgain = genSignAgain(nonce_str, jsAPIsignParam,
				prepay_id);
		orderFlow.setSign(signAgain);
		
		orderFlowService.insert(orderFlow);
		return signAgain;
	}

	private OrderFlow prepareOrderFlowParams(String orderid, String prepay_id,
			Order order) {
		BigDecimal tradeFee = new BigDecimal(0);
		if(null != order){
			tradeFee = order.getAmount();
		}
		//流水入库
		OrderFlow orderFlow = new OrderFlow();
		orderFlow.setOrderNo(orderid);
		orderFlow.setPrepareId(prepay_id);
		orderFlow.setTradeMode(PayConstants.PayMode.WXPAY.toString());
		orderFlow.setTradeFee(tradeFee);
		orderFlow.setTradeStatus(PayConstants.OrderStatus.WZF.toString());
		orderFlow.setCreateTime(new Date());
		return orderFlow;
	}

	
	private String genSignAgain(String nonce_str,
			SortedMap<String, String> jsAPIsignParam, String prepay_id) {
		long currentTimeMillis = System.currentTimeMillis();// 生成时间戳
		long second = currentTimeMillis / 1000L; // （转换成秒）
		String seconds = String.valueOf(second).substring(0, 10); // （截取前10位）
		//准备签名参数
		jsAPIsignParam.put("appId", APPID);// app_id
		jsAPIsignParam.put("package", "prepay_id=" + prepay_id);// 默认sign=WXPay
		jsAPIsignParam.put("nonceStr", nonce_str);// 自定义不重复的长度不长于32位
		jsAPIsignParam.put("timeStamp", seconds);// 北京时间时间戳
		jsAPIsignParam.put("signType", MD5);
		// 再次生成签名
		String signAgain = SignUtil.createSign("UTF-8", jsAPIsignParam);
		return signAgain;
	}

	private void exceptionHandle(Map map) {
		//异常处理
		String err_code = (String) map.get("err_code");
		String err_code_des = (String) map.get("err_code_des");
		int exception_error_code = 0;
		switch(err_code){
			case "NOAUTH":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.NOAUTH.getCode();
				break;
			case "NOTENOUGH":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.NOTENOUGH.getCode();
				break;
			case "ORDERPAID":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.ORDERPAID.getCode();
				break;
			case "ORDERCLOSED":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.ORDERCLOSED.getCode();
				break;
			case "SYSTEMERROR":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.SYSTEMERROR.getCode();
				break;
			case "APPID_NOT_EXIST":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.APPID_NOT_EXIST.getCode();
				break;
			case "MCHID_NOT_EXIST":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.MCHID_NOT_EXIST.getCode();
				break;
			case "APPID_MCHID_NOT_MATCH":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.APPID_MCHID_NOT_MATCH.getCode();
				break;
			case "LACK_PARAMS":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.LACK_PARAMS.getCode();
				break;
			case "OUT_TRADE_NO_USED":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.OUT_TRADE_NO_USED.getCode();
				break;
			case "SIGNERROR":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.SIGNERROR.getCode();
				break;
			case "XML_FORMAT_ERROR":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.XML_FORMAT_ERROR.getCode();
				break;
			case "REQUIRE_POST_METHOD":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.REQUIRE_POST_METHOD.getCode();
				break;
			case "POST_DATA_EMPTY":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.POST_DATA_EMPTY.getCode();
				break;
			case "NOT_UTF8":
				exception_error_code = MsgCodeConstant.WXPAY_ERROR_CODE.NOT_UTF8.getCode();
				break;
		}
		throw new BusinessException(exception_error_code, err_code_des);
	}

	/**
	 * 准备微信统一下单接口传参
	 * 
	 * @param openid
	 * @param orderid
	 * @param request
	 * @param wei_xin_notify_url
	 * @param nonce_str
	 * @param signParams
	 */
	private void prepareParameters(String openid, String orderid,
			HttpServletRequest request, String wei_xin_notify_url,
			String nonce_str, SortedMap<String, String> signParams) {
		
		Order order = orderService.findByOrderNo(orderid);
		if(null == order){
			throw new BusinessException(MsgCodeConstant.NOT_EXIST_ORDER, "不存在该订单");
		}
		if(null == order.getPayAmount()){
			throw new BusinessException(MsgCodeConstant.NOT_EXIST_ORDER_PAYAMOUNT, "不存在该订单的【实付金额】字段");
		}
		BigDecimal payAmount = order.getPayAmount();
	    String total_fee = String.valueOf(payAmount.multiply(new BigDecimal(100)).longValue());  
		
		signParams.put("appid", APPID); // 公众账号ID
		signParams.put("mch_id", MCH_ID); // 商户号
		signParams.put("device_info", WEB); // 设备号,PC网页或公众号内支付请传"WEB"
		signParams.put("nonce_str", nonce_str); // 随机数算法
		signParams.put("body", "JSAPI支付测试"); // 商品描述
		// signParams.put("detail", ""); //商品详情
		// signParams.put("attach", ""); //附加数据
		signParams.put("out_trade_no", orderid); // 商户订单号
		signParams.put("fee_type", CNY); // 货币类型
		signParams.put("total_fee", total_fee); // 总金额(单位：分)
		signParams.put("spbill_create_ip", request.getRemoteAddr()); // 终端IP
		// signParams.put("time_start", "");
		// signParams.put("time_expire", "");
		// signParams.put("goods_tag", ""); //商品标记
		signParams.put("notify_url", wei_xin_notify_url); // 接收微信支付异步通知回调地址
		signParams.put("trade_type", "JSAPI"); // 交易类型
		// signParams.put("product_id", "12235413214070356458058");
		// //商品ID,此id为二维码中包含的商品ID，商户自行定义。
		// signParams.put("limit_pay", ""); //指定支付方式
		signParams.put("openid", openid); // trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
	}
}

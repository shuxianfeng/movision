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

	/**
	 * 处理微信支付完成的通知回调
	 * TODO 业务锁控制并发
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

		Map requestParams = request.getParameterMap();
		log.info("处理微信支付完成的通知回调,requestParams="+requestParams);
		
		if (null == requestParams) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}

		Object returnObj = requestParams.get("return");
		if (null == returnObj) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}

		Map requestMap = new HashMap<>();
		requestMap = XmlUtil.doXMLParse(String.valueOf(returnObj));
		if (null == requestMap) {
			throw new BusinessException(
					MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR,
					"微信支付结果通用通知接口参数为空");
		}
		
		log.info("处理微信支付完成的通知回调，requestMap="+requestMap);
//		String orderno = (String)requestParams.get("out_trade_no");
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

	private void payHandler(String tradeType, ModelAndView modelAndView,
			Map requestParams, Map requestMap) throws ParseException {
		String return_code = (String) requestParams.get("return_code");

		if ("SUCCESS".equals(return_code)) {

			// 返回成功时业务逻辑处理
			log.info("微信支付回调成功业务处理，开始");
			Map<String, String> resultMap = tradeSuccessDeal(
					requestMap, PayConstants.NotifyType.SYNC.toString(),
					tradeType);
			log.info("微信支付回调成功业务处理，结束");
			
			log.info("***同步回调：支付平台回调发起方支付方结果：" + resultMap);
			if (resultMap != null
					&& String.valueOf(PayConstants.HTTP_SUCCESS_CODE).equals(
							resultMap.get("statusCode"))) {

				if ("SUCCESS".equals(resultMap.get("result"))) {

					modelAndView.addObject("return_code", "SUCCESS");
					modelAndView.addObject("return_msg", "支付成功");
				}

			}
		} else {
			modelAndView.addObject("return_code", "FAIL");
			modelAndView.addObject("return_msg", "支付失败");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, String> tradeSuccessDeal(Map<String, String> params,
			String notifyType, String tradeType) throws ParseException {
		log.info("支付返回成功,业务逻辑处理...");
		Map<String, String> resultMap = new HashMap<>();

		try {

			// 1-> 记录微信支付通知信息 交易流水信息
			// 订单
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

	private void callbackNotice(Map<String, String> params, String tradeType,
			Order order) {
		// 即时到账支付
		if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
			// 记录 微信支付结果通用通知 返回记录
			recordPayAsyncCallbackLog(params);
			// 2-> 判断是否存在筑慧币支付方式
			OrderFlow orderFlow = orderFlowService.findByOrderNoAndTradeMode(
					params.get("out_trade_no"),
					PayConstants.PayMode.ZHBPAY.toString());
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
				alFlow.setTradeStatus(PayConstants.OrderStatus.YZF.toString());
				alFlow.setTradeTime(new Date());
				alFlow.setUpdateTime(new Date());
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
		/*
		 * if (tradeType.equals(PayConstants.TradeType.REFUND.toString())) {
		 * recordRefundAsyncCallbackLog(params); //修改订单状态为已退款
		 * order.setStatus(PayConstants.OrderStatus.YTK.toString());
		 * 
		 * //2. 修改订单状态 orderService.update(order); }
		 */
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
	 * @param code
	 * @return
	 */
	public String getOpenId(String code) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", APPID);
		map.put("secret", SECRET);
		map.put("code", code);
		map.put("grant_type", AUTHORIZATION_CODE);

		String openid = "";
		Map<String, String> resultMap = HttpClientUtils.doGet(GET_OPENID_URL,
				map, "UTF-8");
		if (null != resultMap && null != resultMap.get("openid")) {
			openid = (String) resultMap.get("openid");
		}

		return openid;
	}

	/**
	 * 调微信统一下单接口，并处理返回数据
	 * 
	 * @param openid
	 * @param orderid
	 * @param request
	 * @return
	 */
	public Map<String, String> handleOrder(String openid, String orderid,
			HttpServletRequest request) {
		// 生成随机数
		String nonce_str = SignUtil.generateString(32);
		log.info("【调微信统一下单接口】生成的【随机数】，【nonce_str】=" + nonce_str);
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		// 准备统一下单接口传参
		prepareParameters(openid, orderid, request, WEI_XIN_NOTIFY_URL,
				nonce_str, signParams);
		// 生成签名
		String sign = SignUtil.createSign("UTF-8", signParams);
		signParams.put("sign", sign);
		log.info("【调用微信统一下单接口】的签名——【sign】=" + sign);

		signParams.remove("key");// 调用统一下单无需key（商户应用密钥）
		log.info("【调用微信统一下单接口】的传参——【signParams】=" + signParams);

		// 向微信接口端发起请求
		log.info("调用微信统一下单接口:【开始】");
		Map<String, String> doOrderResultMap = HttpClientUtils.doPostForXml(
				WX_DO_ORDER_URL, signParams, "UTF-8");
		log.info("调用微信统一下单接口:【结束】");

		log.info("调用微信统一下单接口的【xml形式的返回值】,doOrderResultMap =" + doOrderResultMap);
		// 准备前端调用getBrandWCPayRequest接口所需的参数
		SortedMap<String, String> jsAPIsignParam = prepareJSAPIParams(
				nonce_str, doOrderResultMap);
		Map resultMap = new HashMap<>();
		resultMap.put("doOrderResultMap", doOrderResultMap);
		resultMap.put("jsAPIsignParam", jsAPIsignParam);
		log.info("最终返回给前端的结果集：【resultMap】=" + resultMap);
		return resultMap;
	}

	/**
	 * 准备前端调用getBrandWCPayRequest接口所需的参数
	 * 
	 * @param nonce_str
	 * @param doOrderResultMap
	 * @return
	 */
	private SortedMap<String, String> prepareJSAPIParams(String nonce_str,
			Map<String, String> doOrderResultMap) {
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
			String resultStr = String.valueOf(resultObj);
			Map map;
			try {
				map = XmlUtil.doXMLParse(resultStr);
				log.info("调用微信统一下单接口的【map形式的返回值】，map=" + map);
				String return_code = (String) map.get("return_code"); // 返回状态码
				String result_code = (String) map.get("result_code"); // 业务结果
				String prepay_id = null;

				if (return_code.equals("SUCCESS")
						&& result_code.equals("SUCCESS")) {
					prepay_id = (String) map.get("prepay_id");// 获取到prepay_id
					log.info("成功获取到prepay_id=" + prepay_id);
				}

				long currentTimeMillis = System.currentTimeMillis();// 生成时间戳
				long second = currentTimeMillis / 1000L; // （转换成秒）
				String seconds = String.valueOf(second).substring(0, 10); // （截取前10位）

				jsAPIsignParam.put("appId", APPID);// app_id
				jsAPIsignParam.put("package", "prepay_id=" + prepay_id);// 默认sign=WXPay
				jsAPIsignParam.put("nonceStr", nonce_str);// 自定义不重复的长度不长于32位
				jsAPIsignParam.put("timeStamp", seconds);// 北京时间时间戳
				jsAPIsignParam.put("signType", MD5);

				String signAgain = SignUtil.createSign("UTF-8", jsAPIsignParam);// 再次生成签名
				log.info("第二次生产签名：【jsAPIsignParam】=" + jsAPIsignParam);

				jsAPIsignParam.put("paySign", signAgain);

			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsAPIsignParam;
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
		signParams.put("appid", APPID); // 公众账号ID
		signParams.put("mch_id", MCH_ID); // 商户号
		signParams.put("device_info", "WEB"); // 设备号,PC网页或公众号内支付请传"WEB"
		signParams.put("nonce_str", nonce_str); // 随机数算法
		signParams.put("body", "JSAPI支付测试"); // 商品描述
		// signParams.put("detail", ""); //商品详情
		// signParams.put("attach", ""); //附加数据
		signParams.put("out_trade_no", orderid); // 商户订单号
		signParams.put("fee_type", "CNY"); // 货币类型
		signParams.put("total_fee", "888"); // 总金额
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

	/**
	 * 获取微信等支付的APPID
	 * 
	 * @param token
	 * @return
	 */
	public String getAppID(String token) {

		return null;
	}
}

package com.zhuhuibao.business.pay;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.pojo.CourseOrderReqBean;
import com.zhuhuibao.common.pojo.ZHBOrderReqBean;
import com.zhuhuibao.common.pojo.PayReqBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.service.zhpay.ZhpayService;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单下单支付
 * @author jianglz
 * @since 16/6/23.
 */
@RestController
@RequestMapping("/rest/pay/site")
@Api(value = "zhbOrderPAY", description = "订单下单支付")
public class ZhbPayController {
    private static final Logger log = LoggerFactory.getLogger(ZhbPayController.class);

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    ZhpayService zhpayService;

    @Autowired
    CourseService courseService;

    @Autowired
    ZHOrderService zhOrderService;

    @Autowired
    ZhbService zhbService;


    @ApiOperation(value = "筑慧币购买提交订单", notes = "筑慧币购买提交订单", response = Response.class)
    @RequestMapping(value = "do_zhb_order", method = RequestMethod.POST)
    public Response doZHBOrder(@ApiParam @ModelAttribute ZHBOrderReqBean order) {
        Gson gson = new Gson();
        String json = gson.toJson(order);

        log.info("筑慧币下单页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);

        //根据商品ID查询商品价格
        DictionaryZhbgoods zhbgoods = zhbService.getZhbGoodsById(Long.valueOf(order.getGoodsId()));
        if(zhbgoods == null){
            log.error("未找到对应商品");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"未找到对应商品");
        }
        BigDecimal price =  zhbgoods.getPrice();
        if(price == null){
            log.error("价格为设置");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"价格未设置");
        }

        paramMap.put("goodsPrice",price.toString());
        paramMap.put("goodsName",zhbgoods.getName());

        checkUserLogin(paramMap);
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号
        //生成订单编号
        String orderNo = IdGenerator.createOrderNo();
        paramMap.put("orderNo", orderNo);

        //提交订单
        zhOrderService.createOrder(paramMap);

        return new Response();
    }

    @ApiOperation(value = "培训课程下单", notes = "培训课程下单", response = Response.class)
    @RequestMapping(value = "do_course_order", method = RequestMethod.POST)
    public Response createOrder(@ApiParam @ModelAttribute CourseOrderReqBean order){
        Gson gson = new Gson();
        String json = gson.toJson(order);

        log.info("培训下单页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);

        checkUserLogin(paramMap);

        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用下单接口......");

        courseService.createOrder(paramMap);

        return  new Response();
    }

    /**
     * 检查购买用户是否登录
     * @param paramMap
     */
    private void checkUserLogin(Map paramMap) {
        String buyerId = (String) paramMap.get("buyerId");
        if(StringUtils.isEmpty(buyerId)){
            Long userId = ShiroUtil.getCreateID();
            if (userId == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }else{
                paramMap.put("buyerId",String.valueOf(userId));
            }
        }
    }

    @ApiOperation(value = "立即支付", notes = "立即支付")
    @RequestMapping(value = "do_pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam @ModelAttribute PayReqBean pay) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(pay);


        log.info("技术培训支付页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);

        checkUserLogin(paramMap);

        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("alipay_goods_type", PayConstants.GoodsType.XNL.toString());//商品类型  0 , 1
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用立即支付接口......");

        //判断支付方式   是否使用筑慧币
        String userZHB = pay.getUserZHB();

        switch(userZHB){
            case "true":
                zhpayService.doPayMultiple(response,paramMap);
                break;
            case "false":
                zhpayService.doPay(response,paramMap);
                break;
            default:
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"是否使用筑慧币,传参错误");
        }

    }
}

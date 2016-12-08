package com.zhuhuibao.service.zhbPay;

import com.google.gson.Gson;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.pojo.CourseOrderReqBean;
import com.zhuhuibao.common.pojo.PayReqBean;
import com.zhuhuibao.common.pojo.ZHBOrderReqBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.service.zhpay.ZhpayService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * ZHB支付服务
 *
 * @author zhuangyuhao
 * @time 2016年10月17日 下午7:27:42
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class MobileZhbPayService {

    private static final Logger log = LoggerFactory.getLogger(MobileZhbPayService.class);

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    ZhpayService zhpayService;

    @Autowired
    CourseService courseService;

    @Autowired
    ZHOrderService zhOrderService;

    @Autowired
    ZhbService zhbService;

    @Autowired
    ExpertService expertService;

    /**
     * 提交ZHB订单
     *
     * @param order
     */
    public String doZHBOrder(ZHBOrderReqBean order) {

        Gson gson = new Gson();
        String json = gson.toJson(order);

        log.info("筑慧币下单页面,请求参数:{}", json);
        @SuppressWarnings("unchecked")
        Map<String, String> paramMap = gson.fromJson(json, Map.class);
        //检查购买用户是否登录
        checkUserLogin(paramMap);
        //根据商品ID查询商品价格
        DictionaryZhbgoods zhbgoods = zhbService.getZhbGoodsById(order.getGoodsId());
        if (zhbgoods == null) {
            log.error("未找到对应商品");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "未找到对应商品");
        }
        BigDecimal price = zhbgoods.getPrice();
        if (price == null) {
            log.error("价格未设置");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "价格未设置");
        }
        //购买VIP套餐判断  个人VIP和企业VIP只能购买对应的VIP套餐
        checkVip(order.getGoodsType(), zhbgoods.getValue());

        paramMap.put("goodsPrice", price.toString());
        paramMap.put("goodsName", zhbgoods.getName());

        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号
        //生成订单编号
        String orderNo = IdGenerator.createOrderNo();
        paramMap.put("orderNo", orderNo);
        //生成订单 (事务管理)
        zhOrderService.createOrder(paramMap);

        return orderNo;
    }

    /**
     * 判断用户是否具有改VIP套餐
     *
     * @param goodsType
     * @param value     viplevel
     */
    private void checkVip(String goodsType, String value) {

        if (goodsType.equals(OrderConstants.GoodsType.VIP.toString())) {

            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) session.getAttribute("member");
            String identify = user.getIdentify();

            if (identify.equals("2")) {     //个人
                boolean suc1 = value.equals(VipConstant.VipLevel.PERSON_GOLD.toString());
                boolean suc2 = value.equals(VipConstant.VipLevel.PERSON_PLATINUM.toString());
                //如果suc1不是真，并且suc2不是真，则抛异常
//                if (!(!suc1 || !suc2)) {
                if (!suc1 && !suc2) {    //!false && !false
                    throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "个人用户无此VIP套餐");
                }
            } else {  //企业
                boolean suc1 = value.equals(VipConstant.VipLevel.ENTERPRISE_GOLD.toString());
                boolean suc2 = value.equals(VipConstant.VipLevel.ENTERPRISE_PLATINUM.toString());

                if (!suc1 && !suc2) {
                    throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "企业用户无此VIP套餐");
                }
            }

        }
    }

    /**
     * 检查购买用户是否登录
     *
     * @param paramMap
     */
    private void checkUserLogin(Map<String, String> paramMap) {
        String buyerId = (String) paramMap.get("buyerId");
        if (StringUtils.isEmpty(buyerId)) {
            Long userId = ShiroUtil.getCreateID();
            if (userId == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            } else {
                paramMap.put("buyerId", String.valueOf(userId));
            }
        }
    }

    /**
     * 生成订单
     *
     * @param order
     * @return
     */
    public String createOrder(CourseOrderReqBean order) {
        Gson gson = new Gson();
        String json = gson.toJson(order);

        log.info("培训下单页面,请求参数:{}", json);

        @SuppressWarnings("unchecked")
        Map<String, String> paramMap = gson.fromJson(json, Map.class);

        checkUserLogin(paramMap);

        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用下单接口......");
        String orderNo = IdGenerator.createOrderNo();
        paramMap.put("orderNo", orderNo);

        courseService.createOrder(paramMap);

        return orderNo;
    }

    /**
     * 立即支付
     *
     * @param request
     * @param response
     * @param pay
     * @throws Exception
     */
    public void doPay(HttpServletRequest request, HttpServletResponse response, PayReqBean pay) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(pay);

        log.info("技术培训支付页面,请求参数:{}", json);
        @SuppressWarnings("unchecked")
        Map<String, String> paramMap = gson.fromJson(json, Map.class);

        checkUserLogin(paramMap);

        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("alipay_goods_type", PayConstants.GoodsType.XNL.toString());//商品类型  0 , 1
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用立即支付接口......");

        //判断支付方式   是否使用筑慧币
        String userZHB = pay.getUserZHB();

        switch (userZHB) {
            case "true":
                zhpayService.doPayMultiple(response, paramMap);
                break;
            case "false":
                zhpayService.doPay(response, paramMap);
                break;
            default:
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "是否使用筑慧币,传参错误");
        }

    }

    public Response getTrainMobileCode(String mobile, String mobileCodeSessionTypeSupport, String imgCode, String sessImgCode) throws Exception{
        Response response = new Response();
        if (imgCode.equalsIgnoreCase(sessImgCode)) {
            expertService.getTrainMobileCode(mobile, mobileCodeSessionTypeSupport);
            response.setCode(200);
            response.setMessage("验证码输入正确！");
        } else {
            response.setCode(400);
            response.setMessage("验证码输入错误！");
        }
        return response;
    }
}

package com.movision.facade.wechat;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.PointConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.couponTemp.entity.CouponTemp;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.mybatis.user.service.UserService;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.DateUtils;
import com.movision.utils.HttpClientUtils;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2018/2/1 16:55
 * 小程序使用的Facade层（租赁商城）
 */
@Service
public class WechatFacade {

    private static Logger log = LoggerFactory.getLogger(WechatFacade.class);

    @Autowired
    private PropertiesDBLoader propertiesDBLoader;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private CouponService couponService;

    /**
     * code换openid
     */
    public Response getOpenidBycode(String code){
        Response response = new Response();

        //调用微信官方接口：code换openid
        String appid = propertiesDBLoader.getValue("appid");//小程序的appid（参数配置表）
        String secret = propertiesDBLoader.getValue("appsecret");//小程序的appsecret（参数配置表）
        String grant_type = propertiesDBLoader.getValue("grant_type");//grant_type填写为 authorization_code（参数配置表）
        String requrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("js_code", code);
        params.put("grant_type", grant_type);
        Map<String, String> resmap = HttpClientUtils.doGet(requrl, params, "utf-8");

        log.debug(resmap.get("status"));
        log.debug(resmap.get("result"));
        JSONObject json = JSONObject.fromObject(resmap.get("result"));

        if (resmap.get("status").equals("200")) {
            try {
                String unionid = json.getString("unionid");
                //session_key是用来解密$encryptData的，暂时用不上
                //String session_key = json.getString("session_key");
                response.setCode(200);
                response.setMessage("生成成功");
                response.setData(unionid);
            } catch (Exception e) {
                response.setCode(40029);
                response.setMessage("code失效!");
            }
        }
        return response;
    }

    /**
     * 使用登录凭证 code 进行登录
     * @param unionid
     * @return
     */
    public Response getWechatLogin(String unionid, Response response, User user, String appToken){

        //1 校验appToken和serverToken非空
        String serverToken = this.validateAppTokenAndServerToken(appToken, response, user);

        //2 检验appToken和serverToken是否相等
        if (serverToken.equalsIgnoreCase(appToken)) {

            Subject currentUser = SecurityUtils.getSubject();
            Gson gson = new Gson();
            UsernamePasswordToken token = gson.fromJson(appToken, UsernamePasswordToken.class);

            //3 开始进入shiro的认证流程，对应 ShiroRealm.doGetAuthenticationInfo
            shiroLogin(response, currentUser, token);

            /**
             *  4 若shiro获取身份验证信息通过，则进行下面操作
             */
            if (currentUser.isAuthenticated()) {

                //查询该openid是否注册过
                LoginUser loginuser = userFacade.getLoginuserByOpenid(unionid);
                if (null != loginuser) {
                    ShiroRealm.ShiroUser shiroUser = ShiroUtil.getShiroUserFromLoginUser(loginuser);
                    //将shiroUser对象存入session中
                    Session session = currentUser.getSession();
                    session.setAttribute(SessionConstant.WECHAT_USER, shiroUser);
                    session.removeAttribute(SessionConstant.APP_USER);
                    session.removeAttribute(SessionConstant.BOSS_USER);

                    if (null == shiroUser) {
                        //该openid未注册过---注册
                        response.setCode(201);
                        response.setMessage("未注册，请先注册");
                        response.setData(unionid);
                    } else {
                        //该openid已注册--判断是否绑定了手机号
                        if (null != shiroUser.getPhone()) {
                            //已经绑定
                            response.setCode(200);
                            response.setMessage("登录成功");
                            response.setData(shiroUser);
                        } else {
                            //未绑定
                            response.setCode(202);
                            response.setMessage("未绑定手机号，请先绑定");
                            response.setData(shiroUser.getId());
                        }
                    }
                } else {
                    //该openid未注册过---注册
                    response.setCode(201);
                    response.setMessage("未注册，请先注册");
                    response.setData(unionid);
                }
            }
        }else {
            log.info("appToken和serverToken不相等");
            response.setCode(203);
            response.setMessage("appToken和serverToken不相等,请传入正确的token！");
            response.setData(unionid);
        }
        return response;
    }

    @Transactional
    public void bindPhoneProcess(String phone, String code, Validateinfo validateinfo, Session session, String userid) throws IOException {
        //session中缓存的短信验证码
        String verifyCode = validateinfo.getCheckCode();
        if (verifyCode != null) {

            Date currentTime = new Date();
            Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(validateinfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 12, 10);
            //校验是否在短信验证码有效期内
            if (currentTime.before(sendSMStime)) {

                log.debug("mobile verifyCode == " + code);
                //比较服务器端session中的验证码和App端输入的验证码
                if (validateinfo.getCheckCode().equalsIgnoreCase(code)) {

                    log.info("【获取userid】:" + userid);

                    User user = userService.selectByPrimaryKey(Integer.parseInt(userid));
                    if (null != user) {
                        //1 增加绑定手机号积分流水
                        pointRecordFacade.addPointRecordOnly(PointConstant.POINT_TYPE.binding_phone.getCode(), user.getId());

                        int newPoint = user.getPoints() + PointConstant.POINT.binding_phone.getCode();
                        user.setPoints(newPoint);   //积分
                        user.setPhone(phone);   //手机号
                        //2 修改用户基本信息
                        userService.updateByPrimaryKeySelective(user);
                        //3 更新session用户信息
                        ShiroUtil.updateAppuserPhoneAndPoints(phone, newPoint);

                        //4 如果用户当前手机号有领取过H5页面分享的优惠券，那么不管新老用户统一将优惠券临时表yw_coupon_temp中的优惠券信息全部放入优惠券正式表yw_coupon中
                        this.processCoupon(phone, Integer.parseInt(userid));

                        //5 登录成功则清除session中验证码的信息
                        session.removeAttribute("bind" + validateinfo.getAccount());

                    } else {
                        throw new BusinessException(MsgCodeConstant.NOT_EXIST_APP_ACCOUNT, "不存在该APP用户");
                    }
                } else {
                    //不需要清除验证码
                    throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, "手机验证码不正确");
                }
            } else {
                //超过短信验证码有效期，则清除session信息
                session.removeAttribute("bind" + validateinfo.getAccount());
                throw new BusinessException(MsgCodeConstant.member_mcode_sms_timeout, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
            }
        } else {
            throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
        }
    }

    /**
     * 如果当前手机号在分享的H5页面领取过优惠券，那么不管新老用户统一在这里将优惠券临时表中的数据同步到优惠券正式表中
     *
     * @param phone
     * @param userid
     */
    @Transactional
    void processCoupon(String phone, int userid) {
        //首先检查当前手机号是否领取过优惠券
        List<CouponTemp> couponTempList = couponService.checkIsGetCoupon(phone);
        List<Coupon> couponList = new ArrayList<>();
        if (couponTempList.size() > 0) {
            //遍历替换phone为userid，放入List<Coupon>
            for (int i = 0; i < couponTempList.size(); i++) {
                CouponTemp couponTemp = couponTempList.get(i);
                Coupon coupon = new Coupon();
                coupon.setUserid(userid);
                coupon.setTitle(couponTemp.getTitle());
                coupon.setContent(couponTemp.getContent());
                coupon.setType(couponTemp.getType());
                if (null != couponTemp.getShopid()) {
                    coupon.setShopid(couponTemp.getShopid());
                }
                coupon.setStatue(couponTemp.getStatue());
                coupon.setBegintime(couponTemp.getBegintime());
                coupon.setEndtime(couponTemp.getEndtime());
                coupon.setIntime(couponTemp.getIntime());
                coupon.setTmoney(couponTemp.getTmoney());
                coupon.setUsemoney(couponTemp.getUsemoney());
                coupon.setIsdel(couponTemp.getIsdel());
                couponList.add(coupon);
            }

            //插入优惠券列表
            couponService.insertCouponList(couponList);
            //删除临时表中的优惠券领取记录
            couponService.delCouponTemp(phone);
        }
    }

    /**
     * 校验app_token和Server_token是否都存在
     *
     * @param appToken
     * @param response
     * @param user
     * @return
     */
    private String validateAppTokenAndServerToken(String appToken, Response response, User user) {
        if (StringUtils.isEmpty(appToken)) {
            log.warn("app本地的token丢失");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_token_missing)));
            response.setMsgCode(MsgCodeConstant.app_token_missing);
        }

        String serverToken = user.getToken();
        if (StringUtils.isEmpty(serverToken)) {
            log.warn("服务器存储的token丢失");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.server_token_missing)));
            response.setMsgCode(MsgCodeConstant.server_token_missing);
        }
        return serverToken;
    }

    public void shiroLogin(Response response, Subject currentUser, UsernamePasswordToken token) {
        try {
            //登录，即身份验证 , 开始进入shiro的认证流程
            currentUser.login(token);

        } catch (UnknownAccountException e) {
            log.warn("用户名不存在");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_user_not_exist)));
            response.setMsgCode(MsgCodeConstant.app_user_not_exist);
        } catch (LockedAccountException e) {
            log.warn("帐户状态异常");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_account_status_error)));
            response.setMsgCode(MsgCodeConstant.app_account_status_error);
        } catch (AuthenticationException e) {
            log.warn("用户名或验证码错误");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_account_name_error)));
            response.setMsgCode(MsgCodeConstant.app_account_name_error);
        }
    }
}

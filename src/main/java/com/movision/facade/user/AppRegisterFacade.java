package com.movision.facade.user;

import com.google.gson.Gson;
import com.movision.common.constant.ImConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.UserConstants;
import com.movision.exception.BusinessException;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.couponTemp.entity.CouponTemp;
import com.movision.mybatis.deviceAccid.entity.DeviceAccid;
import com.movision.mybatis.deviceAccid.service.DeviceAccidService;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.DateUtils;
import com.movision.utils.StrUtil;
import com.movision.utils.im.CheckSumBuilder;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/3 18:03
 */
@Service
public class AppRegisterFacade {

    private static Logger log = LoggerFactory.getLogger(AppRegisterFacade.class);

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ImFacade imFacade;

    @Autowired
    private CouponService couponService;

    @Autowired
    private DeviceAccidService deviceAccidService;

    @Autowired
    private UserService userService;

    /**
     * 1 校验登录用户信息：手机号+短信验证码
     * 2 若用户不存在，则新增用户信息；
     *   若用户存在，则更新用户token
     * 3 登录成功，则清除session中的验证码，
     *
     * @param member
     * @param validateinfo
     * @param session
     * @return
     * {
     *  token_detail:xxx,
     *  token:yyy,
     *  imuser:zzz
     * }
     */
    public Map<String, Object> validateLoginUser(RegisterUser member, Validateinfo validateinfo, Session session) throws IOException {

        String phone = member.getPhone();
        String verifyCode = validateinfo.getCheckCode();
        String mobileCheckCode = member.getMobileCheckCode();
        if (verifyCode != null) {

            Date currentTime = new Date();
            Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(validateinfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 12, 10);
            //校验是否在短信验证码有效期内
            if (currentTime.before(sendSMStime)) {

                log.debug("mobile verifyCode == " + mobileCheckCode);
                //比较服务器端session中的验证码和App端输入的验证码
                if (validateinfo.getCheckCode().equalsIgnoreCase(mobileCheckCode)) {
                    //1 生成token
                    UsernamePasswordToken newToken = new UsernamePasswordToken(phone, verifyCode.toCharArray());
                    //校验是否手机号存在
                    Map<String, Object> result = new HashedMap();
                    //2 注册用户，并把token入库, 放入session缓存
                    Gson gson = new Gson();
                    String json = gson.toJson(newToken);
                    member.setToken(json);

                    int userid = 0;
                    User user = userFacade.queryUserByPhone(phone);
                    if (null != user) {
                        //存在该用户,修改app用户token和设备号
                        this.updateAppRegisterUser(member);
                        userid = user.getId();
                    } else {
                        //手机号不存在,则新增用户，并且新增token
                        userid = this.registerMember(member);
                    }
                    log.info("【获取userid】:" + userid);

                    //如果用户当前手机号有领取过H5页面分享的优惠券，那么不管新老用户统一将优惠券临时表yw_coupon_temp中的优惠券信息全部放入优惠券正式表yw_coupon中
                    this.processCoupon(phone, userid);

                    //判断该userid是否存在一个im用户，若不存在，则注册im用户;若存在，则查询
                    this.getImuserForReturn(phone, result, userid);

                    //判断t_device_accid中是否存在该设备号的记录，若存在，则删除该记录
                    deleteSameDevicenoRecord(member.getDeviceno());

                    //3 登录成功则清除session中验证码的信息
                    session.removeAttribute("r" + validateinfo.getAccount());
                    //4 返回token（后期可加密）
                    result.put("token_detail", newToken);
                    result.put("token", json);
                    return result;

                } else {
                    //不需要清除验证码
                    throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, "手机验证码不正确");
                }

            } else {
                //超过短信验证码有效期，则清除session信息
                session.removeAttribute("r" + validateinfo.getAccount());
                throw new BusinessException(MsgCodeConstant.member_mcode_sms_timeout, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
            }

        } else {
            throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
        }
    }

    /**
     * 判断该userid是否存在一个im用户，若不存在，则注册im用户
     * @param phone
     * @param result
     * @throws IOException
     */
    private void getImuserForReturn(String phone, Map<String, Object> result, int userid) throws IOException {

        Boolean isExistImUser = imFacade.isExistAPPImuser(userid);
        if (!isExistImUser) {
            //若不存在，则注册im用户
            ImUser imUser = new ImUser();
            imUser.setUserid(userid);
            imUser.setAccid(CheckSumBuilder.getAccid(String.valueOf(userid)));  //根据userid生成accid
            imUser.setName(StrUtil.genDefaultNickNameByPhone(phone));
            ImUser newImUser = imFacade.AddImUser(imUser);
            result.put("imuser", newImUser);
        } else {
            //若存在，则查询im用户
            result.put("imuser", imFacade.getImuserByCurrentAppuser(userid));
        }
    }

    /**
     * 如果当前手机号在分享的H5页面领取过优惠券，那么不管新老用户统一在这里将优惠券临时表中的数据同步到优惠券正式表中
     *
     * @param phone
     * @param userid
     */
    @Transactional
    private void processCoupon(String phone, int userid) {
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
     * 注册用户，新增用户信息
     *
     * @param member
     * @return 新注册的用户id
     */
    public int registerMember(RegisterUser member) {
        log.debug("注册会员");
        int memberId = 0;
        try {
            if (member != null) {
                String phone = member.getPhone();

                User user = new User();
                user.setNickname(StrUtil.genDefaultNickNameByPhone(phone)); //昵称
                user.setPhone(phone);   //手机号
                user.setToken(member.getToken());   //token
                user.setDeviceno(member.getDeviceno()); //设备号
                user.setPoints(35); //积分：注册25+绑定手机15

                memberId = userService.insertSelective(user);
            }
        } catch (Exception e) {
            log.error("register member error", e);
        }
        return memberId;
    }

    /**
     * 修改app用户token和设备号
     *
     * @param member
     */
    public void updateAppRegisterUser(RegisterUser member) {
        log.debug("修改会员token");
        try {
            if (member != null) {
                userFacade.updateAccount(member);
            }
        } catch (Exception e) {
            log.error("register member error", e);
        }
    }

    /**
     * 新增设备-accid记录
     *
     * @param deviceid
     */
    public void addDeviceAccid(String deviceid) {
        DeviceAccid deviceAccid = new DeviceAccid();
        deviceAccid.setDeviceid(deviceid);

        deviceAccid.setAccid(CheckSumBuilder.getAccid(deviceid));
        deviceAccidService.add(deviceAccid);
    }


    /**
     * 注册QQ账号
     * <p>
     * 步骤：
     * 1 生成新的token
     * 2 判断qq是否注册过
     * 若qq从未注册过：a 则注册qq账号; b 注册im用户，
     * 若qq已经注册过：更新用户记录中的token
     * 3 判断t_device_accid中是否存在该设备号的记录，若存在，则删除该记录；
     * （方便设置后面的系统推送中的toAccids）
     * <p>
     * 下面是token的数据结构
     * token:{
     * username: qq,
     * password: openid+deviceno,
     * rememberme: false
     * }
     *
     * @param qq
     * @param openid
     * @param deviceno
     * @return
     * @throws IOException
     */
    public Map<String, Object> registerQQAccount(String qq, String openid, String deviceno) throws IOException {

        Map<String, Object> result = new HashedMap();
        //1 生成新的token
        UsernamePasswordToken newToken = new UsernamePasswordToken(qq, (openid + deviceno).toCharArray());
        result.put("token_detail", newToken);
        Gson gson = new Gson();
        String tokenJson = gson.toJson(newToken);
        result.put("token", tokenJson);

        //2 判断是否存在这条qq用户记录
        Map map = new HashedMap();
        map.put("qq", qq);
        User originUser = userService.selectUserByThirdAccount(map);

        if (null == originUser) {
            //不存在qq账号
            //3.1 根据QQ注册账号
            User newUser = new User();
            newUser.setToken(tokenJson);    //token
            newUser.setQq(qq);              //qq
            newUser.setNickname(StrUtil.genDefaultNicknameByQQ(qq));    //昵称
            newUser.setDeviceno(deviceno);  //设备号
            newUser.setPoints(25);  //注册25分
            int userid = userService.insertSelective(newUser);

            //3.2 根据该新的userid注册im用户，即在yw_im_user中新增一条记录
            ImUser imUser = new ImUser();
            imUser.setUserid(userid);
            imUser.setAccid(CheckSumBuilder.getAccid(String.valueOf(userid)));
            imUser.setName(StrUtil.genDefaultNicknameByQQ(qq));
            ImUser newImUser = imFacade.AddImUser(imUser);
            result.put("imuser", newImUser);

        } else {
            //存在qq账号
            //3 更新原来的token
            originUser.setToken(tokenJson);
            originUser.setDeviceno(deviceno);
            userService.updateByPrimaryKeySelective(originUser);

            result.put("imuser", imFacade.getImuserByCurrentAppuser(originUser.getId()));
        }

        //4 判断t_device_accid中是否存在该设备号的记录，若存在，则删除该记录；
        deleteSameDevicenoRecord(deviceno);

        return result;
    }

    /**
     * 判断t_device_accid中是否存在该设备号的记录，若存在，则删除该记录；
     *
     * @param deviceno
     */
    private void deleteSameDevicenoRecord(String deviceno) {
        DeviceAccid deviceAccid = deviceAccidService.selectByDeviceno(deviceno);
        if (null == deviceAccid) {
            //不存在，则不操作
        } else {
            //删除该条记录
            deviceAccidService.delete(deviceAccid.getId());
        }
    }


}


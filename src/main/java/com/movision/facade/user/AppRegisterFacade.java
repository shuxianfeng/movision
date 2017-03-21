package com.movision.facade.user;

import com.google.gson.Gson;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.UserConstants;
import com.movision.exception.BusinessException;
import com.movision.facade.im.ImFacade;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.DateUtils;
import com.movision.utils.im.CheckSumBuilder;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
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

    /**
     * 校验登录用户信息：手机号+短信验证码
     * 若用户不存在，则新增用户信息；
     * 若用户存在，则更新用户token
     * 登录成功，则清除session中的验证码，
     *
     * @param member
     * @param validateinfo
     * @param session
     * @return
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
                    //2 注册用户，并把token入库, 放入缓存
                    Gson gson = new Gson();
                    String json = gson.toJson(newToken);
                    member.setToken(json);

                    int userid = 0;
                    User user = userFacade.queryUserByPhone(phone);
                    if (null != user) {
                        //存在该用户,则更新token
                        this.updateAppRegisterUser(member);
                        userid = user.getId();
                    } else {
                        //手机号不存在,则新增用户，并且新增token
                        userid = this.registerMember(member);
                    }
                    log.info("【获取userid】:" + userid);

                    // 判断该userid是否存在一个im用户，
                    this.getImuserForReturn(phone, result, userid);

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
            imUser.setAccid(CheckSumBuilder.getAccid(phone));
            ImUser newImUser = imFacade.AddImUser(imUser);
            result.put("imuser", newImUser);
        } else {
            result.put("imuser", imFacade.getImuserByCurrentAppuser(userid));
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

                if (member.getPhone() != null) {
                    // 手机默认注册成功
                    member.setStatus(Integer.parseInt(UserConstants.USER_STATUS.normal.toString()));
                }
                member.setIntime(new Date());

                memberId = userFacade.registerAccount(member);

            }
        } catch (Exception e) {
            log.error("register member error", e);
        }
        return memberId;
    }

    /**
     * 修改app注册用户的token
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
}


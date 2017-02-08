package com.movision.facade.user;

import com.google.gson.Gson;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.UserConstants;
import com.movision.exception.BusinessException;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.DateUtils;
import com.movision.utils.MsgPropertiesUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/3 18:03
 */
@Service
public class AppRegisterFacade {

    private static Logger log = LoggerFactory.getLogger(AppRegisterFacade.class);

    @Autowired
    private UserFacade userFacade;

    public UsernamePasswordToken validateLoginUser(RegisterUser member, Validateinfo validateinfo, Session session) {
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
                    int isExist = userFacade.isExistAccount(phone);
                    if (isExist == 0) { //手机号不存在

                        //2 注册用户，并把token入库, 放入缓存
                        Gson gson = new Gson();
                        String json = gson.toJson(newToken);
                        member.setToken(json);
                        this.registerMember(member);
                        //3 清除session中验证码的信息
                        session.removeAttribute("r" + validateinfo.getAccount());
                        //4 返回token（后期可加密）
                        return newToken;

                    } else {
                        //存在该用户
                        throw new BusinessException(MsgCodeConstant.member_mcode_account_exist, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
                    }
                } else {
                    throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
                }

            } else {
                //超过短信验证码有效期则清除session信息
                session.removeAttribute("r" + validateinfo.getAccount());
                throw new BusinessException(MsgCodeConstant.member_mcode_sms_timeout, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
            }

        } else {
            throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
        }
    }

    /**
     * 注册用户
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

}

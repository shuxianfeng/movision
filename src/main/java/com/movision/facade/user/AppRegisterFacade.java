package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.UserConstants;
import com.movision.controller.app.RegisterController;
import com.movision.exception.BusinessException;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.DateUtils;
import com.movision.utils.MsgPropertiesUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    public Boolean registerMobileMember(RegisterUser member, Validateinfo validateinfo, Session session) {
        String verifyCode = validateinfo.getCheckCode();
        if (verifyCode != null) {

            Date currentTime = new Date();
            Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(validateinfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 12, 10);
            if (currentTime.before(sendSMStime)) {

                log.debug("mobile verifyCode == " + member.getMobileCheckCode());
                if (validateinfo.getCheckCode().equalsIgnoreCase(member.getMobileCheckCode())) {
                    int isExist = userFacade.isExistAccount(member.getPhone());
                    if (isExist == 0) {
                        //注册用户，入库
                        this.registerMember(member);
                        //清除session信息
                        session.removeAttribute("r" + validateinfo.getAccount());
                        return true;

                    } else {
                        throw new BusinessException(MsgCodeConstant.member_mcode_account_exist, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
                    }
                } else {
                    throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
                }

            } else {
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

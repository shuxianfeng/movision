package com.zhuhuibao.mybatis.memCenter.service;

/**
 * Created by cxx on 2016/3/14 0014.
 */

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.MemRealCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemInfoCheckMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemRealCheckMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.SendEmail;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 发送邮件
 *
 * @author cxx
 */
@Service
@Transactional
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    MemInfoCheckMapper infoCheckMapper;

    @Autowired
    MemRealCheckMapper realCheckMapper;

    @Autowired
    VipInfoService vipInfoService;

    /**
     * 发送邮件
     */
    public void sendChangeEmail(String email, String id) {
        ///邮件的内容
        String currentTime = DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer("");
        String serverIp = PropertiesUtils.getValue("host.ip");

        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("筑慧宝邮箱修改：");
        sb.append("</strong>");
        sb.append("</p>");
        sb.append("<p style=\"margin:0; padding:20px 0 12px 0; color:#555555;\">");
        sb.append("您收到这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请修改了绑定邮箱。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。");
        sb.append("</p>");
        sb.append("<p>请使用以下链接激活该邮箱：</p>");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"");
        sb.append(serverIp);
        sb.append("/rest/member/mc/user/upd_email?email=");
        sb.append(new String(EncodeUtil.encodeBase64(email)) + "&time=" + new String(EncodeUtil.encodeBase64(currentTime)));
        sb.append("&id=" + new String(EncodeUtil.encodeBase64(id)));
        sb.append("\">");
        sb.append(serverIp);
        sb.append("/rest/member/mc/user/upd_email?email=");
        sb.append(new String(EncodeUtil.encodeBase64(email)) + "&time=" + new String(EncodeUtil.encodeBase64(currentTime)));
        sb.append("&id=" + new String(EncodeUtil.encodeBase64(id)));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        sb.append("</p>为了保障您帐号的安全性，请在24小时内完成邮箱修改！</p>");
        sb.append("<p>筑慧宝团队</p>");
        sb.append("<p>");
        sb.append(DateUtils.date2Str(new Date(), "yyyy-MM-dd"));
        sb.append("</p>");
        log.info("send email link == " + sb.toString());
        //发送邮件
        SendEmail.send(email, sb.toString(), "筑慧宝-邮箱修改");
    }

    /**
     * 发送邀请代理商邮件
     */
    public void sendInviteEmail(Member member, String email) {

        String companyName = member.getEnterpriseName();
        String linkman = member.getEnterpriseLinkman();
        if (linkman == null) {
            linkman = "";
        }
        log.info("发送邀请代理商邮件");
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("筑慧宝代理商邀请：");
        sb.append("</strong>");
        sb.append("</p>");
        sb.append("<p style=\"margin:0; padding:20px 0 12px 0; color:#555555;\">");
        sb.append("您好，我是");
        sb.append(companyName);
        sb.append("销售总监");
        sb.append(linkman);
        sb.append(",现邀请你入驻筑慧宝平台，共创大业！\n" +
                "筑慧宝是优秀的Icity一站式服务平台，我司已入驻筑慧宝，期待着你的加入！");
        sb.append("</p>");
        sb.append("<p>点击下面链接完成账号一键注册激活（账号默认该邮箱，密码默认123456，请登陆后修改密码）：</p>");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"" + PropertiesUtils.getValue("host.ip") + "/rest/agent/agentRegister?vm=");
        sb.append(new String(EncodeUtil.encodeBase64(email)));
        sb.append("\">" + PropertiesUtils.getValue("host.ip") + "/rest/agent/agentRegister?vm=");
        sb.append(new String(EncodeUtil.encodeBase64(email)));
        sb.append("</a>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        sb.append("<div style=\"line-height:80px;height:80px\" </div>");
        sb.append("<p>筑慧宝团队</p>");
        sb.append("<p>");
        sb.append(DateUtils.date2Str(new Date(), "yyyy-MM-dd"));
        sb.append("</p>");
        log.info("send email link == " + sb.toString());
        //发送邮件
        try {
            SendEmail.send(email, sb.toString(), "筑慧宝-代理商邀请");
        } catch (Exception e) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "发送邮件失败");
        }

    }

    /**
     * 代理商邮件注册
     */
    public Response agentRegister(String email) {
        Member member = new Member();
        String md5Pwd = new Md5Hash("123456", null, 2).toString();
        member.setEmail(email);
        member.setPassword(md5Pwd);
        try {
            Member newMember = memberMapper.findMember(member);
            if (newMember != null) {
                throw new BusinessException(MsgCodeConstant.member_mcode_mail_registered,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_registered)));
            } else {
                member.setIdentify("4");//代理商
                member.setStatus(MemberConstant.MemberStatus.ZCCG.toString());
                memberMapper.agentRegister(member);

                //基本资料审核表+实名认证审核表插入数据
                MemInfoCheck infoCheck = new MemInfoCheck();
                BeanUtils.copyProperties(member,infoCheck);
                infoCheck.setId(Long.valueOf(member.getId()));
                infoCheck.setStatus(MemberConstant.MemberStatus.ZCCG.toString());
                infoCheckMapper.insertSelective(infoCheck);

                MemRealCheck realCheck = new MemRealCheck();
                BeanUtils.copyProperties(member,realCheck);
                realCheck.setId(Long.valueOf(member.getId()));
                realCheck.setStatus(MemberConstant.MemberStatus.ZCCG.intValue());
                realCheckMapper.insertSelective(realCheck);

                //会员注册初始化特权
                if (member.getId() != null) {
                    vipInfoService.initDefaultExtraPrivilege(Long.valueOf(member.getId()), member.getIdentify());
                }
            }
        } catch (Exception e) {
            log.error("agentRegister error >>>", e);
            e.printStackTrace();
            throw e;
        }
        return new Response();
    }

    /**
     * 获得跳转页面的URL
     *
     * @param response
     * @return
     */
    public String getRedirectUrl(Response response) {
        String redirectUrl;
        if (response.getCode() == 200) {
            redirectUrl = PropertiesUtils.getValue("host.ip") + "/" + PropertiesUtils.getValue("active.mail.page");
        } else {
            redirectUrl = PropertiesUtils.getValue("host.ip") + "/" + PropertiesUtils.getValue("active.mail.replay.page");
        }
        return redirectUrl;
    }
}

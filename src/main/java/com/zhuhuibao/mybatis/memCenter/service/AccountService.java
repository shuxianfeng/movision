package com.zhuhuibao.mybatis.memCenter.service;

/**
 * Created by cxx on 2016/3/14 0014.
 */

import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发送邮件
 * @author cxx
 *
 */
@Service
@Transactional
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    /**
     * 发送邮件
     * @param member 会员信息
     * @param serverIp 服务器IP
     */
    public void sendChangeEmail(Member member, String serverIp){
        ///邮件的内容
        StringBuffer sb=new StringBuffer("");
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
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"http://");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("\">http://");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString(),"筑慧宝-邮箱修改");
    }

    /**
     * 发送邀请代理商邮件
     */
    public void sendInviteEmail(String title,String content,String email){
        log.info("发送邀请代理商邮件");
        //发送邮件
        SendEmail.send(email, content,title);
    }
}

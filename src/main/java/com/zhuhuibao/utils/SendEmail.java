package com.zhuhuibao.utils;

import java.util.*;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.security.EncodeUtil;


public class SendEmail {
	private static final Logger log = LoggerFactory.getLogger(SendEmail.class);
 	public static final String HOST = "smtp.exmail.qq.com";
    public static final String PROTOCOL = "smtp";   
    public static final int PORT = 465;
    public static final String FROM = "zhuhuibao@zhuhui8.com";//发件人的email
    public static final String PWD = "Zhb@D503";//发件人密码
    
    /**
     * 获取Session
     * @return
     */
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , "true");//通过验证
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }

        };
        Session session = Session.getDefaultInstance(props , authenticator);

        return session;
    }
    
    public static void send(String toEmail , String content,String title) {
        Session session = getSession();
        try {
            log.info("--send--"+content);
            // Instantiate a message
            Message msg = new MimeMessage(session);

            //Set message attributes
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");

            //Send the message
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    /**
     * 邮件群发，发送个多个用户
     * @param toEmail  收件人数组集合
     * @param content  发送邮件内容
     * @param title  邮件标题
     */
    public static void sendMutilMail(String[] toEmail , String content,String title) {
        Session session = getSession();
        try {
            log.info("--send--"+content);
            // Instantiate a message
            Message msg = new MimeMessage(session);

            //Set message attributes
            msg.setFrom(new InternetAddress(FROM));
            if(toEmail != null && toEmail.length > 0)
            {
            	InternetAddress[] address = new InternetAddress[toEmail.length];  
                for (int i = 0; i < toEmail.length; i++) {  
                    address[i] = new InternetAddress(toEmail[i]);  
                }  
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject(title);
                msg.setSentDate(new Date());
                msg.setContent(content , "text/html;charset=utf-8");

                //Send the message
                Transport.send(msg);
            }
            
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
    	String serverIp = "139.196.189.100";
    	Member member = new Member();
    	member.setEmail("19630759@qq.com");
    	StringBuffer sb=new StringBuffer("");
        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("亲爱的用户：");
        sb.append("</strong>");
        sb.append("<p>您好！");
        sb.append("</p>");
        sb.append("<p>您收到这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。</p>");
        sb.append("<p>请使用以下链接修改密码：</p>");
        sb.append("<p style=\"padding:0px\">");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"http://");
        sb.append(serverIp);
        sb.append("/rest/validateMail?action=validate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("\">http://");
        sb.append(serverIp);
        sb.append("/rest/validateMail?action=validate&vm="+new String(EncodeUtil.encodeBase64(member.getEmail()))+"</a>"); 
        //sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        sb.append("</p>为了保障您帐号的安全性，请在24小时内完成密码重置！</p>");
        sb.append("<div style=\"line-height:80px;height:80px\" </div>");
        sb.append("<p>筑慧宝团队</p>");
        sb.append("<p>");
        sb.append(DateUtils.date2Str(new Date(), "yyyy-MM-dd"));
        sb.append("</p>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString(),"筑慧宝-找回账户密码");
        /*String[] toEmail = {"19630759@qq.com","23915054@qq.com"};        		
        SendEmail.sendMutilMail(toEmail, "ceshi", "测试");*/
	}
}

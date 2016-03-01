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

import com.zhuhuibao.security.EncodeUtil;


public class SendEmail {
	private static final Logger log = LoggerFactory.getLogger(SendEmail.class);
 	public static final String HOST = "smtp.exmail.qq.com";
    public static final String PROTOCOL = "smtp";   
    public static final int PORT = 465;
    public static final String FROM = "penglong@zhuhui8.com";//发件人的email
    public static final String PWD = "Pl@123456";//发件人密码
    
    /**
     * 获取Session
     * @return
     */
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , "true");

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
    
    public static void main(String[] args) {
	}
}

package com.mbrickspan.mbpan.service;

import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;



/**
 * @author: Guessed 魏嘉辰
 * @Date: 2020/10/20 13:15
 */
@Service("mailService")
public  class MailService {
    private String from = "w791598204@163.com";

    public static JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.163.com");
        javaMailSender.setPort(465);
        javaMailSender.setUsername("w791598204@163.com");
        javaMailSender.setPassword("UPMUGVTBYZYGEAGY");
        javaMailSender.setProtocol("smtps");
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public  void sendSimpleMail(String to, String title, String content) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender().send(message);
        logger.info("邮件发送成功");
    }
}
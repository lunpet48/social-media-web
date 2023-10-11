package com.webapp.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;



    public boolean sendMail(String sendTo, String content, String subject) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(sendTo);
            mailMessage.setText(content);
            mailMessage.setSubject(subject);

            javaMailSender.send(mailMessage);
            return true;
        }

        catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }
}

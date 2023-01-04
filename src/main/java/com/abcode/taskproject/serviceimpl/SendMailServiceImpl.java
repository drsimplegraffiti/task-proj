package com.abcode.taskproject.serviceimpl;

import com.abcode.taskproject.entity.Email;
import com.abcode.taskproject.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;

    public String sendSimpleMail(Email details)
    {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(details.getRecipient());
            message.setSubject(details.getSubject());
            message.setText(details.getMsgBody());
            javaMailSender.send(message);
            return "Mail sent successfully";
        } catch (Exception e) {
            return "Mail not sent";
        }
    }

    @Override
    public void sendMail(Email email) throws IOException {
        sendSimpleMail(email);
    }
}

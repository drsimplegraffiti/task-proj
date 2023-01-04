package com.abcode.taskproject.service;

import com.abcode.taskproject.entity.Email;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;


@Service
public interface SendMailService {
    void sendMail(Email email) throws IOException;

}

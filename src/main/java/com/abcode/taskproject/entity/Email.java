package com.abcode.taskproject.entity;


import lombok.*;

@Getter
@Setter
@Builder
public class Email {
    private String recipient;
    private String msgBody;
    private String subject;
}

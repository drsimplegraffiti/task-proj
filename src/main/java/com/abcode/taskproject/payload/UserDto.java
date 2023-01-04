package com.abcode.taskproject.payload;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime dateCreated;
}

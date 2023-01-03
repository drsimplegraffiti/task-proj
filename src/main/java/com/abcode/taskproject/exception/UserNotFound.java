package com.abcode.taskproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException { // RuntimeException is a class that extends Exception class
    private String message; // message to be displayed
    public UserNotFound(String message){
        super(message); // super is a keyword to call the constructor of the parent class
        this.message = message;  // this is a keyword to refer to the current object
    }
}


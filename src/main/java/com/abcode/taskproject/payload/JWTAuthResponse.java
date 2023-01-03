package com.abcode.taskproject.payload;


import lombok.Getter;

@Getter

public class JWTAuthResponse {
    private String  token;
    private String  tokenType = "Bearer"; // Bearer is a type of token that is used in the Authorization header

    public JWTAuthResponse(String token) {
        this.token = token;
    }
}

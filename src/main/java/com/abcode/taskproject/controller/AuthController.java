package com.abcode.taskproject.controller;

import com.abcode.taskproject.payload.JWTAuthResponse;
import com.abcode.taskproject.payload.LoginDto;
import com.abcode.taskproject.payload.UserDto;
import com.abcode.taskproject.security.JwtTokenProvider;
import com.abcode.taskproject.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Auto-wire userService
    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Auto wire the authentication manager
    @Autowired
    private AuthenticationManager authenticationManager;

    // Auto wire the jwt token provider
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // register
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
       return new ResponseEntity<>( userService.createUser(userDto), HttpStatus.CREATED);
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> loginUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        System.out.println("authentication = " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate token and return it
        String  token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }


}

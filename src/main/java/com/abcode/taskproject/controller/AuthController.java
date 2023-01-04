package com.abcode.taskproject.controller;

import com.abcode.taskproject.entity.Email;
import com.abcode.taskproject.payload.ApiResponse;
import com.abcode.taskproject.payload.JWTAuthResponse;
import com.abcode.taskproject.payload.LoginDto;
import com.abcode.taskproject.payload.UserDto;
import com.abcode.taskproject.security.JwtTokenProvider;
import com.abcode.taskproject.service.SendMailService;
import com.abcode.taskproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    // Auto-wire userService
    private final UserService userService;

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

    // auto wire the mail service
    @Autowired
    private SendMailService sendMailService;


    // register and send email
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) throws Exception {
        UserDto savedUser = userService.createUser(userDto);
        // get details from the userDto
        String email = userDto.getEmail();
        String subject = "Welcome to Task Project";
        String body = "Hello welcome to Task Project";

        // send email
        Email emailDetails = Email.builder()
                .recipient(email)
                .subject(subject)
                .msgBody(body)
                .build();
           sendMailService.sendMail(emailDetails);

        ApiResponse apiResponse = ApiResponse.builder()
                .isSuccessful(true)
                .body(savedUser)
                .timeStamp(LocalDateTime.now())
                .code("200")
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
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

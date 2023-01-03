package com.abcode.taskproject.security;

import com.abcode.taskproject.exception.APIException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 3600000); // 1 hour
        String token  = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "JWTSecretKey")
                .compact();
        return token;
    }
    public String getEmailFromToken(String token){
        Claims claims =  Jwts.parser().setSigningKey("JWTSecretKey")
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
         try{
             Jwts.parser().setSigningKey("JWTSecretKey")
                     .parseClaimsJws(token);
                return true;
         } catch (Exception e){
                throw new APIException("Invalid token: " + e.getMessage());
         }
    }
}

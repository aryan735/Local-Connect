package com.localconnct.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    private String  SECRET_KEY ="aryanSuperSecretKeyForJWTToken2025!@#";

    @Bean
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String extractEmail(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Date extractExpiration(String email){
        return extractAllClaims(email).getExpiration();
    }

    public String generateToken(String email){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,email);
    }

    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setHeaderParam("typ","JWT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token){
        return !isTokenExpired(token);
    }

}

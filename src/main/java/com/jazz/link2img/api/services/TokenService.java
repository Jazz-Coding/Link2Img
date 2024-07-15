package com.jazz.link2img.api.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    private String SECRET_KEY = "1234";

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }
    public String extractUsernameHeader(String authorizationHeader){
        return extractUsername(authorizationHeader.substring(7));
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        Date now = new Date();
        return extractExpiration(token).before(now);
    }

    public boolean checkToken(String token) {
        return !isTokenExpired(token);
    }

    public boolean checkTokenHeader(String authorizationHeader){
        return checkToken(authorizationHeader.substring(7));
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}

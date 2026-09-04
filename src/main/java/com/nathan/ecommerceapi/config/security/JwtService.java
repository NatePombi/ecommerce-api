package com.nathan.ecommerceapi.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service

public class JwtService {
    @Value("${security.jwt.secret}")
    private String secretKey;
    @Value("${security.jwt.expiration}")
    private long expiration;


    public String createToken(Long customerId,String email) {
        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("customerId",customerId)
                .issuedAt(now)
                .expiration(new Date(now.getTime()+expiration))
                .signWith(getSecretKey())
                .compact();

    }


    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
       try {
           String email = extractClaims(token).getSubject();
           return email.equals(userDetails.getUsername()) && !isExpired(token);

       }
       catch (Exception e) {
           return false;
       }
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }


    public String extractEmail(String token){
        return getClaims(token,Claims::getSubject);
    }

    public Long extractCustomerId(String token){
        return getClaims(token, claim -> claim.get("customerId",Long.class));
    }

    private boolean isExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }



}

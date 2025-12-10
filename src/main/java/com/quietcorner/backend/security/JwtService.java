package com.quietcorner.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // Секретный ключ из application.properties
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Время жизни токена
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Генерация токена для UserDetails (User)
    public String generateToken(UserDetails userDetails) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername()) // <-- ИСПРАВЛЕНО
                .setIssuedAt(new Date(System.currentTimeMillis())) // <-- ИСПРАВЛЕНО
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // <-- ИСПРАВЛЕНО
                .signWith(getSignInKey()) // <-- ИСПРАВЛЕНО (используем более старый, но рабочий метод)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // <-- ДОБАВЛЕНО для старых версий
                .setSigningKey(getSignInKey()) // <-- ИСПРАВЛЕНО (на более старый метод)
                .build()
                .parseClaimsJws(token) // <-- ИСПРАВЛЕНО (используем более старый метод парсинга)
                .getBody(); // <-- ДОБАВЛЕНО
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
package com.hospital.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Key;
import java.util.Date;

public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours

    public String generateToken(int userId, String username, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
            .setSubject(username)
            .claim("userId", userId)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SECRET_KEY)
            .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return null;
        }
    }

    public int getUserId(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.get("userId", Integer.class) : -1;
    }

    public String getUsername(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getRole(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.get("role", String.class) : null;
    }
} 
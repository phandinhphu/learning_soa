package com.example.auth.util;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	private static final Key SECRET_KEY = Keys.hmacShaKeyFor("MySuperStrongSecretKey1234567890987654321".getBytes());
	private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
	
	public static String generateToken(String userId) {
		return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
	}
	
	public static String validateToken(String token) {
		try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (JwtException e) {
            return null; // Token không hợp lệ hoặc hết hạn
        }
	}
	
	public static boolean verifyToken(String token) {
		return validateToken(token) != null;
	}
}

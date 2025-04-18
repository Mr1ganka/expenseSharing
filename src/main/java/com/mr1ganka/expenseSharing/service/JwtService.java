package com.mr1ganka.expenseSharing.service;

import com.mr1ganka.expenseSharing.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private static final String SECRET_KEY = "746B6270357538782F4125442A462D4A614E645267556B58703273357638792F";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; //valid for 24hrs

    private byte[] getSigningKey() {
        return Decoders.BASE64.decode(SECRET_KEY);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        long currentTimeMillis = System.currentTimeMillis();
        long newTimeMillis = currentTimeMillis + EXPIRATION_TIME;

        Date expirationDate = new Date(newTimeMillis);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(expirationDate).signWith(Keys.hmacShaKeyFor(getSigningKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, User user) {
        final String userName = extractUsername(token);
        return (userName.equals(user.getUsername()) && !isTokenExpired(token) && !tokenBlacklistService.isTokenBlackListed(token));
    }

    public Long getRemainingExpiration(String token) {
        return (Long) extractExpiration(token).getTime() - System.currentTimeMillis();
    }
}

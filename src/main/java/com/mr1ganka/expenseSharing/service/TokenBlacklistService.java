package com.mr1ganka.expenseSharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private StringRedisTemplate redis;

    public void setTokenToBlackList (String token, long expirationTimeMillis) {
        redis.opsForValue().set(token, "blackListed", expirationTimeMillis, TimeUnit.MILLISECONDS);
    }

    protected boolean isTokenBlackListed (String token) {
        return redis.hasKey(token);
    }
}

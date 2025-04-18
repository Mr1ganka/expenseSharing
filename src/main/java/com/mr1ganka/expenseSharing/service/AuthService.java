package com.mr1ganka.expenseSharing.service;

import com.mr1ganka.expenseSharing.model.User;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public String authenticateUser (String email, String pass) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, pass)
        );

        if (auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            return jwtService.generateToken(user);
        } else {
            throw new BadCredentialsException("Invalid credentials passed");
        }
    }

    public void logout(String token) {
        Long timeToExpire = jwtService.getRemainingExpiration(token);
        System.out.println("Token: {"+ token+"} expires in "+timeToExpire+" ms");
        tokenBlacklistService.setTokenToBlackList(token, timeToExpire);
    }

}


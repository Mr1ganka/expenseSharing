package com.mr1ganka.expenseSharing.controller;

import com.mr1ganka.expenseSharing.dto.LoginRequest;
import com.mr1ganka.expenseSharing.dto.LoginResponseDto;
import com.mr1ganka.expenseSharing.service.AuthService;
import com.mr1ganka.expenseSharing.service.JwtService;
import com.mr1ganka.expenseSharing.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private final String BEARER = "Bearer ";

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest request) {
        String token = authService.authenticateUser(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(new LoginResponseDto(token), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String token = authHeader.substring(BEARER.length());
            authService.logout(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}

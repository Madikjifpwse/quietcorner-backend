package com.quietcorner.backend.controller;

import com.quietcorner.backend.dto.auth.LoginRequest;
import com.quietcorner.backend.dto.auth.AuthResponse;
import com.quietcorner.backend.dto.auth.RegisterRequest;
import com.quietcorner.backend.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    /**
     * POST /api/auth/register
     * Эндпоинт для регистрации нового пользователя.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * POST /api/auth/login
     * Эндпоинт для входа (авторизации).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
}
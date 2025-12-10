package com.quietcorner.backend.service.auth;

import com.quietcorner.backend.dto.auth.LoginRequest;
import com.quietcorner.backend.dto.auth.RegisterRequest;
import com.quietcorner.backend.dto.auth.AuthResponse;
import com.quietcorner.backend.security.JwtService;
import com.quietcorner.backend.user.Role;
import com.quietcorner.backend.user.User;
import com.quietcorner.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация нового пользователя
     */
    public AuthResponse register(RegisterRequest request) {

        // 1. Создаем объект User
        var user = User.builder()
                .username(request.getUsername())
                // 2. Хешируем пароль перед сохранением
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) // Все новые пользователи - USER
                .favoritePlaces(Collections.emptySet()) // Изначально избранное пустое
                .build();

        // 3. Сохраняем в БД
        userRepository.save(user);

        // 4. Генерируем токен
        var jwtToken = jwtService.generateToken(user);

        // 5. Возвращаем токен клиенту
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    /**
     * Аутентификация существующего пользователя
     */
    public AuthResponse login(LoginRequest request) {

        // 1. Аутентификация через менеджер (проверяет логин/пароль)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Если аутентификация успешна, загружаем пользователя
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")); // По идее, не должно произойти

        // 3. Генерируем новый токен
        var jwtToken = jwtService.generateToken(user);

        // 4. Возвращаем токен
        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }
}
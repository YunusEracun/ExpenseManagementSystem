package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.dto.AuthenticationRequest;
import com.example.ExpenseManagementSystem.dto.AuthenticationResponse;
import com.example.ExpenseManagementSystem.dto.RegisterRequest;
import com.example.ExpenseManagementSystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Adresimiz: /api/auth
@RequiredArgsConstructor
@Tag(name = "Kimlik Doğrulama", description = "Kayıt olma ve Giriş yapma işlemleri")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @Operation(summary = "Kayıt Ol", description = "Yeni kullanıcı oluşturur ve Token döner.")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate") // veya /login
    @Operation(summary = "Giriş Yap", description = "Email ve Şifre ile giriş yapar, JWT Token döner.")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.dto.AuthenticationRequest;
import com.example.ExpenseManagementSystem.dto.AuthenticationResponse;
import com.example.ExpenseManagementSystem.dto.RegisterRequest;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.Role;
import com.example.ExpenseManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Şifreleyici (BCrypt)
    private final JwtService jwtService; // Token Fabrikası
    private final AuthenticationManager authenticationManager; // Giriş Müdürü

    // KAYIT OLMA (REGISTER)
    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // --- GÜVENLİK YAMASI BURADA ---
        // Eski hali: user.setRole(request.getRole()); -> TEHLİKELİ!
        // Yeni hali: Herkesi zorla çalışan yap.
        user.setRole(Role.EMPLOYEE);
        // ------------------------------

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // GİRİŞ YAPMA (LOGIN)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Spring Security'nin kendi giriş fonksiyonunu çağır.
        // Eğer şifre yanlışsa bu satır hata fırlatır ve aşağıya geçmez.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Eğer buraya geldiyse şifre doğrudur. Kullanıcıyı bul.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // 3. Yeni bir token üret ve ver.
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
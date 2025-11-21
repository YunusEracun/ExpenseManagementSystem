package com.example.ExpenseManagementSystem.config;

import com.example.ExpenseManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    // 1. KULLANICI BULMA MANTIĞI
    // Spring Security "Bana şu kullanıcıyı bul" dediğinde bu çalışacak.
    // Biz de repository'deki "findByEmail" metodunu çağırıyoruz.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı!"));
    }

    // 2. DOĞRULAMA SAĞLAYICISI (Logic Birimi)
    // Hangi servisi ve hangi şifreleyiciyi kullanacağını burada söylüyoruz.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 3. GİRİŞ MÜDÜRÜ (AuthenticationManager)
    // Login işleminde "username/password doğru mu?" diye soracağımız yetkili.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 4. ŞİFRELEYİCİ (BCrypt)
    // Kullanıcı "12345" girerse biz onu veritabanındaki "$2a$10$..." ile kıyaslayamayız.
    // Bu alet, girilen şifreyi aynı yöntemle şifreleyip kıyaslar.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
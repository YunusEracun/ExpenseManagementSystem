package com.example.ExpenseManagementSystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF Korumasını Kapat (Çünkü biz JWT kullanıyoruz, tarayıcı oturumu değil)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. URL İzinlerini Ayarla
                .authorizeHttpRequests(auth -> auth
                        // A) Herkese Açık Olanlar (Login, Register ve Swagger)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // B) Diğer Tüm İstekler İçin Token Şart!
                        .anyRequest().authenticated()
                )

                // 3. Oturum Yönetimi: Stateless (Sunucuda kimseyi tutma)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Kimlik Doğrulayıcıyı Tanıt (Bizim ApplicationConfig'deki)
                .authenticationProvider(authenticationProvider)

                // 5. Filtreleme Sırası: Standart filtrelerden ÖNCE bizim JWT filtremizi çalıştır.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
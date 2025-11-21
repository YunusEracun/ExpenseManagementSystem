package com.example.ExpenseManagementSystem.config;

import com.example.ExpenseManagementSystem.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Spring bu sınıfı otomatik tanısın ve yönetsin.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Token üretip çözen servisimiz
    private final UserDetailsService userDetailsService; // Kullanıcı veritabanından bulan servis (Birazdan tanımlayacağız)

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. İsteğin başlığından "Authorization" bilgisini al
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Kontrol: Başlık yoksa veya "Bearer " ile başlamıyorsa bizi ilgilendirmez.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Diğer filtrelere pasla, biz karışmıyoruz.
            return;
        }

        // 3. "Bearer " kısmını atıp sadece Token'ı (Kıymayı) al
        jwt = authHeader.substring(7);

        // 4. Token'ın içinden E-mail'i çıkar (JwtService yapıyor bunu)
        userEmail = jwtService.extractUsername(jwt);

        // 5. Eğer email varsa VE kullanıcı henüz sisteme giriş yapmamışsa (SecurityContext boşsa)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Veritabanından kullanıcıyı bul
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Token geçerli mi? (İmzası doğru mu, süresi dolmuş mu?)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // GEÇERLİ! O zaman Spring Security'e "Bu adam bizden, içeri al" diyoruz.
                // (Burası teknik bir kart oluşturma kısmı)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // KİMLİĞİ MASAYA KOYUYORUZ: Artık sistem bu kişiyi tanıyor.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Zinciri devam ettir (Sıradaki filtreye geç)
        filterChain.doFilter(request, response);
    }
}
package com.example.ExpenseManagementSystem.config;

import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.Role;
import com.example.ExpenseManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // DİKKAT: Bu import'u seç!
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // application.properties dosyasındaki değerleri buraya çekiyoruz
    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // E-postayı koddan değil, değişkenden alıyoruz
        if (!userRepository.existsByEmail(adminEmail)) {

            User newAdmin = new User();
            newAdmin.setFirstName("Süper");
            newAdmin.setLastName("Admin");
            newAdmin.setEmail(adminEmail); // Değişkeni kullandık
            newAdmin.setPassword(passwordEncoder.encode(adminPassword)); // Şifreyi değişkenden aldık
            newAdmin.setRole(Role.ADMIN);

            userRepository.save(newAdmin);
            System.out.println("SİSTEM: Admin oluşturuldu. Email: " + adminEmail);
        }
    }
}
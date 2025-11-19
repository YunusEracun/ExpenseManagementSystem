package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.exception.ResourceNotFoundException;
import com.example.ExpenseManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Spring'e diyoruz ki: "Bu sınıf iş mantığı içerir."
@RequiredArgsConstructor // Repository'yi otomatik enjekte et (Dependency Injection)
public class UserService {

    private final UserRepository userRepository;

    // 1. Kullanıcıyı Veritabanına Kaydet (UserController kullanıyor)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 2. ID ile Kullanıcı Bul (ExpenseService kullanıyor)
    public User getUserById(Long id) {
        // Veritabanına bak, varsa getir, yoksa hata fırlat.
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı! ID: " + id));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
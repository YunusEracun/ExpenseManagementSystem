package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Bu sınıfın bir API noktası olduğunu belirtir.
@RequestMapping("/api/users") // Adresimiz: localhost:8080/api/users
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
}
package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ExpenseManagementSystem.dto.UserDto;

@RestController // Bu sınıfın bir API noktası olduğunu belirtir.
@RequestMapping("/api/users") // Adresimiz: localhost:8080/api/users
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User user) {

        User savedUser = userService.saveUser(user);

        UserDto userDto = UserDto.mapToDto(savedUser);

        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = UserDto.mapToDto(user);
        return ResponseEntity.ok(userDto);
    }
}
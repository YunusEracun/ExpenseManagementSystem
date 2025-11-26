package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.dto.UserDto;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(user -> {
                    UserDto dto = UserDto.mapToDto(user);
                    dto.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ExpenseController.class).createExpense(user.getId(), null,null)).withRel("create-expense"));
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = UserDto.mapToDto(user);
        userDto.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        userDto.add(linkTo(methodOn(ExpenseController.class).createExpense(id, null,null)).withRel("create-expense"));

        return ResponseEntity.ok(userDto);
    }

}
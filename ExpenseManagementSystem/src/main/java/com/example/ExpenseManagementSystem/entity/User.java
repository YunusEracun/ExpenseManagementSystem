package com.example.ExpenseManagementSystem.entity;


import com.example.ExpenseManagementSystem.enums.Role;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.Pattern;
@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[a-zA-ZğüşıöçĞÜŞİÖÇ ]+$", message = "İsimde sayı veya özel karakter olamaz")
    @Size(min = 2, max = 50, message = "İsim en az 2 karakter olmalı")
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    /*       bu kısmı AllArgsConstructor yapıyormuş
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
*/
}

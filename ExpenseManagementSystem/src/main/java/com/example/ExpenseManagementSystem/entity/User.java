package com.example.ExpenseManagementSystem.entity;

import com.example.ExpenseManagementSystem.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails { // DEĞİŞİKLİK 1: UserDetails eklendi

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

    // --- SPRING SECURITY İÇİN GEREKLİ METOTLAR ---

    // 1. Yetkileri (Rolleri) Bildiriyoruz
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security, rolü "SimpleGrantedAuthority" nesnesi olarak ister.
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
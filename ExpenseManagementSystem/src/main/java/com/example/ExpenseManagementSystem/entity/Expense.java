package com.example.ExpenseManagementSystem.entity;

import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String description;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    // --- EKLENEN KISIM 1: Kategori İlişkisi ---
    // Projende Category diye bir Entity olduğu için ilişki kuruyoruz.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // --- EKLENEN KISIM 2: Dosya Yolu ---
    // MinIO'dan dönen dosya ismini burada saklayacağız.
    @Column(name = "receipt_url")
    private String receiptUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
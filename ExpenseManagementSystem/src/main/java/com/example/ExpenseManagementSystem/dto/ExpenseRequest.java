package com.example.ExpenseManagementSystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseRequest {

    @NotNull(message = "Tutar boş olamaz")
    @DecimalMin(value = "0.01", message = "Tutar 0'dan büyük olmalıdır")
    private BigDecimal amount;

    @NotBlank(message = "Açıklama boş olamaz")
    private String description;

    @NotBlank(message = "Kategori id boş olamaz")
    private Long categoryId;

    private LocalDateTime date;
}
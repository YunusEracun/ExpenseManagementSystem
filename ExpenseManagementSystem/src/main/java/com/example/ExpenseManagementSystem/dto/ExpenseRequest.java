package com.example.ExpenseManagementSystem.dto;

import jakarta.validation.constraints.*; // Hepsini kapsar
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequest {

    // Senin yazdığın kısım (Doğru)
    @NotNull(message = "Tutar boş olamaz")
    @DecimalMin(value = "0.01", message = "Tutar 0'dan büyük olmalıdır")
    private BigDecimal amount;

    // Senin yazdığın kısım (Doğru)
    @NotBlank(message = "Açıklama boş olamaz")
    private String description;

    // --- EKLENMESİ ŞART OLANLAR ---

    @NotNull(message = "Kategori seçilmelidir")
    private Long categoryId; // Örn: 1 (Yemek), 2 (Taksi)

    @NotNull(message = "Fiş tarihi boş olamaz")
    @PastOrPresent(message = "Gelecek tarihli fiş giremezsiniz")
    private LocalDate expenseDate; // Fişin üzerindeki tarih
}
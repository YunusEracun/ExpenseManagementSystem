package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.dto.ExpenseRequest;
import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Harcama İşlemleri", description = "Harcama ekleme, listeleme ve onaylama yönetimi")
public class ExpenseController {

    private final ExpenseService expenseService;

    // --- DEĞİŞEN KISIM BURASI ---
    @PostMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Kargo Tipini Belirttik
    @Operation(summary = "Yeni Harcama Ekle", description = "Kullanıcı adına resimli harcama fişi oluşturur.")
    public ResponseEntity<Void> createExpense(
            @PathVariable Long userId,

            // JSON Kısmı:
            @Valid @RequestPart("request") ExpenseRequest request,

            // Dosya Kısmı (Zorunlu değilse false diyebilirsin):
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        // Artık entity oluşturma işini Service yapıyor. Biz sadece iletiyoruz.
        expenseService.createExpense(userId, request, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // ----------------------------

    @GetMapping("/pending")
    public ResponseEntity<List<Expense>> getPendingExpenses() {
        return ResponseEntity.ok(expenseService.getPendingExpenses());
    }

    @PatchMapping("/{id}/{status}")
    @Operation(
            summary = "Harcama Onayla/Reddet",
            description = "Verilen ID'li harcamanın durumunu değiştirir. DİKKAT: memberId parametresi ile ADMIN ID'si gönderilmelidir."
    )
    public ResponseEntity<Expense> updateExpenseStatus(
            @PathVariable Long id,
            @PathVariable ExpenseStatus status,
            @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(expenseService.processExpense(id, status, memberId));
    }
}
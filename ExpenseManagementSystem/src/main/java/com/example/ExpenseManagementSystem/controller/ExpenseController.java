package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.dto.ExpenseRequest;
import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.service.ExpenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Harcama İşlemleri", description = "Harcama ekleme, listeleme ve onaylama yönetimi")
public class ExpenseController {

    private final ExpenseService expenseService;

    // Harcama Ekleme (Dosya + JSON)
    @PostMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Expense> createExpense(
            @PathVariable Long userId,
            @RequestPart("expense") String expenseJson, // JSON string olarak gelir
            @RequestPart("file") MultipartFile file // Dosya binary olarak gelir
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseRequest expenseDto;

        try {
            // String olan JSON'ı Java nesnesine çeviriyoruz
            // Hata olursa burada yakalıyoruz ki UserController'daki link bozulmasın
            expenseDto = objectMapper.readValue(expenseJson, ExpenseRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON formatı hatalı: " + e.getMessage());
        }

        // Servise gönder
        Expense newExpense = expenseService.createExpense(userId, expenseDto, file);
        return ResponseEntity.ok(newExpense);
    }

    // Harcama Onaylama / Reddetme
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
package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.dto.ExpenseRequest;
import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Harcama İşlemleri", description = "Harcama ekleme, listeleme ve onaylama yönetimi") // BAŞLIK
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/{userId}")
    @Operation(summary = "Yeni Harcama Ekle", description = "Kullanıcı adına yeni bir harcama fişi oluşturur.")
    public ResponseEntity<Expense> createExpense(
            @PathVariable Long userId,
            @Valid @RequestBody ExpenseRequest request // ARTIK ENTITY DEĞİL DTO BEKLİYORUZ
    ) {

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());

        Expense savedExpense = expenseService.createExpense(userId, expense);

        return ResponseEntity.ok(savedExpense);
    }
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
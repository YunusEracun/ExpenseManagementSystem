package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/{userId}")
    public ResponseEntity<Expense> createExpense(@PathVariable Long userId, @RequestBody Expense expense) {
        Expense savedExpense = expenseService.createExpense(userId, expense);
        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Expense>> getPendingExpenses() {
        return ResponseEntity.ok(expenseService.getPendingExpenses());
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Expense> updateExpenseStatus(@PathVariable Long id, @PathVariable ExpenseStatus status) {
        return ResponseEntity.ok(expenseService.processExpense(id, status));
    }
}
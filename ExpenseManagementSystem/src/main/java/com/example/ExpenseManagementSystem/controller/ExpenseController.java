package com.example.ExpenseManagementSystem.controller;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
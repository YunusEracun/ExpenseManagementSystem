package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    // İŞTE EKSİK OLAN PARÇA BU METOT:
    public Expense createExpense(Long userId, Expense expense) {
        User user = userService.getUserById(userId);
        expense.setUser(user);
        expense.setDate(LocalDateTime.now());
        expense.setStatus(ExpenseStatus.PENDING);
        return expenseRepository.save(expense);
    }
    public List<Expense> getExpensesByUserId(Long userId) {
        userService.getUserById(userId);
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getPendingExpenses() {
        return expenseRepository.findByStatus(ExpenseStatus.PENDING);
    }

    public Expense processExpense(Long id, ExpenseStatus newStatus) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harcama bulunamadı! ID: " + id));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new RuntimeException("Bu harcama zaten işlenmiş! (Durumu: " + expense.getStatus() + ")");
        }

        expense.setStatus(newStatus);
        return expenseRepository.save(expense);
    }
}
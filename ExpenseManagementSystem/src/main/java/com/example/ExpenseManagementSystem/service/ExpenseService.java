package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
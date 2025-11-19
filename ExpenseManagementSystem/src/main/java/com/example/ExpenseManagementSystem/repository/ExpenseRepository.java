package com.example.ExpenseManagementSystem.repository;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);
    List<Expense> findByStatus(ExpenseStatus status);
}
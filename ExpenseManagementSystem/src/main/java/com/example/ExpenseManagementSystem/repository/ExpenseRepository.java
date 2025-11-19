package com.example.ExpenseManagementSystem.repository;

import com.example.ExpenseManagementSystem.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

}
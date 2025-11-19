package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.enums.Role;
import com.example.ExpenseManagementSystem.exception.ResourceNotFoundException;
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

    public Expense processExpense(Long id, ExpenseStatus newStatus, Long memberId) {

        // 1. İşlemi yapmaya çalışanı bul
        User member = userService.getUserById(memberId);

        // 2. KURAL: Sadece ADMIN onaylayabilir/reddedebilir
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Yetkisiz işlem! Sadece YÖNETİCİLER onay verebilir.");
        }

        // 3. Fişi bul
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harcama bulunamadı! ID: " + id));

        // 4. KURAL: Zaten işlem görmüşse dokunma
        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new RuntimeException("Bu harcama zaten işlenmiş!");
        }

        // 5. Yeni durumu işle
        expense.setStatus(newStatus);
        return expenseRepository.save(expense);
    }


}

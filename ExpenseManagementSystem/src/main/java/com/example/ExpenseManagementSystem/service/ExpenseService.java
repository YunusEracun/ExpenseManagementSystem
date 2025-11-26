package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.dto.ExpenseRequest; // DTO importu (Adı sendeki ile aynı olmalı)
import com.example.ExpenseManagementSystem.entity.Category;
import com.example.ExpenseManagementSystem.entity.Expense;
import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.ExpenseStatus;
import com.example.ExpenseManagementSystem.enums.Role;
import com.example.ExpenseManagementSystem.exception.ResourceNotFoundException;
import com.example.ExpenseManagementSystem.repository.CategoryRepository;
import com.example.ExpenseManagementSystem.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // Dosya için gerekli

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService; // 1. EKLENDİ: MinIO servisi
    private final CategoryRepository categoryRepository;


    // 2. GÜNCELLENDİ: Parametreler Controller ile uyumlu hale getirildi
    public Expense createExpense(Long userId, ExpenseRequest expenseRequest, MultipartFile file) {

        // Kullanıcıyı bul
        User user = userService.getUserById(userId);

        Category category = categoryRepository.findById(expenseRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı! ID: " + expenseRequest.getCategoryId()));

        // 3. EKLENDİ: Dosyayı MinIO'ya yükle ve ismini al
        String receiptUrl = fileStorageService.save(file);

        // 4. EKLENDİ: DTO'dan Entity'e dönüşüm (Mapper kullanmıyorsak elle yaparız)
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(LocalDateTime.now());
        expense.setStatus(ExpenseStatus.PENDING);
        expense.setReceiptUrl(receiptUrl);

        // 5. EKLENDİ: Dosya yolunu veritabanına kaydet
        expense.setCategory(category);
        return expenseRepository.save(expense);    }


    public List<Expense> getExpensesByUserId(Long userId) {
        userService.getUserById(userId); // Kullanıcı var mı kontrolü
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getPendingExpenses() {
        return expenseRepository.findByStatus(ExpenseStatus.PENDING);
    }

    public Expense processExpense(Long id, ExpenseStatus newStatus, Long memberId) {
        User member = userService.getUserById(memberId);

        if (!member.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Yetkisiz işlem! Sadece YÖNETİCİLER onay verebilir.");
        }

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harcama bulunamadı! ID: " + id));

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new RuntimeException("Bu harcama zaten işlenmiş!");
        }

        expense.setStatus(newStatus);
        return expenseRepository.save(expense);
    }
}
package com.example.ExpenseManagementSystem.service;

import com.example.ExpenseManagementSystem.dto.ExpenseRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService; // Dosya İşçisi (Interface)
    private final CategoryRepository categoryRepository; // Kategori verisi için

    /**
     * YENİ METOT: Hem veriyi (DTO) hem dosyayı (MultipartFile) işler.
     */
    @Transactional // Dosya kaydedilir ama veritabanı patlarsa işlemi geri al
    public void createExpense(Long userId, ExpenseRequest request, MultipartFile file) {

        // 1. Kullanıcıyı Bul
        User user = userService.getUserById(userId);

        // 2. Kategoriyi Bul (DTO'dan gelen ID ile)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı! ID: " + request.getCategoryId()));

        // 3. Dosyayı Kaydet (Interface üzerinden)
        // Eğer dosya boşsa hata fırlatabilirsin veya null geçebilirsin. Burası sana kalmış.
        String receiptPath = null;
        if (file != null && !file.isEmpty()) {
            receiptPath = fileStorageService.save(file);
        }

        // 4. Entity Oluştur ve Map'le (Manuel Mapping)
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());

        // DİKKAT: DTO'da LocalDate (YYYY-MM-DD) tutuyoruz, Entity LocalDateTime istiyor olabilir.
        // Günün başlangıcına (00:00) çeviriyoruz.
        expense.setDate(request.getExpenseDate().atStartOfDay());

        expense.setUser(user);
        expense.setCategory(category);      // İlişkiyi kurduk
        expense.setReceiptUrl(receiptPath); // Dosya yolunu kaydettik
        expense.setStatus(ExpenseStatus.PENDING); // Varsayılan durum

        // 5. Kaydet
        expenseRepository.save(expense);
    }

    // --- AŞAĞIDAKİ METOTLARIN MANTIĞI DOĞRUYDU, AYNEN KORUNDU ---

    public List<Expense> getExpensesByUserId(Long userId) {
        userService.getUserById(userId); // Kullanıcı kontrolü (yoksa hata fırlatır)
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getPendingExpenses() {
        return expenseRepository.findByStatus(ExpenseStatus.PENDING);
    }

    public Expense processExpense(Long id, ExpenseStatus newStatus, Long memberId) {
        // 1. İşlemi yapmaya çalışanı bul
        User member = userService.getUserById(memberId);

        // 2. KURAL: Sadece ADMIN onaylayabilir
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
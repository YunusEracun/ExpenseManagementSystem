package com.example.ExpenseManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Eğer bir yerlerde "ResourceNotFoundException" fırlatılırsa bu metot çalışsın.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage()); // Bizim yazdığımız mesaj (Örn: Kullanıcı bulunamadı)
        body.put("status", HttpStatus.NOT_FOUND.value()); // 404

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Diğer genel hataları (RuntimeException) yakalamak için:
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Beklenmeyen bir hata oluştu: " + ex.getMessage());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
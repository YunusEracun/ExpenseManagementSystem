# 💰 Masraf Yönetim Sistemi (Expense Management System)

Bu proje, kurumsal harcama süreçlerini dijitalleştirmek amacıyla geliştirilmiş, **RESTful mimariye** sahip, güvenli ve ölçeklenebilir bir backend uygulamasıdır.

Çalışanlar harcamalarını sisteme girer, yöneticiler (Admin) ise bu harcamaları onaylar veya reddeder.

---

## 🚀 Özellikler

- **Kimlik Doğrulama:** JWT (JSON Web Token) tabanlı güvenli giriş sistemi.
- **Yetki Yönetimi (RBAC):** Admin ve Çalışan rolleri ile endpoint güvenliği.
- **Veri Güvenliği:** DTO Pattern kullanımı ve şifrelenmiş (BCrypt) parola saklama.
- **Validasyon:** Hatalı veri girişlerini engelleyen doğrulama katmanı.
- **Hata Yönetimi:** Global Exception Handling ile anlaşılır hata mesajları.
- **Dokümantasyon:** Swagger (OpenAPI) ile canlı API testi.
- **HATEOAS:** Navigasyonel API yanıtları.

---

## 🛠️ Teknolojiler

- **Dil:** Java 17
- **Framework:** Spring Boot 3.3
- **Veritabanı:** PostgreSQL
- **Güvenlik:** Spring Security & JWT
- **Araçlar:** Maven, Lombok, Docker (Opsiyonel)

---

## ⚙️ Kurulum ve Çalıştırma

Projeyi yerel ortamınızda çalıştırmak için şu adımları izleyin:

### 1. Gereksinimler
- JDK 17 veya üzeri
- PostgreSQL Veritabanı
- Maven

### 2. Veritabanı Ayarları
- PostgreSQL'de `expense_tracker_db` adında boş bir veritabanı oluşturun.
- `src/main/resources/application.properties` dosyasındaki veritabanı bilgilerini güncelleyin.
  *(Alternatif olarak IDE üzerinden Environment Variables tanımlayabilirsiniz)*

### 3. Başlatma
Terminali açın ve şu komutu çalıştırın:
```bash
mvn spring-boot:run
```

👤 Admin Hesabı (Varsayılan)
Proje ilk kez çalıştırıldığında, sistem otomatik olarak varsayılan bir Admin hesabı oluşturur. (Environment Variables ayarlanmadıysa varsayılan değerler şunlardır):

Email: admin123@hotmail.com

Şifre: admin123

📖 API Dokümantasyonu ve Test
Bu projede API uçlarını (endpoints) test etmek ve backend sözleşmesini incelemek için iki yöntem sunulmuştur:

1. Canlı Test Arayüzü (Swagger UI)
Proje çalışır durumdayken, aşağıdaki adrese giderek tüm istekleri tarayıcı üzerinden test edebilirsiniz:

👉 [Canlı Swagger Arayüzüne Git](http://localhost:8080/swagger-ui/index.html)

🔒 Swagger'da Nasıl Yetki Alınır?
/api/auth/authenticate adresinden giriş yapıp token değerini kopyalayın.

Swagger'da sağ üstteki Authorize butonuna tıklayın.

Kutucuğa Bearer SİZİN_TOKENINIZ formatında yapıştırın ve Authorize deyin.

2. API Sözleşmesi (OpenAPI JSON)
Eğer projeyi çalıştırmadan API yapısını incelemek veya bu sözleşmeyi Postman gibi araçlara import etmek isterseniz:

📄 [ExpenseManagementContract.json Dosyasını Görüntüle](./docs/ExpenseManagementContract.json)

(Not: Bu dosyayı GitHub üzerinden direkt görüntüleyebilir veya Raw modunda indirip kullanabilirsiniz.)
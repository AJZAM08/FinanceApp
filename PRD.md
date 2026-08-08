# Product Requirements Document (PRD)
## Finance App — Personal Finance Manager
**Version:** 1.0.0
**Last Updated:** Juli 2026
**Status:** In Development
**Platform:** Android

---

## 1. Overview

### 1.1 Ringkasan Produk

Finance App adalah aplikasi manajemen keuangan pribadi berbasis Android yang memungkinkan pengguna mencatat, memantau, dan menganalisis pemasukan serta pengeluaran secara real-time. Aplikasi ini dirancang dengan pendekatan keamanan tinggi mengingat sensitivitas data keuangan pengguna.

### 1.2 Visi Produk

> "Membantu setiap orang memahami ke mana uang mereka pergi, sehingga mereka dapat membuat keputusan keuangan yang lebih bijak."

### 1.3 Target Pengguna

| Segmen | Deskripsi |
|--------|-----------|
| Mahasiswa | Usia 18-24, baru mengelola keuangan sendiri |
| Pekerja Muda | Usia 22-35, ingin kontrol pengeluaran lebih baik |
| Keluarga Muda | Pasangan yang ingin kelola keuangan bersama |
| Freelancer | Perlu tracking pemasukan tidak tetap |

### 1.4 Problem Statement

Banyak orang tidak mengetahui ke mana uang mereka pergi setiap bulan karena:
- Tidak ada pencatatan yang sistematis
- Aplikasi yang ada terlalu kompleks atau berbayar
- Tidak ada insight yang actionable dari data pengeluaran

---

## 2. Goals & Success Metrics

### 2.1 Business Goals

- Mencapai 10.000 pengguna aktif dalam 6 bulan pertama
- Rating minimal 4.2 di Google Play Store
- Retensi pengguna 30 hari minimal 40%

### 2.2 User Goals

- Pengguna dapat mencatat transaksi dalam waktu < 30 detik
- Pengguna dapat melihat ringkasan keuangan setiap hari
- Pengguna mendapat insight pengeluaran per kategori

### 2.3 Key Metrics (KPI)

| Metric | Target |
|--------|--------|
| DAU (Daily Active Users) | 30% dari total user |
| Transaksi per user per bulan | Minimal 15 |
| Crash rate | < 0.5% |
| ANR rate | < 0.1% |
| App load time | < 2 detik |

---

## 3. Features & Requirements

### 3.1 Fase 1 — Core Features (MVP) ✅ In Development

#### 3.1.1 Onboarding

| ID | Requirement | Priority |
|----|-------------|----------|
| F-001 | Splash screen dengan branding aplikasi | High |
| F-002 | Onboarding 3 halaman dengan swipe gesture | High |
| F-003 | Simpan status onboarding di local storage | High |
| F-004 | Skip onboarding tersedia | Medium |

#### 3.1.2 Pencatatan Transaksi

| ID | Requirement | Priority |
|----|-------------|----------|
| F-010 | Catat transaksi pemasukan | High |
| F-011 | Catat transaksi pengeluaran | High |
| F-012 | Input judul transaksi | High |
| F-013 | Input nominal dalam Rupiah | High |
| F-014 | Pilih kategori transaksi | High |
| F-015 | Pilih metode pembayaran (Cash/Debit/Credit/E-Wallet) | High |
| F-016 | Input nama bank untuk pembayaran kredit | Medium |
| F-017 | Input nama e-wallet untuk pembayaran digital | Medium |
| F-018 | Pilih tanggal transaksi | High |
| F-019 | Input catatan opsional | Low |
| F-020 | Edit transaksi yang sudah ada | High |
| F-021 | Hapus transaksi | High |
| F-022 | Validasi input sebelum simpan | High |

#### 3.1.3 Dashboard & Overview

| ID | Requirement | Priority |
|----|-------------|----------|
| F-030 | Tampilkan total saldo (pemasukan - pengeluaran) | High |
| F-031 | Tampilkan total pemasukan | High |
| F-032 | Tampilkan total pengeluaran | High |
| F-033 | Tampilkan 5 transaksi terbaru | High |
| F-034 | Navigasi ke list transaksi lengkap | High |
| F-035 | Real-time update saat ada transaksi baru | High |

#### 3.1.4 Riwayat Transaksi

| ID | Requirement | Priority |
|----|-------------|----------|
| F-040 | Tampilkan semua transaksi | High |
| F-041 | Urutkan berdasarkan tanggal terbaru | High |
| F-042 | Tampilkan ikon dan warna per kategori | Medium |

---

### 3.2 Fase 2 — Enhanced Features 🔜 Planned

#### 3.2.1 Filter & Pencarian

| ID | Requirement | Priority |
|----|-------------|----------|
| F-050 | Filter transaksi berdasarkan kategori | High |
| F-051 | Filter transaksi berdasarkan rentang tanggal | High |
| F-052 | Filter berdasarkan tipe (pemasukan/pengeluaran) | High |
| F-053 | Cari transaksi berdasarkan judul | Medium |

#### 3.2.2 Statistik & Analisis

| ID | Requirement | Priority |
|----|-------------|----------|
| F-060 | Grafik pengeluaran per kategori (pie chart) | High |
| F-061 | Grafik tren pengeluaran bulanan (line chart) | High |
| F-062 | Perbandingan pengeluaran bulan ini vs bulan lalu | Medium |
| F-063 | Kategori pengeluaran terbesar | High |

---

### 3.3 Fase 3 — Advanced Features 📋 Backlog

#### 3.3.1 Export & Backup

| ID | Requirement | Priority |
|----|-------------|----------|
| F-070 | Export laporan ke PDF | High |
| F-071 | Export data ke CSV/Excel | Medium |
| F-072 | Backup data ke Google Drive | High |
| F-073 | Restore data dari backup | High |

#### 3.3.2 Sinkronisasi Cloud

| ID | Requirement | Priority |
|----|-------------|----------|
| F-080 | Sinkronisasi real-time via Firebase | High |
| F-081 | Sinkronisasi multi-device | Medium |
| F-082 | Conflict resolution saat sync | Medium |

---

### 3.4 Fase 4 — Authentication 📋 Backlog

| ID | Requirement | Priority |
|----|-------------|----------|
| F-090 | Registrasi dengan email & password | High |
| F-091 | Login dengan email & password | High |
| F-092 | Login dengan Google Account | High |
| F-093 | Biometric authentication (fingerprint/face) | High |
| F-094 | PIN lock aplikasi | Medium |
| F-095 | Multi-user support | Low |
| F-096 | Logout dan hapus data lokal | High |

---

### 3.5 Fase 5 — AI Features 📋 Backlog

| ID | Requirement | Priority |
|----|-------------|----------|
| F-100 | AI insight pengeluaran bulanan | High |
| F-101 | Prediksi pengeluaran bulan depan | Medium |
| F-102 | Saran penghematan berdasarkan pola | Medium |
| F-103 | Deteksi transaksi anomali | Low |
| F-104 | Chatbot tanya jawab keuangan | Low |

---

## 4. Non-Functional Requirements

### 4.1 Performance

| Requirement | Target |
|-------------|--------|
| App startup time | < 2 detik |
| Screen load time | < 500ms |
| Database query time | < 100ms |
| Memory usage | < 150MB |
| APK size | < 20MB |

### 4.2 Security

| Requirement | Implementation |
|-------------|---------------|
| Database encryption | SQLCipher AES-256 |
| Screenshot prevention | FLAG_SECURE |
| Root detection | RootBeer library |
| Network security | HTTPS only, Network Security Config |
| Secure storage | EncryptedSharedPreferences |
| Passphrase management | Android Keystore |
| Reverse engineering protection | ProGuard/R8 obfuscation |

### 4.3 Compatibility

| Requirement | Value |
|-------------|-------|
| Minimum Android version | Android 8.0 (API 26) |
| Target Android version | Android 14 (API 34) |
| Screen sizes | Phone & Tablet |
| Orientations | Portrait (primary), Landscape |
| Dark mode | Supported |

### 4.4 Quality Standards (ISO 25010)

| Karakteristik | Indikator |
|--------------|-----------|
| Functional Suitability | Semua fitur bekerja sesuai spesifikasi |
| Performance Efficiency | Memenuhi target performa di atas |
| Compatibility | Berjalan di Android 8.0+ |
| Usability | Task completion rate > 90% |
| Reliability | Crash rate < 0.5%, uptime 99.9% |
| Security | Lulus OWASP Mobile Top 10 |
| Maintainability | Code coverage > 70%, Clean Architecture |
| Portability | Mendukung berbagai ukuran layar |

---

## 5. Tech Stack

### 5.1 Architecture

```
Presentation Layer  →  Jetpack Compose + Material Design 3
Domain Layer        →  Pure Kotlin (Use Cases, Domain Models)
Data Layer          →  Room Database + Retrofit + DataStore
```

### 5.2 Libraries

| Kategori | Library | Versi |
|----------|---------|-------|
| UI | Jetpack Compose BOM | 2026.02.01 |
| Navigation | Navigation Compose | 2.9.0 |
| Database | Room + SQLCipher | 2.7.1 + 4.5.4 |
| DI | Hilt | 2.60.1 |
| Async | Kotlin Coroutines | 1.10.2 |
| Preferences | DataStore | 1.1.4 |
| Networking | Retrofit + OkHttp | 2.11.0 + 4.12.0 |
| Security | Security Crypto | 1.1.0-alpha06 |
| Root Detection | RootBeer | 0.1.0 |

### 5.3 Development Tools

| Tool | Keterangan |
|------|------------|
| Android Studio | Meerkat 2026.1.1 |
| Kotlin | 2.4.10 |
| Gradle | 9.2.1 |
| Git | Version control |
| GitHub | Repository & CI/CD |

---

## 6. User Flow

```
Install App
    │
    ▼
Splash Screen (2 detik)
    │
    ├── Pengguna Baru → Onboarding (3 halaman) → Dashboard
    │
    └── Pengguna Lama → Dashboard
                            │
            ┌───────────────┼───────────────┐
            ▼               ▼               ▼
    Tambah Transaksi  Lihat Riwayat   Lihat Statistik
            │               │
            ▼               ▼
    Form Input        List Transaksi
    (Judul, Nominal,       │
     Kategori,             ▼
     Pembayaran,      Edit/Hapus
     Tanggal)         Transaksi
```

---

## 7. UI/UX Guidelines

### 7.1 Design System

- **Framework:** Material Design 3
- **Color Scheme:** Dynamic color (mengikuti wallpaper Android 12+)
- **Typography:** Default Material 3 type scale
- **Dark Mode:** Fully supported

### 7.2 Komponen Utama

| Komponen | Deskripsi |
|----------|-----------|
| BalanceCard | Kartu utama menampilkan saldo, pemasukan, pengeluaran |
| TransactionCard | Card per transaksi dengan warna merah/hijau |
| CategoryPickerBottomSheet | Bottom sheet grid pilih kategori dengan icon |
| PaymentMethodPicker | Chip selector untuk metode pembayaran |

### 7.3 Color Coding Transaksi

```
Pemasukan  →  Hijau  (#2E7D32)
Pengeluaran →  Merah  (#C62828)
```

---

## 8. Security Requirements

### 8.1 OWASP Mobile Top 10 Compliance

| OWASP ID | Ancaman | Mitigasi |
|----------|---------|----------|
| M1 | Improper Credential Usage | EncryptedSharedPreferences + Android Keystore |
| M4 | Insufficient Input/Output Validation | Validasi di Use Case layer |
| M8 | Security Misconfiguration | Network Security Config |
| M9 | Insecure Data Storage | SQLCipher AES-256 |
| M10 | Insufficient Cryptography | Android Keystore untuk key management |

### 8.2 Data Privacy

- Semua data disimpan lokal di device pengguna (Fase 1)
- Tidak ada data yang dikirim ke server tanpa persetujuan user
- Data dienkripsi at-rest menggunakan SQLCipher
- Backup terenkripsi (Fase 3)

---

## 9. Release Plan

### 9.1 Versioning Strategy

```
Format: MAJOR.MINOR.PATCH
Contoh: 1.0.0

MAJOR → Breaking change / fitur besar
MINOR → Fitur baru backward compatible
PATCH → Bug fix
```

### 9.2 Roadmap

| Version | Fase | Target | Status |
|---------|------|--------|--------|
| 0.1.0 | Setup & Arsitektur | Selesai | ✅ Done |
| 0.2.0 | Core Features MVP | Q3 2026 | 🔄 In Progress |
| 0.3.0 | Security | Q3 2026 | 🔄 In Progress |
| 0.4.0 | Filter & Statistik | Q4 2026 | 📋 Planned |
| 0.5.0 | Export & Backup | Q4 2026 | 📋 Planned |
| 1.0.0 | Authentication + Play Store | Q1 2027 | 📋 Planned |
| 1.1.0 | AI Features | Q2 2027 | 📋 Planned |

---

## 10. Play Store Requirements

### 10.1 Listing

| Item | Keterangan |
|------|------------|
| App Name | Finance App — Catat Keuangan |
| Short Description | Catat pemasukan & pengeluaran dengan mudah dan aman |
| Category | Finance |
| Content Rating | Everyone |
| Target Age | 18+ |

### 10.2 Assets yang Dibutuhkan

- [ ] App Icon (512x512 PNG)
- [ ] Feature Graphic (1024x500 PNG)
- [ ] Screenshots Phone (min. 2, max. 8)
- [ ] Screenshots Tablet (opsional tapi direkomendasikan)
- [ ] Privacy Policy URL
- [ ] Terms & Conditions URL

### 10.3 Pre-Launch Checklist

- [ ] ProGuard/R8 aktif di release build
- [ ] App Signing dengan upload key
- [ ] Target SDK minimal API 26
- [ ] Tidak ada API key hardcoded
- [ ] Semua permission dijelaskan
- [ ] Privacy Policy tersedia
- [ ] Crash rate < 1% di internal testing
- [ ] Android App Bundle (AAB) format

---

## 11. Risks & Mitigations

| Risk | Probability | Impact | Mitigasi |
|------|-------------|--------|----------|
| SQLCipher compatibility issue | Medium | High | Testing di berbagai device |
| Play Store rejection | Low | High | Review policy sebelum submit |
| Data loss saat migrasi DB | Medium | High | Migration strategy + backup |
| Performance di device low-end | Medium | Medium | Profiling + optimization |
| Security vulnerability | Low | Critical | Security audit sebelum release |

---

## 12. Glossary

| Term | Definisi |
|------|----------|
| Transaksi | Setiap pencatatan pemasukan atau pengeluaran |
| Saldo | Total pemasukan dikurangi total pengeluaran |
| Kategori | Klasifikasi transaksi (Makanan, Transport, dll) |
| Metode Pembayaran | Cara pembayaran (Cash, Debit, Credit, E-Wallet) |
| Onboarding | Proses pengenalan aplikasi untuk pengguna baru |
| DAU | Daily Active Users — pengguna aktif harian |
| OWASP | Open Web Application Security Project |
| SQLCipher | Library enkripsi database SQLite |
| Clean Architecture | Pola arsitektur dengan pemisahan layer yang jelas |

---

*Dokumen ini bersifat living document dan akan diupdate seiring perkembangan produk.*

**Author:** Finance App Development Team
**Reviewer:** -
**Approved by:** -

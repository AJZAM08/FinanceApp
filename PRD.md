# Product Requirements Document (PRD)
## Finance App — Personal Finance Manager
**Version:** 0.4.0
**Last Updated:** Agustus 2026
**Status:** In Development
**Platform:** Android

---

## 1. Overview

### 1.1 Ringkasan Produk

Finance App adalah aplikasi manajemen keuangan pribadi berbasis Android yang memungkinkan pengguna mencatat, memantau, dan menganalisis pemasukan serta pengeluaran secara real-time. Aplikasi ini dirancang dengan pendekatan keamanan tinggi mengingat sensitivitas data keuangan pengguna, dengan data tersimpan sepenuhnya secara lokal dan terenkripsi di perangkat pengguna.

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

- Mencapai 10.000 pengguna aktif dalam 6 bulan pertama setelah rilis di Play Store
- Rating minimal 4.2 di Google Play Store
- Retensi pengguna 30 hari minimal 40%

### 2.2 User Goals

- Pengguna dapat mencatat transaksi dalam waktu < 30 detik
- Pengguna dapat melihat ringkasan keuangan setiap hari
- Pengguna mendapat insight pengeluaran per kategori dalam bentuk grafik visual

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

### 3.1 Fase 1 — Core Features (MVP) ✅ Done

#### 3.1.1 Onboarding

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-001 | Splash screen dengan branding aplikasi | High | ✅ Done |
| F-002 | Onboarding 3 halaman dengan swipe gesture (HorizontalPager) | High | ✅ Done |
| F-003 | Simpan status onboarding di local storage (Jetpack DataStore) | High | ✅ Done |
| F-004 | Skip onboarding tersedia (tombol "Lewati") | Medium | ✅ Done |

#### 3.1.2 Pencatatan Transaksi

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-010 | Catat transaksi pemasukan | High | ✅ Done |
| F-011 | Catat transaksi pengeluaran | High | ✅ Done |
| F-012 | Input judul transaksi | High | ✅ Done |
| F-013 | Input nominal dalam Rupiah | High | ✅ Done |
| F-014 | Pilih kategori transaksi (12 kategori dengan ikon & warna) | High | ✅ Done |
| F-015 | Pilih metode pembayaran (Cash / Debit / Credit / E-Wallet) | High | ✅ Done |
| F-016 | Input nama bank untuk pembayaran kredit | Medium | ✅ Done |
| F-017 | Input nama e-wallet untuk pembayaran digital | Medium | ✅ Done |
| F-018 | Pilih tanggal transaksi (Material 3 DatePickerDialog) | High | ✅ Done |
| F-019 | Input catatan opsional | Low | ✅ Done |
| F-020 | Edit transaksi yang sudah ada | High | ✅ Done |
| F-021 | Hapus transaksi | High | ✅ Done |
| F-022 | Validasi input sebelum simpan (di Use Case layer) | High | ✅ Done |

#### 3.1.3 Dashboard & Overview

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-030 | Tampilkan total saldo (pemasukan - pengeluaran) | High | ✅ Done |
| F-031 | Tampilkan total pemasukan | High | ✅ Done |
| F-032 | Tampilkan total pengeluaran | High | ✅ Done |
| F-033 | Tampilkan 5 transaksi terbaru | High | ✅ Done |
| F-034 | Navigasi ke halaman riwayat transaksi lengkap | High | ✅ Done |
| F-035 | Real-time update via Kotlin Flow saat ada transaksi baru | High | ✅ Done |

#### 3.1.4 Riwayat Transaksi

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-040 | Tampilkan semua transaksi | High | ✅ Done |
| F-041 | Urutkan berdasarkan tanggal terbaru (ORDER BY date DESC) | High | ✅ Done |
| F-042 | Tampilkan ikon dan warna per kategori di setiap item transaksi | Medium | ✅ Done |

---

### 3.2 Fase 1.5 — Security ✅ Done

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| S-001 | Enkripsi database lokal dengan SQLCipher AES-256 | Critical | ✅ Done |
| S-002 | Manajemen kunci enkripsi via Android Keystore | Critical | ✅ Done |
| S-003 | Penyimpanan kunci aman via EncryptedSharedPreferences | High | ✅ Done |
| S-004 | Pencegahan screenshot & screen recording (FLAG_SECURE) | High | ✅ Done |
| S-005 | Deteksi perangkat root (RootBeer library) | High | ✅ Done |
| S-006 | Tampilkan layar peringatan jika perangkat ter-root | High | ✅ Done |
| S-007 | ProGuard/R8 aktif pada release build | Medium | ✅ Done |

---

### 3.3 Fase 2 — Enhanced Features ✅ Done

#### 3.3.1 Filter & Pencarian

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-050 | Filter transaksi berdasarkan kategori (12 kategori tersedia) | High | ✅ Done |
| F-051 | Filter transaksi berdasarkan rentang tanggal (DateRangePicker) | High | ✅ Done |
| F-052 | Filter berdasarkan tipe (Pemasukan / Pengeluaran) | High | ✅ Done |
| F-053 | Cari transaksi berdasarkan judul (real-time search bar) | Medium | ✅ Done |
| F-054 | Reset semua filter sekaligus | Low | ✅ Done |
| F-055 | Skeleton loader (shimmer) saat data dimuat | Low | ✅ Done |

#### 3.3.2 Statistik & Analisis

| ID | Requirement | Priority | Status |
|----|-------------|----------|--------|
| F-060 | Grafik distribusi per kategori — Donut Chart (Canvas Compose) | High | ✅ Done |
| F-061 | Grafik tren pengeluaran bulanan — Bar Chart (4 bulan terakhir) | High | ✅ Done |
| F-062 | Perbandingan pengeluaran bulan ini vs bulan lalu (teks dinamis) | Medium | ✅ Done |
| F-063 | Pemilih periode: Bulan Ini, Bulan Lalu, Semua Waktu | High | ✅ Done |
| F-064 | Toggle tampilan Pengeluaran / Pemasukan di halaman statistik | Medium | ✅ Done |
| F-065 | Indikator visual perbandingan (ikon naik/turun dengan warna) | Low | ✅ Done |

---

### 3.4 Fase 3 — Advanced Features 📋 Backlog

#### 3.4.1 Export & Backup

| ID | Requirement | Priority |
|----|-------------|----------|
| F-070 | Export laporan ke PDF | High |
| F-071 | Export data ke CSV/Excel | Medium |
| F-072 | Backup data ke Google Drive | High |
| F-073 | Restore data dari backup | High |

#### 3.4.2 Sinkronisasi Cloud

| ID | Requirement | Priority |
|----|-------------|----------|
| F-080 | Sinkronisasi real-time via Firebase | High |
| F-081 | Sinkronisasi multi-device | Medium |
| F-082 | Conflict resolution saat sync | Medium |

---

### 3.5 Fase 4 — Authentication 📋 Backlog

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

### 3.6 Fase 5 — AI Features 📋 Backlog

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

| Requirement | Implementation | Status |
|-------------|---------------|--------|
| Database encryption | SQLCipher AES-256 via `SupportOpenHelperFactory` | ✅ Implemented |
| Screenshot prevention | `WindowManager.FLAG_SECURE` di MainActivity | ✅ Implemented |
| Root detection | RootBeer library, dialihkan ke `RootedDeviceScreen` | ✅ Implemented |
| Secure key storage | Android Keystore + EncryptedSharedPreferences | ✅ Implemented |
| Input validation | Validasi di layer Use Case (domain layer) | ✅ Implemented |
| Network security | HTTPS only, Network Security Config (untuk Fase Cloud) | 📋 Planned |
| Reverse engineering protection | ProGuard/R8 aktif di release build | ✅ Implemented |

### 4.3 Compatibility

| Requirement | Value |
|-------------|-------|
| Minimum Android version | Android 8.0 (API 26) |
| Target Android version | Android 15 (API 35) |
| Compiled SDK | API 37 |
| Screen sizes | Phone & Tablet |
| Orientations | Portrait (primary), Landscape |
| Dark mode | Fully supported (Material 3 dynamic theming) |

### 4.4 Quality Standards (ISO 25010)

| Karakteristik | Indikator |
|--------------|-----------|
| Functional Suitability | Semua fitur bekerja sesuai spesifikasi |
| Performance Efficiency | Memenuhi target performa di atas |
| Compatibility | Berjalan di Android 8.0+ |
| Usability | Task completion rate > 90% |
| Reliability | Crash rate < 0.5%, uptime 99.9% |
| Security | Lulus OWASP Mobile Top 10 |
| Maintainability | Code coverage > 70%, Clean Architecture (Unit Test dengan JUnit 5 + MockK) |
| Portability | Mendukung berbagai ukuran layar |

---

## 5. Tech Stack

### 5.1 Architecture

```
Presentation Layer  →  Jetpack Compose + Material Design 3
Domain Layer        →  Pure Kotlin (Use Cases, Domain Models, Repository Interfaces)
Data Layer          →  Room + SQLCipher + Retrofit + DataStore
DI Layer            →  Hilt (Dagger)
```

**Pola Arsitektur:** Clean Architecture + MVVM
**Build Tool:** Gradle 9.2.1 dengan Kotlin DSL (`.kts`)
**Annotation Processing:** KSP (Kotlin Symbol Processing)

### 5.2 Libraries (Versi Aktual)

| Kategori | Library | Versi Aktual |
|----------|---------|--------------|
| UI | Jetpack Compose BOM | 2026.06.01 |
| Navigation | Navigation Compose | 2.9.8 |
| Database | Room | 2.8.4 |
| Enkripsi DB | SQLCipher Android | 4.17.0 |
| DI | Hilt | 2.60.1 |
| DI (Compose) | Hilt Navigation Compose | 1.4.0 |
| Async | Kotlin Coroutines | 1.11.0 |
| Preferences | Jetpack DataStore | 1.2.1 |
| Networking | Retrofit | 3.0.0 |
| Networking | OkHttp Logging Interceptor | 5.4.0 |
| Security | Security Crypto | 1.1.0 |
| Root Detection | RootBeer | 0.1.2 |
| Testing | JUnit 5 (Jupiter) | 6.1.2 |
| Testing | MockK | 1.14.11 |
| Testing | Turbine (Flow testing) | 1.2.1 |
| Testing | Coroutines Test | 1.11.0 |

### 5.3 Development Tools

| Tool | Versi / Keterangan |
|------|-------------------|
| Android Studio | Meerkat 2026.1.1 |
| Kotlin | 2.4.10 |
| AGP (Android Gradle Plugin) | 9.2.1 |
| KSP | 2.3.10 |
| Git | Version control |
| GitHub | Repository & CI/CD |

---

## 6. User Flow

```
Install App
    │
    ▼
Splash Screen (Cek status onboarding via DataStore)
    │
    ├── Pengguna Baru → Onboarding (3 halaman, HorizontalPager) → Dashboard
    │
    └── Pengguna Lama → Dashboard
                            │
        ┌───────────────────┼──────────────────────┐
        ▼                   ▼                      ▼
Tambah Transaksi     Lihat Riwayat           Lihat Statistik
        │                   │                      │
        ▼                   ▼               ┌──────┴──────┐
 Form Input           List Transaksi        Pie Chart   Bar Chart
 (Judul, Nominal,      (Bisa di-filter     per Kategori  Bulanan
  Kategori,             Kategori, Tipe,
  Pembayaran,           Tanggal, Search)
  Tanggal, Catatan)         │
        │                   ▼
        ▼             Edit / Hapus
  Simpan / Edit        Transaksi
  Transaksi
```

---

## 7. UI/UX Guidelines

### 7.1 Design System

- **Framework:** Material Design 3 (Material You)
- **Color Scheme:** Dynamic color (mengikuti wallpaper Android 12+), dengan tema fallback
- **Typography:** Default Material 3 type scale
- **Dark Mode:** Fully supported
- **Loading Pattern:** Shimmer Skeleton Loader (bukan CircularProgressIndicator)
- **Animasi:** Canvas-based chart animation, slide-in untuk list items

### 7.2 Komponen Utama

| Komponen | File | Deskripsi |
|----------|------|-----------|
| `BalanceCard` | `BalanceCard.kt` | Kartu utama menampilkan saldo, pemasukan, pengeluaran |
| `TransactionCard` | `TransactionCard.kt` | Card per transaksi dengan ikon kategori berwarna + warna nominal |
| `CategoryPickerBottomSheet` | `CategoryPickerBottomSheet.kt` | Bottom sheet grid pilih kategori dengan ikon |
| `PaymentMethodPicker` | `PaymentMethodPicker.kt` | Chip selector untuk metode pembayaran |
| `PieChart` | `CustomCharts.kt` | Donut chart animasi untuk distribusi kategori |
| `MonthlyBarChart` | `CustomCharts.kt` | Bar chart gradien untuk tren bulanan |
| `TransactionItemSkeleton` | `SkeletonLoader.kt` | Shimmer placeholder item transaksi saat loading |
| `BalanceCardSkeleton` | `SkeletonLoader.kt` | Shimmer placeholder balance card saat loading |

### 7.3 Color Coding Transaksi

```
Pemasukan  →  Hijau  (#2E7D32)
Pengeluaran →  Merah  (#C62828)
```

### 7.4 Kategori Transaksi yang Tersedia (12 Kategori)

| Kategori | Label Indonesia | Tipe |
|----------|----------------|------|
| FOOD | Makanan | Pengeluaran |
| TRANSPORT | Transportasi | Pengeluaran |
| SHOPPING | Belanja | Pengeluaran |
| HEALTH | Kesehatan | Pengeluaran |
| ENTERTAINMENT | Hiburan | Pengeluaran |
| EDUCATION | Pendidikan | Pengeluaran |
| BILLS | Tagihan | Pengeluaran |
| SALARY | Gaji | Pemasukan |
| FREELANCE | Freelance | Pemasukan |
| INVESTMENT | Investasi | Pemasukan |
| GIFT | Hadiah | Pemasukan / Pengeluaran |
| OTHER | Lainnya | Pemasukan / Pengeluaran |

---

## 8. Security Requirements

### 8.1 OWASP Mobile Top 10 Compliance

| OWASP ID | Ancaman | Mitigasi | Status |
|----------|---------|----------|--------|
| M1 | Improper Credential Usage | EncryptedSharedPreferences + Android Keystore | ✅ Done |
| M4 | Insufficient Input/Output Validation | Validasi di Use Case layer (domain layer) | ✅ Done |
| M8 | Security Misconfiguration | FLAG_SECURE, Root Detection, Network Security Config | ✅ Partial |
| M9 | Insecure Data Storage | SQLCipher AES-256 via SupportOpenHelperFactory | ✅ Done |
| M10 | Insufficient Cryptography | Android Keystore untuk key management passphrase | ✅ Done |

### 8.2 Data Privacy

- Semua data disimpan lokal di device pengguna (Fase 1 & 2)
- Tidak ada data yang dikirim ke server tanpa persetujuan user
- Data dienkripsi at-rest menggunakan SQLCipher AES-256
- Passphrase dikelola oleh Android Keystore (tidak pernah disimpan plain text)
- Backup terenkripsi akan diimplementasikan di Fase 3

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
| 0.1.0 | Setup & Arsitektur | Juli 2026 | ✅ Done |
| 0.2.0 | Core Features MVP (Fase 1) | Juli 2026 | ✅ Done |
| 0.3.0 | Security (Fase 1.5) | Agustus 2026 | ✅ Done |
| 0.4.0 | Filter, Statistik & UI Modernisasi (Fase 2) | Agustus 2026 | ✅ Done |
| 0.5.0 | Export & Backup (Fase 3) | Q4 2026 | 📋 Planned |
| 0.6.0 | Sinkronisasi Cloud Firebase | Q4 2026 | 📋 Planned |
| 1.0.0 | Authentication + Play Store Release | Q1 2027 | 📋 Planned |
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

- [x] ProGuard/R8 aktif di release build
- [ ] App Signing dengan upload key
- [x] minSdk API 26 (Android 8.0)
- [ ] Tidak ada API key hardcoded
- [ ] Semua permission dijelaskan
- [ ] Privacy Policy tersedia
- [ ] Crash rate < 1% di internal testing
- [ ] Android App Bundle (AAB) format

---

## 11. Risks & Mitigations

| Risk | Probability | Impact | Mitigasi |
|------|-------------|--------|----------|
| SQLCipher compatibility issue | Low | High | Sudah diimplementasi & diuji — gunakan `SupportOpenHelperFactory` |
| Play Store rejection | Low | High | Review policy sebelum submit |
| Data loss saat migrasi DB | Medium | High | Room migration strategy wajib diterapkan sebelum rilis publik |
| Performance di device low-end | Medium | Medium | Profiling + optimasi lazy loading |
| Security vulnerability | Low | Critical | Security audit sebelum release v1.0.0 |
| Breaking change library Compose/Hilt | Medium | Medium | Pantau changelog & update secara berkala |

---

## 12. Glossary

| Term | Definisi |
|------|----------|
| Transaksi | Setiap pencatatan pemasukan atau pengeluaran |
| Saldo | Total pemasukan dikurangi total pengeluaran |
| Kategori | Klasifikasi transaksi (Makanan, Transport, dll) — 12 kategori tersedia |
| Metode Pembayaran | Cara pembayaran (Cash, Debit, Credit, E-Wallet) |
| Onboarding | Proses pengenalan aplikasi untuk pengguna baru |
| Skeleton Loader | Animasi shimmer pengganti loading spinner untuk UX yang lebih baik |
| DAU | Daily Active Users — pengguna aktif harian |
| OWASP | Open Web Application Security Project |
| SQLCipher | Library enkripsi database SQLite dengan standar AES-256 |
| Clean Architecture | Pola arsitektur dengan pemisahan layer: Presentation, Domain, Data |
| Use Case | Kelas yang merepresentasikan satu unit logika bisnis di Domain Layer |
| Flow | Kotlin Coroutines API untuk data stream reaktif (real-time update) |
| KSP | Kotlin Symbol Processing — annotation processor modern pengganti KAPT |

---

*Dokumen ini bersifat living document dan akan diupdate seiring perkembangan produk.*

**Author:** Finance App Development Team
**Last Revised:** Agustus 2026
**Reviewer:** -
**Approved by:** -

# Aniverse - Anime Discovery App

Aniverse adalah aplikasi Android modern yang dirancang untuk membantu pengguna menjelajahi dunia anime dengan mudah. Aplikasi ini menawarkan antarmuka yang bersih, navigasi yang intuitif, dan fitur-fitur lengkap untuk para penggemar anime.

## 🚀 Fitur Utama

- **Home dengan Tab Kolaborasi**: Menampilkan berbagai kategori anime menggunakan `TabLayout` dan `ViewPager2`.
- **Navigasi Multi-Level**: Menggabungkan `Navigation Drawer` (samping) dan `Bottom Navigation` ( bawah) untuk akses fitur yang cepat.
- **Pencarian Anime**: Temukan anime favorit Anda melalui fitur search yang responsif.
- **Daftar Favorit**: Simpan anime pilihan Anda ke dalam daftar favorit untuk diakses nanti.
- **Detail Anime**: Informasi lengkap mengenai anime, termasuk skor, gambar poster, dan deskripsi (mendatang).
- **Desain Modern**: Menggunakan prinsip Material Design 3 untuk tampilan yang premium dan nyaman dipandang.

## 🛠️ Tech Stack & Library

Aplikasi ini dibangun menggunakan teknologi terbaru di ekosistem Android:

- **Kotlin**: Bahasa pemrograman utama yang modern dan type-safe.
- **Android Jetpack**:
    - **Navigation Component**: Untuk menangani alur navigasi antar Fragment secara efisien.
    - **View Binding**: Interaksi dengan file layout XML yang aman tanpa `findViewById`.
    - **ViewModel & LiveData**: Manajemen data yang persistent terhadap perubahan orientasi layar.
- **Material Design 3**: Komponen UI (AppBar, BottomNav, Drawer) yang mengikuti standar Google terbaru.
- **Glide**: Library powerful untuk pemuatan gambar (poster anime) secara asinkron.
- **Coroutines**: Menangani operasi background agar aplikasi tetap smooth.
- **RecyclerView**: Menampilkan daftar anime dalam jumlah banyak dengan performa tinggi.

## 📂 Struktur Proyek

```text
app/src/main/java/com/example/aniverse/
├── adapter/       # Kelas adapter untuk RecyclerView dan ViewPager2
├── ui/            # Komponen antarmuka pengguna (Fragments)
│   ├── about/     # Halaman informasi aplikasi
│   ├── detail/    # Halaman detail anime
│   ├── favorite/  # Halaman anime favorit
│   ├── home/      # Halaman utama dengan tabs
│   └── search/    # Halaman pencarian
├── MainActivity.kt # Entry point utama dengan NavDrawer & BottomNav
└── SplashActivity.kt # Layar pembuka aplikasi
```

## ⚙️ Persyaratan Sistem

- Android Studio Giraffe atau versi yang lebih baru.
- Android SDK level 24 (Android 7.0 Nougat) ke atas.
- Gradle v8.0 atau yang lebih baru.

## 🏗️ Cara Install

1. Clone repositori ini.
2. Buka folder proyek menggunakan Android Studio.
3. Tunggu proses Sinkronisasi Gradle selesai.
4. Hubungkan perangkat Android atau jalankan Emulator.
5. Klik tombol **Run** di Android Studio.

## 📖 Materi Referensi
Aplikasi ini diimplementasikan sebagai bagian dari pembelajaran materi:
- Navigation Component
- Material Design (Bottom Navigation, Navigation Drawer, TabLayout)
- View Binding
- RecyclerView & Adapters

---
Developed with ❤️ by boba.

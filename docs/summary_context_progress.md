# 📋 SUMMARY CONTEXT — ANIVERSE PROJECT
## Handover: Anggota 1 → Anggota 2

---

## 🧠 KONTEKS PROJECT

**Nama App:** Aniverse — Aplikasi daftar anime Android
**Mata Kuliah:** UTS Mobile Programming
**Stack:** Kotlin · Android Studio · MVVM · Jikan API · Room DB
**Package:** `com.example.aniverse`
**Min SDK:** API 24 (Android 7.0)
**ViewBinding:** ✅ Aktif

**Pembagian peran:**
- Anggota 1 (SELESAI) → Frontend: UI, Layout, Navigation, Fragment
- Anggota 2 (LANJUT) → Backend: Retrofit, ViewModel, Repository, Room DB

---

## ✅ APA YANG SUDAH DIBUAT ANGGOTA 1

### Build Config
```gradle
// build.gradle (Module: app) — dependencies yang SUDAH ada
navigation-fragment-ktx:2.7.7
navigation-ui-ktx:2.7.7
material:1.11.0
glide:4.16.0
recyclerview:1.3.2
lifecycle-viewmodel-ktx:2.7.0
lifecycle-livedata-ktx:2.7.0
kotlinx-coroutines-android:1.7.3
viewBinding = true

// settings.gradle — SUDAH ada
maven { url 'https://jitpack.io' }
```

### Tema & Warna (colors.xml)
```
primary         = #1A1A2E
primary_variant = #16213E
accent          = #E94560
surface         = #0F3460
on_primary      = #FFFFFF
background      = #1A1A2E
text_secondary  = #B0B0B0
```
Theme: `Theme.Aniverse` extends `MaterialComponents.DayNight.NoActionBar`

---

### Struktur Folder (SUDAH ADA)
```
com.example.aniverse/
├── SplashActivity.kt
├── MainActivity.kt
├── ui/
│   ├── home/
│   │   └── HomeFragment.kt
│   ├── search/
│   │   └── SearchFragment.kt
│   ├── favorite/
│   │   └── FavoriteFragment.kt
│   ├── detail/
│   │   └── DetailActivity.kt
│   └── about/
│       └── AboutFragment.kt
└── adapter/
    ├── AnimeAdapter.kt        ← pakai AnimeItem (data class sementara)
    └── HomePagerAdapter.kt
```

---

### AndroidManifest.xml
```xml
<!-- Launcher = SplashActivity -->
SplashActivity  → LAUNCHER
MainActivity    → normal
DetailActivity  → normal (ui.detail.DetailActivity)

<!-- ANGGOTA 2 WAJIB TAMBAH: -->
<uses-permission android:name="android.permission.INTERNET" />
```

---

### Navigation Graph (`res/navigation/nav_graph.xml`)
```
startDestination = homeFragment

Destinations (ID = nama fragment):
- homeFragment     → HomeFragment.kt
- searchFragment   → SearchFragment.kt
- favoriteFragment → FavoriteFragment.kt
- aboutFragment    → AboutFragment.kt
```
NavGraph belum punya action antar fragment — **anggota 2 boleh tambah jika perlu deep link.**

---

### Menu Files
```
res/menu/bottom_nav_menu.xml  → item ID sama persis dengan fragment ID di nav_graph
res/menu/nav_drawer_menu.xml  → item ID sama persis dengan fragment ID di nav_graph
```
⚠️ **Penting:** ID menu HARUS sama dengan ID fragment di nav_graph agar `setupWithNavController()` jalan.

---

### MainActivity.kt — Struktur Navigasi
```
DrawerLayout (root)
├── LinearLayout
│   ├── AppBarLayout → Toolbar (id: toolbar)
│   ├── FragmentContainerView (id: navHostFragment) ← NavHost
│   └── BottomNavigationView (id: bottomNav)
└── NavigationView (id: navView) ← Drawer

AppBarConfiguration → 4 top-level destinations + drawerLayout
setupActionBarWithNavController() ✅
bottomNav.setupWithNavController() ✅
navView.setupWithNavController()   ✅
```

---

### HomeFragment — Tab System
```
TabLayout + ViewPager2
Tab 0: "🔥 Top Anime"  → AnimeListFragment.newInstance("top")
Tab 1: "🌸 Seasonal"   → AnimeListFragment.newInstance("seasonal")

HomePagerAdapter : FragmentStateAdapter
  → createFragment(0) = AnimeListFragment("top")
  → createFragment(1) = AnimeListFragment("seasonal")
```

---

### AnimeListFragment — Kontainer RecyclerView
```kotlin
// Menerima argument:
arguments.getString("type")  // "top" atau "seasonal"

// Layout sudah ada:
RecyclerView (id: recyclerView)   → GridLayoutManager(2)
ProgressBar   (id: progressBar)   → visibility VISIBLE saat ini (loading state)
TextView      (id: tvError)       → visibility GONE

// onClick sudah handle:
Intent ke DetailActivity + putExtra("ANIME_ID", anime.malId)

// YANG BELUM: observe ViewModel → submitList ke adapter
```

---

### AnimeAdapter.kt — SIAP PAKAI, PERLU DIGANTI MODEL
```kotlin
// Data class SEMENTARA yang dipakai sekarang:
data class AnimeItem(
    val malId: Int,
    val title: String,
    val score: Double,
    val imageUrl: String
)

// Method yang tersedia:
adapter.submitList(List<AnimeItem>)   // ← anggota 2 replace AnimeItem dengan model Retrofit

// ViewHolder sudah handle:
- tvTitle    (binding.tvTitle)
- tvScore    (binding.tvScore)   → format "⭐ $score"
- imgPoster  (binding.imgPoster) → Glide.load(imageUrl)
- root.setOnClickListener → callback onClick(item)
```

---

### SearchFragment
```kotlin
// EditText: id = etSearch
// RecyclerView: id = recyclerSearch → LinearLayoutManager
// Trigger: setOnEditorActionListener → IME_ACTION_SEARCH

// Sudah validasi: query.isNotEmpty()
// YANG BELUM: hubungkan ke ViewModel.searchAnime(query)
// Adapter sudah init: AnimeAdapter { navigasi ke DetailActivity }
```

---

### FavoriteFragment
```kotlin
// RecyclerView: id = recyclerFavorite → GridLayoutManager(2)
// TextView empty state: id = tvEmpty → visibility VISIBLE (sementara)

// YANG BELUM:
// - observe ViewModel/Room DB → submitList ke adapter
// - logic: kalau list kosong → tvEmpty VISIBLE, else GONE
```

---

### DetailActivity
```kotlin
// Menerima dari Intent:
val animeId = intent.getIntExtra("ANIME_ID", -1)

// View yang SUDAH ADA dan siap diisi:
imgPosterDetail  → Glide.load(imageUrl)
tvTitleDetail    → anime.title
tvScoreDetail    → "⭐ ${anime.score}"
tvGenre          → genres.joinToString(", ")
tvSynopsis       → anime.synopsis
btnFavorite      → onClick: insert/delete Room DB

// YANG BELUM:
// - panggil ViewModel.getAnimeDetail(animeId)
// - observe LiveData → isi semua view di atas
// - logic toggle favorite (cek dulu ada di DB atau tidak)
```

---

## 📌 KONTRAK INTERFACE ANGGOTA 2 KE ANGGOTA 1

> Anggota 2 **wajib** mempertahankan kontrak berikut agar tidak break UI anggota 1:

| Kontrak | Detail |
|---|---|
| Model harus punya field | `malId: Int`, `title: String`, `score: Double`, `imageUrl: String` |
| Intent key ke DetailActivity | `"ANIME_ID"` dengan tipe `Int` |
| AnimeAdapter.submitList() | Tetap dipanggil dari Fragment setelah observe |
| ProgressBar hide setelah data masuk | `progressBar.visibility = View.GONE` |
| tvError tampil jika error | `tvError.visibility = View.VISIBLE` + set text error |
| tvEmpty di FavoriteFragment | `VISIBLE` jika list kosong, `GONE` jika ada data |

---

## 🔧 TUGAS ANGGOTA 2 (BACKEND)

### 1. Tambah Dependencies (belum ada)
```gradle
// Retrofit
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"

// Room
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.room:room-ktx:2.6.1"
kapt "androidx.room:room-compiler:2.6.1"

// Tambah plugin kapt di atas file build.gradle:
id 'kotlin-kapt'
```

### 2. Struktur Folder yang Harus Dibuat
```
com.example.aniverse/
├── data/
│   ├── model/
│   │   ├── AnimeResponse.kt     ← parsing JSON Jikan
│   │   ├── AnimeDetail.kt
│   │   └── AnimeFavorite.kt     ← Room Entity
│   ├── remote/
│   │   ├── ApiService.kt        ← Retrofit interface
│   │   └── ApiClient.kt         ← Retrofit instance
│   ├── local/
│   │   ├── AnimeDao.kt
│   │   └── AnimeDatabase.kt
│   └── repository/
│       └── AnimeRepository.kt
└── viewmodel/
    ├── HomeViewModel.kt
    ├── SearchViewModel.kt
    ├── FavoriteViewModel.kt
    └── DetailViewModel.kt
```

### 3. Jikan API Endpoints yang Dipakai
```
Base URL : https://api.jikan.moe/v4/

GET /top/anime              → HomeViewModel (tab "top")
GET /seasons/now            → HomeViewModel (tab "seasonal")
GET /anime?q={query}        → SearchViewModel
GET /anime/{id}             → DetailViewModel
Rate limit: 60 req/menit, 3 req/detik — handle 429!
```

### 4. Yang Harus Disambungkan ke UI Anggota 1

**AnimeListFragment:**
```kotlin
// Observe dari HomeViewModel
viewModel.animeList.observe(viewLifecycleOwner) { result ->
    when (result) {
        is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
        is Resource.Success -> {
            binding.progressBar.visibility = View.GONE
            adapter.submitList(result.data.map { it.toAnimeItem() })
        }
        is Resource.Error -> {
            binding.progressBar.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = result.message
        }
    }
}
```

**SearchFragment:**
```kotlin
// Di dalam setOnEditorActionListener yang sudah ada:
viewModel.searchAnime(query)
viewModel.searchResult.observe(...) { adapter.submitList(...) }
```

**FavoriteFragment:**
```kotlin
viewModel.favorites.observe(...) { list ->
    adapter.submitList(list.map { it.toAnimeItem() })
    binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
}
```

**DetailActivity:**
```kotlin
viewModel.getDetail(animeId)
viewModel.animeDetail.observe(...) { anime ->
    binding.tvTitleDetail.text = anime.title
    binding.tvScoreDetail.text = "⭐ ${anime.score}"
    binding.tvGenre.text = anime.genres.joinToString(", ")
    binding.tvSynopsis.text = anime.synopsis
    Glide.with(this).load(anime.imageUrl).into(binding.imgPosterDetail)
}

binding.btnFavorite.setOnClickListener {
    viewModel.toggleFavorite(anime) // insert atau delete dari Room
}
```

---

## ⚠️ CATATAN PENTING

- Jangan ubah ID view yang ada (toolbar, bottomNav, navView, navHostFragment, dll)
- Jangan ubah ID fragment di nav_graph — sudah terhubung ke menu
- `AnimeItem` di adapter boleh dihapus dan diganti model asli, asal field `malId`, `title`, `score`, `imageUrl` tetap ada (atau buat extension function `toAnimeItem()`)
- Tambah `<uses-permission android:name="android.permission.INTERNET" />` di Manifest
- Semua UI sudah dark theme — jangan override warna tanpa koordinasi

---

**Status Anggota 1: ✅ SELESAI & SIAP HANDOVER**
**Status Anggota 2: 🔧 MULAI DARI SINI**
# Dokumentasi Lengkap Jikan API (v4.0.0)

Jikan adalah API pihak ketiga (tidak resmi) untuk MyAnimeList. API ini melakukan web-scraping ke situs MyAnimeList untuk menutupi kebutuhan akan API yang lengkap (yang tidak disediakan oleh MAL).

## 📌 Informasi Dasar
- **Base URL**: `https://api.jikan.moe/v4/`
- **Metode**: `GET` (Semua request bersifat **READ-ONLY**; API ini tidak dapat digunakan untuk update data daftar anime/manga Anda).
- **Format Respons**: JSON
- **Lisensi**: MIT

---

## ⚡ Rate Limiting (Batasan Permintaan)
| Durasi | Batas Maksimal |
| :--- | :--- |
| **Harian** | Tak Terbatas |
| **Per Menit** | 60 requests |
| **Per Detik** | 3 requests |

> [!NOTE]
> Anda tetap berpotensi terkena *rate limit* langsung dari `MyAnimeList.net` jika melakukan request berturut-turut pada data yang sama (bukan sekedar dari server Jikan).

---

## 📝 Catatan Format JSON
- Properti apapun (kecuali tipe data *array* atau *object*) yang nilainya tidak tersedia atau belum ditentukan akan bernilai `null`.
- Properti *array* atau *object* yang kosong atau tidak dapat diidentifikasi akan dikembalikan dalam kondisi kosong (`[]` atau `{}`).
- Properti nilai skor yang tidak tersedia atau berstatus *undetermined* akan bernilai `0`.
- Semua format tanggal (date) dan timestamps dikembalikan menggunakan format ISO8601 di zona waktu UTC.

---

## 💾 Caching (Penyimpanan Sementara)
Demi memaksimalkan performa, data Jikan akan mem-parsing halaman dari MyAnimeList dan menyimpannya secara temporer (*cache*) di server-nya sendiri. **Semua request di-*cache* selama 24 jam.**

### Response Headers Terkait Cache:
- `Expires`: Tanggal / Waktu *cache* kedaluwarsa.
- `Last-Modified`: Tanggal / Waktu *cache* disimpan server.
- `X-Request-Fingerprint`: ID spesifik (*fingerprint*) per-request unik yang bisa di-*cache* (Hanya ada di *resource* tunggal seperti `/anime/1`. Tidak keluar pada halaman kumpulan pencarian seperti `/anime` atau `/top/anime`).

### Validasi Cache
- Semua request JSON akan mengembalikan header `ETag` (hash MD5 dari respons terkait).
- Anda dapat menyematkan nilai string format ini ke dalam header request Anda di bagian `If-None-Match`.
- Apabila data JSON target **sudah berubah**, server merespons HTTP `200 - OK` beserta data terbarunya.
- Apabila **belum berubah**, server merespons HTTP `304 - Not Modified`. (Fitur ini sangat menghemat bandwidth).

---

## 📡 HTTP Responses & Error Handling
Dalam kondisi *error*, Anda akan selalu menerima JSON Error Response.

| HTTP Status | Trigger Exception | Keterangan |
| :--- | :--- | :--- |
| `200` | N/A | OK - Request sukses |
| `304` | N/A | Not Modified - Anda memiliki *cache* versi terbaru |
| `400` | `BadRequestException` / `ValidationException` | Bad Request - Request tidak lazim. Silakan sesuaikan lagi dengan dokumentasi. |
| `404` | `BadResponseException` | Not Found - *Resource* tidak tersedia atau MyAnimeList mengembalikan status 404. |
| `405` | `BadRequestException` | Method Not Allowed - Hanya izinkan request `GET` |
| `429` | `RateLimitException` | Too Many Requests - Anda terkena *Rate-Limit* Jikan / MAL |
| `500` | `UpstreamException` / `ParserException` / dll | Internal Server Error - Sesuatu yang kacau sedang terjadi. Coba sesaat lagi. |
| `503` | `ServiceUnavailableException` | Service Unavailable - Biasanya sengaja akibat dari server *maintenance* |

### Contoh Bentuk JSON Error Response
```json
{
  "status": 500,
  "type": "InternalException",
  "message": "Exception Message",
  "error": "Exception Trace",
  "report_url": "https://github.com..."
}
```

---

## 🛠️ Daftar Lengkap URL Endpoint

### 🎬 Anime
- `GET /anime/{id}/full`
- `GET /anime/{id}`
- `GET /anime/{id}/characters`
- `GET /anime/{id}/staff`
- `GET /anime/{id}/episodes`
- `GET /anime/{id}/episodes/{episode}`
- `GET /anime/{id}/news`
- `GET /anime/{id}/forum`
- `GET /anime/{id}/videos`
- `GET /anime/{id}/videos/episodes`
- `GET /anime/{id}/pictures`
- `GET /anime/{id}/statistics`
- `GET /anime/{id}/moreinfo`
- `GET /anime/{id}/recommendations`
- `GET /anime/{id}/userupdates`
- `GET /anime/{id}/reviews`
- `GET /anime/{id}/relations`
- `GET /anime/{id}/themes`
- `GET /anime/{id}/external`
- `GET /anime/{id}/streaming`
- `GET /anime` (Pencarian anime secara umum)

### 👤 Characters (Karakter)
- `GET /characters/{id}/full`
- `GET /characters/{id}`
- `GET /characters/{id}/anime`
- `GET /characters/{id}/manga`
- `GET /characters/{id}/voices`
- `GET /characters/{id}/pictures`
- `GET /characters`

### 👥 Clubs (Klub)
- `GET /clubs/{id}`
- `GET /clubs/{id}/members`
- `GET /clubs/{id}/staff`
- `GET /clubs/{id}/relations`
- `GET /clubs`

### 🏷️ Genres (Genre)
- `GET /genres/anime`
- `GET /genres/manga`

### 📰 Magazines (Majalah Terbitan Produksi)
- `GET /magazines`

### 📖 Manga / Komik
- `GET /manga/{id}/full`
- `GET /manga/{id}`
- `GET /manga/{id}/characters`
- `GET /manga/{id}/news`
- `GET /manga/{id}/forum`
- `GET /manga/{id}/pictures`
- `GET /manga/{id}/statistics`
- `GET /manga/{id}/moreinfo`
- `GET /manga/{id}/recommendations`
- `GET /manga/{id}/userupdates`
- `GET /manga/{id}/reviews`
- `GET /manga/{id}/relations`
- `GET /manga/{id}/external`
- `GET /manga`

### 🎤 People (Manusia: Kreator / Seiyuu)
- `GET /people/{id}/full`
- `GET /people/{id}`
- `GET /people/{id}/anime`
- `GET /people/{id}/voices`
- `GET /people/{id}/manga`
- `GET /people/{id}/pictures`
- `GET /people`

### 🏢 Producers (Studio & Produser)
- `GET /producers/{id}`
- `GET /producers/{id}/full`
- `GET /producers/{id}/external`
- `GET /producers`

### 🎲 Random (Fitur Gacha Random)
- `GET /random/anime`
- `GET /random/manga`
- `GET /random/characters`
- `GET /random/people`
- `GET /random/users`

### 👍 Recommendations
- `GET /recommendations/anime`
- `GET /recommendations/manga`

### ✍️ Reviews
- `GET /reviews/anime`
- `GET /reviews/manga`

### 📅 Schedules (Jadwal Tayang Anime Setiap Hari H)
- `GET /schedules`

### 🧑‍💻 Users (Informasi Pengguna Umum MyAnimeList)
- `GET /users`
- `GET /users/userbyid/{id}`
- `GET /users/{username}/full`
- `GET /users/{username}`
- `GET /users/{username}/statistics`
- `GET /users/{username}/favorites`
- `GET /users/{username}/userupdates`
- `GET /users/{username}/about`
- `GET /users/{username}/history`
- `GET /users/{username}/friends`
- `GET /users/{username}/reviews`
- `GET /users/{username}/recommendations`
- `GET /users/{username}/clubs`
- `GET /users/{username}/external`
- `GET /users/{username}/animelist` *(Deprecated)*
- `GET /users/{username}/mangalist` *(Deprecated)*

### 🏆 Top (Peringkat Teratas Populer)
- `GET /top/anime`
- `GET /top/manga`
- `GET /top/people`
- `GET /top/characters`
- `GET /top/reviews`

### 📺 Watch (Video Media MyAnimeList)
- `GET /watch/episodes`
- `GET /watch/episodes/popular`
- `GET /watch/promos`
- `GET /watch/promos/popular`

---

## 🌸 Seasons API (Detail)

Fasilitas endpoint untuk mengecek jajaran lini tayang anime berdasarkan musim (musim semi, panas, gugur, dan dingin).

### 1. `GET /seasons/now`
Menampilkan susunan anime yang ditayangkan di masa ini/eksis saat ini berbarengan (*Current Season*).

### 2. `GET /seasons/{year}/{season}`
Pencarian musim berdasarkan rentang tahun (*year*: berbentuk format 4 angka seperti `2024`) dan Musim spesifik (*season*: berupa string tipe enum `summer`, `spring`, `fall`, `winter`).

### 3. `GET /seasons`
Daftar seluruh rekaman arsip tahun & musim lama yang ada pada sistem Jikan.

### 4. `GET /seasons/upcoming`
Menampilkan susunan seri anime yang rencananya akan mulai tayang musim tayang selanjutnya.

<br/>

### ⚙️ Parameter Query di Endpoint Seasons (`.../now`, `.../upcoming`, dan `.../{year}/{season}`)

| Parameter | Tipe | Deskripsi & Validasi Enum |
| :--- | :--- | :--- |
| `filter` | `string` | Tipe entri media MAL (`tv`, `movie`, `ova`, `special`, `ona`, `music`). |
| `sfw` | `boolean` | Memfilter hasil bersih menurut protokol 'Safe For Work' (Menyembunyikan konten dewasa). Cukup sertakan alias di URI misal: `?sfw`. |
| `unapproved` | `boolean` | Flag unapproved menandakan mengikutsertakan anime buatan entri *user* MAL yang notabenenya 'Belum Disetujui' tayang secara global di situs MAL sehingga berpotensi gampang 404 / hilang. Cukup tambahkan `?unapproved`. |
| `continuing` | `boolean` | Parameter ini berarti tetap menyertakan anime musim sebelumnya dari arsip MAL yang sampai musim ini tayangannya belum tuntas (*TV continuing*). Cukup tambahkan `?continuing`. |
| `page` | `integer` | Pagination nomor angka ke-berapa. |
| `limit` | `integer` | Pembatasan max tampil per-halaman pagination. |

---

## 📈 Struktur Respon Pagination (Contoh `GET /seasons/now`)

```json
{
  "data": [
    { /* Objek List Anime 1 */ },
    { /* Objek List Anime 2 */ },
    { /* Objek List Anime 3 */ }
  ],
  "pagination": {
    "last_visible_page": 2,
    "has_next_page": true,
    "current_page": 1,
    "items": {
      "count": 25,
      "total": 50,
      "per_page": 25
    }
  }
}
```

> **Disclaimer Resmi Jikan**: Jikan sama sekali tidak berafiliasi dengan entitas `MyAnimeList.net`. Ini adalah perangkat utilitas yang gratis dan bersifal *open-sourced*. Silakan pergunakan API ini secara ramah, bertanggung jawab, dan bersedia mentaati seluruh Syarat Ketentuan (*Terms of Service*) mereka.

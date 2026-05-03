package com.example.aniverse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aniverse.adapter.AnimeItem

@Entity(tableName = "favorite_anime")
data class AnimeFavorite(
    @PrimaryKey val malId: Int,
    val title: String,
    val score: Double,
    val imageUrl: String
)

// Konversi kembali ke AnimeItem agar bisa tampil di RecyclerView[cite: 1]
fun AnimeFavorite.toAnimeItem() = AnimeItem(malId, title, score, imageUrl)
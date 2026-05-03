package com.example.aniverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aniverse.adapter.AnimeItem
import com.example.aniverse.data.model.AnimeFavorite
import com.example.aniverse.data.model.toAnimeItem
import com.example.aniverse.data.repository.AnimeRepository

class FavoriteViewModel(private val repository: AnimeRepository) : ViewModel() {
    // Mengambil data LiveData langsung dari Room lewat Repository
    val favorites: LiveData<List<AnimeFavorite>> = repository.allFavorites

    // Fungsi pembantu untuk konversi ke AnimeItem agar bisa masuk ke AnimeAdapter
    fun getFavoriteItems(favList: List<AnimeFavorite>): List<AnimeItem> {
        return favList.map { it.toAnimeItem() }
    }
}
package com.example.aniverse.data.repository

import com.example.aniverse.data.local.AnimeDao
import com.example.aniverse.data.model.AnimeFavorite
import com.example.aniverse.data.remote.ApiService

class AnimeRepository(
    private val apiService: ApiService,
    private val animeDao: AnimeDao
) {
    // API
    suspend fun getTopAnime(filter: String? = null) = apiService.getTopAnime(filter)
    suspend fun getSeasonalAnime() = apiService.getSeasonalAnime()
    suspend fun getUpcomingAnime() = apiService.getUpcomingAnime()
    suspend fun searchAnime(query: String? = null, genres: String? = null) = apiService.searchAnime(query, genres)
    suspend fun getDetail(id: Int) = apiService.getAnimeDetail(id)
    suspend fun getCharacters(id: Int) = apiService.getAnimeCharacters(id)
    suspend fun getSchedules(day: String? = null) = apiService.getSchedules(day)
    suspend fun getGenres() = apiService.getGenres()

    // Room
    val allFavorites = animeDao.getAllFavorites()
    suspend fun addFavorite(anime: AnimeFavorite) = animeDao.insertFavorite(anime)
    suspend fun removeFavorite(anime: AnimeFavorite) = animeDao.deleteFavorite(anime)
    suspend fun isFavorite(id: Int) = animeDao.isFavorite(id)
}
package com.example.aniverse.data.remote

import com.example.aniverse.data.model.AnimeResponse
import com.example.aniverse.data.model.CharacterResponse
import com.example.aniverse.data.model.SingleAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeResponse

    @GET("seasons/now")
    suspend fun getSeasonalAnime(): AnimeResponse

    @GET("anime")
    suspend fun searchAnime(@Query("q") query: String): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(@Path("id") id: Int): SingleAnimeResponse

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") id: Int): CharacterResponse
}
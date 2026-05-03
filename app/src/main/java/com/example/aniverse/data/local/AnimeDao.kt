package com.example.aniverse.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aniverse.data.model.AnimeFavorite

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(anime: AnimeFavorite)

    @Delete
    suspend fun deleteFavorite(anime: AnimeFavorite)

    @Query("SELECT * FROM favorite_anime")
    fun getAllFavorites(): LiveData<List<AnimeFavorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_anime WHERE malId = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
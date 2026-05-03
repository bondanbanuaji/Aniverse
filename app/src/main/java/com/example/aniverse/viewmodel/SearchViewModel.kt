package com.example.aniverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.adapter.AnimeItem
import com.example.aniverse.data.model.toAnimeItem
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.util.Resource
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _searchResult = MutableLiveData<Resource<List<AnimeItem>>>()
    val searchResult: LiveData<Resource<List<AnimeItem>>> = _searchResult

    private val _genres = MutableLiveData<Resource<List<com.example.aniverse.data.model.GenreDetail>>>()
    val genres: LiveData<Resource<List<com.example.aniverse.data.model.GenreDetail>>> = _genres

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        _genres.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getGenres()
                _genres.postValue(Resource.Success(response.data))
            } catch (e: Exception) {
                _genres.postValue(Resource.Error(e.message ?: "Gagal mengambil genre"))
            }
        }
    }

    fun searchAnimeByGenre(genreId: String) {
        _searchResult.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.searchAnime(genres = genreId)
                val items = response.data.map { it.toAnimeItem() }
                _searchResult.postValue(Resource.Success(items))
            } catch (e: Exception) {
                _searchResult.postValue(Resource.Error("Gagal memuat anime berdasarkan genre"))
            }
        }
    }

    fun searchAnime(query: String) {
        _searchResult.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.searchAnime(query)
                val items = response.data.map { it.toAnimeItem() }
                _searchResult.postValue(Resource.Success(items))
            } catch (e: Exception) {
                _searchResult.postValue(Resource.Error("Anime tidak ditemukan"))
            }
        }
    }
}
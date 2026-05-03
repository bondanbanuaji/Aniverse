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
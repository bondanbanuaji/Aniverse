package com.example.aniverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniverse.data.model.AnimeDetail
import com.example.aniverse.data.model.AnimeFavorite
import com.example.aniverse.data.model.CharacterData
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.util.Resource
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _animeDetail = MutableLiveData<Resource<AnimeDetail>>()
    val animeDetail: LiveData<Resource<AnimeDetail>> = _animeDetail

    private val _characters = MutableLiveData<Resource<List<CharacterData>>>()
    val characters: LiveData<Resource<List<CharacterData>>> = _characters

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    // Fungsi untuk ambil detail anime dari API
    fun getAnimeDetail(id: Int) {
        _animeDetail.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getDetail(id)
                _animeDetail.postValue(Resource.Success(response.data))
                getCharacters(id)
            } catch (e: Exception) {
                _animeDetail.postValue(Resource.Error(e.message ?: "Gagal mengambil detail anime"))
            }
        }
    }

    private fun getCharacters(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCharacters(id)
                _characters.postValue(Resource.Success(response.data))
            } catch (e: Exception) {
                _characters.postValue(Resource.Error(e.message ?: "Gagal mengambil karakter"))
            }
        }
    }

    // Fungsi untuk cek apakah anime ini sudah ada di favorit (Room)
    fun checkFavoriteStatus(id: Int) {
        viewModelScope.launch {
            val status = repository.isFavorite(id)
            _isFavorite.postValue(status)
        }
    }

    // Fungsi untuk tambah ke favorit
    fun addToFavorite(anime: AnimeFavorite) {
        viewModelScope.launch {
            repository.addFavorite(anime)
            _isFavorite.postValue(true)
        }
    }

    // Fungsi untuk hapus dari favorit
    fun removeFromFavorite(anime: AnimeFavorite) {
        viewModelScope.launch {
            repository.removeFavorite(anime)
            _isFavorite.postValue(false)
        }
    }
}
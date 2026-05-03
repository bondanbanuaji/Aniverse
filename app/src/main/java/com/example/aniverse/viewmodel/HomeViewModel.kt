package com.example.aniverse.viewmodel

import androidx.lifecycle.*
import com.example.aniverse.adapter.AnimeItem
import com.example.aniverse.data.model.toAnimeItem
import com.example.aniverse.data.repository.AnimeRepository
import com.example.aniverse.util.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _animeList = MutableLiveData<Resource<List<AnimeItem>>>()
    val animeList: LiveData<Resource<List<AnimeItem>>> = _animeList

    fun fetchAnime(type: String) {
        _animeList.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                // Tambahkan delay kecil untuk menghindari rate limit API Jikan (3 request/sec)
                // Jika dua fragment (Top & Seasonal) memanggil bersamaan, salah satu akan kena limit.
                if (type == "seasonal") kotlinx.coroutines.delay(500)
                
                android.util.Log.d("HomeViewModel", "Fetching anime type: $type")
                val response = if (type == "top") repository.getTopAnime()
                else repository.getSeasonalAnime()
                
                if (response != null && response.data != null) {
                    android.util.Log.d("HomeViewModel", "Response received for $type: ${response.data.size} items")
                    val items = response.data.map { it.toAnimeItem() }
                    _animeList.postValue(Resource.Success(items))
                } else {
                    android.util.Log.w("HomeViewModel", "Response or data is null for $type")
                    _animeList.postValue(Resource.Error("Empty response from server"))
                }
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error fetching anime type $type", e)
                _animeList.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }
}
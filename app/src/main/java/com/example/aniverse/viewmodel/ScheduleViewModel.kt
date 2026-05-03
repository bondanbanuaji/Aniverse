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

class ScheduleViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _scheduleResult = MutableLiveData<Resource<List<AnimeItem>>>()
    val scheduleResult: LiveData<Resource<List<AnimeItem>>> = _scheduleResult

    fun fetchSchedules(day: String) {
        _scheduleResult.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getSchedules(day)
                val items = response.data.map { it.toAnimeItem() }
                _scheduleResult.postValue(Resource.Success(items))
            } catch (e: Exception) {
                _scheduleResult.postValue(Resource.Error(e.message ?: "Gagal memuat jadwal"))
            }
        }
    }
}
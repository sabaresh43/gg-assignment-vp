package com.pubscale.basicvideoplayer.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pubscale.basicvideoplayer.model.VideoRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _videoUrl = MutableLiveData<String?>()
    val videoUrl: LiveData<String?> get() = _videoUrl
    private val repository: VideoRepository = VideoRepository() //initializing the repository

    //Initializing the function on viewmodel
    init {
        loadVideo()
    }

    private fun loadVideo() {
        viewModelScope.launch {
            _videoUrl.postValue(repository.fetchVideo()?.url)
        }
    }
}
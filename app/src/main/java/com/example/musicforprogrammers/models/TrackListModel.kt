package com.example.musicforprogrammers.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicforprogrammers.api.Api
import com.example.musicforprogrammers.data.TrackListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackListModel(application: Application) : AndroidViewModel(application) {
    private val _trackState = MutableStateFlow(TrackListState())
    val trackState: StateFlow<TrackListState> = _trackState

    fun getTracks() {
        viewModelScope.launch {
            val response = Api.music.getMusicList().body()


            response?.let {
                _trackState.update { currentState ->
                    currentState.copy(
                        tracks = response.channel?.trekList
                    )
                }
            }
        }
    }
}
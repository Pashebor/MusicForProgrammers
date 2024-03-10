package com.example.musicforprogrammers.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicforprogrammers.api.Api
import com.example.musicforprogrammers.api.MusicTrek
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrackListState(
    val currentTrack: MusicTrek? = null,
    val tracks: List<MusicTrek>? = null
)

class TrackListModel: ViewModel() {
    private val _trackState = MutableStateFlow(TrackListState())
    val trackState: StateFlow<TrackListState> = _trackState

    fun getTracks() {
        viewModelScope.launch {
            val response = Api.music.getMusicList().body()


            response ?.let {
                _trackState.update { currentState ->
                    currentState.copy(
                        tracks = response.channel?.trekList
                    )
                }
            }
        }
    }
}
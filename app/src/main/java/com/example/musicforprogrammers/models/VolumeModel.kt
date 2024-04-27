package com.example.musicforprogrammers.models

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicforprogrammers.data.VolumeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VolumeModel(application: Application): AndroidViewModel(application) {
    private val audioManager: AudioManager =
        application.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _volumeState = MutableStateFlow(VolumeData())
    val volumeState: StateFlow<VolumeData> = _volumeState

    init {
        setCurrentVolumePercentage()
    }

    private fun setCurrentVolumePercentage() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolumePercentage = 100 * currentVolume / maxVolume

        Log.d("SOUND_MODEL", currentVolumePercentage.toString())

        viewModelScope.launch {
            _volumeState.update { soundDataState ->
                soundDataState.copy(
                    percentageVolume = currentVolumePercentage
                )
            }
        }
    }
}
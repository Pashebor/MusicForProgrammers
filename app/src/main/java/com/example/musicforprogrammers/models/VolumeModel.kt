package com.example.musicforprogrammers.models

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.data.VolumeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VolumeModel(application: Application): AndroidViewModel(application) {
    private val beepSoundPlayer = MediaPlayer.create(application, R.raw.button_tap_sound)
    private var beepSoundVolume = 0.1f

    private val audioManager: AudioManager =
        application.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _volumeState = MutableStateFlow(VolumeData())
    val volumeState: StateFlow<VolumeData> = _volumeState

    init {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val backwardPercentage = (100f - (currentVolume / maxVolume * 100f)) / 100f

        beepSoundVolume = if (backwardPercentage < 0.1f) 0.1f else backwardPercentage

        setCurrentVolumePercentage()
    }
    private fun playBeepSound() {
        beepSoundPlayer.setVolume(beepSoundVolume, beepSoundVolume)
        beepSoundPlayer.start()
    }
    fun volumeUp() {
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        setCurrentVolumePercentage()
        playBeepSound()
    }

    fun volumeDown() {
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        setCurrentVolumePercentage()
        playBeepSound()
    }
    private fun setCurrentVolumePercentage() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolumePercentage = 100 * currentVolume / maxVolume

        viewModelScope.launch {
            _volumeState.update { soundDataState ->
                soundDataState.copy(
                    percentageVolume = currentVolumePercentage
                )
            }
        }
    }
}
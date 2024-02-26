package com.example.musicforprogrammers.activities

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.musicforprogrammers.R

class StartScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_screen)

        val button = findViewById<Button>(R.id.button_touch)
        val player = MediaPlayer.create(this, R.raw.button_tap_sound)

        button.setOnClickListener {
            val phoneAudio: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = phoneAudio.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            val maxVolume = phoneAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val backwardPercentage = (100f - (currentVolume / maxVolume * 100f)) / 100f
            val beepSoundVolume = if (backwardPercentage < 0.1f) 0.1f else backwardPercentage

            player.setVolume(beepSoundVolume, beepSoundVolume)
            player.start()
        }
    }
}
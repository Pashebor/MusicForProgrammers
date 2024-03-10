package com.example.musicforprogrammers.activities

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.api.MusicTrek
import com.example.musicforprogrammers.models.TrackListModel
import kotlinx.coroutines.launch


class StartScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_screen)

        val tracksListView = findViewById<ListView>(R.id.tracksListView)
        val inflater = layoutInflater
        val header = inflater.inflate(R.layout.header_layout, tracksListView, false) as ViewGroup
        tracksListView.addHeaderView(header, null, false)

        val viewModelProvider = ViewModelProvider(this)
        val trackListModel = viewModelProvider[TrackListModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trackListModel.trackState.collect { trackState ->
                    when (trackState.tracks) {
                        is List<MusicTrek> -> fillTrackList(trackState.tracks, tracksListView)
                    }
                }
            }
        }

        trackListModel.getTracks()
        onPlayTapActionListener()
    }

    private fun fillTrackList(tracks: List<MusicTrek>, listView: ListView) {
        val listViewItems = tracks.map { it.title.replace("Episode ", "") }
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, listViewItems)

        listView.adapter = arrayAdapter
    }

    private fun onPlayTapActionListener() {
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
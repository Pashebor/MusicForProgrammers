package com.example.musicforprogrammers.activities

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.adapters.TrackListAdapter
import com.example.musicforprogrammers.api.MusicTrack
import com.example.musicforprogrammers.models.TrackListModel
import com.example.musicforprogrammers.views.MFPTextView
import kotlinx.coroutines.launch
import java.io.IOException


class StartScreenActivity : AppCompatActivity() {
    private val musicPlayer: MediaPlayer = MediaPlayer()
    private lateinit var currentTrack: MusicTrack

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
                        is List<MusicTrack> -> {
                            fillTrackList(trackState.tracks, tracksListView)
                        }
                    }
                }
            }
        }

        trackListModel.getTracks()
        onPlayTapActionListener()
        musicPlayer.setOnPreparedListener { player ->
            player.start()
        }
    }

    private fun fillTrackList(tracks: List<MusicTrack>, listView: ListView) {
        val listViewItems = tracks.map { it.title.replace("Episode ", "") }
        val arrayAdapter = TrackListAdapter(this, listViewItems)

        setCurrentTrackData(track = tracks[0])
        arrayAdapter.setSelectedPosition(0)

        listView.adapter = arrayAdapter
        listView.setOnItemClickListener { _, _, position, id ->
            setCurrentTrackData(track = tracks[id.toInt()])
            arrayAdapter.setSelectedPosition(position - 1)
            listView.smoothScrollToPositionFromTop(0, 0, 200)
        }
    }

    private fun setCurrentTrackData(track: MusicTrack) {
        val mainPlayBtnLabel = findViewById<MFPTextView>(R.id.button_play_main_label)
        val sourceBtnLabel = findViewById<MFPTextView>(R.id.button_open_source_label)
        val trackTitle = findViewById<MFPTextView>(R.id.track_title)
        val sourceLink = findViewById<MFPTextView>(R.id.source_link)

        this.currentTrack = track

        trackTitle.text = track.title
        mainPlayBtnLabel.text = track.duration
        sourceLink.text = track.link

        if (track.trackData != null) {
            val megabytes = track.trackData?.length?.div(1024 * 1024)

            sourceBtnLabel.text = getString(R.string.source_size, megabytes)
        }

        sourceLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(track.link))

            startActivity(browserIntent)
        }
    }
    private fun onPlayTapActionListener() {
        val button = findViewById<Button>(R.id.button_play_main)
        val player = MediaPlayer.create(this, R.raw.button_tap_sound)

        button.setOnClickListener {
            val phoneAudio: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = phoneAudio.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            val maxVolume = phoneAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val backwardPercentage = (100f - (currentVolume / maxVolume * 100f)) / 100f
            val beepSoundVolume = if (backwardPercentage < 0.1f) 0.1f else backwardPercentage

            player.setVolume(beepSoundVolume, beepSoundVolume)
            player.start()

            try {
                musicPlayer.reset()
                musicPlayer.setDataSource(currentTrack.trackData?.url)
                musicPlayer.prepareAsync()
            } catch (error: IOException) {
                error.printStackTrace()
            }
        }
    }
}
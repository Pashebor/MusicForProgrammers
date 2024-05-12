package com.example.musicforprogrammers.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.adapters.TrackListAdapter
import com.example.musicforprogrammers.data.MusicTrack
import com.example.musicforprogrammers.models.TrackListModel
import com.example.musicforprogrammers.views.MFPButton
import com.example.musicforprogrammers.views.MFPTextView
import com.example.musicforprogrammers.views.PlayerControlsFragment
import kotlinx.coroutines.launch


class StartScreenActivity : AppCompatActivity() {
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
        onMainControlsTapActionListener()
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
        val playerControlsFragment = supportFragmentManager.findFragmentById(R.id.player_controls) as PlayerControlsFragment


        this.currentTrack = track
        playerControlsFragment.setCurrentTrack(track)

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

    private fun onMainControlsTapActionListener() {
        val buttonPlayStopMain = findViewById<MFPButton>(R.id.button_play_main)
        val buttonSource = findViewById<MFPButton>(R.id.button_open_source)
        val playerControlsFragment = supportFragmentManager.findFragmentById(R.id.player_controls) as PlayerControlsFragment


        buttonSource.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentTrack.trackData?.url))

            startActivity(browserIntent)
        }

        buttonPlayStopMain.setOnClickListener {
            playerControlsFragment.onPlayStopsActionListener()
        }
    }
}
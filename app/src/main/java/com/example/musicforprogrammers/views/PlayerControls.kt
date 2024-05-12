package com.example.musicforprogrammers.views

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.data.MusicTrack
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PlayerControlsFragment: Fragment(R.layout.player_controls) {
    private val musicPlayer: MediaPlayer = MediaPlayer()
    private var trackPlayingTime: Int = 0
    private val rewindFastForwardTime = 30000
    private lateinit var currentTrack: MusicTrack

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<MFPButton>(R.id.button_play_stop)
        val rewindButton = view.findViewById<MFPButton>(R.id.button_rewind)
        val forwardButton = view.findViewById<MFPButton>(R.id.button_forward)

        rewindButton.setOnClickListener {
            rewind()
        }
        forwardButton.setOnClickListener {
            fastForward()
        }
        playButton.setOnClickListener {
            onPlayStopsActionListener()
        }
        musicPlayer.setOnPreparedListener { player ->
            player.start()
            runTrackTimer()
        }
    }

    fun setCurrentTrack(track: MusicTrack) {
        this.currentTrack = track
    }

    private fun runTrackTimer() {
        val trackTime = view?.findViewById<MFPTextView>(R.id.track_time)

        Handler(Looper.getMainLooper()).postDelayed({
            trackPlayingTime += 1000

            if (musicPlayer.isPlaying) {
                lifecycleScope.launch {
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, trackPlayingTime)

                    val time = SimpleDateFormat("HH:mm:ss").format(calendar.time)

                    trackTime?.text = time
                    runTrackTimer()
                }
            }
        }, 1000L)
    }

    private fun rewind() {
        if (trackPlayingTime >= rewindFastForwardTime) {
            trackPlayingTime -= rewindFastForwardTime
        } else {
            trackPlayingTime = 0
        }

        musicPlayer.seekTo(trackPlayingTime)
    }

    private fun fastForward() {
        trackPlayingTime += rewindFastForwardTime

        musicPlayer.seekTo(trackPlayingTime)
    }

    fun onPlayStopsActionListener() {
        val player = MediaPlayer.create(context, R.raw.button_tap_sound)
        val playerControls = view?.findViewById<LinearLayout>(R.id.player_controls_panel)
        val buttonPlayStop = view?.findViewById<MFPButton>(R.id.button_play_stop)
        val playerControlsStub = view?.findViewById<LinearLayout>(R.id.player_controls_stub)
        val scrollableTitle = view?.findViewById<ScrollableMFPTextView>(R.id.scrollable_title)
        val phoneAudio: AudioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = phoneAudio.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = phoneAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val backwardPercentage = (100f - (currentVolume / maxVolume * 100f)) / 100f
        val beepSoundVolume = if (backwardPercentage < 0.1f) 0.1f else backwardPercentage

        playerControls?.visibility = LinearLayout.VISIBLE
        playerControlsStub?.visibility = LinearLayout.GONE
        scrollableTitle?.text = getString(R.string.text_bullet, currentTrack.title).repeat(5)

        player.setVolume(beepSoundVolume, beepSoundVolume)
        player.start()

        if (musicPlayer.isPlaying) {
            musicPlayer.pause()

            buttonPlayStop?.text = getString(R.string.button_brackets, getString(R.string.button_play))

            return
        }

        buttonPlayStop?.text = getString(R.string.button_brackets, getString(R.string.button_stop))

        if (trackPlayingTime != 0) {
            musicPlayer.start()

            return
        }

        try {
            trackPlayingTime = 0
            musicPlayer.reset()
            musicPlayer.setDataSource(currentTrack.trackData?.url)
            musicPlayer.prepareAsync()
        } catch (error: IOException) {
            error.printStackTrace()
        }
    }
}
package com.example.musicforprogrammers.data

data class TrackListState(
    val currentTrack: MusicTrack? = null,
    val tracks: List<MusicTrack>? = null,
)
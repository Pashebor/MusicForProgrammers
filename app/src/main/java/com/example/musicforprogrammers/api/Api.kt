package com.example.musicforprogrammers.api

import com.example.musicforprogrammers.services.Http

class Api {
    companion object {
        val music = Http(MusicApi::class.java).request
    }
}
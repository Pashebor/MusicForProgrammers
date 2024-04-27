package com.example.musicforprogrammers.api

import com.example.musicforprogrammers.data.MusicForProgramming
import retrofit2.Response
import retrofit2.http.GET



interface MusicApi {
    @GET("rss.php")
    suspend fun getMusicList(): Response<MusicForProgramming>
}
package com.example.musicforprogrammers.services

import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class Http<T>(Api: Class<T>) {
    private val BASE_URL = "https://musicforprogramming.net/"
    var request = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()
        .create(Api)
}
package com.example.musicforprogrammers.api

import retrofit2.Response
import retrofit2.http.GET

data class GeoLocation(
    val lat: Float,
    val lng: Float
)

data class UserAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
)

data class CompanyName(
    val name: String,
    val catchPhrase: String,
    val bs: String,
)

data class UserResponse(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val address: UserAddress,
    val geo: GeoLocation,
    val phone: String,
    val website: String,
    val company: CompanyName
)

interface IUsers {
    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>
}
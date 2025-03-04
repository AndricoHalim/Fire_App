package com.andricohalim.fireapp.data.retrofit

import com.andricohalim.fireapp.data.response.FireResponse
import com.andricohalim.fireapp.data.response.LoginResponse
import com.andricohalim.fireapp.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/api/registerdamkar")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("location") location: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/api/logindamkar")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("/api/sensor/latest")
    suspend fun getLatestData(): FireResponse
}

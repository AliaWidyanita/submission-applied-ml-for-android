package com.dicoding.asclepius.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    fun getNews(
        @Query("q") query: String
    ): Call<News>
}
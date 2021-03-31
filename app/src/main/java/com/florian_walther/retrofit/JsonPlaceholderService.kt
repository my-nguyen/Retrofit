package com.florian_walther.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholderService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>
}
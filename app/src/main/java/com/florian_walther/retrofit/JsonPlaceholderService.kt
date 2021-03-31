package com.florian_walther.retrofit

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceholderService {
    @GET("posts")
    fun getPosts(@Query("userId") userId: Int,
                 @Query("_sort") sort: String?, @Query("_order") order: String?): Call<List<Post>>

    @GET("posts")
    fun getPosts(@Query("userId") userId: Int, @Query("userId") userId2: Int,
                 @Query("_sort") sort: String?, @Query("_order") order: String?): Call<List<Post>>

    @GET("posts")
    fun getPosts(@Query("userId") userId: IntArray,
                 @Query("_sort") sort: String?, @Query("_order") order: String?): Call<List<Post>>

    @GET("posts")
    fun getPosts(@QueryMap params: Map<String, String>): Call<List<Post>>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int): Call<List<Comment>>

    @GET
    fun getComments(@Url url: String): Call<List<Comment>>

    @POST("posts")
    fun putPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun putPost(@Field("userId") userId: Int, @Field("title") title: String,
                @Field("body") text: String): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun putPost(@FieldMap fields: Map<String, String>): Call<Post>
}
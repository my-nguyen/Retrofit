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
    fun postPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun postPost(@Field("userId") userId: Int, @Field("title") title: String,
                 @Field("body") text: String): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun postPost(@FieldMap fields: Map<String, String>): Call<Post>

    // update existing Post by replacing it with a new Post
    @PUT("posts/{id}")
    fun putPost(@Path("id") id: Int, @Body post: Post): Call<Post>

    @Headers("Static-Header1: 123", "Static-Header2: 456")
    @PUT("posts/{id}")
    fun putPost(@Header("Dynamic-Header") header: String,
                @Path("id") id: Int, @Body post: Post): Call<Post>

    // update existing Post by changing the fields of the Post
    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body post: Post): Call<Post>

    @PATCH("posts/{id}")
    fun patchPost(@HeaderMap headers: Map<String, String>,
                  @Path("id") id: Int, @Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Unit>
}
package com.florian_walther.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.florian_walther.retrofit.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var service: JsonPlaceholderService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // use this in a PATCH request for GSON to serialize nulls in order to tell the server to
        // delete a value instead of ignoring the null fields
        // this would be passed in as a param to  GsonConverterFactory.create() below
        val gson = GsonBuilder().serializeNulls().create()

        // add logging by using the underlying library OKHttpClient
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor {
                    val originalRequest: Request = it.request()
                    // intercept the original request, add some custom header then send it out
                    // now the new header becomes global, so we don't have to add a header to each
                    // of the individual getPost, putPost and patchPost in JsonPlaceholderService
                    val newRequest = originalRequest.newBuilder()
                            .header("Interceptor-Header", "xyz")
                            .build()
                    return@addInterceptor it.proceed(newRequest)
                }
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(JsonPlaceholderService::class.java)

        // getPosts2()
        // getComments()
        // postPost()
        updatePost()
        // deletePost()
    }

    private fun getPosts1() = service.getPosts(4, "id", "desc")
    private fun getPosts2() = service.getPosts(4, null, null)
    private fun getPosts3() = service.getPosts(1, 4, null, null)
    private fun getPosts4() = service.getPosts(intArrayOf(2,3,6), null, null)
    private fun getPosts5(): Call<List<Post>> {
        val params = mutableMapOf<String, String>()
        params["userId"] = "1"
        params["_sort"] = "id"
        params["_order"] = "desc"
        return service.getPosts(params)
    }
    private fun getPosts() {
        // val call = getPosts1()
        // val call = getPosts2()
        // val call = getPosts3()
        // val call = getPosts4()
        val call = getPosts5()
        call.enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    binding.tvResult.text = "Code: ${response.code()}"
                } else {
                    val posts = response.body()
                    for (post in posts!!) {
                        val content = "ID: ${post.id}\nUser ID: ${post.userId}\nTitle: ${post.title}\nText: ${post.body}\n\n"
                        binding.tvResult.append(content)
                    }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                binding.tvResult.text = t.message
            }
        })
    }

    private fun getComments1() = service.getComments(3)
    private fun getComments2() = service.getComments("posts/3/comments")
    private fun getComments3() = service.getComments("https://jsonplaceholder.typicode.com/posts/3/comments")
    private fun getComments() {
        // val call = getComments1()
        // val call = getComments2()
        val call = getComments3()
        call.enqueue(object: Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (!response.isSuccessful) {
                    binding.tvResult.text = "Code: ${response.code()}"
                } else {
                    val comments = response.body()
                    for (comment in comments!!) {
                        val content = "ID: ${comment.id}\nPost ID: ${comment.postId}\nName: ${comment.name}\nEmail: ${comment.email}\nText: ${comment.body}\n\n"
                        binding.tvResult.append(content)
                    }
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                binding.tvResult.text = t.message
            }
        })
    }

    private fun postPost1(): Call<Post> {
        val post = Post(23, null, "New Title", "New Text")
        return service.postPost(post)
    }
    private fun postPost2() = service.postPost(24, "Second Title", "Second Text")
    private fun postPost3(): Call<Post> {
        val fields = mutableMapOf<String, String>()
        fields["userId"] = "25"
        fields["title"] = "Third Title"
        return service.postPost(fields)
    }
    private fun postPost() {
        // val call = postPost1()
        // val call = postPost2()
        val call = postPost3()
        call.enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    binding.tvResult.text = "Code: ${response.code()}"
                } else {
                    val pst = response.body()!!
                    var content = "Code: ${response.code()}\n"
                    content += "ID: ${pst.id}\nUser ID: ${pst.userId}\nTitle: ${pst.title}\nText: ${pst.body}\n\n"
                    binding.tvResult.append(content)
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                binding.tvResult.text = t.message
            }
        })
    }

    private fun updatePost1(post: Post) = service.putPost(5, post)
    private fun updatePost2(post: Post) = service.patchPost(5, post)
    private fun updatePost3(post: Post) = service.putPost("abc", 5, post)
    private fun updatePost4(post: Post): Call<Post> {
        val headers = HashMap<String, String>()
        headers["Map-Header1"] = "def"
        headers["Map-Header2"] = "ghi"
        return service.patchPost(headers, 5, post)
    }
    private fun updatePost() {
        val post = Post(12, null, null, "New Text")
        // this will return Post with Code=200, id=5, userId=12, title=null, text="New Text"
        // val call = updatePost1(post)

        // call patchPost() with the exact same params as putPost() above
        // this will return Post with Code=200, id=5, userId=12, title="nesciunt quas odio", text="New Text"
        // that's because it's the default title when the field in the patch request is null
        // val call = updatePost2(post)

        // call with header
        // val call = updatePost3(post)

        val call = updatePost4(post)
        call.enqueue(object: Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    binding.tvResult.text = "Code: ${response.code()}"
                } else {
                    val pst = response.body()!!
                    var content = "Code: ${response.code()}\n"
                    content += "ID: ${pst.id}\nUser ID: ${pst.userId}\nTitle: ${pst.title}\nText: ${pst.body}\n\n"
                    binding.tvResult.append(content)
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                binding.tvResult.text = t.message
            }
        })
    }

    private fun deletePost() {
        val call = service.deletePost(5)
        call.enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                binding.tvResult.text = "Code: ${response.code()}"
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                binding.tvResult.text = t.message
            }
        })
    }
}
package com.florian_walther.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.florian_walther.retrofit.databinding.ActivityMainBinding
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

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(JsonPlaceholderService::class.java)

        // getPosts2()
        getComments()
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
}
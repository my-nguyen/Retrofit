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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(JsonPlaceholderService::class.java)

        val call = service.getPosts()
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
}
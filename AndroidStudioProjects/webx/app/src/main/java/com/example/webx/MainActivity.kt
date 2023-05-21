package com.example.webx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.webx.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    object RetrofitBuilder {
        var service : GithubAPIService
        init {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            service = retrofit.create(GithubAPIService::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = GithubAdapter()
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.button.setOnClickListener {
            RetrofitBuilder.service.getUserRepo(binding.editTextTextPersonName.text.toString())
                .enqueue(object: Callback<UserRepo>{
                override fun onResponse(call: Call<UserRepo>, response: Response<UserRepo>) {
                    Log.d("IISE",response.body().toString())
                    response.body()?.forEach{
                        Log.d("IISE",it.git_url)
                    }
                    adapter.list = response.body()
                    adapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<UserRepo>, t: Throwable) {
                    Log.d("IISE",t.message.toString())
                }
            })
        }



    }
}
package com.example.webx

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubAPIService {
    @GET("users/{id}/repos")
    fun getUserRepo(@Path("id") uID: String): Call<UserRepo>
}
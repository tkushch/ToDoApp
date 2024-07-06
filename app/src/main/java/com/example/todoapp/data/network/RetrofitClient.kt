package com.example.todoapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.todoapp.BuildConfig
import okhttp3.OkHttpClient

object RetrofitClient {

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(BuildConfig.TOKEN))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TodoApiService by lazy {
        instance.create(TodoApiService::class.java)
    }
}



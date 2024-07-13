package com.example.todoapp.data.network

import okhttp3.Interceptor
import okhttp3.Response


/**
 * AuthInterceptor - отвечает за добавление заголовка авторизации к запросам
 */
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
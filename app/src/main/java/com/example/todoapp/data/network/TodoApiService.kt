package com.example.todoapp.data.network

import com.example.todoapp.data.network.dto.AddElementRequestDto
import com.example.todoapp.data.network.dto.ElementDto
import com.example.todoapp.data.network.dto.ListDto
import retrofit2.http.*

interface TodoApiService {

    @GET("list")
    suspend fun getTodoList(): ListDto

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): ElementDto

    @POST("list")
    suspend fun addTodoItem(@Body element: AddElementRequestDto): ElementDto

    @PUT("list/{id}")
    suspend fun updateTodoItem(@Path("id") id: String, @Body element: AddElementRequestDto): ElementDto

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Path("id") id: String)
}
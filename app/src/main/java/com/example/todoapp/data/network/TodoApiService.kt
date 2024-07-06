package com.example.todoapp.data.network

import com.example.todoapp.data.network.dto.AddElementRequestDto
import com.example.todoapp.data.network.dto.ElementDto
import com.example.todoapp.data.network.dto.ListDto
import retrofit2.http.*

interface TodoApiService {

    @GET("list")
    suspend fun getTodoList(): ListDto

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: AddElementRequestDto,
    ): ListDto

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body element: AddElementRequestDto,
    ): ListDto

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
    ): ListDto

}

package com.example.todoapp.data.network

import com.example.todoapp.data.network.dto.AddElementRequestDto
import com.example.todoapp.data.network.dto.ListDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @PATCH("list")
    suspend fun patchTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body list: ListDto,
    ): ListDto


}

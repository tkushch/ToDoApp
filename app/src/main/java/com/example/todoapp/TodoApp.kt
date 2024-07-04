package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.network.RetrofitClient
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TodoApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Main)
    val todoItemsRepository by lazy {
        TodoItemsRepository(
            applicationScope,
            Dispatchers.IO
        )
    }

}

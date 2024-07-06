/**
 * TodoApp - класс для инициализации основных компонентов
 * приложения не связанных с жизненным циклом активити или фрагментов
 */

package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.network.RetrofitClient
import com.example.todoapp.data.network.connectivity.NetworkConnectivityObserver
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TodoApp : Application() {
    private val todoApiService = RetrofitClient.api
    private val applicationScope = CoroutineScope(Dispatchers.Main)
    val todoItemsRepository by lazy {
        TodoItemsRepository(
            todoApiService,
            applicationScope,
            Dispatchers.IO
        )
    }
    val connectivityObserver by lazy {
        NetworkConnectivityObserver(applicationContext)
    }
}



/**
 * TodoApp - класс для инициализации основных компонентов
 * приложения не связанных с жизненным циклом активити или фрагментов
 */

package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.network.RetrofitClient
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.ConnectivityObserverImpl
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TodoApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

//    private val todoApiService = RetrofitClient.api
//    private val applicationScope = CoroutineScope(Dispatchers.Main)
//    val todoItemsRepository by lazy {
//        TodoItemsRepository(
//            todoApiService,
//            applicationScope,
//            Dispatchers.IO
//        )
//    }
//    val connectivityObserver: ConnectivityObserver by lazy {
//        ConnectivityObserverImpl(applicationContext)
//    }
}



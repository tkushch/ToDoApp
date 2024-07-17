package com.example.todoapp

import android.app.Application
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent

/**
 * TodoApp - класс для инициализации основных компонентов
 * приложения не связанных с жизненным циклом активити или фрагментов
 */
class TodoApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}



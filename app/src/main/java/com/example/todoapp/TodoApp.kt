package com.example.todoapp

import android.app.Application
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import com.example.todoapp.presentation.ui.theme.control.ThemeController
import javax.inject.Inject

/**
 * TodoApp - класс для инициализации основных компонентов
 * приложения не связанных с жизненным циклом активити или фрагментов
 */
class TodoApp : Application() {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var themeController: ThemeController

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
        themeController.setTheme(themeController.getTheme())
    }

}



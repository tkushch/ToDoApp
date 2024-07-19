package com.example.todoapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.todoapp.TodoApp
import com.example.todoapp.data.db.TodoDatabase
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.network.RetrofitClient
import com.example.todoapp.data.network.TodoApiService
import com.example.todoapp.data.network.background.UpdateWorker
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.ConnectivityObserverImpl
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.theme.control.PreferenceManager
import com.example.todoapp.presentation.ui.theme.control.ThemeController
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class, ConnectivityModule::class, DatabaseModule::class, SharedPreferencesModule::class, ThemeModule::class])
interface AppComponent {
    fun inject(application: UpdateWorker)
    fun inject(application: TodoApp)
    fun activityComponentFactory(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}


@Module
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
}

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideTodoApiService(): TodoApiService = RetrofitClient.api
}

@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTodoItemsRepository(
        todoApiService: TodoApiService,
        todoItemDao: TodoItemDao,
        applicationScope: CoroutineScope,
        sharedPreferences: SharedPreferences
    ): TodoItemsRepository {
        return TodoItemsRepository(
            todoApiService,
            todoItemDao,
            applicationScope,
            Dispatchers.IO,
            sharedPreferences,
        )
    }
}

@Module
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideConnectivityObserver(impl: ConnectivityObserverImpl): ConnectivityObserver {
        return impl
    }

    @Provides
    @Singleton
    fun provideConnectivityObserverImpl(context: Context): ConnectivityObserverImpl {
        return ConnectivityObserverImpl(context)
    }
}

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TodoDatabase::class.java,
            name = "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoItemDao(database: TodoDatabase): TodoItemDao {
        return database.dao
    }
}

@Module
object SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("todo_app_prefs", Context.MODE_PRIVATE)
    }
}

@Module
object ThemeModule {
    @Provides
    @Singleton
    fun providePreferenceManager(context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @Provides
    @Singleton
    fun provideThemeController(preferenceManager: PreferenceManager): ThemeController {
        return ThemeController(preferenceManager)
    }
}
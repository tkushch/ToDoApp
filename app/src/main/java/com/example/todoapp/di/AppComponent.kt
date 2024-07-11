package com.example.todoapp.di

import android.app.Application
import android.content.Context
import com.example.todoapp.TodoApp
import com.example.todoapp.data.network.RetrofitClient
import com.example.todoapp.data.network.TodoApiService
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.ConnectivityObserverImpl
import com.example.todoapp.data.repository.TodoItemsRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class, ConnectivityModule::class])
interface AppComponent {
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
        applicationScope: CoroutineScope
    ): TodoItemsRepository {
        return TodoItemsRepository(
            todoApiService,
            applicationScope,
            Dispatchers.IO
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



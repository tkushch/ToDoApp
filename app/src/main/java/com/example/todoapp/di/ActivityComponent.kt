package com.example.todoapp.di

import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.presentation.ui.MainActivity
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun fragmentComponentFactory(): FragmentComponent.Factory

    fun provideConnectivityObserver(): ConnectivityObserver

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): ActivityComponent
    }
}

@Module
object ActivityModule {
    @Provides
    @ActivityScope
    fun provideOnNetworkErrorListener(activity: MainActivity): OnNetworkErrorListener {
        return activity
    }
}
package com.example.todoapp.di

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.network.TodoApiService
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.ConnectivityObserverImpl
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.screen.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.main_screen.MainFragment
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel
import com.example.todoapp.presentation.ui.screen.viewmodelfactory.TodoViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.internal.Provider
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.reflect.KClass

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: MainFragment)
    fun inject(fragment: EditTaskFragmentCompose)
    fun provideConnectivityObserver(): ConnectivityObserver

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}


//    @Provides
//    @FragmentScope
//    fun provideTasksViewModel(
//        todoItemsRepository: TodoItemsRepository
//    ): TasksViewModel {
//        return TasksViewModel(todoItemsRepository)
//    }
//
//    @Provides
//    @FragmentScope
//    fun provideEditTaskViewModel(
//        todoItemsRepository: TodoItemsRepository
//    ): EditTaskViewModel {
//        return EditTaskViewModel(todoItemsRepository)
//    }
//
//
//    @Provides
//    @FragmentScope
//    fun provideTodoViewModelFactory(
//        tasksViewModelProvider: Provider<TasksViewModel>,
//        editTaskViewModelProvider: Provider<EditTaskViewModel>
//    ): TodoViewModelFactory {
//        return TodoViewModelFactory(tasksViewModelProvider, editTaskViewModelProvider)
//    }

@Module
class FragmentModule {
    @Provides
    @FragmentScope
    fun provideTodoViewModelFactory(
        repository: TodoItemsRepository,
    ): TodoViewModelFactory {
        return TodoViewModelFactory(repository)
    }
}




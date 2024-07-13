package com.example.todoapp.di

import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.screen.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.screen.main_screen.MainFragment
import com.example.todoapp.presentation.ui.screen.viewmodelfactory.TodoViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: MainFragment)
    fun inject(fragment: EditTaskFragmentCompose)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}

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




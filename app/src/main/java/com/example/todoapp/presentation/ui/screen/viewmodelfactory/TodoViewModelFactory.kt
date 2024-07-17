package com.example.todoapp.presentation.ui.screen.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel

/**
 * TodoViewModelFactory - класс-фабрика для создания ViewModel для разных экранов с дополнительными аргументами
 */
class TodoViewModelFactory(
    private val todoItemsRepository: TodoItemsRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return TasksViewModel(
                todoItemsRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return EditTaskViewModel(
                todoItemsRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

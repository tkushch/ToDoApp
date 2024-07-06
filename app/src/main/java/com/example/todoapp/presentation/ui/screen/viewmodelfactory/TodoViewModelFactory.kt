/**
 * TodoViewModelFactory - класс-фабрика для создания ViewModel для разных экранов с дополнительными аргументами
 */

package com.example.todoapp.presentation.ui.screen.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel

class TodoViewModelFactory(
    private val todoItemsRepository: TodoItemsRepository,
    private val connectivityObserver: ConnectivityObserver? = null,
    private val onNetworkErrorListener: OnNetworkErrorListener? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(
                todoItemsRepository,
                onNetworkErrorListener
            ) as T
        }

        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditTaskViewModel(
                todoItemsRepository,
                onNetworkErrorListener
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
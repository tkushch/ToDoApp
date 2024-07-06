/**
 * TasksViewModel - класс VM для связи визуальных элементов и репозитория (основной экран)
 */

package com.example.todoapp.presentation.ui.screen.main_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TasksViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val onNetworkErrorListener: OnNetworkErrorListener? = null
) : ViewModel() {


    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("TasksViewModel", exception.toString())
        onNetworkErrorListener?.onNetworkError()
    }


    init {
        refreshTasks()
    }

    private val _showCompletedTasks = MutableStateFlow(true)
    val showCompletedTasks = _showCompletedTasks.asStateFlow()

    val currentTasks: StateFlow<List<TodoItem>> = combine(
        showCompletedTasks,
        todoItemsRepository.todoItems,
        todoItemsRepository.uncompletedTodoItems
    ) { showCompleted, allTasks, uncompletedTasks ->
        if (showCompleted) {
            allTasks
        } else {
            uncompletedTasks
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )


    val numberOfCompletedTasks: StateFlow<Int> = todoItemsRepository.numCompletedTodoItems

    fun toggleShowCompletedTasks() {
        _showCompletedTasks.value = !_showCompletedTasks.value
    }

    fun refreshTasks() {
        viewModelScope.launch(handler) {
            todoItemsRepository.update()
        }
    }

    fun changeTaskStatus(taskId: String) {
        viewModelScope.launch(handler) {
            todoItemsRepository.changeTodoItemDoneStatus(taskId)
        }
    }
}




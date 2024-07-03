package com.example.todoapp.presentation.ui.screen.main_screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.data.model.TodoItem
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TasksViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val todoItemsRepository = (application as TodoApp).todoItemsRepository

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EditTaskViewModel", exception.toString())
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


    fun changeTaskStatus(taskId: String) {
        viewModelScope.launch(handler) {
            todoItemsRepository.changeTodoItemDoneStatus(taskId)
        }
    }
}




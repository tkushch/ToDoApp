package com.example.todoapp.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository

class TasksViewModel : ViewModel() {
    private val _showCompletedTasks = MutableLiveData<Boolean>(true)
    val showCompletedTasks: LiveData<Boolean> get() = _showCompletedTasks

    private val _currentTasks = MutableLiveData<List<TodoItem>>(listOf())
    val currentTasks: LiveData<List<TodoItem>> get() = _currentTasks

    fun toggleShowCompletedTasks() {
        _showCompletedTasks.value = !(_showCompletedTasks.value ?: true)
        updateTasks()
    }

    private lateinit var todoItemsRepository: TodoItemsRepository
    fun setTodoItemsRepository(todoItemsRepository: TodoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository
        updateTasks()
    }

    fun updateTasks() {
        if (_showCompletedTasks.value == true) {
            _currentTasks.value = todoItemsRepository.getTodoItems()
        } else {
            _currentTasks.value = todoItemsRepository.getUncompletedTodoItems()
        }
        _numberOfCompletedTasks.value  = todoItemsRepository.getNumCompletedTodoItems()
    }

    private val _numberOfCompletedTasks  = MutableLiveData<Int>(0)
    val numberOfCompletedTasks: LiveData<Int> get() = _numberOfCompletedTasks


}
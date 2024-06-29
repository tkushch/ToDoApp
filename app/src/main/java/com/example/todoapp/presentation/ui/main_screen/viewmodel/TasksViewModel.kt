package com.example.todoapp.presentation.ui.main_screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EditTaskViewModel", exception.toString())
    }

    private val _showCompletedTasks = MutableLiveData<Boolean>(true)
    val showCompletedTasks: LiveData<Boolean> get() = _showCompletedTasks

    private val _currentTasks = MutableLiveData<List<TodoItem>>(listOf())
    val currentTasks: LiveData<List<TodoItem>> get() = _currentTasks

    private val _numberOfCompletedTasks  = MutableLiveData<Int>(0)
    val numberOfCompletedTasks: LiveData<Int> get() = _numberOfCompletedTasks

    fun fillData(){
        viewModelScope.launch(handler) {
            todoItemsRepository?.fillDataHardCode()
        }
    }

    fun toggleShowCompletedTasks() {
        _showCompletedTasks.value = !(_showCompletedTasks.value ?: true)
        updateTasks()
    }

    private var todoItemsRepository: TodoItemsRepository? = null
    fun setTodoItemsRepository(todoItemsRepository: TodoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository
        fillData()
        updateTasks()
    }

    fun updateTasks() {
        viewModelScope.launch(handler) {
            val tasks = if (_showCompletedTasks.value == true) {
                todoItemsRepository?.getTodoItems()
            } else {
                todoItemsRepository?.getUncompletedTodoItems()
            }
            val completedCount = todoItemsRepository?.getNumCompletedTodoItems()
            _currentTasks.postValue(tasks)
            _numberOfCompletedTasks.postValue(completedCount)
        }
    }

    fun changeTaskStatus(taskId: String) {
        viewModelScope.launch(handler) {
            todoItemsRepository?.changeTodoItemDoneStatus(taskId)
            updateTasks()
        }
    }

}
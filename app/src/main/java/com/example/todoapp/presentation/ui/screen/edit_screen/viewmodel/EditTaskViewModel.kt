/**
 * EditTaskViewModel - класс VM для связи визуальных элементов и репозитория (экран редактирования задачи)
 */
package com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository,
) : ViewModel() {

    private val _networkErrors = MutableSharedFlow<String>()
    val networkErrors: SharedFlow<String> = _networkErrors

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EditTaskViewModel", "Error: ${exception.message}")
        viewModelScope.launch {
            _networkErrors.emit(exception.toString())
        }
    }

    private var _todoItem: TodoItem? = null
    val todoItem: TodoItem? get() = _todoItem

    fun saveTask(idArg: String?, text: String, importance: Importance, deadline: LocalDateTime?) {
        viewModelScope.launch(handler) {
            if (idArg != null) {
                todoItemsRepository.updateTodoItem(idArg, text, importance, deadline)
            } else {
                todoItemsRepository.addTodoItem(text, importance, deadline)
            }
        }
    }

    fun setTodoItem(idArg: String?) {
        _todoItem = if (idArg != null) {
            todoItemsRepository.findTodoItemById(idArg)
        } else {
            null
        }

        if (this.todoId != idArg) {
            this.todoId = idArg
            text = _todoItem?.text
            importance = _todoItem?.importance
            deadline = _todoItem?.deadline
        }
    }


    fun deleteTask(idArg: String?) {
        viewModelScope.launch(handler) {
            if (idArg != null) {
                todoItemsRepository.removeTodoItemById(idArg)
            }
        }
    }

    fun clearData() {
        todoId = null
        text = null
        importance = null
        deadline = null
    }

    private var todoId: String? = null
    var text: String? = null
    var importance: Importance? = null
    var deadline: LocalDateTime? = null

}
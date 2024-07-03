package com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoApp
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class EditTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val todoItemsRepository = (application as TodoApp).todoItemsRepository

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EditTaskViewModel", exception.toString())
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
        if (this.todoId != idArg || (this.todoId == null && idArg == null)) {
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
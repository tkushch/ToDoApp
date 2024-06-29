package com.example.todoapp.presentation.ui.edit_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import java.time.LocalDateTime
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class EditTaskViewModel : ViewModel() {
    private var todoItemsRepository: TodoItemsRepository? = null
    fun setTodoItemsRepository(todoItemsRepository: TodoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("EditTaskViewModel", exception.toString())
    }

    private var _todoItem: TodoItem? = null
    val todoItem: TodoItem? get() = _todoItem

    fun saveTask(idArg: String?, text: String, importance: Importance, deadline: LocalDateTime?) {
        viewModelScope.launch(handler) {
            if (idArg != null) {
                todoItemsRepository?.updateTodoItem(idArg, text, importance, deadline)
            } else {
                todoItemsRepository?.addTodoItem(text, importance, deadline)
            }
        }
    }

    fun setTodoItem(idArg: String?) {
        _todoItem = if (idArg != null) {
            todoItemsRepository?.findTodoItemById(idArg)
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
                todoItemsRepository?.removeTodoItemById(idArg)
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
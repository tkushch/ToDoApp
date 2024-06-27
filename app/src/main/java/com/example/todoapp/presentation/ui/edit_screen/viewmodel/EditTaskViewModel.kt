package com.example.todoapp.presentation.ui.edit_screen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import java.time.LocalDateTime

class EditTaskViewModel() : ViewModel() {
    private var todoItemsRepository: TodoItemsRepository? = null
    fun setTodoItemsRepository(todoItemsRepository: TodoItemsRepository) {
        this.todoItemsRepository = todoItemsRepository
    }

    private var _todoItem: TodoItem? = null
    val todoItem: TodoItem? get() = _todoItem

    fun saveTask(id: String?, text: String, importance: Importance, deadline: LocalDateTime?) {
        id?.let {
            todoItemsRepository?.updateTodoItem(it, text, importance, deadline)
        } ?: run {
            todoItemsRepository?.addTodoItem(text, importance, deadline)
        }
    }

    fun setTodoItem(id: String?) {
        id?.let {
            _todoItem = todoItemsRepository?.findTodoItemById(id)
        } ?: run {
            _todoItem = null
        }
    }

    fun deleteTask(id: String?) {
        id?.let {
            todoItemsRepository?.removeTodoItemById(it)
        }
    }


}
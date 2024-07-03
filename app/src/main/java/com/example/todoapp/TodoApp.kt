package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.repository.TodoItemsRepository

class TodoApp : Application() {

    val todoItemsRepository by lazy { TodoItemsRepository() }

}

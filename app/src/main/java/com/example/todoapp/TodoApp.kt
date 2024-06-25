package com.example.todoapp

import android.app.Application

class TodoApp : Application() {

    val todoItemsRepository: TodoItemsRepository = TodoItemsRepository()

}

package com.example.todoapp.data.repository

import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import java.util.UUID


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class TodoItemsRepository(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems

    suspend fun update() {
        //getting data from API
    }

    val numCompletedTodoItems: StateFlow<Int> = _todoItems.map { items -> items.count { it.done } }
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)

    val uncompletedTodoItems: StateFlow<List<TodoItem>> =
        _todoItems.map { items -> items.filter { !it.done } }
            .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    fun findTodoItemById(todoId: String): TodoItem? {
        return todoItems.value.find { it.id == todoId }
    }

    suspend fun addTodoItem(taskText: String, importance: Importance, deadline: LocalDateTime?) {
        coroutineScope.launch(ioDispatcher) {
            val newTask = TodoItem(
                id = UUID.randomUUID().toString(),
                text = taskText,
                importance = importance,
                deadline = deadline,
                done = false,
                creationDate = LocalDateTime.now(),
                updatedDate = null
            )
            _todoItems.value += newTask
        }.join()
    }

    suspend fun updateTodoItem(
        taskId: String,
        text: String,
        importance: Importance,
        deadline: LocalDateTime?
    ) {
        coroutineScope.launch(ioDispatcher) {
            val updatedList = _todoItems.value.map { item ->
                if (item.id == taskId) {
                    item.copy(
                        text = text,
                        importance = importance,
                        deadline = deadline,
                        updatedDate = LocalDateTime.now()
                    )
                } else {
                    item
                }
            }
            _todoItems.value = updatedList
        }.join()
    }

    suspend fun changeTodoItemDoneStatus(todoId: String) {
        coroutineScope.launch(ioDispatcher) {
            val updatedList = _todoItems.value.map { item ->
                if (item.id == todoId) {
                    item.copy(done = !item.done)
                } else {
                    item
                }
            }
            _todoItems.value = updatedList
        }.join()
    }

    suspend fun removeTodoItemById(todoId: String) {
        coroutineScope.launch(ioDispatcher) {
            _todoItems.value = _todoItems.value.filter { it.id != todoId }
        }.join()
    }

}
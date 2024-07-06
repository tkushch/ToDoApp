/**
 * TodoItemsRepository - класс репозитория для работы с задачами.
 * Является прослойкой между БД (сетевой или локальной) и бизнес-логикой приложения
 */

package com.example.todoapp.data.repository


import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.network.TodoApiService
import com.example.todoapp.data.network.mapper.ElementMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID


class TodoItemsRepository(
    private val todoApiService: TodoApiService,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems
    private var currentRevision: Int = 0

    val numCompletedTodoItems: StateFlow<Int> =
        _todoItems.map { items -> items.count { it.done } }
            .stateIn(coroutineScope, SharingStarted.Lazily, 0)

    val uncompletedTodoItems: StateFlow<List<TodoItem>> =
        _todoItems.map { items -> items.filter { !it.done } }
            .stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    fun findTodoItemById(todoId: String): TodoItem? {
        return todoItems.value.find { it.id == todoId }
    }

    private suspend fun <T> performNetworkRequest(
        request: suspend () -> T,
        onSuccess: (T) -> Unit,
        maxRetries: Long = 3,
        retryDelay: Long = 1000L
    ) = flow {
        val response = request()
        emit(response)
    }.retryWhen { cause, attempt ->
        if (cause is IOException && attempt < maxRetries) {
            delay(retryDelay)
            true
        } else {
            false
        }
    }.collect { response ->
        onSuccess(response)
    }


    suspend fun update() {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            performNetworkRequest(
                request = {
                    todoApiService.getTodoList()
                },
                onSuccess = { response ->
                    _todoItems.value = response.list.map { ElementMapper.fromDto(it) }
                    currentRevision = response.revision
                }
            )
        }
    }


    suspend fun addTodoItem(
        taskText: String,
        importance: Importance,
        deadline: LocalDateTime?
    ) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            val newTask = TodoItem(
                id = UUID.randomUUID().toString(),
                text = taskText,
                importance = importance,
                deadline = deadline,
                done = false,
                creationDate = LocalDateTime.now(),
                updatedDate = null
            )

            performNetworkRequest(
                request = {
                    todoApiService.addTodoItem(currentRevision, ElementMapper.toAddDto(newTask))
                },
                onSuccess = { response ->
                    _todoItems.value += newTask
                    currentRevision = response.revision
                }
            )
        }
    }

    suspend fun updateTodoItem(
        taskId: String,
        text: String,
        importance: Importance,
        deadline: LocalDateTime?
    ) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            var newTask: TodoItem? = null
            val updatedList = _todoItems.value.map { item ->
                if (item.id == taskId) {
                    newTask = item.copy(
                        text = text,
                        importance = importance,
                        deadline = deadline,
                        updatedDate = LocalDateTime.now()
                    )
                    newTask!!
                } else {
                    item
                }
            }

            if (newTask != null) {
                performNetworkRequest(
                    request = {
                        todoApiService.updateTodoItem(
                            currentRevision,
                            taskId,
                            ElementMapper.toAddDto(newTask!!)
                        )
                    },
                    onSuccess = { response ->
                        _todoItems.value = updatedList
                        currentRevision = response.revision
                    }
                )
            }
        }
    }

    suspend fun changeTodoItemDoneStatus(todoId: String) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            var newTask: TodoItem? = null
            val updatedList = _todoItems.value.map { item ->
                if (item.id == todoId) {
                    newTask = item.copy(done = !item.done)
                    newTask!!
                } else {
                    item
                }
            }
            if (newTask != null) {
                performNetworkRequest(
                    request = {
                        todoApiService.updateTodoItem(
                            currentRevision,
                            todoId,
                            ElementMapper.toAddDto(newTask!!)
                        )
                    },
                    onSuccess = { response ->
                        _todoItems.value = updatedList
                        currentRevision = response.revision
                    }
                )
            }
        }
    }

    suspend fun removeTodoItemById(todoId: String) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            performNetworkRequest(
                request = {
                    todoApiService.deleteTodoItem(currentRevision, todoId)
                },
                onSuccess = { response ->
                    _todoItems.value = _todoItems.value.filter { it.id != todoId }
                    currentRevision = response.revision
                }
            )
        }
    }
}
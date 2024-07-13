package com.example.todoapp.data.repository


import android.util.Log
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.db.toDomainModel
import com.example.todoapp.data.db.toEntity
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

/**
 * TodoItemsRepository - класс репозитория для работы с задачами.
 * Является прослойкой между БД (сетевой или локальной) и бизнес-логикой приложения
 */
class TodoItemsRepository(
    private val todoApiService: TodoApiService,
    private val todoItemDao: TodoItemDao,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems
    private var currentRevision: Int = 0

    val numCompletedTodoItems: StateFlow<Int> = _todoItems.map { items -> items.count { it.done } }
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
        maxRetries: Long = 2,
        retryDelay: Long = 500L
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
            _todoItems.value = todoItemDao.getTodoItems().map { it.toDomainModel() }
            val response = todoApiService.getTodoList()
            Log.d("REPO", "curr $currentRevision serv: ${response.revision}")
            if (_todoItems.value.isNotEmpty() && currentRevision == response.revision) {
                Log.d("REPO", "patch")
                currentRevision = response.revision
                val list = _todoItems.value.map { ElementMapper.toDto(it) }
                val responsePatch = todoApiService.patchTodoList(
                    currentRevision, ElementMapper.toListDto(list, currentRevision)
                )
                currentRevision = responsePatch.revision
                val newList = responsePatch.list.map { ElementMapper.fromDto(it) }
                _todoItems.value = newList
                updateDataBase(newList)
            } else {
                Log.d("REPO", "from server")
                currentRevision = response.revision
                val newList = response.list.map { ElementMapper.fromDto(it) }
                _todoItems.value = newList
                updateDataBase(newList)
            }
        }
    }

    private suspend fun updateDataBase(newList: List<TodoItem>) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            todoItemDao.deleteAll()
            todoItemDao.insertTodoItems(newList.map { it.toEntity() })
        }
    }


    suspend fun addTodoItem(
        taskText: String, importance: Importance, deadline: LocalDateTime?
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
            todoItemDao.insertTodoItem(newTask.toEntity())
            _todoItems.value += newTask
            performNetworkRequest(request = {
                todoApiService.addTodoItem(currentRevision, ElementMapper.toAddDto(newTask))
            }, onSuccess = { response ->
                currentRevision = response.revision
            })
        }
    }

    suspend fun updateTodoItem(
        taskId: String, text: String, importance: Importance, deadline: LocalDateTime?
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
                todoItemDao.updateTodoItem(newTask!!.toEntity())
                _todoItems.value = updatedList
                performNetworkRequest(request = {
                    todoApiService.updateTodoItem(
                        currentRevision, taskId, ElementMapper.toAddDto(newTask!!)
                    )
                }, onSuccess = { response ->
                    currentRevision = response.revision
                })
            }
        }
    }


    suspend fun changeTodoItemDoneStatus(todoId: String) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            var task: TodoItem? = null
            val updatedList = _todoItems.value.map { item ->
                if (item.id == todoId) {
                    task = item.copy(done = !item.done)
                    task!!
                } else {
                    item
                }
            }

            if (task != null) {
                todoItemDao.updateTodoItem(task!!.toEntity())
                _todoItems.value = updatedList
                performNetworkRequest(request = {
                    todoApiService.updateTodoItem(
                        currentRevision, todoId, ElementMapper.toAddDto(task!!)
                    )
                }, onSuccess = { response ->
                    currentRevision = response.revision
                })
            }
        }
    }

    suspend fun removeTodoItemById(todoId: String) {
        withContext(coroutineScope.coroutineContext + ioDispatcher) {
            val task = _todoItems.value.find { it.id == todoId }
            task?.let {
                val updatedList = _todoItems.value.filter { it.id != todoId }
                _todoItems.value = updatedList
                launch {
                    todoItemDao.deleteTodoItem(task.toEntity())
                }
                performNetworkRequest(request = {
                    todoApiService.deleteTodoItem(
                        currentRevision, todoId
                    )
                }, onSuccess = { response -> currentRevision = response.revision })
            }
        }
    }
}
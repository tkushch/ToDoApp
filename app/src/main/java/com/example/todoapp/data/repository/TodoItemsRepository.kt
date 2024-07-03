package com.example.todoapp.data.repository

import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
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

class TodoItemsRepository {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        repositoryScope.launch {
            _fillDataHardCode()
        }
    }

    val numCompletedTodoItems: StateFlow<Int> = _todoItems.map { items -> items.count { it.done } }
        .stateIn(repositoryScope, SharingStarted.Lazily, 0)

    val uncompletedTodoItems: StateFlow<List<TodoItem>> =
        _todoItems.map { items -> items.filter { !it.done } }
            .stateIn(repositoryScope, SharingStarted.Lazily, emptyList())

    fun findTodoItemById(todoId: String): TodoItem? {
        return todoItems.value.find { it.id == todoId }
    }

    suspend fun addTodoItem(taskText: String, importance: Importance, deadline: LocalDateTime?) {
        withContext(Dispatchers.IO) {
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
        }
    }

    private suspend fun addTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            _todoItems.value += todoItem
        }
    }

    suspend fun updateTodoItem(
        taskId: String,
        text: String,
        importance: Importance,
        deadline: LocalDateTime?
    ) {
        withContext(Dispatchers.IO) {
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
        }
    }

    suspend fun changeTodoItemDoneStatus(todoId: String) {
        withContext(Dispatchers.IO) {
            val updatedList = _todoItems.value.map { item ->
                if (item.id == todoId) {
                    item.copy(done = !item.done)
                } else {
                    item
                }
            }
            _todoItems.value = updatedList
        }
    }

    suspend fun removeTodoItemById(todoId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val initialSize = _todoItems.value.size
            _todoItems.value = _todoItems.value.filter { it.id != todoId }
            initialSize > _todoItems.value.size
        }
    }

    private suspend fun _fillDataHardCode() {
        if (_todoItems.value.isEmpty()) {
            withContext(Dispatchers.IO) {
                addTodoItem(
                    TodoItem(
                        "0",
                        "Attend team meeting 2",
                        Importance.MEDIUM,
                        LocalDateTime.now().plusDays(4),
                        true,
                        LocalDateTime.now(),
                        null
                    )
                )

                addTodoItem(
                    TodoItem(
                        "1",
                        "Buy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceriesBuy groceries",
                        Importance.LOW,
                        null,
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "2",
                        "Complete project report",
                        Importance.HIGH,
                        LocalDateTime.now().plusDays(1),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "3",
                        "Pay utility bills",
                        Importance.MEDIUM,
                        LocalDateTime.now().plusWeeks(1),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "4",
                        "Book doctor appointment",
                        Importance.HIGH,
                        LocalDateTime.now().plusDays(3),
                        true,
                        LocalDateTime.now().minusDays(10),
                        LocalDateTime.now().minusDays(2)
                    )
                )
                addTodoItem(
                    TodoItem(
                        "5",
                        "Clean the house",
                        Importance.LOW,
                        null,
                        true,
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().minusDays(1)
                    )
                )
                addTodoItem(
                    TodoItem(
                        "6",
                        "Attend team meeting",
                        Importance.MEDIUM,
                        LocalDateTime.now().plusDays(2),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "7",
                        "Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance Renew car insurance",
                        Importance.HIGH,
                        LocalDateTime.now().plusMonths(1),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "8",
                        "Read a book",
                        Importance.LOW,
                        null,
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "9",
                        "Exercise",
                        Importance.MEDIUM,
                        null,
                        true,
                        LocalDateTime.now().minusDays(7),
                        LocalDateTime.now().minusDays(1)
                    )
                )
                addTodoItem(
                    TodoItem(
                        "10",
                        "Plan vacation",
                        Importance.LOW,
                        LocalDateTime.now().plusMonths(3),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "11",
                        "Prepare for presentation",
                        Importance.HIGH,
                        LocalDateTime.now().plusDays(5),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "12",
                        "Organize workspace",
                        Importance.MEDIUM,
                        LocalDateTime.now().plusDays(10),
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
                addTodoItem(
                    TodoItem(
                        "13",
                        "Buy groceries 2",
                        Importance.HIGH,
                        null,
                        false,
                        LocalDateTime.now(),
                        null
                    )
                )
            }
        }
    }

}
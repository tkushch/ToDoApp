package com.example.todoapp.data.model

import java.time.LocalDateTime

/**
 * TodoItem - модель для хранения "задач"
 */
data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: LocalDateTime?,
    val done: Boolean,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime?
)

/**
 * Importance - модель для хранения приоритета "задач"
 */
enum class Importance {
    LOW, BASIC, IMPORTANT;

}

fun stringToImportance(importance: String): Importance {
    return when (importance) {
        "low" -> Importance.LOW
        "basic" -> Importance.BASIC
        "important" -> Importance.IMPORTANT
        else -> Importance.BASIC
    }
}


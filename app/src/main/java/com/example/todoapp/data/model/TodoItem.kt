package com.example.todoapp.data.model

import java.time.LocalDateTime

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: LocalDateTime?,
    val done: Boolean,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime?
) {

}

enum class Importance {
    LOW, MEDIUM, HIGH;

}

fun stringToImportance(importance: String): Importance {
    return when (importance) {
        "Low" -> Importance.LOW
        "Medium" -> Importance.MEDIUM
        "High" -> Importance.HIGH
        else -> Importance.MEDIUM
    }
}


package com.example.todoapp.data.model

import java.time.LocalDateTime

data class TodoItem(
    val id: String,
    var text: String,
    var importance: Importance,
    var deadline: LocalDateTime?,
    var done: Boolean,
    val creationDate: LocalDateTime,
    var updatedDate: LocalDateTime?
)

enum class Importance {
    LOW, MEDIUM, HIGH
}

fun stringToImportance(importance: String): Importance {
    return when (importance) {
        "Low" -> Importance.LOW
        "Medium" -> Importance.MEDIUM
        "High" -> Importance.HIGH
        else -> Importance.MEDIUM
    }
}


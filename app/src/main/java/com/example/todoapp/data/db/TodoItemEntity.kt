package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.model.Importance
import java.time.LocalDateTime

/**
 * TodoItemEntity - класс для таблицы todo_items
 */

@Entity(tableName = "todo_items")
data class TodoItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "importance")
    val importance: Importance,
    @ColumnInfo(name = "deadline")
    val deadline: LocalDateTime?,
    @ColumnInfo(name = "doneStatus")
    val done: Boolean,
    @ColumnInfo(name = "creationDate")
    val creationDate: LocalDateTime,
    @ColumnInfo(name = "updatedDate")
    val updatedDate: LocalDateTime?,
)